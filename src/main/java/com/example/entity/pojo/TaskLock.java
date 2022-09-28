package com.example.entity.pojo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * task_lock
 *
 * @author
 */
@Data
public class TaskLock implements Serializable {

    @TableId(type = IdType.INPUT)
    private Integer transactionId;

    private Integer taskStatus;

    private static final long serialVersionUID = 1L;
}