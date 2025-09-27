package halfchord.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import halfchord.mapper.FriendRequestMapper;
import halfchord.mapper.FriendRequestMapperCustom;
import halfchord.mapper.FriendshipMapper;
import halfchord.mapper.FriendshipMapperCustom;
import halfchord.service.FriendRequestService;
import jakarta.annotation.Resource;
import org.itzixi.base.BaseInfoProperties;
import org.itzixi.enums.FriendRequestVerifyStatus;
import org.itzixi.enums.YesOrNo;
import org.itzixi.grace.result.GraceJSONResult;
import org.itzixi.utils.PagedGridResult;
import org.springframework.beans.BeanUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pojo.FriendRequest;
import pojo.Friendship;
import pojo.bo.NewFriendRequestBO;
import pojo.vo.NewFriendsVO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 半弦
 * @since 2025-09-20
 */
@Service
public class FriendRequestServiceImpl extends BaseInfoProperties implements FriendRequestService {

    @Resource
    private FriendRequestMapper friendRequestMapper;
    @Resource
    private FriendRequestMapperCustom friendRequestMapperCustom;
    @Autowired
    private FriendshipMapper friendshipMapper;
    @Autowired
    private FriendshipMapperCustom friendshipMapperCustom;

    @Transactional
    @Override
    public GraceJSONResult addNewRequest(NewFriendRequestBO newFriendRequestBO) {

        //删除之前已有的记录
        QueryWrapper<FriendRequest> delWrapper = new QueryWrapper<FriendRequest>()
                .eq("my_id",newFriendRequestBO.getMyId())
                .eq("friend_id",newFriendRequestBO.getFriendId());

        friendRequestMapper.delete(delWrapper);

        FriendRequest pendingFriendRequest = new FriendRequest();

        //再次新增记录
        BeanUtils.copyProperties(newFriendRequestBO,pendingFriendRequest);

        pendingFriendRequest.setVerifyStatus(FriendRequestVerifyStatus.WAIT.type);
        pendingFriendRequest.setRequestTime(LocalDateTime.now());

        friendRequestMapper.insert(pendingFriendRequest);

        return GraceJSONResult.ok();
    }


    @Override
    public GraceJSONResult queryNewFriendList(String userId, Integer page, Integer pageSize) {

        Map<String,Object> map = new HashMap<>();
        map.put("mySelfId",userId);
        Page<NewFriendsVO> pageInfo = new Page<>(page, pageSize);
        friendRequestMapperCustom.queryNewFriend(pageInfo, map);

        PagedGridResult result = setterPagedGridPlus(pageInfo);

        return GraceJSONResult.ok(result);
    }


    @Transactional
    @Override
    public GraceJSONResult passFriendRequest(String friendRemark, String friendRequestId) {

        //获取其他人发送的好友请求，其中朋友Id就是要处理请求时自己的Id
        FriendRequest request = getSingle(friendRequestId);
        String friendId = request.getMyId();
        String mySelfId = request.getFriendId();


        LocalDateTime nowTime =LocalDateTime.now();
        //创建双方的朋友关系
        Friendship friendshipSelf = new Friendship();
        friendshipSelf.setMyId(mySelfId);
        friendshipSelf.setFriendId(friendId);
        friendshipSelf.setFriendRemark(friendRemark);
        friendshipSelf.setIsBlack(YesOrNo.NO.type);
        friendshipSelf.setIsMsgIgnore(YesOrNo.NO.type);
        friendshipSelf.setCreatedTime(nowTime);
        friendshipSelf.setUpdatedTime(nowTime);

        Friendship friendshipOpposite = new Friendship();
        friendshipOpposite.setMyId(friendId);
        friendshipOpposite.setFriendId(mySelfId);
        friendshipOpposite.setFriendRemark(request.getFriendRemark());
        friendshipOpposite.setIsBlack(YesOrNo.NO.type);
        friendshipOpposite.setIsMsgIgnore(YesOrNo.NO.type);
        friendshipOpposite.setCreatedTime(nowTime);
        friendshipOpposite.setUpdatedTime(nowTime);

        friendshipMapper.insert(friendshipSelf);
        friendshipMapper.insert(friendshipOpposite);

        //在通过对方的请求后，修改双方的好友标记为通过
        request.setVerifyStatus(FriendRequestVerifyStatus.SUCCESS.type);
        friendRequestMapper.updateById(request);

        //还有一种情况，对方添加我的请求过期，所以在验证时，我们会向对方发送请求
        QueryWrapper<FriendRequest> updateWrapper = new QueryWrapper<FriendRequest>()
                .eq("my_id",friendId)
                .eq("friend_id",mySelfId);
        FriendRequest oppositeFriendRequest = new FriendRequest();
        oppositeFriendRequest.setVerifyStatus(FriendRequestVerifyStatus.SUCCESS.type);
        friendRequestMapper.update(oppositeFriendRequest,updateWrapper);
        return GraceJSONResult.ok();
    }

    private FriendRequest getSingle(String friendRequestId){

        return friendRequestMapper.selectById(friendRequestId);
    }


}
