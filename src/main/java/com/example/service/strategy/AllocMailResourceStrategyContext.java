package com.example.service.strategy;

import com.example.config.MailsConfig;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 构建分配策略
 * 默认通过 MailsConfig中属性配置
 * {@link MailsConfig#getAllocMailStrategy()}
 *
 * @author zhiqin.zhang
 */
@Service
public class AllocMailResourceStrategyContext implements InitializingBean, ApplicationContextAware {

    @Resource
    private MailsConfig mailsConfig;
    private final Map<String, AllocMailResourceStrategy> saveMaterialStrategyMap = new ConcurrentHashMap<>();

    public AllocMailResourceStrategy getHandler() {
        return this.saveMaterialStrategyMap.get(mailsConfig.getAllocMailStrategy());
    }

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() {
        Map<String, AllocMailResourceStrategy> beanMap = applicationContext.getBeansOfType(AllocMailResourceStrategy.class);
        for (String key : beanMap.keySet()) {
            this.saveMaterialStrategyMap.put(beanMap.get(key).getType(), beanMap.get(key));
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
