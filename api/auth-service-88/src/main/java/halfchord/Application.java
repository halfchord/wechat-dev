package halfchord;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient //开启服务发现
@SpringBootApplication
@ComponentScan(basePackages = {
        "task",
        "org.itzixi",  // 显式指定RedisOperator所在的包
        "halfchord"  // 其他需要扫描的包
})

@MapperScan(basePackages = "halfchord.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}