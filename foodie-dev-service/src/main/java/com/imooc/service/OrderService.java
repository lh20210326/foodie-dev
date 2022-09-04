package com.imooc.service;

import com.imooc.pojo.bo.ShopcartBO;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.OrdersVO;

import java.util.List;

public interface OrderService {
    /**
     * 用于创建订单相关信息
     * @param submitOrderBO
     */
    public OrdersVO createOrder(List<ShopcartBO> shopcartList, SubmitOrderBO submitOrderBO);

    public void updateOrderStatus(String orderId,Integer orderStatus);
}
