package com.imooc.service.center;

import com.imooc.pojo.Orders;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.utils.PagedGridResult;

public interface MyOrdersService {
    public PagedGridResult queryMyOrder(String userId, Integer orderStatus,Integer page,Integer pageSize);

    /**
     * 查询我的订单
     * @param userId
     * @param orderId
     * @return
     */
    public Orders queryMyOrder(String userId,String orderId);

    /**
     * 更新订单
     * @param orderid
     * @return
     */
    public boolean updateReceiveOrderStatus(String orderId);

    /**
     * 删除订单（逻辑删除）
     * @param orderid
     * @return
     */
    public boolean deleteOrder(String userId,String orderId);
}