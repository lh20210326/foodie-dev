package com.imooc.mapper;

import com.imooc.my.mapper.MyMapper;
import com.imooc.pojo.ItemsComments;

import java.util.Map;

public interface ItemsCommentsMapperCustomer extends MyMapper<ItemsComments> {
    public void saveComments( Map<String,Object> map);
}