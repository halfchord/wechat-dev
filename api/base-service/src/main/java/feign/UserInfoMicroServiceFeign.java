package feign;

import org.itzixi.grace.result.GraceJSONResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value="main-service")
public interface UserInfoMicroServiceFeign {

    @PostMapping("/userinfo/updateFace")
    GraceJSONResult updateFace(
            @RequestParam("userId") String userId,
            @RequestParam("face") String face);


    @PostMapping("/userinfo/updateFriendCircleBg")
    GraceJSONResult updateFriendCircleBg(
            @RequestParam("userId") String userId,
            @RequestParam("FriendCircleBg") String FriendCircleBg);

    @PostMapping("/userinfo/updateChatBg")
   GraceJSONResult updateChatBg(
            @RequestParam("userId") String userId,
            @RequestParam("ChatBg") String ChatBg);
}