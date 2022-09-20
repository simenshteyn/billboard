package com.robot.billboard.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {
    private static String secret;

    @Autowired
    public JWTUtil(@Value("${jwt_secret}") String secret) {
        this.secret = secret;
    }

    public String generateToken(String email) {
        Date expirationTime = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());
        return JWT.create()
                .withClaim("email", email)
                .withExpiresAt(expirationTime)
                .withClaim("typ", "access")
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndReturnEmail(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withClaim("typ", "access").build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("email").asString();
    }
}
