package task;

import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.itzixi.utils.EmailUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class emailSMSTask {

    @Resource
    private EmailUtils emailUtils;

    @Async
    public String sendSMSTask(String email) throws Exception {
        log.info("异步开始发送验证码");
        //生成验证码
        String code = RandomUtil.randomNumbers(6);
        emailUtils.sendSimpleMessage(email,"验证码",code);
        log.info("异步结束发送验证码");
        return code;
    }
}
