package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.pojo.TaskLock;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhiqin.zhang
 */
@Mapper
public interface TaskLockMapper extends BaseMapper<TaskLock> {

    /**
     * 查询lock
     *
     * @param transactionId 锁id 默认1
     * @return
     */
    TaskLock selectForKey(int transactionId);

}