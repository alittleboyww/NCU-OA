package com.ncu.oa.admin.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.ncu.oa.admin.config.OSSConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created with IDEA
 * author:G.B.Monkey
 * Date:2019/9/27 0027
 * Time:14:31
 */
public class AliyunOSSUtil {
    static Logger logger = LoggerFactory.getLogger(AliyunOSSUtil.class);
    private static String endpoint;
    private static String accessKeyId;
    private static String accessKeySecret;
    private static String bucketName;
    private static String fileHost;
    static {
       endpoint = OSSConfig.OSS_FILE_ENDPOINT;
       accessKeyId = OSSConfig.OSS_ACCESS_KEY_ID;
       accessKeySecret = OSSConfig.OSS_ACCESS_KEY_SECRET;
       bucketName = OSSConfig.OSS_BUCKET_NAME;
       fileHost = OSSConfig.OSS_FILE_HOST;
    }

    public static OSSClient createOssClient(){
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        return ossClient;
    }
    public static String upload(File file){
        OSSClient ossClient = createOssClient();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(new Date());

        if (null == file){
            return null;
        }

        try {
            //容器不存在，则创建
            if(!ossClient.doesBucketExist(bucketName)){
                ossClient.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }
            //创建文件路径
            String fileUrl = fileHost + "/" + (dateStr + "/" + UUID.randomUUID().toString().replace("-","") + "-" + file.getName());
            //上传文件
            PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, file));
            //设置权限 这里是公开读
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            if (null != result){
                logger.info("========>OSS文件上传成功，OSS地址: "+ fileUrl);
                return fileUrl;
            }
        }catch (OSSException e){
            logger.error(e.getMessage());
        }catch (ClientException e){
            logger.error(e.getMessage());
        }finally {
            ossClient.shutdown();
        }
        return null;
    }

    /**
     * 根据key删除OSS服务器上的文件
     * @param key Bucket下的文件的路径名+文件名 如："/upload/cake.jpg" 这里需要带上斜杠
     */
    public static void deleteFile(String key){
        OSSClient ossClient = createOssClient();
        logger.info(fileHost + key);
        ossClient.deleteObject(bucketName, fileHost + key);
        logger.info("删除" + bucketName + "下的文件" + fileHost + key + "成功");
    }
}
