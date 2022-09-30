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

    /**
     * 加锁
     *
     * @return true = 可使用 false=不可使用
     */
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

    /**
     * 检测锁
     *
     * @return true=有锁状态 false=无锁状态
     */
    public boolean checkLock() {
        TaskLock lock = taskLockMapper.selectForKey(1);
        if (lock == null) {
            return Boolean.FALSE;
        }
        if (lock.getTaskStatus() == TaskStatusEnum.LOCK.getValue()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 解锁
     */
    public void unlock() {
        taskLockMapper.deleteById(1);
    }
}
