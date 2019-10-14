package com.ncu.oa.admin.controller;

import com.ncu.oa.admin.utils.AliyunOSSUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created with IDEA
 * author:G.B.Monkey
 * Date:2019/9/26 0026
 * Time:23:37
 */
@Controller
public class TestController {
    Logger logger = LoggerFactory.getLogger(TestController.class);
    @GetMapping("/login")
    @ResponseBody
    public String login() {
        logger.info("-------登录");
        UsernamePasswordToken token = new UsernamePasswordToken("123","123456");
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        return "login";
    }
    @GetMapping("/index")
    public String index() {
        logger.info("----------index1");
        return "index";
    }
    @GetMapping("/index2")
    public String index2() {
        logger.info("==========index2");
        return "index";
    }

    @PostMapping("upload/uploadBlog")
    public String upload(MultipartFile file){
        logger.info("==========>上传文件");
        try{
            if(null != file){
                String filename = file.getOriginalFilename();
                if (!"".equals(filename.trim())){
                    File newFile = new File(filename);
                    FileOutputStream os = new FileOutputStream(newFile);
                    os.write(file.getBytes());
                    os.close();
                    file.transferTo(newFile);
                    //上传到OSS
                    String uploadUrl = AliyunOSSUtil.upload(newFile);
                    logger.info(uploadUrl);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "index";
    }
    @GetMapping(value = "toUploadBlog")
    public String toUploadBlog(){
        return "upload";
    }
    @GetMapping(value = "deleteFile")
    @ResponseBody
    public String deleteFile(@RequestParam("url") String url){
        System.out.println("===========删除文件");
        AliyunOSSUtil.deleteFile(url);
        return "success";
    }

}
