package task;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.itzixi.utils.SMSUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SMSTask {

    @Resource
    private SMSUtils smsUtils;
    @Async
    public String sendSMSTask(String phone) throws Exception {
        log.info("异步开始发送短信");
        String code = smsUtils.sendSMS(phone);
        return code;
    }
}
