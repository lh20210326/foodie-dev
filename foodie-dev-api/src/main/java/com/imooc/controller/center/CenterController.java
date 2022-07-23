package com.imooc.controller.center;

import com.imooc.pojo.Users;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "center-用户中心",tags = {"用户中心展示的相关接口"})
@RestController
@RequestMapping("center")
public class CenterController {
    @Autowired
    private CenterUserService centerUserService;
    @ApiOperation(value = "获取用户信息",notes = "获取用户信息",httpMethod = "GET")
    @GetMapping("userInfo")
    public IMOOCJSONResult userInfo(
            @ApiParam(name="userId",value = "用户id",required = true)
            @RequestParam String userId){
        //以下是我自己加的逻辑判断,因为如果用户没有登录非法访问的话，不会直接数据库。
        //------------
        System.out.println(userId);
        if(StringUtils.isBlank(userId)||userId.equals("undefined")){
            return IMOOCJSONResult.errorMsg("异常访问，请先登录！");
        }
        //------------
        Users users = centerUserService.queryUserInfo(userId);
        return IMOOCJSONResult.ok(users);
    }
}
