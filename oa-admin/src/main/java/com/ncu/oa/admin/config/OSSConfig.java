package com.ncu.oa.admin.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created with IDEA
 * author:G.B.Monkey
 * Date:2019/9/27 0027
 * Time:14:11
 */
@Configuration
@PropertySource(value = "classpath:config/oss.properties")
public class OSSConfig implements InitializingBean {
    @Value("${oss.file.endpoint}")
    private String ossFileEndpoint;
    @Value("${oss.file.keyid}")
    private String ossFileKeyId;
    @Value("${oss.file.keysecret}")
    private String ossFIleKeySecret;
    @Value("${oss.file.bucketname}")
    private String ossFileBucketName;
    @Value("${oss.file.filehost}")
    private String ossFileFilehost;

    public static String OSS_FILE_ENDPOINT;
    public static String OSS_ACCESS_KEY_ID;
    public static String OSS_ACCESS_KEY_SECRET;
    public static String OSS_BUCKET_NAME;
    public static String OSS_FILE_HOST;

    @Override
    public void afterPropertiesSet() throws Exception {
        OSS_FILE_ENDPOINT = ossFileEndpoint;
        OSS_ACCESS_KEY_ID = ossFileKeyId;
        OSS_ACCESS_KEY_SECRET = ossFIleKeySecret;
        OSS_FILE_HOST = ossFileFilehost;
        OSS_BUCKET_NAME = ossFileBucketName;
    }
}
