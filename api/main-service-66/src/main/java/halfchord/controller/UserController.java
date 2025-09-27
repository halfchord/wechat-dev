package halfchord.controller;

import halfchord.service.UsersService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.itzixi.grace.result.GraceJSONResult;
import org.springframework.web.bind.annotation.*;
import pojo.bo.ModifyUserBO;
import pojo.bo.NewFriendRequestBO;

@RestController
@RequestMapping("/userinfo")
public class UserController {


    @Resource
    private UsersService usersService;
    @PostMapping("/modify")
    public GraceJSONResult modify(@RequestBody ModifyUserBO modifyUserBO) {

        //修改用户信息
        usersService.modify(modifyUserBO);

        //返回用户信息
        return usersService.queryById(modifyUserBO.getUserId(),true);
    }


    @PostMapping("/get")
    public GraceJSONResult getUserInfo(@RequestParam("userId") String userId) {

        //返回用户信息
        return usersService.queryById(userId,false);
    }

    @PostMapping("/updateFace")
    public GraceJSONResult updateFace(
            @RequestParam("userId") String userId,
            @RequestParam("face") String face) {

        //返回用户信息
        return usersService.upDataFace(userId,face,true);
    }

    @PostMapping("/updateFriendCircleBg")
    public GraceJSONResult updateFriendCircleBg(
            @RequestParam("userId") String userId,
            @RequestParam("FriendCircleBg") String FriendCircleBg) {

        //返回用户信息
        return usersService.updateFriendCircleBg(userId,FriendCircleBg,true);
    }

    @PostMapping("/updateChatBg")
    public GraceJSONResult updateChatBg(
            @RequestParam("userId") String userId,
            @RequestParam("ChatBg") String ChatBg) {

        //返回用户信息
        return usersService.updateChatBg(userId,ChatBg,true);
    }
    @PostMapping("queryFriend")
    public GraceJSONResult queryFriend(String queryString, HttpServletRequest request){

        if(StringUtils.isBlank(queryString)){
            return GraceJSONResult.error();
        }

        return usersService.getByWhatNumOrMobile(queryString,request);

    }

}