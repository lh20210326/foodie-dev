package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.resource.FileUpload;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.DateUtil;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "用户信息接口",tags = {"用户信息相关接口"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {
    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private FileUpload fileUpload;

    @ApiOperation(value = "用户头像修改",notes = "用户头像修改",httpMethod = "POST")
    @PostMapping("uploadFace")
    public IMOOCJSONResult uploadFace(
            @ApiParam(name="userId",value = "用户id",required = true)
            @RequestParam String userId,
            @ApiParam(name="file",value = "用户头像",required = true)
                    MultipartFile file,
            HttpServletRequest request, HttpServletResponse response){
        //定义头像保存地址
//        String fileSpace=IMAGE_USER_FACE_LOCATION;
        String fileSpace=fileUpload.getImageUserFaceLocation();
        //在路径上为每个用增加一个自己的文件夹
        String uploadFileSpace= File.separator+userId;
        if(file!=null){
            String fileName = file.getOriginalFilename();
            if(StringUtils.isNotBlank(fileName)){
                FileOutputStream fileOutputStream =null;
                try {
                    //测试git冲突后的
                    String[] fileNameArr = fileName.split("\\.");
                    //获取文件的后缀名
                    String suffix = fileNameArr[fileNameArr.length - 1];
                    if(!suffix.equalsIgnoreCase("png")&&
                            !suffix.equalsIgnoreCase("jpg")&&
                            !suffix.equalsIgnoreCase("jpeg")){
                        return IMOOCJSONResult.errorMsg("图片格式不正确！");
                    }
                    //文件名称重组
                    String newFileName="face-"+userId+"."+suffix;
                    //上传最终位置
                    String finalFacePath=fileSpace+uploadFileSpace+File.separator+newFileName;
                    //用于给web服务访问的地址
                    uploadFileSpace+=("/"+newFileName);
                    File outFile = new File(finalFacePath);
                    if(outFile.getParentFile()!=null){
                        outFile.getParentFile().mkdirs();
                    }
                    fileOutputStream = new FileOutputStream(outFile);
                    InputStream inputStream = file.getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(fileOutputStream!=null){
                        try {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }else {
            return IMOOCJSONResult.errorMsg("文件不能为空");
        }
        String imageServelUrl = fileUpload.getImageServerUrl();
        Users users = centerUserService.updateUserFace(userId, (imageServelUrl + uploadFileSpace)
        +"?t="+ DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN));
        users = setNullProperty(users);
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(users),true);
        // TODO 后续要改，增加令牌token,会整合进redis，分布式会话
        return IMOOCJSONResult.ok(users);
    }



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
