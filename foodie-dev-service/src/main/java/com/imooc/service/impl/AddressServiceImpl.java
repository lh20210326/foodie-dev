package com.imooc.service.impl;

import com.imooc.mapper.UserAddressMapper;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;
import com.imooc.service.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private Sid sid;
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {
        UserAddress ua = new UserAddress();
        ua.setUserId(userId);

        return userAddressMapper.select(ua);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addNewUserAddress(AddressBO addressBO) {
        //判断当前用户是否存在地址，如果没有，则新增为‘默认地址’
        Integer isDefault=0;
        List<UserAddress> addressList = this.queryAll(addressBO.getUserId());
        if(addressList==null||addressList.isEmpty()||addressList.size()==0){
            isDefault=1;
        }
        String addressId = sid.nextShort();
        //保存地址到数据库
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO,userAddress);
        userAddress.setId(addressId);
        userAddress.setIsDefault(isDefault);
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());
        userAddressMapper.insert(userAddress);
    }
}










