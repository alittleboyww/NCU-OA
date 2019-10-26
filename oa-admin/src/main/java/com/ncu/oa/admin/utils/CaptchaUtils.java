package com.ncu.oa.admin.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created with IDEA
 * author:G.B.Monkey
 * Date:2019/10/23 0023
 * Time:10:26
 */
public class CaptchaUtils {
    public static char[] chars = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    public static String getCaptcha(int count){
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < count; i++) {
            char c = chars[ThreadLocalRandom.current().nextInt(chars.length)];
            captcha.append(c);
        }
        return captcha.toString();
    }

    /**
     * 为一个验证码生成一个图片
     *
     * 特性：
     * - 颜色随机
     * - 上下位置随机
     * - 左右位置随机，但字符之间不会重叠
     * - 左右随机旋转一个角度
     * - 避免字符出界
     * - 随机颜色的小字符做背景干扰
     * - 根据字符大小自动调整图片大小、自动计算干扰字符的个数
     *
     * @param captcha
     * @return
     */
    public static BufferedImage getCaptchaImage(String captcha){
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int count = captcha.length();
        int fontSize = 80; //code的字体大小
        int fontMargin = fontSize/4; //字体间距
        int width = (fontSize + fontMargin) * count + fontMargin; //图片宽度
        int height = (int) (fontSize * 1.2);
        int avgWidth = width / count;// 字符平均占位宽度
        int maxDegree = 26; // 最大旋转度数

        //背景颜色
        Color bkColor = Color.WHITE;

        //验证码颜色
        Color[] captchaColor = {Color.MAGENTA, Color.BLACK, Color.BLUE, Color.CYAN, Color.GREEN, Color.ORANGE, Color.PINK};

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        //填充底色为灰白
        g.setColor(bkColor);
        g.fillRect(0, 0, width, height);

        //画边框
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width-1, height-1);
        //画干扰字母、数字
        int dSize = fontSize/3; //调整分母大小以调整干扰字符大小
        Font font = new Font("Fixedsys", Font.PLAIN, dSize);
        g.setFont(font);
        int dNumber = width*height/dSize/dSize;//根据面积计算干扰字母的个数
        for(int i=0; i<dNumber; i++){
            char d_code = chars[random.nextInt(chars.length)];
            g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
            g.drawString(String.valueOf(d_code), random.nextInt(width), random.nextInt(height));
        }

        //开始画验证码：

        // 创建字体
        font = new Font(Font.MONOSPACED, Font.ITALIC|Font.BOLD, fontSize);
        // 设置字体
        g.setFont(font);

        for(int i=0; i<count; i++){
            char c = captcha.charAt(i);
            g.setColor(captchaColor[random.nextInt(captchaColor.length)]);//随机选取一种颜色

            //随机旋转一个角度[-maxDegre, maxDegree]
            int degree = random.nextInt(-maxDegree, maxDegree+1);

            //偏移系数，和旋转角度成反比，以避免字符在图片中越出边框
            double offsetFactor = 1-(Math.abs(degree)/(maxDegree+1.0));//加上1，避免出现结果为0

            g.rotate(degree * Math.PI / 180); //旋转一个角度
            int x = (int) (fontMargin + random.nextInt(avgWidth-fontSize)*offsetFactor); //横向偏移的距离
            int y = (int) (fontSize + random.nextInt(height-fontSize)*offsetFactor); //上下偏移的距离

            g.drawString(String.valueOf(c), x, y); //x,y是字符的左下角，偏离原点的距离！！！

            g.rotate(-degree * Math.PI / 180); //画完一个字符之后，旋转回原来的角度
            g.translate(avgWidth, 0);//移动到下一个画画的原点
            //System.out.println(c+": x="+x+" y="+y+" degree="+degree+" offset="+offsetFactor);

            //X、Y坐标在合适的范围内随机，不旋转：
            //g.drawString(String.valueOf(c), width/count*i+r.nextInt(width/count-fontSize), fontSize+r.nextInt(height-fontSize));
        }

        g.dispose();

        return image;
    }

    /**
     * 设置cookie到response中
     * @param response
     * @param name  cookie名字
     * @param value cookie值
     * @param maxAge cookie生命周期， 以分钟为单位
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if(maxAge>0){
            cookie.setMaxAge(maxAge*60);
        }
        response.addCookie(cookie);
    }
    /**
     * 根据名字从request中获取cookie的值
     * @param request
     * @param name cookie名字
     * @return
     */
    public static String getCookieByName(HttpServletRequest request, String name){
        Cookie[] cookies = request.getCookies();//获取cookie数组
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(name)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
