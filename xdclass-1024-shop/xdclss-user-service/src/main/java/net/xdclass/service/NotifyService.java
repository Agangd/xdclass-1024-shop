package net.xdclass.service;

import net.xdclass.util.JsonData;
import net.xdclass.enums.SendCodeEnum;

public interface NotifyService {

    /**
     * 发送验证码
     * @param sendCodeEnum
     * @param to
     * @return
     */
    JsonData sendCode(SendCodeEnum sendCodeEnum, String to );


    /**
     * 判断验证码是否一样
     * @param sendCodeEnum
     * @param to
     * @param code
     * @return
     */
    Boolean checkCode(SendCodeEnum sendCodeEnum,String to ,String code);
}
