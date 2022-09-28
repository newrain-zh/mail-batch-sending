package com.example.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.common.MysqlLock;
import com.example.config.MailsConfig;
import com.example.entity.dto.BatchMailConfigDto;
import com.example.entity.pojo.MailSendLog;
import com.example.enums.AllocMailResourceEnum;
import com.example.enums.MailSendStatusEnum;
import com.example.mapper.BatchMailConfigMapper;
import com.example.mapper.MailSendLogMapper;
import com.example.service.*;
import com.example.service.strategy.AllocMailResourceStrategy;
import com.example.service.strategy.AllocMailResourceStrategyContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhiqin.zhang
 */
@Slf4j
@Service
public class MailBatchSendServiceImpl implements IMailBatchSendService {

    @Resource
    private IMailResourceService mailResourceService;
    @Resource
    private BatchMailConfigMapper batchMailConfigMapper;
    @Resource
    private MailSendLogMapper mailSendLogMapper;

    @Resource
    private IMailSendAsyncService mailSendAsyncService;
    @Resource
    private MailsConfig mailsConfig;

    @Resource
    private AllocMailResourceStrategyContext allocMailResourceStrategyContext;

    @Resource
    private MysqlLock mysqlLock;

    @Override
    public void start(String date) {
        Long aLong = batchMailConfigMapper.selectCount(new QueryWrapper<>());
        long limit = Optional.ofNullable(aLong).orElse(1L) * mailsConfig.getMailMaxMinuteTotal();
        QueryWrapper<MailSendLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("create_time", date + " 00:00:00");
        queryWrapper.le("create_time", date + " 23:59:59");
        queryWrapper.eq("send_status", MailSendStatusEnum.SENDING.getCode());
        queryWrapper.last(" limit " + limit);
        List<MailSendLog> list = mailSendLogMapper.selectList(queryWrapper);
        if (CollectionUtil.isEmpty(list)) {
            log.info("batchMailJob-start没有需要发送邮件的用户 日期:{}", date);
            return;
        }
        batchSend(list);
    }


    public void batchSend(List<MailSendLog> sendList) {
        if (CollectionUtil.isEmpty(sendList)) {
            log.info("batchSend===未有邮件需要发送");
            return;
        }
        boolean lock = mysqlLock.lock();
        if (!lock) {
            log.info("获取锁失败!");
            return;
        }
        try {
            List<BatchMailConfigDto> mailResourceList = mailResourceService.getMailResourceList();
            if (CollectionUtil.isEmpty(mailResourceList)) {
                log.info("batchSend===没有邮箱资源可以发送邮件");
                return;
            }
            log.info("batchSend===开始分配资源-获取邮箱资源详细:{}", JSON.toJSONString(mailResourceList.stream().collect(Collectors.toMap(BatchMailConfigDto::getUsername, BatchMailConfigDto::getCanUsedCount))));
            Map<String, BatchMailConfigDto> map = mailResourceList.stream().collect(Collectors.toMap(BatchMailConfigDto::getUsername, Function.identity()));
            log.info("batchSend===开始分配资源-总共资源数{}", sendList.size());
            AllocMailResourceStrategy handler = allocMailResourceStrategyContext.getHandler(AllocMailResourceEnum.DEFAULT.getType());
            Map<String, Integer> count = handler.allocMailResource(mailResourceList, sendList);
            log.info("batchSend===开始分配资源-已配置邮箱目前资源:{}", JSON.toJSONString(count));
            //获取需发送邮件的客户列表
            List<MailSendLog> collect = sendList.stream().filter(d -> StringUtils.isNotBlank(d.getUsername())).collect(Collectors.toList());
            log.info("batchSend===开始发送邮件,需发送邮件个数:{}", collect.size());
            int splitLen = collect.size() / mailsConfig.getMailPoolSize();
            List<List<MailSendLog>> split = CollectionUtil.split(collect, splitLen);
            int size = split.size();
            CountDownLatch countDownLatch = new CountDownLatch(size);
            log.info("batchSend===执行发送任务-总共任务数:{}", size);
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            for (List<MailSendLog> list : split) {
                mailSendAsyncService.executeAsync(list, map, countDownLatch);
            }
            countDownLatch.await();
            stopWatch.stop();
            log.info("batchSend===执行发送任务结束:耗时{}", stopWatch.getTotalTimeSeconds());
        } catch (Exception e) {
            log.error("batchSend===发送失败", e);
        } finally {
            mysqlLock.unlock();
            log.info("batchSend===finally释放锁");
        }
    }


}
