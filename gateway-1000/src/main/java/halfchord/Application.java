package halfchord;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@EnableDiscoveryClient //开启服务发现
@SpringBootApplication
@ComponentScan(basePackages = {
        "org.itzixi.base",  // 显式指定RedisOperator所在的包
        "org.itzixi.utils",  // 显式指定RedisOperator所在的包
        "halfchord"  // 其他需要扫描的包
})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}