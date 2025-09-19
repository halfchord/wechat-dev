package halfchord.filter;

import lombok.extern.slf4j.Slf4j;
import org.itzixi.base.BaseInfoProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class TestFilter extends BaseInfoProperties implements GlobalFilter, Ordered {
    /*
     * 需求：
     * 判断某个请求的ip在20s内访问的次数不能超过3次
     * 如果超过三次，则限制访问30秒
     * 等待静默30s后，才能够再次访问
     * */

    @Override

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //默认放行请求到后续的路由
        log.info("Test 当前执行顺序为0");
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
