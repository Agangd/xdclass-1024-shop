package net.xdclass.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.xdclass.model.ProductDO;
import net.xdclass.mapper.ProductMapper;
import net.xdclass.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.xdclass.vo.ProductVO;
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
 * @since 2022-12-16
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    /**
     * 分页查询商品列表
     * @param page
     * @param size
     * @return
     */
    @Override
    public Map<String, Object> page(int page, int size) {

        Page<ProductDO> pageInfo = new Page<>(page,size);

        IPage<ProductDO> productDOIPage = productMapper.selectPage(pageInfo,null);

        Map<String,Object> pageMap = new HashMap<>(3);
        pageMap.put("total_record",productDOIPage.getTotal());
        pageMap.put("total_page",productDOIPage.getPages());
        pageMap.put("current_data",productDOIPage.getRecords().stream().map(obj -> beansProcess(obj)).collect(Collectors.toList()));
        return pageMap;
    }

    /**
     * 根据id找商品详情
     * @param productId
     * @return
     */
    @Override
    public ProductVO findDetailById(long productId) {

        ProductDO productDO = productMapper.selectById(productId);

        return beansProcess(productDO);

    }

    private ProductVO beansProcess(ProductDO productDO) {
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(productDO,productVO);

        //库存减去锁定的库存
        productVO.setStock(productDO.getStock() - productDO.getLockStock());
        return productVO;
    }
}
