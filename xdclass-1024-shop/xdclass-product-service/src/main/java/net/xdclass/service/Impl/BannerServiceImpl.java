package net.xdclass.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.model.BannerDO;
import net.xdclass.mapper.BannerMapper;
import net.xdclass.service.BannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.xdclass.vo.BannerVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 阿刚
 * @since 2022-12-16
 */
@Service
@Slf4j
public class BannerServiceImpl implements BannerService {

    @Resource
    private BannerMapper bannerMapper;


    @Override
    public List<BannerVO> list() {

        List<BannerDO> bannerDOList = bannerMapper.selectList(new QueryWrapper<BannerDO>().orderByAsc("weight"));

        List<BannerVO> bannerVOList = bannerDOList.stream().map(obj -> {
            BannerVO bannerVO = new BannerVO();
            BeanUtils.copyProperties(obj,bannerVO);
            return bannerVO;
        }).collect(Collectors.toList());
        return bannerVOList;
    }
}
