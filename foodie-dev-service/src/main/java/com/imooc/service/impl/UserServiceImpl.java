package com.imooc.service.impl;

import com.imooc.enums.Sex;
import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    public UsersMapper usersMapper;
    @Autowired
    private Sid sid;
    private static final String USER_FACE="https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fup.enterdesk.com%2Fedpic_source%2Fe3%2Fae%2F0a%2Fe3ae0a982d289de2d90fce4d0aa808c8.jpg&refer=http%3A%2F%2Fup.enterdesk.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1659101812&t=f005e5ae204e497c2d25ea2eba0b8d91";
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
//        try {
//            Thread.sleep(3500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Example userExample = new Example(Users.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("username",username);

        Users users = usersMapper.selectOneByExample(userExample);
        return users == null ? false : true;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserBO userBO) {
        String userId = sid.nextShort();
        Users user = new Users();
        user.setId(userId);
        user.setUsername(userBO.getUsername());
        try {
            user.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //默认用户昵称同用户名
        user.setNickname(userBO.getUsername());
        //默认头像
        user.setFace(USER_FACE);
        //设置默认生日
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        //设置性别(默认保密)
        user.setSex(Sex.secret.type);
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());
        usersMapper.insert(user);
        return user;
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {
        Example userExample = new Example(Users.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("username",username);
        userCriteria.andEqualTo("password",password);
        Users users = usersMapper.selectOneByExample(userExample);
        return users;
    }
}
