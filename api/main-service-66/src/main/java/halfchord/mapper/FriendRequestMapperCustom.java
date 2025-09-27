package halfchord.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import pojo.FriendRequest;
import pojo.vo.NewFriendsVO;

import java.util.Map;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author 半弦
 * @since 2025-09-20
 */
public interface FriendRequestMapperCustom extends BaseMapper<FriendRequest> {

    Page<NewFriendsVO> queryNewFriend(@Param("page") Page<NewFriendsVO> page,
                                   @Param("paramMap") Map<String,Object> map);
}
