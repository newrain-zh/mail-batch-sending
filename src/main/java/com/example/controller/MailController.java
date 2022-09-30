package com.example.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.common.Result;
import com.example.entity.dto.BatchMailConfigDto;
import com.example.service.IMailBatchSendService;
import com.example.service.IMailResourceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 邮件资源api
 *
 * @author zhiqin.zhang
 */
@RestController
@RequestMapping("/mail")
public class MailController {

    @Resource
    private IMailResourceService mailResourceService;

    @Resource
    private IMailBatchSendService mailBatchSendService;

    @GetMapping("/detail")
    public Result<List<BatchMailConfigDto>> detail() {
        return Result.success(mailResourceService.getMailResourceList());
    }

    @GetMapping("/start")
    public Result<Void> start(@RequestParam("startDate") String startDate) {
        if (StringUtils.isBlank(startDate)) {
            startDate = DateUtil.format(new Date(), "yyyy-MM-dd");
        }
        mailBatchSendService.start(startDate);
        return Result.success();
    }

    @GetMapping("/start/error")
    public Result<Void> startByError(@RequestParam("startDate") String startDate) {
        if (StringUtils.isBlank(startDate)) {
            startDate = DateUtil.format(new Date(), "yyyy-MM-dd");
        }
        mailBatchSendService.startByError(startDate);
        return Result.success();
    }
}
