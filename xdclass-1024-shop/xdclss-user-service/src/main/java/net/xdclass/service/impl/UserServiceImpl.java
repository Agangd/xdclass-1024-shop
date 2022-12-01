package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.SendCodeEnum;
import net.xdclass.mapper.UserMapper;
import net.xdclass.model.UserDO;
import net.xdclass.request.UserRegisterRequest;
import net.xdclass.service.NotifyService;
import net.xdclass.service.UserService;
import net.xdclass.util.CommonUtil;
import net.xdclass.util.JsonData;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
        //设置密码 TODO
        //生成密钥 盐
        userDO.setSecret("$1$"+ CommonUtil.getStringNumRandom(8));

        //密码加盐处理
        String cryptPwd = Md5Crypt.md5Crypt(registerRequest.getPwd().getBytes(),userDO.getSecret());
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
     * 校验用户账号唯一
     * @param mail
     * @return
     */
    private boolean checkUnique(String mail) {
        return true;
    }


    /**
     * 用户注册，初始化福利信息 TODO
     * @param userDO
     */
    private void userRegisterInitTask(UserDO userDO){

    }
}
