package halfchord.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import halfchord.mapper.FriendRequestMapper;
import halfchord.service.FriendRequestService;
import jakarta.annotation.Resource;
import org.itzixi.base.BaseInfoProperties;
import org.itzixi.enums.FriendRequestVerifyStatus;
import org.itzixi.grace.result.GraceJSONResult;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pojo.FriendRequest;
import pojo.bo.NewFriendRequestBO;

import java.time.LocalDateTime;

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

        return null;
    }
}
