package net.xdclass.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel(value = "用户注册服务",description = "用户注册服务")
@Data
public class UserRegisterRequest {

    @ApiModelProperty(value = "昵称",example = "agang")
    private String name;

    @ApiModelProperty(value = "密码",example = "123456")
    private String pwd;

    @ApiModelProperty(value = "头像",example = "https://xdclass-1024-shop-user-service-agang.oss-cn-hangzhou.aliyuncs.com/user/2022-11-30/0e6502b18a474ffcba92380bf360121e.jpeg")
    @JsonProperty("head_img")
    private String headImg;

    @ApiModelProperty(value = "用户个人签名",example = "人生需要动态规划，学习需要贪心算法")
    private String slogan;

    @ApiModelProperty(value = "0表示女，1表示男",example = "1")
    private String sex;

    @ApiModelProperty(value = "邮箱",example = "1613853412@qq.com")
    private String mail;

    @ApiModelProperty(value = "验证码",example = "2333")
    private String code;
}
