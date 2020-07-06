package com.bootvue.utils.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Base64Utils;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class JwtUtil {

    // 生成签名是所使用的私钥
    private static RSAPrivateKey privateKey;
    private static RSAPublicKey publicKey;

    static {
        try {
            String Key = IOUtils.toString(new ClassPathResource("key/private_p8.pem").getInputStream())
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\r", "")
                    .replaceAll("\n", "");
            String publicK = IOUtils.toString(new ClassPathResource("key/public.pem").getInputStream())
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\r", "")
                    .replaceAll("\n", "");
            byte[] keyBytes = Base64Utils.decodeFromString(Key);
            byte[] publicKeyBytes = Base64Utils.decodeFromString(publicK);

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
            publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
        } catch (Exception e) {
            log.error("pkcs8私钥有问题...", e);
        }
    }

    // 签发人
    private static final String iss = "app";

    // 生成签名的时候所使用的加密算法
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;


    /**
     * 生成 JWT Token 字符串
     *
     * @param ttlMillis jwt 过期时间  毫秒
     * @param claims    额外添加到payload部分的信息。
     *                  例如可以添加用户名、用户ID、用户（加密前的）密码等信息
     */
    public static String encode(long ttlMillis, Map<String, Object> claims) {
        if (claims == null) {
            claims = new HashMap<>();
        }

        // 签发时间（iat）：payload部分的标准字段之一
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder()
                // payload部分的非标准字段/附加字段，一般写在标准的字段之前。
                .setClaims(claims)
                // JWT ID（jti）：payload部分的标准字段之一，JWT 的唯一性标识，虽不强求，但尽量确保其唯一性。
                .setId(UUID.randomUUID().toString())
                // 签发时间（iat）：payload部分的标准字段之一，代表这个 JWT 的生成时间。
                .setIssuedAt(now)
                // 签发人（iss）：payload部分的标准字段之一，代表这个 JWT 的所有者。通常是 username、userid 这样具有用户代表性的内容。
                .setSubject(iss)
                // 设置生成签名的算法和秘钥
                .signWith(signatureAlgorithm, privateKey);

        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            // 过期时间（exp）：payload部分的标准字段之一，代表这个 JWT 的有效期。
            builder.setExpiration(exp);
        }

        return builder.compact();
    }

    /**
     * JWT Token 由 头部 payload 和 签名部 三部分组成。签名部分是由加密算法生成，无法反向解密。
     * 而 头部 和 payload是由 Base64 编码算法生成，是可以反向反编码回原样的。
     * 这也是为什么不要在 JWT Token 中放敏感数据的原因。
     *
     * @param jwtToken 加密后的token
     * @return claims 返回payload的键值对
     */
    public static Claims decode(String jwtToken) {

        // 得到 DefaultJwtParser
        return Jwts.parser()
                .setSigningKey(publicKey)
                // 设置需要解析的 jwt
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    /**
     * 校验 token
     * 在这里可以使用官方的校验，或，
     * 自定义校验规则，例如在 token 中携带密码，进行加密处理后和数据库中的加密密码比较。
     *
     * @param jwtToken 被校验的 jwt Token
     */
    public static boolean isVerify(String jwtToken) {
        Algorithm algorithm = null;
        boolean flag = false;

        switch (signatureAlgorithm) {
            case RS256:
                algorithm = Algorithm.RSA256(publicKey, privateKey);
                break;
            default:
                throw new RuntimeException("不支持该算法");
        }

        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            verifier.verify(jwtToken);
            flag = true;
        } catch (JWTVerificationException e) {
            flag = false;
        }

        return flag;
    }

}
