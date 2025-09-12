package com.agile.framework.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author chenzhanshang
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "agile.framework.oss")
public class OssProperties {

    /**
     * OSS 提供者
     */
    private String provider = "ali";

    /**
     * 阿里 OSS 配置
     */
    private Ali ali = new Ali();

    @Getter
    @Setter
    public static class Ali {
        private String endpoint;
        private String bucketName;
        private String accessKeyId;
        private String secretAccessKey;

        //sts配置
        private String stsEndpoint;
        private String roleArn;
    }
}
