package com.ncu.oa.admin.service;

import com.github.pagehelper.PageInfo;
import com.ncu.oa.admin.pojo.User;

/**
 * Created with IDEA
 * author:G.B.Monkey
 * Date:2019/10/9 0009
 * Time:19:12
 */
public interface UserService {
    PageInfo<User> getUserList(int start, int length);
}
