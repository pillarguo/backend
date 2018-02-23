package cmdc.dao;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.Map;

import javax.annotation.Priority;
import javax.inject.Singleton;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;



@Provider
@Singleton 
@Priority(value = 0)
@LoginVerify
public class LoginVerifyFilter implements ContainerRequestFilter {	
	
	@Override
    public void filter(final ContainerRequestContext ctx) throws IOException {
		//获取name
		Map<String, Cookie> cookies = ctx.getCookies();
		// Cookie[] cookies = hrequest.getCookies();		
		String tokenstr = "";// 存放jwt字符串
		if (cookies != null) {
			for (String cookiekey : cookies.keySet()) {
				Cookie cookie = cookies.get(cookiekey);
				String cookieName = cookie.getName();
				if (cookieName.equals("jwt")) {
					System.out.println("jwt-value " + cookie.getValue());
					tokenstr = cookie.getValue();
					break;
				}
			}
		}

		String id = "";// 从token中获取用户名
		String role="";
		String key = KeyUtil.getKey();// 获取秘钥
		if (TokenUtil.isValid(tokenstr, key)) {// 检查token是否有效
			id = TokenUtil.getId(tokenstr, key);// 获取用户名
			role=TokenUtil.getSubject(tokenstr, key);
			Date expiry = TokenUtil.getExpiry(tokenstr, key);
			Date now = new Date();			
			if (id == "" || !now.before(expiry)) {				
				ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
			}
		}
		else{//tokenstr为空或者无效，则返回
			ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}
		
        final String user = (role == null) ? "U" : role;
        
        ctx.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return new Principal() {
                    @Override
                    public String getName() {
                        return user;
                    }
                };
            }

            @Override
            public boolean isUserInRole(String role) {
            	System.out.println("aufilter user"+user);
            	//return true;
                return user.equals(role);
            }

            @Override
            public boolean isSecure() {
                return ctx.getUriInfo()
                        .getRequestUri()
                        .getScheme().equalsIgnoreCase("https");
            }

            @Override
            public String getAuthenticationScheme() {
                return "CUSTOM";
            }
        });
    }
	

}
