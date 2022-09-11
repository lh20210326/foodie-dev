package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.pojo.vo.UsersVO;
import com.imooc.service.UserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.utils.RedisOperator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Api(value = "注册登录",tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("passport")
public class PassportController extends BaseController{
    @Autowired
    private UserService userService;
    @Autowired
    private RedisOperator redisOperator;

    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    @ApiOperation(value = "用户名是否存在",notes = "用户名是否存在",httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(@RequestParam String username){
        //判断用户名不能为空
        if(StringUtils.isBlank(username)){
            return IMOOCJSONResult.errorMsg("用户名不能为空");
        }
        //查找注册的用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if(isExist){
            return IMOOCJSONResult.errorMsg("用户名已经存在");
        }
        //请求成功
        return IMOOCJSONResult.ok();
    }
    /**
     *  注册
     * @param username
     * @return
     */
    @ApiOperation(value = "用户注册",notes = "用户注册",httpMethod = "POST")
    @PostMapping("/regist")
    public IMOOCJSONResult regist(@RequestBody UserBO userBO,
                                  HttpServletRequest request,
                                  HttpServletResponse response){
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPwd = userBO.getConfirmPassword();
        //1.判断用户名和密码必须不为空
        if(StringUtils.isBlank(username)||StringUtils.isBlank(password)
                ||StringUtils.isBlank(confirmPwd)){
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }
        //2.查询用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if(isExist){
            return IMOOCJSONResult.errorMsg("用户名已经存在");
        }
        //3.密码长度不能少于6位
        if(password.length()<6){
            return IMOOCJSONResult.errorMsg("密码长度不能少于6");
        }
        //4.判断两次密码是否一致
        if(!password.equals(confirmPwd)){
            return IMOOCJSONResult.errorMsg("两次密码输入不一致");
        }
        //5.实现注册
        Users users = userService.createUser(userBO);

//        users=setNullProperty(users);
        UsersVO usersVO = convertUsersVO(users);
        //保存信息到cookie
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(usersVO),true);
        return IMOOCJSONResult.ok();
    }

    /**
     * 登录
     * @param userBO
     * @return
     */
    @ApiOperation(value = "用户登录",notes = "用户登录",httpMethod = "POST")
    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody UserBO userBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response){
        String username = userBO.getUsername();
        String password = userBO.getPassword();

        //判断用户名和密码必须不为空
        if(StringUtils.isBlank(username)||StringUtils.isBlank(password)){
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }
        //实现登录
        Users users = null;
        try {
            users = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(users==null){
            return IMOOCJSONResult.errorMsg("用户名或密码不正确");
        }
//        users=setNullProperty(users);
        UsersVO usersVO = convertUsersVO(users);

        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(usersVO),true);
        //TODO 同步购物车数据
        return IMOOCJSONResult.ok(users);
    }
    @ApiOperation(value = "用户退出登录",notes = "用户退出登录",httpMethod = "POST")
    @PostMapping("/logout")
    /**
     * 退出登录
     * @param users
     * @return
     */
    public IMOOCJSONResult logout(@RequestParam String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response){
        //清楚用户相关cookie
        CookieUtils.deleteCookie(request,response,"user");
        //用户退出登录，需要清空购物车
        //分布式会话需要清除redis用户数据
        redisOperator.del(REDIS_USER_TOKEN+":"+userId);
        return IMOOCJSONResult.ok();
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
    private UsersVO convertUsersVO(Users users){
        //redis实现用户的分布式会话
        String uniqueToken= UUID.randomUUID().toString().trim();
        UsersVO usersVO = new UsersVO();
        //比如密码手机号邮箱多余的不会拷贝过去
        BeanUtils.copyProperties(users,usersVO);
        usersVO.setUserUniqueToken(uniqueToken);
        redisOperator.set(REDIS_USER_TOKEN+":"+users.getId(),uniqueToken,30*60);
        return usersVO;
    }
}







