package halfchord.filter;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.itzixi.base.BaseInfoProperties;
import org.itzixi.grace.result.GraceJSONResult;
import org.itzixi.grace.result.ResponseStatusEnum;
import org.itzixi.utils.IPUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RefreshScope
public class IPLimitFilter extends BaseInfoProperties implements GlobalFilter, Ordered {
    /*
    * 需求：
    * 判断某个请求的ip在20s内访问的次数不能超过3次
    * 如果超过三次，则限制访问30秒
    * 等待静默30s后，才能够再次访问
    * */

    @Value("${blackId.continueCounts}")
    private Integer continueCounts;
    @Value("${blackId.timeInterval}")
    private  Integer timeInterval;
    @Value("${blackId.limitTimes}")
    private  Integer limitTimes;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //默认放行请求到后续的路由
        log.info("IPLimit 当前执行顺序为0");

        log.info("最大连续刷新次数{}",continueCounts);
        log.info("ip判断时间间隔{}",timeInterval);
        log.info("黑名单ip限制时间{}",limitTimes);
        /*if(1==1)
        {
            //终止，放回错误信息
            return renderErrorMsg(exchange,ResponseStatusEnum.SYSTEM_ERROR_BLACK_IP);
        }*/
        return doLimit(exchange,chain);
        //return chain.filter(exchange);
    }

    public Mono<Void> doLimit(ServerWebExchange exchange,GatewayFilterChain chain){
        /*//根据请求获取ip
        ServerHttpRequest request = exchange.getRequest();
        String ip = IPUtil.getIP(request);

        //设置正常ip 以及黑名单ip
        final String isRedisKey = "gateWay:ip:" + ip;
        final String isBlackRedisKey = "gateWay:blackIp:" + ip;

        long limitLeftTimes = redis.ttl(isBlackRedisKey);
        //判断是否黑名单
        if(limitLeftTimes>0){
            //是黑名单ip
            return renderErrorMsg(exchange,ResponseStatusEnum.SYSTEM_ERROR_BLACK_IP);
        }

        //不是黑名单ip,判断请求次数
        long requestCounts = redis.increment(isRedisKey, 1);
        if(requestCounts==1){
            //第一次请求，设置过期时间
            redis.expire(isRedisKey,timeInterval);
        }

        //不是第一次请求
        if(requestCounts>continueCounts){
            redis.set(isBlackRedisKey,ip,limitTimes);
            //是黑名单ip
            return renderErrorMsg(exchange,ResponseStatusEnum.SYSTEM_ERROR_BLACK_IP);
        }

        return chain.filter(exchange);*/
        // 根据request获得请求ip
        ServerHttpRequest request = exchange.getRequest();
        String ip = IPUtil.getIP(request);

        // 正常的ip定义
        final String ipRedisKey = "gateway-ip:" + ip;
        // 被拦截的黑名单ip，如果在redis中存在，则表示目前被关小黑屋
        final String ipRedisLimitKey = "gateway-ip:limit:" + ip;

        // 获得当前的ip并且查询还剩下多少时间，如果时间存在（大于0），则表示当前仍然处在黑名单中
        long limitLeftTimes = redis.ttl(ipRedisLimitKey);
        if (limitLeftTimes > 0) {
            // 终止请求，返回错误
            return renderErrorMsg(exchange, ResponseStatusEnum.SYSTEM_ERROR_BLACK_IP);
        }

        // 在redis中获得ip的累加次数
        long requestCounts = redis.increment(ipRedisKey, 1);
        /**
         * 判断如果是第一次进来，也就是从0开始计数，则初期访问就是1，
         * 需要设置间隔的时间，也就是连续请求的次数的间隔时间
         */
        if (requestCounts == 1) {
            redis.expire(ipRedisKey, timeInterval);
        }

        /**
         * 如果还能获得请求的正常次数，说明用户的连续请求落在限定的[timeInterval]之内
         * 一旦请求次数超过限定的连续访问次数[continueCounts]，则需要限制当前的ip
         */
        if (requestCounts > continueCounts) {
            // 限制ip访问的时间[limitTimes]
            redis.set(ipRedisLimitKey, ipRedisLimitKey, limitTimes);
            // 终止请求，返回错误
            return renderErrorMsg(exchange, ResponseStatusEnum.SYSTEM_ERROR_BLACK_IP);
        }

        return chain.filter(exchange);

    }


    public Mono<Void> renderErrorMsg(ServerWebExchange exchange, ResponseStatusEnum statusEnum){

        //获取响应response
        ServerHttpResponse response = exchange.getResponse();
        //构建jsonResult
        GraceJSONResult graceJSONResult = GraceJSONResult.exception(statusEnum);
        //设置响应头类型
        if(!response.getHeaders().containsKey("Content-Type")){
            response.getHeaders().add("Content-Type", MimeTypeUtils.APPLICATION_JSON_VALUE);
        }
        //转换成json并且向response中写入数据
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

        String resultJson = new Gson().toJson(graceJSONResult);

        DataBuffer buffer = response.bufferFactory().wrap(resultJson.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
