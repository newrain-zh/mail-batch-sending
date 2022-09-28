package com.example.service.strategy;

import cn.hutool.core.collection.CollectionUtil;
import com.example.entity.dto.BatchMailConfigDto;
import com.example.entity.pojo.MailSendLog;
import com.example.enums.AllocMailResourceEnum;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 随机分配邮箱资源策略
 *
 * @author zhiqin.zhang
 */
@Component
public class RandomAllocMailResourceStrategy implements AllocMailResourceStrategy {
    @Override
    public Map<String, Integer> allocMailResource(List<BatchMailConfigDto> mailResourceList, List<MailSendLog> list) {
        //总资源个数
        int mailResourceCount = mailResourceList.stream().mapToInt(BatchMailConfigDto::getCanUsedCount).sum();
        List<String> mailResourceUserName = new ArrayList<>(mailResourceCount);
        mailResourceList.forEach(v -> {
            Integer canUsedCount = v.getCanUsedCount();
            for (int i = 0; i < canUsedCount; i++) {
                mailResourceUserName.add(v.getUsername());
            }
        });
        Collections.shuffle(mailResourceUserName);
        Map<String, Integer> count = new HashMap<>(mailResourceList.size());
        //发送个数
        int sendCount = Math.min(list.size(), mailResourceCount);
        if (sendCount != list.size()) {
            list = CollectionUtil.sub(list, 0, sendCount);
        }
        for (int i = 0; i < list.size(); i++) {
            String username = mailResourceUserName.get(i);
            if (!count.containsKey(username)) {
                count.put(username, 1);
            } else {
                count.put(username, count.get(username) + 1);
            }
            list.get(i).setUsername(mailResourceUserName.get(i));
        }
        return count;
    }

    @Override
    public String getType() {
        return AllocMailResourceEnum.RANDOM.getType();
    }
}
