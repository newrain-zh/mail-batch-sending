package com.example.job;

import cn.hutool.core.date.DateUtil;
import com.example.service.IMailBatchSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 批量发送邮件job
 *
 * @author zhiqin.zhang
 */
@Component
@Slf4j
public class BatchSendMailJob {

    @Resource
    private IMailBatchSendService mailBatchService;


    @Scheduled(cron = "${mail.job.cron:0 0/5 * * * ?}")
    public void batchMailJob() {
        log.info("batchMailJob===开始任务");
        Date currDate = new Date();
        String date = DateUtil.format(currDate, "yyyy-MM-dd");
        mailBatchService.start(date);
        log.info("batchMailJob===任务结束");
    }
}
