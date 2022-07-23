package com.imooc.controller.center;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "用户信息接口",tags = {"用户信息相关接口"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController {
    @Autowired
    private CenterUserService centerUserService;
    @ApiOperation(value = "修改用户信息",notes = "修改用户信息",httpMethod = "POST")
    @PostMapping("update")
    public IMOOCJSONResult update(
            @ApiParam(name="userId",value = "用户id",required = true)
            @RequestParam String userId,
            @RequestBody @Valid CenterUserBO centerUserBO,
            BindingResult result,
            HttpServletRequest request, HttpServletResponse response){
        //判断BindingResult是否保存错误的验证信息，如果有，则直接return
        if(result.hasErrors()){
            Map<String, String> errorMap = getErrors(result);
            return IMOOCJSONResult.errorMap(errorMap);
        }
        Users users = centerUserService.updateUserInfo(userId, centerUserBO);
        users = setNullProperty(users);
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(users),true);
        // TODO 后续要改，增加令牌token,会整合进redis，分布式会话
        return IMOOCJSONResult.ok();
    }
    private Map<String,String> getErrors(BindingResult result){
        Map<String,String> map=new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for(FieldError error:errorList){
            String errorField = error.getField();
            String errorMsg = error.getDefaultMessage();
            map.put(errorField,errorMsg);
        }
        return map;
    }
    private Users setNullProperty(Users users) {
        users.setPassword(null);
        users.setMobile(null);
        users.setEmail(null);
        users.setCreatedTime(null);
        users.setUpdatedTime(null);
        users.setBirthday(null);
        return users;
    }
}
