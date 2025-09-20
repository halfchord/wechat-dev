package interceptor;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.itzixi.exceptions.GraceException;
import org.itzixi.grace.result.ResponseStatusEnum;
import org.itzixi.utils.IPUtil;
import org.itzixi.utils.RedisOperator;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@Slf4j
public class EmailSMSInterceptor implements HandlerInterceptor {

    @Resource
    private RedisOperator redis;

    /*
     * 在请求controller之后，渲染视图之前执行
     * */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /*
     * 在请求controller之前执行
     * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取用户IP
        String userIp = IPUtil.getRequestIp(request);
        String key="emailSMS:"+userIp;


        //判断用户ip是否在60s内发送过验证码
        boolean isExist = redis.keyIsExist(key);
        if(isExist){
            log.error("发送验证码的间隔太短了~~~");
            GraceException.display(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
            return false;
        }

        //将用户ip存到redis中，设置过期时间60s
        redis.setnx60s(key, "1");
        /*
        * 请求通过
        * */
        return true;
    }

    /*
    * 在请求controller之后，渲染视图之后执行
    * */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
