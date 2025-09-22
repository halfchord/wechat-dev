package halfchord.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import halfchord.mapper.UsersMapper;
import halfchord.service.UsersService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.itzixi.base.BaseInfoProperties;
import org.itzixi.enums.Sex;
import org.itzixi.grace.result.GraceJSONResult;
import org.itzixi.utils.DesensitizationUtil;
import org.itzixi.utils.LocalDateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pojo.Users;
import pojo.bo.RegistLoginBO;
import pojo.vo.UsersVO;

import java.time.LocalDateTime;
import java.util.UUID;

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


    /*@Resource
    private UsersServiceImpl self;          //通过在当前类中注入自身（利用 Spring 的代理特性），再通过注入的代理对象调用 @Transactional 方法,确保事务生效*/

    @Override
    public GraceJSONResult registerAndLogin(RegistLoginBO registLoginBo) {
        //从请求体中获取数据
        String email = registLoginBo.getEmail();
        String smsCode = registLoginBo.getSmsCode();

        //获取redis中的验证码
        String redisSmsCode = redis.get(EMAIL_SMSCODE + ":" + email);

        //判断验证码是否一致
        if(StringUtils.isBlank(redisSmsCode)||!redisSmsCode.equalsIgnoreCase(smsCode)){
            return GraceJSONResult.errorMsg("验证码不正确");
        }

        //根据验证码查询用户是否存在，如果存在则返回用户已存在
        Users user = queryEmailIfExist(email);

        if(user==null){
            user = createUser(email);
        }

        redis.del(EMAIL_SMSCODE + ":" + email);

        //生成用户token,封装VO,返回前端
        UsersVO userVO = convertUsersVO(user);

        return GraceJSONResult.ok(userVO);
    }


    public Users queryEmailIfExist(String email) {

        return usersMapper.selectOne(new QueryWrapper<Users>().eq("email", email));
    }



    @Transactional
    public Users createUser(String email) {

        Users user = new Users();

        user.setMobile("15180680576");

        String nickName = DesensitizationUtil.commonDisplay(user.getMobile());
        user.setNickname(nickName);
        user.setEmail(email);

        //生成随机微信号
        String UUIDStr = UUID.randomUUID().toString();
        String split[] = UUIDStr.split("-");
        String wechatNum ="wx"+split[0]+split[1];
        user.setWechatNum(wechatNum);

        user.setBirthday(LocalDateUtils.parseLocalDate("1980-01-01", LocalDateUtils.DATE_PATTERN));

        user.setSex(Sex.secret.type);
        user.setFriendCircleBg("");
        user.setChatBg("");
        user.setFace("");
        user.setCountry("中国");
        user.setProvince("北京");
        user.setDistrict("");
        user.setWechatNumImg("");
        user.setRealName("天才");

        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());
        usersMapper.insert(user);

        return user;
    }

    public UsersVO convertUsersVO(Users user) {
        //设置分布式会话，保存用户的token令牌，存储到redis中
        String uToken= TOKEN_USER_PREFIX+":"+UUID.randomUUID();

        redis.set(REDIS_USER_TOKEN+":"+user.getId(), uToken);

        //封装VO
        UsersVO userVO = new UsersVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setUserToken(uToken);

        return userVO;
    }

    @Override
    public GraceJSONResult logout(String userId, HttpServletRequest request) {

        //删除分布式会话中redis中保存的token令牌
        redis.del(REDIS_USER_TOKEN+":"+userId);

        return GraceJSONResult.ok();
    }
}
