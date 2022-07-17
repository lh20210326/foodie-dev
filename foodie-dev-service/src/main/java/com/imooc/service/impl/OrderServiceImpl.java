package com.imooc.service.impl;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.OrderItemsMapper;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.pojo.*;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.service.AddressService;
import com.imooc.service.ItemService;
import com.imooc.service.OrderService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private Sid sid;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String createOrder(SubmitOrderBO submitOrderBO) {
        //订单表信息封装
        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        Integer payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();
        //包邮费用
        Integer postAmount=0;
        String orderId = sid.nextShort();
        //创建新订单
        Orders newOrder = new Orders();
        newOrder.setId(orderId);
        newOrder.setUserId(userId);
        UserAddress userAddress = addressService.queryUserAddress(userId, addressId);
        newOrder.setReceiverName(userAddress.getReceiver());
        newOrder.setReceiverMobile(userAddress.getMobile());
        newOrder.setReceiverAddress(userAddress.getProvince()+" "+
                userAddress.getCity()+" "+userAddress.getDistrict()+" "+userAddress.getDetail());

        newOrder.setPostAmount(postAmount);
        newOrder.setPayMethod(payMethod);
        newOrder.setLeftMsg(leftMsg);
        newOrder.setIsComment(YesOrNo.NO.type);
        newOrder.setIsDelete(YesOrNo.NO.type);
        newOrder.setCreatedTime(new Date());
        newOrder.setUpdatedTime(new Date());
        //循环itemSpecIds保存商品信息表
        String[] itemSpecIdArr = itemSpecIds.split(",");
        Integer totalAmount=0;//原价累计
        Integer realPayAmount=0;//优惠后价格累计
        for(String itemSpecId:itemSpecIdArr){
            //TODO 整合redis后，商品数量重新从redis获取
            int buyCounts=1;
            ItemsSpec itemsSpec = itemService.queryItemSpecById(itemSpecId);
            totalAmount+=itemsSpec.getPriceNormal()*buyCounts;
            realPayAmount+=itemsSpec.getPriceDiscount()*buyCounts;
            String itemId = itemsSpec.getItemId();
            Items items = itemService.queryItemById(itemId);
            String imgUrl = itemService.queryItemMainImgById(itemId);

            String subOrderId = sid.nextShort();
            OrderItems subOrderItems = new OrderItems();
            subOrderItems.setId(subOrderId);
            subOrderItems.setOrderId(orderId);
            subOrderItems.setItemId(itemId);
            subOrderItems.setItemName(items.getItemName());
            subOrderItems.setItemImg(imgUrl);
            subOrderItems.setBuyCounts(buyCounts);
            subOrderItems.setItemSpecId(itemSpecId);
            subOrderItems.setItemSpecName(itemsSpec.getName());
            subOrderItems.setPrice(itemsSpec.getPriceDiscount());

            orderItemsMapper.insert(subOrderItems);
            //用户提交订单后扣减库存
            itemService.decreaseItemSpecStock(itemSpecId,buyCounts);
        }
        newOrder.setTotalAmount(totalAmount);
        newOrder.setRealPayAmount(realPayAmount);
        ordersMapper.insert(newOrder);
        //保存订单状态表
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(waitPayOrderStatus);
        return orderId;
    }
}
