package com.agile.framework.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * JWT签名工具 (基于Auth0 java-jwt)
 * @author: chenzhanshang
 **/
@Slf4j
public class JwtTokenHelper {

    // ... 常量定义保持不变 (RANDOM_TOKEN_ID, USER_ID, etc.) ...
    public static final String RANDOM_TOKEN_ID = "randomTokenId";
    public static final String USER_ID = "userId";
    public static final String USERNAME = "username";
    public static final String ENDPOINT = "endpoint";
    public static final String CREATED_AT = "createdAt";
    public static final String USER_TYPE = "userType";
    public static final String EXPIRES_AT = "expiresAt";

    /**
     * 加密秘钥
     */
    private static final String SECRET = "BEgNqAftcXQQlAVLxp421HD9YzlHseTp";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    public static UserToken parse(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        try {
            // 验证并解析Token
            DecodedJWT jwt = JWT.require(ALGORITHM)
                    .build()
                    .verify(token); // 自动验证过期时间(exp)

            // 从JWT Claims中提取数据并构建UserToken对象
            return new UserToken(
                    jwt.getClaim(RANDOM_TOKEN_ID).asString(),
                    jwt.getClaim(USER_ID).asLong(),
                    jwt.getClaim(USERNAME).asString(),
                    jwt.getClaim(CREATED_AT).asDate(),
                    jwt.getClaim(EXPIRES_AT).asDate()
            );
        } catch (JWTVerificationException | IllegalArgumentException e) {
            // JWTVerificationException 包含：签名无效、Token过期、结构无效等所有验证错误
            // IllegalArgumentException 处理 valueOf 枚举转换可能的错误
            log.info("解析或验证token异常：{}", e.getMessage());
            log.debug("详细异常信息：", e); // 调试级别记录完整堆栈
        }
        return null;
    }

    public static String create(UserToken userToken) {
        // 使用链式调用构建JWT
        return JWT.create()
                .withClaim(RANDOM_TOKEN_ID, userToken.getRandomTokenId())
                .withClaim(USER_ID, userToken.getUserId())
                .withClaim(USERNAME, userToken.getUsername())
                .withClaim(CREATED_AT, userToken.getCreatedAt())
                .withClaim(EXPIRES_AT, userToken.getExpiresAt())
                // 直接使用过期时间字段，而不是单独设置
                // .withExpiresAt(userToken.getExpiresAt())
                .sign(ALGORITHM);
    }
}