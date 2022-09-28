package com.example.common;

import com.example.entity.pojo.TaskLock;
import com.example.enums.TaskStatusEnum;
import com.example.mapper.TaskLockMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zhiqin.zhang
 */
@Component
public class MysqlLock {

    @Resource
    private TaskLockMapper taskLockMapper;

    public boolean lock() {
        TaskLock lock = taskLockMapper.selectForKey(1);
        if (lock == null) {
            TaskLock taskLock = new TaskLock();
            taskLock.setTransactionId(1);
            taskLock.setTaskStatus(TaskStatusEnum.LOCK.getValue());
            return taskLockMapper.insert(taskLock) > 0;
        }
        if (lock.getTaskStatus() == TaskStatusEnum.LOCK.getValue()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void unlock() {
        taskLockMapper.deleteById(1);
    }
}
