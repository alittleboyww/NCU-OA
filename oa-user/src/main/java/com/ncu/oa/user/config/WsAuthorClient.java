package com.ncu.oa.user.config;

import com.ncu.oa.user.ws.*;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.util.UUID;

/**
 * Created with IDEA
 * author:G.B.Monkey
 * Date:2019/10/28 0028
 * Time:12:31
 */
public class WsAuthorClient extends WebServiceGatewaySupport {
    public AuthorResponse getAuthor(String name){
        AuthorRequest request = new AuthorRequest();
        request.setName(name);

        return (AuthorResponse) getWebServiceTemplate().marshalSendAndReceive(request);
    }

    private AuthorListResponse getAuthorList(){
        AuthorListRequest request = new AuthorListRequest();
        request.setNonce(UUID.randomUUID().toString());
        return (AuthorListResponse) getWebServiceTemplate().marshalSendAndReceive(request);
    }
}
