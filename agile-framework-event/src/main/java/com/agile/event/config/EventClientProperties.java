package com.agile.event.config;

import com.agile.framework.exception.BusinessException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

/**
 * @author: chenzhanshang
 * @date 2025/9/10 15:27
 * @desc:
 **/
@Getter
@Setter
@ConfigurationProperties(prefix = "agile.framework.event")
public class EventClientProperties {
    /**
     * 是否启用
     */
    private boolean enable;
    /**
     * 默认值：rabbitmq
     */
    private String mq = "rabbitmq";
    /**
     * 服务节点名称
     */
    private String serviceName;

    public void check() {
        if(StringUtils.isEmpty(serviceName)) {
            throw BusinessException.of("[agile.framework.event], serviceName 不能为空");
        }
    }

    @Getter
    public static enum MqEnum {
        RABBIT_MQ("rabbitmq"),
        ROCKET_MQ("rocketmq"),
        KAFKA("kafka");

        private String value;

        MqEnum(String value) {
            this.value = value;
        }

        /**
         * 获取启用禁用状态枚举对象
         *
         * @param value
         * @return
         */
        public static MqEnum getEnum(String value) {
            if (null == value) {
                return null;
            }
            for (MqEnum s : MqEnum.values()) {
                if (value.equals(s.getValue())) {
                    return s;
                }
            }
            return null;
        }

    }
}
