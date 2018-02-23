package cmdc.dao;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import cmdc.bean.MessageBean;

@Path("AdminsAOP")
public class AdminsAOP {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{admin_id}")
	public MessageBean get_auth (@PathParam("admin_id") String admin_id,@QueryParam("tokenstr") String tokenstr)
			throws ServletException, IOException {		
		String id = "";// 从token中获取用户名
		String role="";
		String key = KeyUtil.getKey();// 获取秘钥
		MessageBean messageBean=new MessageBean();
		if (TokenUtil.isValid(tokenstr, key)) {// 检查token是否有效
			id = TokenUtil.getId(tokenstr, key);// 获取用户名
			role=TokenUtil.getSubject(tokenstr, key);
			if(role.equals("S")||id.equals(admin_id)){
				messageBean.setResultCode("1");
				messageBean.setResultMsg("可查看");
				return messageBean;
			}
			
		}
		messageBean.setResultCode("0");
		messageBean.setResultMsg("不可查看");
		return messageBean;
	}

	
	@PUT	
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{admin_id}")
	public MessageBean put_auth (@PathParam("admin_id") String admin_id,@QueryParam("tokenstr") String tokenstr)
			throws ServletException, IOException {
		
		String id = "";// 从token中获取用户名
		String role="";
		String key = KeyUtil.getKey();// 获取秘钥
		MessageBean messageBean=new MessageBean();
		if (TokenUtil.isValid(tokenstr, key)) {// 检查token是否有效
			id = TokenUtil.getId(tokenstr, key);// 获取用户名
			role=TokenUtil.getSubject(tokenstr, key);
			
			if(role.equals("S")||id.equals(admin_id)){
				messageBean.setResultCode("1");
				messageBean.setResultMsg("可编辑");				
				return messageBean;
			}
			
		}
		messageBean.setResultCode("0");
		messageBean.setResultMsg("不可编辑");
		return messageBean;
	}
}
