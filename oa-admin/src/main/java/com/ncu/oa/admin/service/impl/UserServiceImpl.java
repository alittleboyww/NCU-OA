package com.ncu.oa.admin.service.impl;

import com.github.pagehelper.PageInfo;
import com.ncu.oa.admin.mapper.UserMapper;
import com.ncu.oa.admin.pojo.User;
import com.ncu.oa.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IDEA
 * author:G.B.Monkey
 * Date:2019/10/9 0009
 * Time:19:13
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageInfo<User> getUserList(int start, int length) {
        PageInfo<User> pageInfo = new PageInfo<>();
        //获取用户数量
        int userCount = userMapper.count();
        //获取用户列表
        List<User> userList = userMapper.getUserList(start, length);

        return null;
    }
}
