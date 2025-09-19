package halfchord.filter;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.itzixi.base.BaseInfoProperties;
import org.itzixi.grace.result.GraceJSONResult;
import org.itzixi.grace.result.ResponseStatusEnum;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
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
public class IPLimitFilter extends BaseInfoProperties implements GlobalFilter, Ordered {
    /*
    * 需求：
    * 判断某个请求的ip在20s内访问的次数不能超过3次
    * 如果超过三次，则限制访问30秒
    * 等待静默30s后，才能够再次访问
    * */

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //默认放行请求到后续的路由
        log.info("IPLimit 当前执行顺序为1");
        if(1==1)
        {
            //终止，放回错误信息
            return renderErrorMsg(exchange,ResponseStatusEnum.SYSTEM_ERROR_BLACK_IP);
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
        return 1;
    }
}
