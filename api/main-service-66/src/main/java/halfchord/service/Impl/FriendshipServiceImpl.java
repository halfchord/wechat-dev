package halfchord.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import halfchord.mapper.FriendshipMapper;
import halfchord.mapper.FriendshipMapperCustom;
import halfchord.service.FriendshipService;
import jakarta.annotation.Resource;
import org.itzixi.base.BaseInfoProperties;
import org.itzixi.enums.YesOrNo;
import org.itzixi.grace.result.GraceJSONResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pojo.Friendship;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 半弦
 * @since 2025-09-20
 */
@Service
public class FriendshipServiceImpl extends BaseInfoProperties implements FriendshipService {


    @Resource
    private FriendshipMapperCustom friendshipMapperCustom;
    @Resource
    private FriendshipMapper friendshipMapper;

    @Override
    public GraceJSONResult queryFriend(String myId,boolean needBlack) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("myId",myId);
        map.put("needBlack",needBlack);
        return GraceJSONResult.ok(friendshipMapperCustom.queryFriend(map));
    }

    @Override
    public GraceJSONResult getFriendship(String myId, String friendId) {
        QueryWrapper<Friendship> queryWrapper = new QueryWrapper<Friendship>()
                .eq("my_id",myId)
                .eq("friend_id",friendId);


        return GraceJSONResult.ok(friendshipMapper.selectOne(queryWrapper));
    }

    @Transactional
    @Override
    public GraceJSONResult updateFriendRemark(String myId, String friendId, String friendRemark) {
        QueryWrapper<Friendship> updateWrapper = new QueryWrapper<Friendship>()
                .eq("my_id",myId)
                .eq("friend_id",friendId);

        Friendship friendship = new Friendship();
        friendship.setFriendRemark(friendRemark);
        friendship.setUpdatedTime(LocalDateTime.now());

        friendshipMapper.update(friendship,updateWrapper);
        return GraceJSONResult.ok();
    }

    @Transactional
    @Override
    public GraceJSONResult updateBlack(String myId, String friendId, YesOrNo yesOrNo) {
        QueryWrapper<Friendship> updateWrapper = new QueryWrapper<Friendship>()
                .eq("my_id",myId)
                .eq("friend_id",friendId);

        Friendship friendship = new Friendship();
        friendship.setIsBlack(yesOrNo.type);
        friendship.setUpdatedTime(LocalDateTime.now());

        friendshipMapper.update(friendship,updateWrapper);
        return GraceJSONResult.ok();
    }

    /*
    * 删除双方之间的朋友关系
    * */
    @Override
    public GraceJSONResult delete(String myId, String friendId) {

        QueryWrapper<Friendship> deleteWrapper1 = new QueryWrapper<Friendship>()
                .eq("my_id",myId)
                .eq("friend_id",friendId);

        friendshipMapper.delete(deleteWrapper1);

        QueryWrapper<Friendship> deleteWrapper2 = new QueryWrapper<Friendship>()
                .eq("my_id",friendId)
                .eq("friend_id",myId);

        friendshipMapper.delete(deleteWrapper2);
        return GraceJSONResult.ok();
    }
}
