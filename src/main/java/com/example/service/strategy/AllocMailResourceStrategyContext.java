package com.example.service.strategy;

import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhiqin.zhang
 */
@Service
public class AllocMailResourceStrategyContext implements InitializingBean, ApplicationContextAware {

    private final Map<String, AllocMailResourceStrategy> saveMaterialStrategyMap = new ConcurrentHashMap<>();

    public AllocMailResourceStrategy getHandler(String allocType) {
        return this.saveMaterialStrategyMap.get(allocType);
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
