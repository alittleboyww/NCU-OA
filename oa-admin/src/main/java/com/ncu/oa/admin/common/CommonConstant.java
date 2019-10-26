package com.ncu.oa.admin.common;

/**
 * Created with IDEA
 * author:G.B.Monkey
 * Date:2019/10/23 0023
 * Time:10:51
 */
public class CommonConstant {
    /** 验证码，Hash类型， 后面跟着cookie Id */
    public static final String CAPTCHA = "captcha:";
    /** 验证码，field，验证码内容*/
    public static final String CAPTCHA_CODE = "code";
    /** 验证码，field，验证码是否已经验证过 */
    public static final String CAPTCHA_CHECKED = "checked";
    /** 验证码失效时间，分钟 */
    public static final int CAPTCHA_EXPIRED = 2;

    //verify
    public static final int VERIFY_SUCCESS = 200;
    //验证失败
    public static final int VERIFY_ERROR = 400;
    //Invalid
    public static final int VERIFY_INVALID = 300;
}
