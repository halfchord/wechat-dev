package halfchord.service;

import org.itzixi.grace.result.GraceJSONResult;
import pojo.bo.NewFriendRequestBO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 半弦
 * @since 2025-09-20
 */
public interface FriendRequestService {


      GraceJSONResult addNewRequest(NewFriendRequestBO newFriendRequestBO);

      GraceJSONResult queryNewFriendList(String userId, Integer page, Integer pageSize);

      GraceJSONResult passFriendRequest(String friendRemark, String friendRequestId);

}
