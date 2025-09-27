package halfchord.controller;

import halfchord.service.FriendRequestService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.itzixi.grace.result.GraceJSONResult;
import org.springframework.web.bind.annotation.*;
import pojo.bo.NewFriendRequestBO;

import static org.itzixi.base.BaseInfoProperties.HEADER_USER_ID;

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

    @PostMapping("/queryNew")
    public GraceJSONResult queryNew(HttpServletRequest request,
     @RequestParam(defaultValue = "1", name= "page") Integer page,
     @RequestParam(defaultValue = "10", name="pageSize") Integer pageSize)
    {

        String userId = request.getHeader(HEADER_USER_ID);
        return friendRequestService.queryNewFriendList(userId,page,pageSize);
    }

    @PostMapping("/pass")
    public GraceJSONResult pass(@RequestParam("friendRequestId") String friendRequestId,@RequestParam("friendRemark") String friendRemark){

        return friendRequestService.passFriendRequest(friendRemark,friendRequestId);

    }


}
