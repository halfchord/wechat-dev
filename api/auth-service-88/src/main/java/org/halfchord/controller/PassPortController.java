package org.halfchord.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.itzixi.grace.result.GraceJSONResult;
import org.itzixi.utils.RedisOperator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import task.emailSMSTask;

import static org.itzixi.base.BaseInfoProperties.MOBILE_SMSCODE;

@RestController
@RequestMapping("/passport")
public class PassPortController {
    @Resource
    private emailSMSTask emailSmsTask;

    @Resource
    private RedisOperator redis;

    @GetMapping("/getSMSCode")
    public Object smsTask(String  email, HttpServletRequest request)throws Exception {

        if(StringUtils.isBlank(email)){
            return GraceJSONResult.errorMsg("邮箱不能为空");
        }

        String code = emailSmsTask.sendSMSTask(email);

        //将验证码存入redis
        redis.set(MOBILE_SMSCODE + ":" + email, code,5*60);
        return GraceJSONResult.ok();
    }
}