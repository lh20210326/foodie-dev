package com.imooc.controller;

import com.imooc.enums.PayMethod;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.service.AddressService;
import com.imooc.service.OrderService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "订单相关",tags = {"订单相关的api接口"})
@RestController
@RequestMapping("orders")
public class OrdersConroller extends BaseController{
    @Autowired
    private AddressService addressService;
    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "用户下单",notes = "用户下单",httpMethod = "POST")
    @PostMapping("/create")
    public IMOOCJSONResult create(@RequestBody SubmitOrderBO submitOrderBO,
                                  HttpServletRequest request,
                                  HttpServletResponse response){
        System.out.println(submitOrderBO.toString());
        //1.创建订单
        //2.创建订单后，移除购物车
        //3.向支付中心发送当前订单，用于保存支付中心的订单数据，创建支付中心的
        if(submitOrderBO.getPayMethod()!= PayMethod.WEIXIN.type
                &&submitOrderBO.getPayMethod()!= PayMethod.ALIPAY.type){
            return IMOOCJSONResult.ok();
        }
        String orderId = orderService.createOrder(submitOrderBO);
        //TODO 整合redis之后，完善购物车中的已结算商品清除，并且同步到前端cookie
        CookieUtils.setCookie(request,response,FOODIE_SHOPCART,"",true);
        return IMOOCJSONResult.ok(orderId);
    }


}

