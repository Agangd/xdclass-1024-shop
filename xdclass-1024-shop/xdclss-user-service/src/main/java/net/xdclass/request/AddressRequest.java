package net.xdclass.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "地址对象",description = "新增收获地址对象")
public class AddressRequest {

    /**
     * 是否默认收货地址：0->否；1->是
     */
    @ApiModelProperty(value = "是否默认收货地址",example = "0")
    @JsonProperty("default_status")
    private Integer defaultStatus;

    /**
     * 收发货⼈姓名
     */
    @ApiModelProperty(value = "收发货⼈姓名",example = "小陈同学")
    @JsonProperty("receive_name")
    private String receiveName;

    /**
     * 收货⼈电话
     */
    @ApiModelProperty(value = "收货⼈电话",example = "17135615806")
    private String phone;

    /**
     * 省/直辖市
     */
    @ApiModelProperty(value = "省/直辖市",example = "安徽省")
    private String province;

    /**
     * 市
     */
    @ApiModelProperty(value = "市",example = "池州市")
    private String city;

    /**
     * 区
     */
    @ApiModelProperty(value = "区",example = "胜利镇")
    private String region;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址",example = "运营中心")
    private String detailAddress;

}
