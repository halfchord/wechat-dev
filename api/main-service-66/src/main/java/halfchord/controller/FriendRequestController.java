package halfchord.controller;

import halfchord.service.FriendRequestService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.itzixi.grace.result.GraceJSONResult;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pojo.bo.NewFriendRequestBO;

@RestController
@Slf4j
@RequestMapping("/friendRequest")
public class FriendRequestController {

    @Resource
    private FriendRequestService friendRequestService;

    @PostMapping("/add")
    public GraceJSONResult addFriend(@RequestBody NewFriendRequestBO newFriendRequestBO){
        return friendRequestService.addNewRequest(newFriendRequestBO);
    }
}
