package com.ncu.oa.admin.controller;

import com.ncu.oa.admin.common.CommonConstant;
import com.ncu.oa.admin.pojo.Captcha;
import com.ncu.oa.admin.pojo.ResultObject;
import com.ncu.oa.admin.service.UserService;
import com.ncu.oa.admin.utils.CaptchaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IDEA
 * author:G.B.Monkey
 * Date:2019/10/23 0023
 * Time:10:53
 */
@Controller
public class LoginController {

    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("/captcha")
    @ResponseBody
    public void captcha(HttpServletRequest request, HttpServletResponse response){
        //输出图像
        try {
            Map map = getCaptcha(response);
            userService.captcha((String) map.get("cookieId"), (String) map.get("captcha"));
            response.setContentType("image/jpeg");
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write((BufferedImage) map.get("image"), "jpeg", outputStream);
            outputStream.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
    @RequestMapping(value = "/captcha/verifyCaptcha",method = RequestMethod.POST)
    @ResponseBody
    public ResultObject verifyCaptcha(HttpServletRequest request, @RequestBody  Map<String, String> map){
        String cookieId = CaptchaUtils.getCookieByName(request, "captcha");
        if (cookieId != null){
            Captcha captcha = userService.getCaptchaFromRedis(cookieId);
            Captcha updateCaptcha = userService.verifyCaptcha(captcha, map);
            userService.updateCaptchaStatus(updateCaptcha);
            // 验证失败
            if (updateCaptcha.getChecked() == 0){
                return new ResultObject(CommonConstant.VERIFY_ERROR, "校验失败");
            }
            // 验证成功
            else if(updateCaptcha.getChecked() == 1){
                return new ResultObject(CommonConstant.VERIFY_SUCCESS, "校验成功");
            }
            // 验证码失效
            else{
                return new ResultObject(CommonConstant.VERIFY_INVALID, "验证码失效");
            }
        }else{
            return new ResultObject(CommonConstant.VERIFY_INVALID, "验证码失效");
        }

    }


    public Map<String, Object> getCaptcha(HttpServletResponse response){
        //生成一个cookieId,并放入response里面
        String cookieId = UUID.randomUUID().toString().replace("-","").toUpperCase();
        CaptchaUtils.addCookie(response, "captcha", cookieId, CommonConstant.CAPTCHA_EXPIRED);
        //生成一个校验码
        String captcha = CaptchaUtils.getCaptcha(5);
        BufferedImage image = CaptchaUtils.getCaptchaImage(captcha);
        Map<String, Object> map = new HashMap<>();
        map.put("image", image);
        map.put("cookieId", cookieId);
        map.put("captcha", captcha);
        return map;
    }
}
