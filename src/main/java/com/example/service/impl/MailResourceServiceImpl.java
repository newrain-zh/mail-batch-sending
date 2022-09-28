package com.example.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.config.MailsConfig;
import com.example.entity.pojo.BatchMailConfig;
import com.example.entity.dto.BatchMailConfigDto;
import com.example.entity.pojo.MailSendLog;
import com.example.entity.dto.MailSendLogDto;
import com.example.enums.MailSendStatusEnum;
import com.example.exception.BusinessException;
import com.example.mapper.BatchMailConfigMapper;
import com.example.mapper.MailSendLogMapper;
import com.example.service.IMailResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhiqin.zhang
 */
@Slf4j
@Service
public class MailResourceServiceImpl implements IMailResourceService {

    @Resource
    private BatchMailConfigMapper batchMailConfigMapper;

    @Resource
    private MailSendLogMapper mailSendLogMapper;

    @Resource
    private MailsConfig mailsConfig;

    @Override
    public void checkMailResource() {
        List<BatchMailConfig> batchMailConfigs = batchMailConfigMapper.selectList(new QueryWrapper<>());
        if (CollectionUtil.isEmpty(batchMailConfigs)) {
            throw new BusinessException("未配置邮箱", 10002);
        }
        Date date = new Date();
        //总次数
        QueryWrapper<MailSendLog> sendLogNewQueryWrapper = new QueryWrapper<>();
        sendLogNewQueryWrapper.in("send_status", Stream.of(MailSendStatusEnum.FAIL.getCode(), MailSendStatusEnum.SUCCESS.getCode()).collect(Collectors.toSet()));
        sendLogNewQueryWrapper.ge("send_time", DateUtil.beginOfDay(date).toString());
        sendLogNewQueryWrapper.le("send_time", DateUtil.endOfDay(date).toString());
        Long todayTotal = mailSendLogMapper.selectCount(sendLogNewQueryWrapper);
        if (todayTotal == null) {
            return;
        }
        long mailTotal = batchMailConfigs.size();
        if (todayTotal >= (mailTotal * mailsConfig.getMailMaxDayTotal())) {
            log.info("今日邮箱资源已用尽");
            throw new BusinessException("今日邮箱资源已用尽", 10001);
        }

        List<MailSendLog> list = getMailUsedRecord(mailsConfig.getMailMinuteRate());
        if (list.size() >= mailsConfig.getMailMaxMinuteTotal() * mailTotal) {
            throw new BusinessException("当前邮箱资源已用尽", 10002);
        }

    }


    @Override
    public List<BatchMailConfigDto> getMailResourceList() {
        checkMailResource();
        List<MailSendLog> mailUsedRecord = getMailUsedRecord(mailsConfig.getMailMinuteRate());
        List<BatchMailConfig> reachMailServiceConfigNews = batchMailConfigMapper.selectList(new QueryWrapper<>());
        List<BatchMailConfigDto> dtoList = BeanUtil.copyToList(reachMailServiceConfigNews, BatchMailConfigDto.class);
        if (CollectionUtil.isEmpty(mailUsedRecord)) {
            dtoList.forEach(v -> v.setCanUsedCount(mailsConfig.getMailMaxMinuteTotal()));
            return dtoList;
        }
        Map<String, Long> collect = mailUsedRecord.stream().collect(Collectors.groupingBy(MailSendLog::getUsername, Collectors.counting()));
        List<BatchMailConfigDto> result = new ArrayList<>(reachMailServiceConfigNews.size());
        for (BatchMailConfigDto v : dtoList) {
            if (collect.containsKey(v.getUsername())) {
                int count = mailsConfig.getMailMaxMinuteTotal() - collect.get(v.getUsername()).intValue();
                if (count > 0) {
                    v.setCanUsedCount(count);
                    result.add(v);
                }
            } else {
                v.setCanUsedCount(mailsConfig.getMailMaxMinuteTotal());
                result.add(v);
            }
        }
        return result;
    }

    @Override
    public Map<String, Integer> getMailResourceInfo() {
        List<BatchMailConfig> reachMailServiceConfigNews = batchMailConfigMapper.selectList(new QueryWrapper<>());
        Map<String, Integer> info = new HashMap<>(5);
        if (CollectionUtil.isEmpty(reachMailServiceConfigNews)) {
            log.info("未配置邮箱资源");
            return info;
        }
        Date currDate = new Date();
        String date = DateUtil.format(currDate, "yyyy-MM-dd");

        //获取当前mysql中的邮件资源情况
        QueryWrapper<MailSendLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("create_time", date + " 23:59:59");
        queryWrapper.le("create_time", DateUtil.endOfDay(currDate).toString());
        queryWrapper.ge("create_time", date + " 00:00:00");
        queryWrapper.ge("create_time", DateUtil.beginOfDay(currDate).toString());
        List<MailSendLog> list = mailSendLogMapper.selectList(queryWrapper);
        if (CollectionUtil.isEmpty(list)) {
            return info;
        }

        List<MailSendLogDto> dtoList = BeanUtil.copyToList(list, MailSendLogDto.class);
        Map<String, Integer> mailCountMap = new HashMap<>(reachMailServiceConfigNews.size());
        int successTotal = 0;
        int failTotal = 0;
        int sendingTotal = 0;
        int minuteTotal = 0;
        for (MailSendLogDto dto : dtoList) {
            if (dto.getSendTime() != null) {
                long between = DateUtil.between(dto.getSendTime(), currDate, DateUnit.MINUTE, true);
                if (between <= 15) {
                    minuteTotal += 1;
                }
            }
            mailCountMap.put(dto.getUsername(), mailCountMap.getOrDefault(dto.getUsername(), 0) + 1);
            if (MailSendStatusEnum.SUCCESS.getCode() == dto.getSendStatus()) {
                successTotal += 1;
            } else if (MailSendStatusEnum.FAIL.getCode() == dto.getSendStatus()) {
                failTotal += 1;
            } else {
                sendingTotal += 1;
            }
        }
        info.put("dayTotal", list.size());
        info.put("minuteTotal", minuteTotal);
        info.put("successTotal", successTotal);
        info.put("failTotal", failTotal);
        info.put("sendingTotal", sendingTotal);
        return info;
    }

    @Override
    public List<MailSendLog> getMailUsedRecord(int minute) {
        //15分钟资源情况
        Date date = new Date();
        String endTime = DateUtil.format(date, "yyy-MM-dd HH:mm:ss");
        String startTime = DateUtil.offsetMinute(date, -minute).toString();
        QueryWrapper<MailSendLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("send_status", Stream.of(MailSendStatusEnum.FAIL.getCode(), MailSendStatusEnum.SUCCESS.getCode()).collect(Collectors.toSet()));
        queryWrapper.ge("send_time", startTime);
        queryWrapper.le("send_time", endTime);
        queryWrapper.select("id", "send_time", "username");
        return mailSendLogMapper.selectList(queryWrapper);
    }
}
