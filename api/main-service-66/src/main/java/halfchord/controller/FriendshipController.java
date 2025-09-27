package halfchord.controller;

import halfchord.service.FriendshipService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.itzixi.enums.YesOrNo;
import org.itzixi.grace.result.GraceJSONResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.itzixi.base.BaseInfoProperties.HEADER_USER_ID;


@RestController
@Slf4j
@RequestMapping("/friendship")
public class FriendshipController {

    @Resource
    private FriendshipService friendshipService;

    @PostMapping("/queryMyFriends")
    public GraceJSONResult queryFriend(HttpServletRequest request){

        String myId = request.getHeader(HEADER_USER_ID);

        if(StringUtils.isBlank(myId)){
            return GraceJSONResult.errorMsg("用户id不能为空");
        }

        return friendshipService.queryFriend(myId,false);

    }

    @PostMapping("/getFriendship")
    public GraceJSONResult pass(String friendId,HttpServletRequest request){

        String myId = request.getHeader(HEADER_USER_ID);

        return  friendshipService.getFriendship(myId,friendId);

    }

    @PostMapping("/updateFriendRemark")
    public GraceJSONResult updateFriendRemark(String friendId,String friendRemark,HttpServletRequest request){

        String myId = request.getHeader(HEADER_USER_ID);

        return  friendshipService.updateFriendRemark(myId,friendId,friendRemark);

    }

    @PostMapping("/tobeBlack")
    public GraceJSONResult tobeBlack(String friendId,HttpServletRequest request){

        String myId = request.getHeader(HEADER_USER_ID);

        return  friendshipService.updateBlack(myId,friendId,YesOrNo.YES);

    }

    @PostMapping("/moveOutBlack")
    public GraceJSONResult moveOutBlack(String friendId,HttpServletRequest request){

        String myId = request.getHeader(HEADER_USER_ID);

        return  friendshipService.updateBlack(myId,friendId, YesOrNo.NO);

    }

    @PostMapping("/queryMyBlackList")
    public GraceJSONResult queryMyBlackList(HttpServletRequest request){

        String myId = request.getHeader(HEADER_USER_ID);

        if(StringUtils.isBlank(myId)){
            return GraceJSONResult.errorMsg("用户id不能为空");
        }

        return  friendshipService.queryFriend(myId,true);

    }

    @PostMapping("/delete")
    public GraceJSONResult delete(HttpServletRequest request,String friendId){


        String myId = request.getHeader(HEADER_USER_ID);

        if(StringUtils.isBlank(friendId)){
            return GraceJSONResult.errorMsg("用户id不能为空");
        }

        return  friendshipService.delete(myId,friendId);

    }
}
