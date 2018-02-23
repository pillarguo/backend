package cmdc.dao;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.annotation.security.PermitAll;
import javax.inject.Singleton;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Token的工具类
 */
//@PermitAll
@Singleton
public class TokenUtil {    
	
	//生成token字符串
	public static String getJWTString(String id,String role,Date expires,String key){
        if (id == null) {
            throw new NullPointerException("null id is illegal");
        }
        if (expires == null) {
            throw new NullPointerException("null expires is illegal");
        }
        if (key == null) {
            throw new NullPointerException("null key is illegal");
        }
        SignatureAlgorithm signatureAlgorithm =SignatureAlgorithm.HS256;
        String jwtString = Jwts.builder()
                .setIssuer("Jersey-Security-Basic")//token的签发主体，没用到这个  
                .setSubject(role)//                
                .setAudience("user") //没用到这个  
                .setExpiration(expires)
                .setIssuedAt(new Date())//没用到这个  
                .setId(id)  
                .signWith(signatureAlgorithm,key)
                .compact();
        return jwtString;
    }
	
	//验证token是否合法
    public static boolean isValid(String token, String key) {
          try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token.trim());
            return true;
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
    }
    //
    public static String getSubject(String jwsToken, String key) {
        if (isValid(jwsToken, key)) {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);           
            return claimsJws.getBody().getSubject();
            //return "1";
        }
        return null;
    }
  //根据token，获取id
    public static String getId(String jwsToken, String key) {
        if (isValid(jwsToken, key)) {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);    
            //return "1";
            return claimsJws.getBody().getId();
        }
        return null;
    }
    //获取过期时间戳
    public static Date getExpiry(String jwsToken, String key) {
        if (isValid(jwsToken, key)) {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
            return claimsJws.getBody().getExpiration();
        }
        Calendar calendar = new GregorianCalendar(0000, 01, 01,0,0,0);   
        Date date = calendar.getTime();
        return date;
    }
    //没用到下面的方法
    public static String[] getRoles(String jwsToken, String key) {
        if (isValid(jwsToken, key)) {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
            return claimsJws.getBody().getAudience().split(",");
        }
        return new String[]{};
    }
    
    //没用到下面的方法
    public static int getVersion(String jwsToken, String key) {
        if (isValid(jwsToken, key)) {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
            return Integer.parseInt(claimsJws.getBody().getId());
        }
        return -1;
    }

}
