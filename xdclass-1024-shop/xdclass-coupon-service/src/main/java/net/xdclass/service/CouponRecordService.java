package net.xdclass.service;


import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 阿刚
 * @since 2022-12-08
 */
public interface CouponRecordService {

    /**
     * 分页查询领券记录
     * @param page
     * @param size
     * @return
     */
    Map<String,Object> page(int page ,int size);
}
