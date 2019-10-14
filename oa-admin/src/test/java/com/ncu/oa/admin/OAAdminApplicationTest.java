package com.ncu.oa.admin;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created with IDEA
 * author:G.B.Monkey
 * Date:2019/9/26 0026
 * Time:23:13
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class OAAdminApplicationTest {
    @Test
    public void testLogin(){
        UsernamePasswordToken token = new UsernamePasswordToken("zhang","123");
        Subject subject = SecurityUtils.getSubject();
        subject.login( token);
    }
}
