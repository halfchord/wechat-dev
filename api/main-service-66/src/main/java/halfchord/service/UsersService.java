package halfchord.service;

import org.itzixi.grace.result.GraceJSONResult;
import pojo.bo.ModifyUserBO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 半弦
 * @since 2025-09-20
 */
public interface UsersService {

      void modify(ModifyUserBO modifyUserBO);

      GraceJSONResult queryById(String userId,boolean needToken);

      GraceJSONResult upDataFace(String userId, String face, boolean b);
}
