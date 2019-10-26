package com.ncu.oa.admin.service.impl;

import com.github.pagehelper.PageInfo;
import com.ncu.oa.admin.common.CommonConstant;
import com.ncu.oa.admin.mapper.UserMapper;
import com.ncu.oa.admin.pojo.Captcha;
import com.ncu.oa.admin.pojo.User;
import com.ncu.oa.admin.service.UserService;
import com.ncu.oa.admin.utils.CaptchaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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


    @Cacheable(value = "captcha", key = "#cookieId")
    public Captcha captcha(String cookieId, String captcha){
        //正常情况下图片是不需要缓存的   后面等这里调通之后再来处理这个问题
        Captcha imageCaptcha = new Captcha(cookieId, captcha);
        return imageCaptcha;
    }
    @Cacheable(value = "captcha", key = "#cookieId")
    public Captcha getCaptchaFromRedis(String cookieId){
        return null;
    }

    public Captcha verifyCaptcha(Captcha captcha,Map<String, String> map){
        String receivedCaptcha = map.get("captcha");
        if (!StringUtils.isEmpty(receivedCaptcha)){
            if(captcha != null){
                String storeCaptcha = captcha.getCode();
                if (receivedCaptcha.toUpperCase().equals(storeCaptcha)){
                    //为1 表示这个验证码已经接受过校验
                    captcha.setChecked(1);
                    return captcha;
                }
            }else{
                captcha.setChecked(-1);
                return captcha;
            }
        }
        //为0 表示还未接受过校验
        captcha.setChecked(0);
        return captcha;
    }
    @CachePut(cacheNames = "captcha", key = "#captcha.cookieId")
    public Captcha updateCaptchaStatus(Captcha captcha){
        return captcha;
    }
}
