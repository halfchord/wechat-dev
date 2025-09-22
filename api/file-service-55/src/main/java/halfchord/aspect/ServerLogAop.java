package halfchord.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Slf4j
@Aspect
public class ServerLogAop {

    /*// 环绕通知(* 表示所有返回值类型 指定要通知的包名 ..表示可以扫描到该包下的子包 *表示可以扫描的所有的类 )
    @Around("execution(* halfchord.service.Impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {

        //统计每一个service执行所消耗的时间，如果执行时间太久，则进行error级别的日志输出

        StopWatch stopWatch = new StopWatch();
        //获取方法名称
        String pointName = joinPoint.getTarget().getClass().getName()
                + "."
                + joinPoint.getSignature().getName();

        stopWatch.start("执行主业务"+pointName);
        Object proceed = joinPoint.proceed();
        stopWatch.stop();

        //获取方法执行所消耗的时间
        long takeTimes = stopWatch.getTotalTimeMillis();

        if(takeTimes > 3000){
            log.error("执行位置{}，执行时间太长了，执行耗费了{}毫秒",pointName, takeTimes);
        }else if(takeTimes > 2000){
            log.warn("执行位置{}，执行时间有点长，执行耗费了{}毫秒",pointName, takeTimes);
        }else log.info("执行位置{}，执行耗费了{}毫秒",pointName, takeTimes);

        return proceed;
    }*/
}
