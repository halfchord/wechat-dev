package org.itzixi.utils;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.itzixi.base.BaseInfoProperties;
import org.itzixi.grace.result.GraceJSONResult;
import org.itzixi.grace.result.ResponseStatusEnum;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RefreshScope
public class RenderErrorUtils extends BaseInfoProperties {

    public static Mono<Void> display(ServerWebExchange exchange, ResponseStatusEnum statusEnum){

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

}
