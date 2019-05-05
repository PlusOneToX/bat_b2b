package com.bat.gateway.common;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/14 20:33
 */
public class JwtUtil {
    /**
     * 密钥
     */
    private static final String SECRET = "7786-df7f-c3a34e26-a61c034d-5ec8-245d";
    /**
     * 过期时间8小时（单位：秒）
     **/
    private static final long EXPIRATION = 24 * 60 * 60;

    /**
     * 生成用户token,设置token超时时间
     *
     * @param userId
     * @param password
     * @return
     */
    public static String createToken(Integer userId, String userName, String password, String contactId, String contactsName, String tenantNo) {
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token = null;
        // 租户编码不为空情况，需添加租户编码加密
        if (StringUtils.isBlank(tenantNo)) {
            token = JWT.create()
                    // 添加头部
                    .withHeader(map)
                    // 放入用户的id
                    .withAudience(String.valueOf(userId))
                    // 可以将基本信息放到claims中
                    .withClaim("userName", userName)
                    .withClaim("password", password)
                    .withClaim("contactId", contactId)
                    .withClaim("contactName", contactsName)
                    // 超时设置,设置过期的日期
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION * 1000))
                    // 签发时间
                    .withIssuedAt(new Date())
                    // SECRET加密
                    .sign(Algorithm.HMAC256(SECRET));
        } else {
            token = JWT.create()
                    // 添加头部
                    .withHeader(map)
                    // 放入用户的id
                    .withAudience(String.valueOf(userId))
                    // 可以将基本信息放到claims中
                    .withClaim("userName", userName)
                    .withClaim("password", password)
                    .withClaim("contactId", contactId)
                    .withClaim("contactName", contactsName)
                    .withClaim("tenantNo", tenantNo)
                    // 超时设置,设置过期的日期
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION * 1000))
                    // 签发时间
                    .withIssuedAt(new Date())
                    // SECRET加密
                    .sign(Algorithm.HMAC256(SECRET));
        }

        return token;
    }

    /**
     * 校验token并解析token，校验通过刷新最新token(主要是为了刷新有效时间)
     */
    public static List<String> verityAndUpdate(String token, String tenantNo) {
        List<String> results = new ArrayList<>();
        if (StringUtils.isBlank(token) || token.equals("null")) {
            return results;
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            if (null != jwt) {
                // 拿到我们放置在token中的信息
                List<String> audience = jwt.getAudience();
                if (CollectionUtils.isNotEmpty(audience)) {
                    Integer userId = Integer.parseInt(audience.get(0));
                    results.add(String.valueOf(userId));
                    Map<String, Claim> claims = jwt.getClaims();
                    String userName = null;
                    if (claims.get("userName") != null) {
                        userName = claims.get("userName").asString();
                    }
                    results.add(userName);
                    String password = null;
                    if (claims.get("password") != null) {
                        password = claims.get("password").asString();
                    }
                    String contactId = null;
                    if (claims.get("contactId") != null) {
                        contactId = claims.get("contactId").asString();
                    }
                    results.add(contactId);
                    String contactsName = null;
                    if (claims.get("contactName") != null) {
                        password = claims.get("contactName").asString();
                    }
                    results.add(contactsName);
                    String userTenantNo = null;
                    if (claims.get("tenantNo") != null && !claims.get("tenantNo").asString().equals("null")) {
                        userTenantNo = claims.get("tenantNo").asString();
                    }
                    results.add(userTenantNo);
                    String newToken = createToken(userId, userName, password, contactId, contactsName, tenantNo);
                    results.add(newToken);
                    return results;
                }
            }
            return results;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return results;
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            return results;
        }
    }
}
