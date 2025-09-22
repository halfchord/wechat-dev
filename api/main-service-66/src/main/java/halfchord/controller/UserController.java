package halfchord.controller;

import halfchord.service.UsersService;
import jakarta.annotation.Resource;
import org.itzixi.grace.result.GraceJSONResult;
import org.springframework.web.bind.annotation.*;
import pojo.bo.ModifyUserBO;

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
}