package feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="file-service")
public interface FileMicroServiceFeign {

    @PostMapping("/file/generatorQrCode")
    String generatorQrCode(@RequestParam("wechatNumber") String wechatNumber,
                           @RequestParam("userId") String userId) throws Exception;

}
