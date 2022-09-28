package com.example.common;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 启动服务重置锁的状态
 *
 * @author zhiqin.zhang
 */
@Component
public class CleanRunner implements ApplicationRunner {

    @Resource
    private MysqlLock mysqlLock;

    @Override
    public void run(ApplicationArguments args) {
        mysqlLock.unlock();
    }
}
