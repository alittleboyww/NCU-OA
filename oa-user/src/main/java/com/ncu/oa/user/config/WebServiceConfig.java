package com.ncu.oa.user.config;

import com.ncu.oa.user.constant.WsConst;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

/**
 * Created with IDEA
 * author:G.B.Monkey
 * Date:2019/10/28 0028
 * Time:10:55
 */
@EnableWs //开启webService
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext){
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true); // 地址会进行转换 不然都是本地地址

        return new ServletRegistrationBean(servlet, "/ws/*");
    }


    //name 对应 wsdl名如 ：/ws/author.wsdl
    @Bean(name = "author")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema xsdSchema){
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("AuthorPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setSchema(xsdSchema);
        wsdl11Definition.setTargetNamespace(WsConst.NAMESPACE_URI);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema authorSchema(){
        return new SimpleXsdSchema(new ClassPathResource("xsd/author.xsd"));
    }

    //自定义  SaajSoapMessageFactory 然后指定soap版本
    public SaajSoapMessageFactory messageFactory(){
        SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
        //指定版本
        messageFactory.setSoapVersion(SoapVersion.SOAP_11);
        return messageFactory;
    }
}
