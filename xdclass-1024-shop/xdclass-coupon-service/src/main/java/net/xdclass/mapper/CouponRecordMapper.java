package net.xdclass.mapper;

import io.swagger.annotations.ApiParam;
import net.xdclass.model.CouponRecordDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.xdclass.model.CouponTaskDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 阿刚
 * @since 2022-12-08
 */
public interface CouponRecordMapper extends BaseMapper<CouponRecordDO> {

    /**
     * 批量更新优惠券使用记录
     * @param userId
     * @param userState
     * @param lockCouponRecordIds
     * @return
     */
    int lockUserStateBatch(@Param("userId") Long userId, @Param("userState") String userState,@Param("lockCouponRecordIds") List<Long> lockCouponRecordIds);

}
