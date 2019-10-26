package com.ncu.oa.admin.pojo;

import lombok.Data;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * Created with IDEA
 * author:G.B.Monkey
 * Date:2019/10/23 0023
 * Time:13:07
 */
@Data
public class Captcha implements Serializable {

    private String cookieId;
    private String code;
    private int checked;

    public Captcha(){}
    public Captcha(String cookieId){
        this.cookieId = cookieId;
    }
    public Captcha(String cookieId, String code) {
        this.cookieId = cookieId;
        this.code = code;
        this.checked = 0;
    }
}
