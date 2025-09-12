//package com.agile.framework.service.impl;
//
//import cn.hutool.core.util.StrUtil;
//import com.agile.framework.config.OssProperties;
//import com.agile.framework.service.OssService;
//import com.aliyun.oss.*;
//import com.aliyun.oss.internal.OSSHeaders;
//import com.aliyun.oss.model.GeneratePresignedUrlRequest;
//import com.aliyun.oss.model.ObjectMetadata;
//import com.aliyun.oss.model.PutObjectRequest;
//import com.aliyuncs.DefaultAcsClient;
//import com.aliyuncs.auth.sts.AssumeRoleRequest;
//import com.aliyuncs.auth.sts.AssumeRoleResponse;
//import com.aliyuncs.exceptions.ServerException;
//import com.aliyuncs.http.MethodType;
//import com.aliyuncs.profile.DefaultProfile;
//import com.aliyuncs.profile.IClientProfile;
//import com.dmhxm.framework.config.OssProperties;
//import com.dmhxm.framework.constant.StringPool;
//import com.dmhxm.framework.exception.PaddyException;
//import com.dmhxm.framework.service.OssService;
//import com.dmhxm.framework.service.response.StsTokenResponse;
//import com.dmhxm.framework.util.DateUtil;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.File;
//import java.io.InputStream;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 阿里云 OSS 文件上传实现类
// *
// * @author chenzhanshang
// */
//@Slf4j
//@RequiredArgsConstructor
//public class OssServiceAliImpl implements OssService {
//
//    private final OssProperties ossProperties;
//
//    @Override
//    public String uploadFile(String key, InputStream input, ObjectMetadata metadata) throws OSSException, ClientException {
//        return this.uploadFile(key, metadata, input, null);
//    }
//
//    @Override
//    public String uploadFile(String key, File file, ObjectMetadata metadata) throws OSSException, ClientException {
//        return this.uploadFile(key, metadata, null, file);
//    }
//
//    /**
//     * 文件上传
//     *
//     * @param key      存储到 OSS 的路径，如：test/123.txt
//     * @param metadata 元数据
//     * @param input    文件输入流（和 file 二选一）
//     * @param file     本地文件信息（和 input 二选一）
//     * @return 上传成功返回文件的访问 URL
//     */
//    private String uploadFile(String key, ObjectMetadata metadata, InputStream input, File file) throws OSSException, ClientException {
//        OssProperties.Ali aliOssProperties = ossProperties.getAli();
//        OSS ossClient = null;
//        try {
//            ossClient = getClient();
//            PutObjectRequest putObjectRequest;
//            if (input != null) {
//                putObjectRequest = new PutObjectRequest(aliOssProperties.getBucketName(), key, input);
//            } else {
//                putObjectRequest = new PutObjectRequest(aliOssProperties.getBucketName(), key, file);
//            }
//            putObjectRequest.setMetadata(metadata);
//            ossClient.putObject(putObjectRequest);
//            return "https://"
//                    + aliOssProperties.getBucketName()
//                    + DOT
//                    + aliOssProperties.getEndpoint()
//                    + SLASH
//                    + key;
//        } finally {
//            if (ossClient != null) {
//                ossClient.shutdown();
//            }
//        }
//    }
//
//
//    /**
//     * 生成一个预签名的url，给前端js上传
//     * 参考官网文档： https://help.aliyun.com/document_detail/32016.html
//     *
//     * @param ossFileName 上传到oss的文件相对路径
//     * @param contentType 签名里会加入contentType进行计算，因此此参数必须
//     * @return 签名后的url
//     */
//    @Override
//    public String getSign(String ossFileName, String contentType) {
//        OSS ossClient = getClient();
//        // 生成签名URL。
//        try {
//            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(
//                    ossProperties.getAli().getBucketName(),
//                    ossFileName,
//                    HttpMethod.PUT
//            );
//            // 设置过期时间1小时。
//            request.setExpiration(DateUtil.afterOneHourDate(new Date()));
//
//            if (StrUtil.isNotBlank(contentType)) {
//                // 设置请求头。
//                Map<String, String> headers = new HashMap<>();
//                // 指定ContentType，注意：必须指定，这个header加入签名了，不指定时前端带Content-Type上传，会导致签名验证不通过
//                headers.put(OSSHeaders.CONTENT_TYPE, contentType);
//                // 将请求头加入到request中。
//                request.setHeaders(headers);
//            }
//
//            return ossClient.generatePresignedUrl(request).toString();
//        } finally {
//            if (null != ossClient){
//                ossClient.shutdown();
//            }
//        }
//    }
//
//    @Override
//    public StsTokenResponse getStsToken() {
//        try {
//            // 发起STS请求所在的地域。建议保留默认值，默认值为空字符串（""）。
//            String regionId = "";
//            DefaultProfile.addEndpoint("",regionId, "Sts", ossProperties.getAli().getStsEndpoint());
//            IClientProfile profile = DefaultProfile.getProfile(
//                    regionId,
//                    ossProperties.getAli().getAccessKeyId(),
//                    ossProperties.getAli().getSecretAccessKey());
//            DefaultAcsClient client = new DefaultAcsClient(profile);
//            final AssumeRoleRequest request = new AssumeRoleRequest();
//            request.setMethod(MethodType.POST);
//            request.setRoleArn(ossProperties.getAli().getRoleArn());
//            request.setRoleSessionName("default_session");
//            request.setDurationSeconds(3600L);
//            final AssumeRoleResponse response = client.getAcsResponse(request);
//            AssumeRoleResponse.Credentials credentials = response.getCredentials();
//
//            return StsTokenResponse.builder()
//                    .accessKeySecret(credentials.getAccessKeySecret())
//                    .accessKeyId(credentials.getAccessKeyId())
//                    .securityToken(credentials.getSecurityToken())
//                    .expiration(credentials.getExpiration())
//                    .bucketName(ossProperties.getAli().getBucketName())
//                    .build();
//        } catch (ServerException e) {
//            log.info("获取aliyun oss sts 失败：{}", e.getMessage(), e);
//            throw new RuntimeException(e);
//        } catch (com.aliyuncs.exceptions.ClientException e) {
//            log.info("获取aliyun oss sts 失败：{}", e.getMessage(), e);
//            throw PaddyException.of("获取授权失败");
//        }
//    }
//
//    private OSS getClient() {
//        OssProperties.Ali aliOssProperties = ossProperties.getAli();
//        return new OSSClientBuilder().build(
//                aliOssProperties.getEndpoint(),
//                aliOssProperties.getAccessKeyId(),
//                aliOssProperties.getSecretAccessKey()
//        );
//    }
//
//}
