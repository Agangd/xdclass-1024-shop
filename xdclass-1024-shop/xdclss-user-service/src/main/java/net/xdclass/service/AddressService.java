package net.xdclass.service;

import net.xdclass.request.AddressRequest;
import net.xdclass.vo.AddressVO;

public interface AddressService {

    /**
     * 根据id查找详情
     * @param id
     * @return
     */
    AddressVO detail(Long id);

    /**
     * 新增收获地址
     * @param addressRequest
     */
    void add(AddressRequest addressRequest);

    /**
     * 根据id删除地址
     * @param addressId
     * @return
     */
    int del(int addressId);
}
