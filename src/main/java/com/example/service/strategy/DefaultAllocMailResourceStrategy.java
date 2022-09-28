package com.example.service.strategy;

import com.example.entity.dto.BatchMailConfigDto;
import com.example.entity.pojo.MailSendLog;
import com.example.enums.AllocMailResourceEnum;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhiqin.zhang
 */
@Service
public class DefaultAllocMailResourceStrategy implements AllocMailResourceStrategy {
    @Override
    public Map<String, Integer> allocMailResource(List<BatchMailConfigDto> mailResourceList, List<MailSendLog> list) {
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
        return count;
    }

    @Override
    public String getType() {
        return AllocMailResourceEnum.DEFAULT.getType();
    }
}
