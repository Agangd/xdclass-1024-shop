package net.xdclass.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.AddressStatus;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.mapper.AddressMapper;
import net.xdclass.model.AddressDO;
import net.xdclass.model.LoginUser;
import net.xdclass.request.AddressRequest;
import net.xdclass.service.AddressService;
import net.xdclass.vo.AddressVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;
    @Override
    public AddressVO detail(Long id) {

        AddressDO addressDO = addressMapper.selectOne(new QueryWrapper<AddressDO>().eq("id", id));

        if(addressDO == null){
            return null;
        }
        AddressVO addressVO = new AddressVO();
        BeanUtils.copyProperties(addressDO,addressVO);


        return addressVO;
    }

    /**
     * 新增收获地址
     * @param addressRequest
     */
    @Override
    public void add(AddressRequest addressRequest) {

        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        AddressDO addressDO = new AddressDO();
        addressDO.setCreateTime(new Date());
        addressDO.setUserId(loginUser.getId());

        BeanUtils.copyProperties(addressRequest, addressDO);

        //是否有默认收获地址
        if (addressDO.getDefaultStatus() == AddressStatus.DEFAULT_STATUS.getStatus()) {
            //查找数据库是否有默认地址，若有则修改原默认地址
            AddressDO defaultAddressDO = addressMapper.selectOne(new QueryWrapper<AddressDO>()
                    .eq("user_id",loginUser.getId())
                    .eq("default_status",AddressStatus.DEFAULT_STATUS.getStatus()));
            if (defaultAddressDO != null){
                //修改为非默认地址
                defaultAddressDO.setDefaultStatus(AddressStatus.COMMON_STATUS.getStatus());
                addressMapper.update(defaultAddressDO,new QueryWrapper<AddressDO>().eq("id",defaultAddressDO.getId()));
                log.info("修改原来");
            }
        }

        int rows = addressMapper.insert(addressDO);
        log.info("新增收货地址:rows={},data={}",rows,addressDO);
    }

    /**
     * 删除收获地址
     * @param addressId
     * @return
     */
    @Override
    public int del(int addressId) {

//        int rows = addressMapper.deleteById(addressId);

        int rows = addressMapper.delete(new QueryWrapper<AddressDO>().eq("id",addressId));

        return rows;
    }
}
