package halfchord.service.Impl;

import feign.FileMicroServiceFeign;
import halfchord.mapper.UsersMapper;
import halfchord.service.UsersService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.itzixi.base.BaseInfoProperties;
import org.itzixi.exceptions.GraceException;
import org.itzixi.grace.result.GraceJSONResult;
import org.itzixi.grace.result.ResponseStatusEnum;
import org.itzixi.utils.JsonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import pojo.Users;
import pojo.bo.ModifyUserBO;
import pojo.vo.UsersVO;

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
public class UsersServiceImpl extends BaseInfoProperties implements UsersService {

    @Resource
    private UsersMapper usersMapper;

    @Resource
    private FileMicroServiceFeign fileMicroServiceFeign;

    @Override
    public void modify(ModifyUserBO modifyUserBO) {
        Users PendingUser = new Users();
        String userId = modifyUserBO.getUserId();

        String wechatNum = modifyUserBO.getWechatNum();

        if(StringUtils.isNotBlank(wechatNum)){
            //判断是否已经修改过微信号
            String redisKey = REDIS_USER_ALREADY_UPDATE_WECHAT_NUM +":"+userId;
            if(redis.keyIsExist(redisKey)){
                GraceException.display(ResponseStatusEnum.WECHAT_NUM_ALREADY_MODIFIED_ERROR);
            }else{
                try {
                    String QrCodeUrl = fileMicroServiceFeign.generatorQrCode(wechatNum, userId);
                    PendingUser.setWechatNumImg(QrCodeUrl);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if(StringUtils.isBlank(userId)){
            GraceException.display(ResponseStatusEnum.USER_INFO_UPDATED_ERROR);
        }

        PendingUser.setUpdatedTime(LocalDateTime.now());

        //将数据复制到PendingUser中以便于更新用户信息
        BeanUtils.copyProperties(modifyUserBO, PendingUser);

        PendingUser.setId(userId);

        //更新数据
        usersMapper.updateById(PendingUser);

        //将微信号保存到redis中，判断一年只能修改一次
        if(StringUtils.isNotBlank(wechatNum)){
            redis.setByDays(REDIS_USER_ALREADY_UPDATE_WECHAT_NUM +":"+userId, userId, 365);
        }
    }

    @Override
    public GraceJSONResult queryById(String userId,boolean needToken) {

        Users user = usersMapper.selectById(userId);

        UsersVO userVO = new UsersVO();
        BeanUtils.copyProperties(user, userVO);

        if(needToken){
            String uToken = redis.get(REDIS_USER_TOKEN + ":" + userId);
            userVO.setUserToken(uToken);
        }

        return GraceJSONResult.ok(userVO);
    }

    @Override
    public GraceJSONResult upDataFace(String userId, String face, boolean needToken) {

        ModifyUserBO modifyUserBO = new ModifyUserBO();
        modifyUserBO.setUserId(userId);
        modifyUserBO.setFace(face);
        modify(modifyUserBO);

       return queryById(userId, needToken);
    }
}
