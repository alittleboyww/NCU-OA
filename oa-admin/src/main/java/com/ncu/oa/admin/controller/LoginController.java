package com.ncu.oa.admin.controller;

import com.ncu.oa.admin.common.CommonConstant;
import com.ncu.oa.admin.pojo.Captcha;
import com.ncu.oa.admin.pojo.ResultObject;
import com.ncu.oa.admin.service.UserService;
import com.ncu.oa.admin.utils.CaptchaUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @GetMapping("/login")
    public String loginPage(){
        Subject subject = SecurityUtils.getSubject();
        //如果用户已经验证过 则跳转到主页 否则跳转到登录页面
        if(subject.isAuthenticated()){
            if (subject.hasRole(CommonConstant.ADMIN)){
                return "redirect:index";
            }else{
                return "redirect:staffIndex";
            }
        }
        return "login";
    }
    @PostMapping("/login")
    public String login(@RequestParam Map<String, String> userInfo, RedirectAttributes redirect){
        logger.info(userInfo.get("userType"));
        //管理员  和 普通员工怎么 区分
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()){
            String username = userInfo.get("username");
            String password = userInfo.get("password");
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            String rememberMe = userInfo.get("rememberMe");
            //是否设置了记住我
            if(CommonConstant.REMEBERME.equals(rememberMe)){
                token.setRememberMe(true);
            }
            try{
                subject.login(token);
            }
            //账号 不存在 返回登录页面  带回返回信息
            catch (UnknownAccountException e){
                logger.info("账号补存在");
                ResultObject message = new ResultObject();
                message.setCode(CommonConstant.NOT_ACCOUNT);
                message.setMessage("用户不存在");
                redirect.addFlashAttribute("message", message);
                return "redirect:login";
            }
            //密码不正确 返回登录页面 带回信息 带回用户名和 remeberMe的状态
            catch (IncorrectCredentialsException e){
                logger.info("密码错误" + "------" + password);
                ResultObject message = new ResultObject();
                message.setCode(CommonConstant.INCORRECT);
                message.setMessage("密码不正确");
                redirect.addFlashAttribute("username", username);
                redirect.addFlashAttribute("rememberMe", rememberMe);
                redirect.addFlashAttribute("message", message);
                return "redirect:login";
            }
        }
        String userType = userInfo.get("userType");
        //如果 传来的参数是管理员登录 并且该用户具有管理员权限则显示登录成功的页面
        if (CommonConstant.ADMIN.equals(userType) && subject.hasRole(CommonConstant.ADMIN)){
            return "redirect:index";
        }
        //传来的参数为admin 但用户不具有管理员角色 则重新跳到登录页面
        else if (CommonConstant.ADMIN.equals(userType) && !subject.hasRole(CommonConstant.ADMIN)){
            subject.logout();

            return "redirect:login";
        }
        //传来的参数为 staff 并且 具有staff角色 跳到员工主页
        else if (CommonConstant.STAFF.equals(userType) && subject.hasRole(CommonConstant.STAFF)){
            return "redirect:staffIndex";
        }else{
            subject.logout();
            return "redirect:login";
        }
    }
    @GetMapping("/index")
    public String index(){
        return "index";
    }
    @GetMapping("/staffIndex")
    public String staffIndex(){
        return "staffIndex";
    }
    @GetMapping("/logout")
    public String logout(){
        SecurityUtils.getSubject().logout();
        return "login";
    }
}
