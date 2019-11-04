package com.ncu.oa.user.controller;

import com.ncu.oa.user.constant.WsConst;
import com.ncu.oa.user.ws.*;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.Arrays;
import java.util.Date;

/**
 * Created with IDEA
 * author:G.B.Monkey
 * Date:2019/10/28 0028
 * Time:10:43
 */
@Endpoint
public class AuthorEndpoint {
    @PayloadRoot(namespace = WsConst.NAMESPACE_URI, localPart = "authorRequest")
    @ResponsePayload
    public AuthorResponse getAuthor(@RequestPayload AuthorRequest authorRequest){
        AuthorResponse resp = new AuthorResponse();
        Author author = new Author();
        author.setBirthday("1990-01-23");
        author.setName("姓名：" + authorRequest.getName());
        author.setSex(Sex.FEMALE);
        author.getHobby().addAll(Arrays.asList("电影","旅游"));
        author.setDescription("描述：一枚趔趄的猿。现在时间：" + new Date().getTime());
        resp.setAuthor(author);
        return resp;
    }

    @PayloadRoot(namespace = WsConst.NAMESPACE_URI, localPart = "authorListRequest")
    @ResponsePayload
    public AuthorListResponse getAuthorList(@RequestPayload AuthorListRequest authorListRequest){
        AuthorListResponse resp = new AuthorListResponse();
        Author author = new Author();
        author.setBirthday("1990-01-23");
        author.setName("姓名：oKong");
        author.setSex(Sex.FEMALE);
        author.getHobby().addAll(Arrays.asList("电影","旅游"));
        author.setDescription("描述：一枚趔趄的猿。现在时间：" + new Date().getTime());
        resp.getAuthor().add(author);
        resp.getAuthor().add(author);
        return resp;
    }
}
