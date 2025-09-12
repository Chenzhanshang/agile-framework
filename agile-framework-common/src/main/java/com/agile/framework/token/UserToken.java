package com.agile.framework.token;

import com.agile.framework.util.RandomUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

/**
 * @author: chenzhanshang
 * @date 2025-04-29 11:53
 * @desc:
 **/
@Getter
@AllArgsConstructor
public class UserToken {
    /**
     * 随机id
     */
    private String randomTokenId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 过期时间
     */
    private Date expiresAt;

    public UserToken(Long userId, String username, Date expiresAt) {
        this.userId = userId;
        this.username = username;
        this.expiresAt = expiresAt;
        this.createdAt = new Date();
        this.randomTokenId = RandomUtils.createUUID();
    }

    public boolean isExpired() {
        return expiresAt.before(new Date());
    }

}
