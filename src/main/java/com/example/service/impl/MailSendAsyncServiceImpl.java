package com.example.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.example.entity.dto.BatchMailConfigDto;
import com.example.entity.pojo.MailSendLog;
import com.example.enums.MailSendStatusEnum;
import com.example.service.IMailSendAsyncService;
import com.example.service.IMailSendLogService;
import com.example.utils.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhiqin.zhang
 */
@Service
@Slf4j
public class MailSendAsyncServiceImpl implements IMailSendAsyncService {

    @Resource
    private IMailSendLogService mailSendLogService;

    /**
     * 更新数组大小
     * 影响资源计算、更新速度
     */
    @Value("${batch.update.size:50}")
    private int batchUpdateSize;

    @Async("asyncServiceExecutor")
    @Override
    public void executeAsync(List<MailSendLog> collect, Map<String, BatchMailConfigDto> map, CountDownLatch countDownLatch) {
        Date currTime = new Date();
        List<MailSendLog> updatedList = new ArrayList<>(batchUpdateSize);
        for (MailSendLog sendLogNew : collect) {
            BatchMailConfigDto dto = map.get(sendLogNew.getUsername());
            MailSendLog updateEntity = new MailSendLog();
            updateEntity.setSendTime(new Date());
            Map<String, String> resultMap = MailUtils.sendMail(dto, new String[]{sendLogNew.getToUser()}, sendLogNew.getUsername(), sendLogNew.getSubject());
            updateEntity.setId(sendLogNew.getId());
            updateEntity.setUsername(sendLogNew.getUsername());
            if ("0".equals(resultMap.get("code"))) {
                updateEntity.setSendStatus(MailSendStatusEnum.SUCCESS.getCode());
            } else {
                updateEntity.setSendStatus(MailSendStatusEnum.FAIL.getCode());
                updateEntity.setSendFailResult(resultMap.get("message"));
            }
            updateEntity.setUpdateTime(currTime);
            updatedList.add(updateEntity);
            if (updatedList.size() == batchUpdateSize) {
                log.info("executeAsync===开始批量更新发送邮件记录");
                mailSendLogService.updateBatchById(updatedList);
                log.info("executeAsync===结束批量更新发送邮件记录");
                updatedList.clear();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (CollectionUtil.isNotEmpty(updatedList)) {
            mailSendLogService.updateBatchById(updatedList);
        }
        countDownLatch.countDown();
        log.info("executeAsync===开始执行发送任务结束");
    }
}