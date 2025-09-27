package halfchord.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import pojo.Friendship;
import pojo.vo.ContactsVO;
import pojo.vo.NewFriendsVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 朋友关系表 Mapper 接口
 * </p>
 *
 * @author 半弦
 * @since 2025-09-20
 */
public interface FriendshipMapperCustom{


    List<ContactsVO> queryFriend(@Param("paramMap") Map<String,Object> map);
}
