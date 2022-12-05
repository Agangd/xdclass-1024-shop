package net.xdclass.service;

import net.xdclass.request.UserLoginRequest;
import net.xdclass.request.UserRegisterRequest;
import net.xdclass.util.JsonData;

/**
 * 用户注册服务
 * 邮箱验证码验证
 * 密码加密
 * 账号唯一性检查
 * 插入数据库
 * 新注册用户福利发放
 */
public interface UserService {

    /**
     * 用户注册
     * @param registerRequest
     * @return
     */
    JsonData register(UserRegisterRequest registerRequest);

    /**
     * 用户登录
     * @param userLoginRequest
     * @return
     */
    JsonData login(UserLoginRequest userLoginRequest);
}
