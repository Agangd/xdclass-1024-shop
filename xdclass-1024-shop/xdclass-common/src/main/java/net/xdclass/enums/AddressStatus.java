package net.xdclass.enums;

/**
 * 收获地址状态
 */
public enum AddressStatus {

    //是默认收货地址
    DEFAULT_STATUS(1),

    //非
    COMMON_STATUS(0);


    private int status;

    private AddressStatus(int status){
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
