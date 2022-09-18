package com.imooc.controller.interceptor;

import com.imooc.controller.BaseController;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class UserTokenInterceptor extends BaseController implements HandlerInterceptor{
    /**
     * 拦截请求，在访问controller调用之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Autowired
    private RedisOperator redisOperator;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("进入到拦截器，被拦截。--。");
        /**
         * false请求被拦截|ture验证ok
         */
        String userId = request.getHeader("headerUserId");
        String userToken = request.getHeader("headerUserToken");
        if(StringUtils.isNotBlank(userId)&&StringUtils.isNotBlank(userToken)){
            String uniqueToken=redisOperator.get(REDIS_USER_TOKEN+":"+userId);
            if(StringUtils.isBlank(uniqueToken)){
                System.out.println("请登录--");
                returnErrorResponse(response,IMOOCJSONResult.errorMsg("请登录"));
                return false;
            }else{
                if(!uniqueToken.equals(userToken)){
                    System.out.println("账号在异地登录");
                    returnErrorResponse(response,IMOOCJSONResult.errorMsg("账号在异地登录"));
                    return false;
                }else{
                    return true;
                }
            }
        }else{
            System.out.println("请登录");
            returnErrorResponse(response,IMOOCJSONResult.errorMsg("请登录"));
            return false;
        }
    }
    public void returnErrorResponse(HttpServletResponse response,
                                    IMOOCJSONResult result){
        OutputStream out=null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            out=response.getOutputStream();
            out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(out!=null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 请求访问controller之后，渲染视图之前
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 请求访问controller之后，渲染视图之后
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
