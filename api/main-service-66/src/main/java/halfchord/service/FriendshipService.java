package halfchord.service;

import org.itzixi.enums.YesOrNo;
import org.itzixi.grace.result.GraceJSONResult;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 半弦
 * @since 2025-09-20
 */
public interface FriendshipService {

      GraceJSONResult queryFriend(String myId,boolean needBlack);

      GraceJSONResult getFriendship(String myId, String friendId);

      GraceJSONResult updateFriendRemark(String myId, String friendId, String friendRemark);

      GraceJSONResult updateBlack(String myId, String friendId, YesOrNo yesOrNo);

      GraceJSONResult delete(String myId, String friendId);
}
