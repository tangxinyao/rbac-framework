package cn.tangxinyao.thrift.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final String CLAIM_KEY_USERNAME = "username";
    private static final String CLAIM_KEY_PASSWORD = "password";
    private static final String CLAIM_SECRET = "secret";
    private static final long EXPIRATION = 7200000;

    public static String getToken(Integer id, String username, String password) {
        Map<String, Object> claims = new HashMap<>();

        claims.put(CLAIM_KEY_USERNAME, username);
        claims.put(CLAIM_KEY_PASSWORD, password);

        return Jwts.builder()
                .setId(String.valueOf(id))
                .setClaims(claims)
                .setExpiration(computeExpired())
                .signWith(SignatureAlgorithm.HS512, CLAIM_SECRET)
                .compact();
    }

    public static Integer getId(String token) {
        Integer id;
        try {
            id = Integer.parseInt(getClaims(token).getId());
        } catch (Exception e) {
            id = null;
        }
        return id;
    }

    public static String getUsername(String token) {
        String username;
        try {
            username = (String) getClaims(token).get(CLAIM_KEY_USERNAME);
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public static String getPassword(String token) {
        String password;
        try {
            password = (String) getClaims(token).get(CLAIM_KEY_PASSWORD);
        } catch (Exception e) {
            password = null;
        }
        return password;
    }

    public static boolean isNotExpired(String token) {
        final Date expired = getExpired(token);
        return expired.after(new Date());
    }

    private static Claims getClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(CLAIM_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private static Date getExpired(String token) {
        Date expired;
        try {
            final Claims claims = getClaims(token);
            expired = claims.getExpiration();
        } catch (Exception e) {
            expired = null;
        }
        return expired;
    }

    private static Date computeExpired() {
        return new Date(System.currentTimeMillis() + EXPIRATION);
    }

//    public static JwtUser getJwtUser(){
//        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        JwtUser user;
//        if("anonymousUser".equals(object)){
//            return null;
//        } else {
//            return (JwtUser)object;
//        }
//    }
}
