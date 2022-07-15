package com.imooc.controller;

import com.imooc.pojo.bo.ShopcartBO;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "购物车接口controller",tags = {"购物车接口相关的api"})
@RequestMapping("shopcart")
@RestController
public class ShopcatConroller {
    @ApiOperation(value = "添加商品到购物车",notes = "添加商品到购物车",httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(
            @RequestParam String userId,
            @RequestBody ShopcartBO shopCartBO,
            HttpServletRequest request,
            HttpServletResponse response) {
        if(StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("");
        }
        System.out.println(shopCartBO);
        // TODO 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis
        return IMOOCJSONResult.ok();
    }


    @ApiOperation(value = "从购物车中删除商品",notes = "从购物车中删除商品",httpMethod = "POST")
    @PostMapping("/del")
    public IMOOCJSONResult del(
            @RequestParam String userId,
            @RequestParam String itemSpecId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        if(StringUtils.isBlank(userId)||StringUtils.isBlank(itemSpecId)){
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }
        // TODO 用户在页面删除购物车的商品数据，如果此时用户已经登录，则需要同步删除后端购物车中的数据
        return IMOOCJSONResult.ok();
    }
}

