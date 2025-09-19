package halfchord.controller;

import jakarta.annotation.Resource;
import org.itzixi.base.BaseInfoProperties;
import org.itzixi.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/g")
public class HelloController extends BaseInfoProperties{

    @GetMapping("/hello")
    public Object hello() {
        return "hello world";
    }

    @GetMapping("/setRedis")
    public Object hello2(String k,String v) {
        redis.set(k,v);
        return "set redis ok";
    }

    @GetMapping("/getRedis")
    public Object hello3(String k) {
        return redis.get(k);
    }
}