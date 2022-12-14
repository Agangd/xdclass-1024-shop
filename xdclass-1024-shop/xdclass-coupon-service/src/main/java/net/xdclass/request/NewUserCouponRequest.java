package net.xdclass.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class NewUserCouponRequest {

    @ApiModelProperty(value = "用户id" ,example = "1")
    @JsonProperty("user_id")
    private long userId;

    @ApiModelProperty(value = "用户昵称" ,example = "agang")
    @JsonProperty("name")
    private String name;


}
