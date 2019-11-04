package com.ncu.oa.admin.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IDEA
 * author:G.B.Monkey
 * Date:2019/10/23 0023
 * Time:19:06
 */
@Data
public class ResultObject implements Serializable {
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
