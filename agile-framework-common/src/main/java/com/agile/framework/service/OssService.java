//package com.agile.framework.service;
//
//import com.aliyun.oss.ClientException;
//import com.aliyun.oss.OSSException;
//import com.aliyun.oss.model.ObjectMetadata;
//import com.dmhxm.framework.service.response.StsTokenResponse;
//
//import java.io.File;
//import java.io.InputStream;
//
///**
// * @author chenzhanshang
// */
//public interface OssService {
//
//    /**
//     * 文件上传
//     *
//     * @param key      存储到 OSS 的路径，如：test/123.txt
//     * @param input    文件输入流
//     * @return 上传成功返回文件的访问 URL
//     * @throws OSSException    OssException
//     * @throws ClientException ClientException
//     */
//    default String uploadFile(String key, InputStream input) throws OSSException, ClientException {
//        return uploadFile(key, input, new ObjectMetadata());
//    }
//
//    /**
//     * 文件上传
//     *
//     * @param key      存储到 OSS 的路径，如：test/123.txt
//     * @param input    文件输入流
//     * @param metadata 元数据
//     * @return 上传成功返回文件的访问 URL
//     * @throws OSSException    OssException
//     * @throws ClientException ClientException
//     */
//    String uploadFile(String key, InputStream input, ObjectMetadata metadata) throws OSSException, ClientException;
//
//
//    /**
//     * 文件上传
//     *
//     * @param key      存储到 OSS 的路径，如：test/123.txt
//     * @param file     本地文件信息
//     * @return 上传成功返回文件的访问 URL
//     * @throws OSSException    OssException
//     * @throws ClientException ClientException
//     */
//    default String uploadFile(String key, File file) throws OSSException, ClientException {
//        return uploadFile(key, file, new ObjectMetadata());
//    }
//
//    /**
//     * 文件上传
//     *
//     * @param key      存储到 OSS 的路径，如：test/123.txt
//     * @param file     本地文件信息
//     * @param metadata 元数据
//     * @return 上传成功返回文件的访问 URL
//     * @throws OSSException    OssException
//     * @throws ClientException ClientException
//     */
//    String uploadFile(String key, File file, ObjectMetadata metadata) throws OSSException, ClientException;
//
//    /**
//     * 获取签名地址
//     *
//     * @param ossFileName 上传到oss的文件相对路径
//     * @param contentType 签名里会加入contentType进行计算，因此此参数必须
//     */
//    String getSign(String ossFileName, String contentType);
//
//    /**
//     * 获取sts token
//     * @return
//     */
//    StsTokenResponse getStsToken();
//}
