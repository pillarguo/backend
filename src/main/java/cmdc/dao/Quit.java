package cmdc.dao;

import java.io.IOException;
import java.util.Date;

import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.CookieParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;

import cmdc.bean.Admin;
import cmdc.bean.MessageBean;

/**
 * @author GLZ
 *	管理员退出
 */
@Path("Quit")
@Singleton 
@Produces(MediaType.APPLICATION_JSON)
public class Quit {	
    @POST 
    public MessageBean delCookie(@CookieParam("cmdcAdminId") String cmdcAdminId,@Context HttpServletRequest req,@Context HttpServletResponse res) 
    		throws ServletException, IOException {
    	MessageBean messageBean=new MessageBean();
    	// 获取cmdcAdminId
    	/*Cookie[] cookies = req.getCookies();
 		String cmdcAdminId = "";
 		for (int i = 0; i < cookies.length; i++) {
 			Cookie cookie = cookies[i];
 			String cookieName = cookie.getName();
 			if (cookieName.equals("cmdcAdminId")) {
 				cmdcAdminId = cookie.getValue();
 				break;
 			}
 		}*/
 		if(cmdcAdminId.trim().length()==0){
 			 messageBean.setResultCode("0");
 	    	 messageBean.setResultMsg("未登录");
 	    	 return messageBean;    	
 		}
    	 	Admin admin_new =new Admin();
			admin_new.setLogoutTime(new Date());
			ClientConfig clientConfig = new ClientConfig();
			Client client = ClientBuilder.newClient(clientConfig);
			WebTarget wt = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/cmdc_admin/");
			wt = wt.path(cmdcAdminId);
			final Invocation.Builder ib = wt.request();
			Response resp = ib.buildPut(Entity.entity(admin_new, MediaType.APPLICATION_JSON)).invoke();
			MessageBean aff = resp.readEntity(MessageBean.class);		
			resp.close();	
 	    	 
    	 
    	 //生成cookie
    	 Cookie cookie=new Cookie("jwt",null); 
    	 //cookie在客户端存储0秒
    	 cookie.setMaxAge(0);
    	 //设置cookie的应用路径
    	 cookie.setPath("/");
    	 res.addCookie(cookie);   
    	 
    	//生成cookie
    	 Cookie cookie2=new Cookie("realName",null); 
    	 //cookie在客户端存储0秒
    	 cookie2.setMaxAge(0);
    	 //设置cookie的应用路径
    	 cookie2.setPath("/");
    	 res.addCookie(cookie2);
    	 
    	 //生成cookie
    	 Cookie cookie3=new Cookie("cmdcAdminId",null); 
    	 //cookie在客户端存储3600秒
    	 cookie3.setMaxAge(0);
    	 //设置cookie的应用路径
    	 cookie3.setPath("/");
    	 res.addCookie(cookie3); 
    	 
    	 
    	//生成cookie
    	 Cookie cookie4=new Cookie("roleType",null); 
    	 //cookie在客户端存储3600秒
    	 cookie4.setMaxAge(0);
    	 //设置cookie的应用路径
    	 cookie4.setPath("/");
    	 res.addCookie(cookie4); 
    	     	     	 
    	 
    	 messageBean.setResultCode("1");
    	 messageBean.setResultMsg("成功");
    	 return messageBean;    	  
    }
}
