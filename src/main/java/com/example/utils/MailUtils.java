package com.example.utils;

import com.example.entity.dto.BatchMailConfigDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author zhiqin.zhang
 */
@Slf4j
public class MailUtils {

    private MailUtils() {

    }

    /**
     * 发送邮件
     *
     * @param reachMailServiceConfig 邮箱配置
     * @param strings                接受人
     * @param subject                邮件主题
     * @param content                邮件内容
     * @return
     */
    public static Map<String, String> sendMail(BatchMailConfigDto reachMailServiceConfig, String[] strings, String subject, String content) {
        //创建一个复杂的邮寄对象
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(reachMailServiceConfig.getHost());
        javaMailSender.setUsername(reachMailServiceConfig.getUsername());
        javaMailSender.setPassword(reachMailServiceConfig.getPassword());
        javaMailSender.setProtocol(reachMailServiceConfig.getProtocol());
        Properties properties = new Properties();
        //开启认证
        properties.setProperty("mail.smtp.auth", "true");
        //设置链接超时
        properties.setProperty("mail.smtp.timeout", "60000");
        //设置端口
        properties.setProperty("mail.smtp.port", reachMailServiceConfig.getPort().toString());
        if (reachMailServiceConfig.getIsSsl() != null) {
            switch (reachMailServiceConfig.getIsSsl()) {
                case 1:
                    //启用调试
                    properties.setProperty("mail.debug", "true");
                    //设置ssl端口
                    properties.setProperty("mail.smtp.socketFactory.port", Integer.toString(reachMailServiceConfig.getPort()));
                    properties.setProperty("mail.smtp.socketFactory.fallback", "false");
                    properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                    break;
                case 2:
                    //tls
                    properties.setProperty("mail.smtp.starttls.enable", "true");
                    //properties.put("mail.smtp.ssl.protocols", "TLSv1.2");//设置使用TLS1.2版本
                default:
                    break;
            }
        }
        javaMailSender.setJavaMailProperties(properties);
        Map<String, String> resultMap = new HashMap<>(2);
        resultMap.put("code", "0");
        resultMap.put("message", "");
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            //创建一个MimeMessageHelper，来组装参数 true表示开启附件，utf-8表示编码
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            //1、设置发送人
            mimeMessageHelper.setFrom(reachMailServiceConfig.getUsername(), reachMailServiceConfig.getFromName());
            //2、设置接收人
            mimeMessageHelper.setTo(strings);
            mimeMessageHelper.setCc(reachMailServiceConfig.getUsername());
            //3、设置主题
            mimeMessageHelper.setSubject(subject);
            //拼接内容参数
            //4、设置内容，可以被html解析
            mimeMessageHelper.setText(content, true);
            // 5、发送邮件
            javaMailSender.send(mimeMessage);
        } catch (MailException | MessagingException | UnsupportedEncodingException e) {
            //TODO 错误类型进行细分且记录并可以生成类似报表最佳
            log.error("错误:", e);
            resultMap.put("code", "1");
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }
}
