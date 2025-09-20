package org.halfchord.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.itzixi.grace.result.GraceJSONResult;
import org.itzixi.utils.RedisOperator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import task.SMSTask;

import static org.itzixi.base.BaseInfoProperties.MOBILE_SMSCODE;

@RestController
@RequestMapping("/passport")
public class PassPortController {
    @Resource
    private SMSTask smsTask;

    @Resource
    private RedisOperator redis;

    @GetMapping("/getSMSCode")
    public Object smsTask(String  phone, HttpServletRequest request)throws Exception {
        if(StringUtils.isBlank(phone)){
            return GraceJSONResult.errorMsg("手机号不能为空");
        }

        String code = smsTask.sendSMSTask("15180680576");

        //将验证码存入redis
        redis.set(MOBILE_SMSCODE + ":" + phone, code,5*60);

        return GraceJSONResult.ok();
    }
}