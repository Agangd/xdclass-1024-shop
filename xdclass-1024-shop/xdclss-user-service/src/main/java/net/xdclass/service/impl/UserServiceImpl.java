package net.xdclass.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.SendCodeEnum;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.mapper.UserMapper;
import net.xdclass.model.LoginUser;
import net.xdclass.model.UserDO;
import net.xdclass.request.UserLoginRequest;
import net.xdclass.request.UserRegisterRequest;
import net.xdclass.service.NotifyService;
import net.xdclass.service.UserService;
import net.xdclass.util.CommonUtil;
import net.xdclass.util.JWTUtil;
import net.xdclass.util.JsonData;
import net.xdclass.vo.UserVO;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public JsonData register(UserRegisterRequest registerRequest) {

        boolean checkCode = false;
        //校验验证码
        if (StringUtils.isNotBlank(registerRequest.getMail())) {
            checkCode = notifyService.checkCode(SendCodeEnum.USER_REGISTER, registerRequest.getMail(), registerRequest.getCode());
        }
        if (!checkCode) {
            return JsonData.buildResult(BizCodeEnum.CODE_ERROR);
        }

        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(registerRequest, userDO);
        userDO.setCreateTime(new Date());
        userDO.setSlogan("心若有所往，何惧道阻长");
        //设置密码
        //生成密钥 盐
        userDO.setSecret("$1$" + CommonUtil.getStringNumRandom(8));

        //密码加盐处理
        String cryptPwd = Md5Crypt.md5Crypt(registerRequest.getPwd().getBytes(), userDO.getSecret());
        userDO.setPwd(cryptPwd);

        //账号唯一性检查 TODO
        if (checkUnique(userDO.getMail())) {

            int rows = userMapper.insert(userDO);
            log.info("rows:{},注册成功:{}", rows, userDO.toString());

            //新用户注册成功，初始化信息，发放福利 TODO
            userRegisterInitTask(userDO);
            return JsonData.buildSuccess();
        } else {
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_REPEAT);
        }
    }

    /**
     * 用户登录
     * 1、根据mail找是否存在这条记录
     * 2、有则用密钥+用户传递的明文密码，进行加密，再和数据库的密文进行匹配
     * @param userLoginRequest
     * @return
     */
    @Override
    public JsonData login(UserLoginRequest userLoginRequest) {

        List<UserDO> userDOList = userMapper.selectList(new QueryWrapper<UserDO>().eq("mail",userLoginRequest.getMail()));
        if (userDOList != null && userDOList.size() == 1){
            //已经注册
            UserDO userDo = userDOList.get(0);
            String crypt = Md5Crypt.md5Crypt(userLoginRequest.getPwd().getBytes(),userDo.getSecret());

            if (crypt.equals(userDo.getPwd())){
                //登录成功，生成token
                LoginUser loginUser = LoginUser.builder().build();
                BeanUtils.copyProperties(userDo,loginUser);

                String token = JWTUtil.geneJsonWebToken(loginUser);

                return JsonData.buildSuccess(token);
            }
            else {
                //密码错误
                return JsonData.buildResult(BizCodeEnum.ACCOUNT_PWD_ERROR);
            }
        }else {
            //账号未注册
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_UNREGISTER);
        }
    }

    /**
     * 查找用户详情
     * @return
     */
    @Override
    public UserVO findUserDetail() {

        LoginUser loginUser = LoginInterceptor.threadLocal.get();

        UserDO userDO = userMapper.selectOne(new QueryWrapper<UserDO>().eq("id",loginUser.getId()));

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDO,userVO);
        return userVO;
    }

    /**
     * 校验用户账号唯一
     *
     * @param mail
     * @return
     */
    private boolean checkUnique(String mail) {

        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<UserDO>().eq("mail",mail);

        List<UserDO> list = userMapper.selectList(queryWrapper);

        return list.size() > 0 ? false : true;
    }


    /**
     * 用户注册，初始化福利信息 TODO
     *
     * @param userDO
     */
    private void userRegisterInitTask(UserDO userDO) {

    }
}
