package net.xdclass.mapper;

import net.xdclass.model.CouponDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 阿刚
 * @since 2022-12-08
 */
public interface CouponMapper extends BaseMapper<CouponDO> {

    /**
     * 扣减库存
     * @param couponId
     * @return
     */
    int reduceStock(@Param("couponId") long couponId);
}
