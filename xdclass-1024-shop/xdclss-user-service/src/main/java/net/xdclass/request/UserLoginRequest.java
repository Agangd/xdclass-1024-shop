package net.xdclass.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "登录对象",description = "用户登录请求对象")
public class UserLoginRequest {

    @ApiModelProperty(value = "邮箱",example = "1613853412@qq.com")
    private String mail;

    private String pwd;


}
