package net.xdclass.mapper;

import net.xdclass.model.CouponTaskDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 阿刚
 * @since 2022-12-28
 */
public interface CouponTaskMapper extends BaseMapper<CouponTaskDO> {

    /**
     * 批量插入
     * @param couponTaskDOList
     * @return
     */
    int insertBatch(@Param("couponTaskDOList") List<CouponTaskDO> couponTaskDOList);

}
