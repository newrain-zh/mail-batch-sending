package com.example.service.strategy;

import com.example.entity.dto.BatchMailConfigDto;
import com.example.entity.pojo.MailSendLog;
import com.example.enums.AllocMailResourceEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认分配策略
 *
 * @author zhiqin.zhang
 */
@Service
@Slf4j
public class DefaultAllocMailResourceStrategy implements AllocMailResourceStrategy {
    @Override
    public void allocMailResource(List<BatchMailConfigDto> mailResourceList, List<MailSendLog> list) {
        Map<String, Integer> count = new HashMap<>(mailResourceList.size());
        int hasUserNameTotal = 0;
        for (BatchMailConfigDto configNewDto : mailResourceList) {
            Integer canUsedCount = configNewDto.getCanUsedCount();
            if (hasUserNameTotal >= list.size()) {
                break;
            }
            if (hasUserNameTotal + canUsedCount >= list.size()) {
                canUsedCount = list.size() - hasUserNameTotal;
            }
            if (canUsedCount == 0) {
                continue;
            }
            for (int j = hasUserNameTotal; j < hasUserNameTotal + canUsedCount; j++) {
                list.get(j).setUsername(configNewDto.getUsername());
            }
            hasUserNameTotal += canUsedCount;
            count.put(configNewDto.getUsername(), canUsedCount);
        }
        log.debug("allocMailResource 分配资源:{}", count);
    }

    @Override
    public String getType() {
        return AllocMailResourceEnum.DEFAULT.getType();
    }
}
