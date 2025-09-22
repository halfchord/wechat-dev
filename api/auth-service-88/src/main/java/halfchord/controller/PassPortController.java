package halfchord.controller;

import halfchord.service.UsersService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.itzixi.grace.result.GraceJSONResult;
import org.itzixi.utils.RedisOperator;
import org.springframework.web.bind.annotation.*;
import pojo.bo.RegistLoginBO;
import task.emailSMSTask;

import static org.itzixi.base.BaseInfoProperties.EMAIL_SMSCODE;

@RestController
@RequestMapping("/passport")
public class PassPortController {
    @Resource
    private emailSMSTask emailSmsTask;

    @Resource
    private RedisOperator redis;

    @Resource
    private UsersService usersService;

    @GetMapping("/getSMSCode")
    public Object smsTask(String  email, HttpServletRequest request)throws Exception {

        if(StringUtils.isBlank(email)){
            return GraceJSONResult.errorMsg("邮箱不能为空");
        }

        String code = emailSmsTask.sendSMSTask(email);

        //将验证码存入redis
        redis.set(EMAIL_SMSCODE + ":" + email, code,5*60);
        return GraceJSONResult.ok();
    }

    @PostMapping("/register")
    public GraceJSONResult register(@RequestBody @Valid RegistLoginBO registLoginBo) {

        return usersService.registerAndLogin(registLoginBo);
    }

    @PostMapping("/login")
    public GraceJSONResult login(@RequestBody @Valid RegistLoginBO registLoginBo){

        return usersService.registerAndLogin(registLoginBo);
    }
    @PostMapping("/logout")
    public GraceJSONResult logout(@RequestParam String userId,HttpServletRequest request){

    	return usersService.logout(userId,request);
    }
}