package com.imooc.controller;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class BaseController {
    public static final Integer COMMENT_PAGE_SIZE=10;
    public static final Integer PAGE_SIZE=20;
    public static final String FOODIE_SHOPCART="shopcart";
    //用户上传头像的地址
    public static final String IMAGE_USER_FACE_LOCATION = File.separator + "workspaces" +
            File.separator + "images" +
            File.separator + "foodie" +
            File.separator + "faces";


}
