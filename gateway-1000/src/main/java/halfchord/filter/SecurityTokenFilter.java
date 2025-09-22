package halfchord.filter;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.itzixi.base.BaseInfoProperties;
import org.itzixi.grace.result.ResponseStatusEnum;
import org.itzixi.utils.RenderErrorUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RefreshScope
public class SecurityTokenFilter extends BaseInfoProperties implements GlobalFilter, Ordered {

    @Resource
    private excludePathProperties excludePathProperties;

    //路径规则匹配器
    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("IPLimit 当前执行顺序为0");

        String url = exchange.getRequest().getURI().getPath();

        log.info("SecurityTokenFilter 当前用户url: {}", url);
        List<String> urls = excludePathProperties.getUrls();

        //匹配排除的url
        if(urls!=null&&!urls.isEmpty()){
            for (String excludeUrl : urls) {
                if(antPathMatcher.match(excludeUrl,url)){
                    //如果匹配到排除的url，则不进行过滤，直接放行
                    return chain.filter(exchange);
                }
            }
        }

        //没匹配到排除的url，则进行过滤
        log.info("SecurityTokenFilter 当前url被拦截" );

        //获取请求头中的用户id和token
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String userId = headers.getFirst(HEADER_USER_ID);
        String userToken = headers.getFirst(HEADER_USER_TOKEN);
        log.info("userId={}",userId);
        log.info("userToken={}",userToken);

        if(StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(userToken)){
            String redisToken = redis.get(REDIS_USER_TOKEN + ":" + userId);
            if(userToken.equals(redisToken)){
                //校验成功，放行
                return chain.filter(exchange);
            }
        }

        //不成功，则返回错误信息
        return RenderErrorUtils.display(exchange, ResponseStatusEnum.UN_LOGIN);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
