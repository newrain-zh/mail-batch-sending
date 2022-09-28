package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.pojo.MailSendLog;
import com.example.mapper.MailSendLogMapper;
import com.example.service.IMailSendLogService;
import org.springframework.stereotype.Service;

/**
 * @author zhiqin.zhang
 */
@Service
public class MailSendLogServiceImpl extends ServiceImpl<MailSendLogMapper, MailSendLog> implements IMailSendLogService {
}
