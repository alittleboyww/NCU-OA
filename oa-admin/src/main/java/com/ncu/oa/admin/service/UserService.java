package com.ncu.oa.admin.service;

import com.github.pagehelper.PageInfo;
import com.ncu.oa.admin.pojo.Captcha;
import com.ncu.oa.admin.pojo.User;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * Created with IDEA
 * author:G.B.Monkey
 * Date:2019/10/9 0009
 * Time:19:12
 */
public interface UserService {
    PageInfo<User> getUserList(int start, int length);

    Captcha captcha(String cookieId, String captcha);
    Captcha verifyCaptcha(Captcha captcha, Map<String, String> map);
    Captcha getCaptchaFromRedis(String cookieId);
    Captcha updateCaptchaStatus(Captcha captcha);
}
