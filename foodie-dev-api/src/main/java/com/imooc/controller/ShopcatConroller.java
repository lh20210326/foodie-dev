package com.imooc.controller;

import com.imooc.pojo.bo.ShopcartBO;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(value = "购物车接口controller",tags = {"购物车接口相关的api"})
@RequestMapping("shopcart")
@RestController
public class ShopcatConroller extends BaseController{
    @Autowired
    private RedisOperator redisOperator;
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
        // 需要判断当前购物车中包含已经存在的商品，如果存在则累加购买数量
        String shopcartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        List<ShopcartBO> shopcartList=null;
        if(StringUtils.isNotBlank(shopcartJson)){
            shopcartList= JsonUtils.jsonToList(shopcartJson,ShopcartBO.class);
            boolean isHaving=false;
            for(ShopcartBO sc:shopcartList){
                String tmpSpecId = sc.getSpecId();
                if(tmpSpecId.equals(shopCartBO.getSpecId())){
                    sc.setBuyCounts(sc.getBuyCounts()+shopCartBO.getBuyCounts());
                    isHaving=true;
                }
            }
            if(!isHaving){
                shopcartList.add(shopCartBO);
            }
        }else{
            //redis没有购物车数据，直接把前端对象放到redis
            shopcartList=new ArrayList<>();
            shopcartList.add(shopCartBO);
        }
        //更新购物车数据
        redisOperator.set(FOODIE_SHOPCART+":"+userId,JsonUtils.objectToJson(shopcartList));
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
        String shopcartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        if(StringUtils.isNotBlank(shopcartJson)){
            List<ShopcartBO> shopcartBOList = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);
            for(ShopcartBO sc:shopcartBOList){
                String specId = sc.getSpecId();
                if(specId.equals(itemSpecId)){
                    shopcartBOList.remove(sc);
                    break;
                }
            }
            redisOperator.set(FOODIE_SHOPCART+":"+userId,JsonUtils.objectToJson(shopcartBOList));
        }
        return IMOOCJSONResult.ok();
    }
}

