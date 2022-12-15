package net.xdclass.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.mapper.CouponRecordMapper;
import net.xdclass.model.CouponRecordDO;
import net.xdclass.model.LoginUser;
import net.xdclass.service.CouponRecordService;
import net.xdclass.util.JsonData;
import net.xdclass.vo.CouponRecordVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 阿刚
 * @since 2022-12-08
 */
@Service
@Slf4j
public class CouponRecordServiceImpl implements CouponRecordService {


    @Resource
    private CouponRecordMapper couponRecordMapper;
    /**
     * 分页查询领券记录
     * @param page
     * @param size
     * @return
     */
    @Override
    public Map<String, Object> page(int page, int size) {

        LoginUser loginUser = LoginInterceptor.threadLocal.get();

        QueryWrapper<CouponRecordDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",loginUser.getId()).orderByDesc("create_time");
        //封装分页信息
        Page<CouponRecordDO> pageInfo = new Page<>(page, size);

        IPage<CouponRecordDO> recordIPage = couponRecordMapper.selectPage(pageInfo,queryWrapper);

        Map<String,Object> pageMap = new HashMap<>(3);
        pageMap.put("total_record",recordIPage);
        pageMap.put("total_page",recordIPage.getPages());
        pageMap.put("current_data",recordIPage.getRecords().stream().map(obj -> beanProcess(obj)).collect(Collectors.toList()));
        return pageMap;
    }

    private CouponRecordVO beanProcess(CouponRecordDO couponRecordDO) {

        CouponRecordVO couponRecordVO = new CouponRecordVO();
        BeanUtils.copyProperties(couponRecordDO,couponRecordVO);
        return couponRecordVO;
    }
}
