package com.ncu.oa.admin.pojo;

import lombok.Data;

/**
 * Created with IDEA
 * author:G.B.Monkey
 * Date:2019/10/23 0023
 * Time:19:06
 */
@Data
public class ResultObject {
    //响应码
    private int code;
    //返回信息
    private String message;

    public ResultObject(){

    }

    public ResultObject(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
