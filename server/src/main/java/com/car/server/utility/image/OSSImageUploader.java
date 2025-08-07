package com.car.server.utility.image;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;

@Component
public  class OSSImageUploader {
    @Value("${aliyun.oss.endpoint}")
    private  String endpoint;

    @Value("${aliyun.oss.bucket-name}")
    private  String bucketName;

    @Value("${aliyun.oss.access-key-id}")
    private  String accessKeyId;

    @Value("${aliyun.oss.access-key-secret}")
    private  String accessKeySecret;


    public  String uploadAndProcess(InputStream inputStream, String objectKey,String type) {


        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 1. 设置HTTP头，强制浏览器直接显示图片
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentDisposition("inline");

        // 1. 上传原图
        ossClient.putObject(bucketName, "origin/" + objectKey, inputStream,meta);

        String processedUrl = null;

        if(type.equals("car")){
            // 2. 生成处理后的图片URL（压缩+WebP）
             processedUrl = "https://" + bucketName + "." + endpoint.replace("https://", "")
                    + "/origin/" + objectKey
                    + "?x-oss-process=image/resize,w_1280/quality,q_80/format,webp";
        }else if(type.equals("user")) {
            // 2. 生成处理后的图片URL（压缩+WebP）
            processedUrl = "https://" + bucketName + "." + endpoint.replace("https://", "")
                    + "/origin/" + objectKey
                    + "?x-oss-process=image/resize,w_400/quality,q_80/format,webp";
        }



        ossClient.shutdown();
        return processedUrl; // 返回可直接访问的URL
    }
}
