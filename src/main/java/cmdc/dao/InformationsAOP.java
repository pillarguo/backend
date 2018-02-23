package cmdc.dao;

import java.io.IOException;

import javax.servlet.ServletException;
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

@Path("InformationsAOP")
public class InformationsAOP {
	
	
	//判断资讯可不可以编辑，超级管理员和普通管理员自己的资讯可以编辑
	//这个方法暂时没用到
	@GET
	@Produces(MediaType.APPLICATION_JSON)	
	@Path("{author_id}")
	public MessageBean getinformationid (@PathParam("author_id") String author_id,@QueryParam("tokenstr") String tokenstr,@Context HttpServletRequest req, @Context HttpServletResponse res)
			throws ServletException, IOException {	
		String id = "";// 从token中获取用户名
		String role="";
		String key = KeyUtil.getKey();// 获取秘钥
		MessageBean messageBean=new MessageBean();
		if (TokenUtil.isValid(tokenstr, key)) {// 检查token是否有效
			id = TokenUtil.getId(tokenstr, key);// 获取用户名
			role=TokenUtil.getSubject(tokenstr, key);			
			if(role.equals("S")||id.equals(author_id)){
				messageBean.setResultCode("1");
				messageBean.setResultMsg("可编辑");
				return messageBean;
			}
			
		}
		messageBean.setResultCode("0");
		messageBean.setResultMsg("不可编辑");
		return messageBean;
	}


	//判断资讯可不可以编辑，超级管理员和普通管理员自己的资讯可以编辑
	//@PUT	
	//@Produces(MediaType.APPLICATION_JSON)
	//@Path("{author_id}")
	public MessageBean put_author_id ( String author_id,String tokenstr)
			throws ServletException, IOException {
		
		String id = "";// 从token中获取用户名
		String role="";
		String key = KeyUtil.getKey();// 获取秘钥
		MessageBean messageBean=new MessageBean();		
		if (TokenUtil.isValid(tokenstr, key)) {// 检查token是否有效
			id = TokenUtil.getId(tokenstr, key);// 获取用户名
			role=TokenUtil.getSubject(tokenstr, key);				
			if(role.equals("S")||id.equals(author_id)){				
				messageBean.setResultCode("1");
				messageBean.setResultMsg("可编辑");					
				return messageBean;
			}
			
		}
		messageBean.setResultCode("0");
		messageBean.setResultMsg("无权编辑");
		return messageBean;
	}
	
	//超级管理员查看所有的资讯，普通管理员只能查看自己的资讯	
	public MessageBean getrole (String tokenstr)
			throws ServletException, IOException {	
	
		String role="";// 从token中获取用户角色
		String key = KeyUtil.getKey();// 获取秘钥
		MessageBean messageBean=new MessageBean();			
		role=TokenUtil.getSubject(tokenstr, key);			
		if(role.equals("S")){
			messageBean.setResultCode("1");
			messageBean.setResultMsg("超级管理员");
			return messageBean;
		}
		messageBean.setResultCode("0");
		messageBean.setResultMsg("普通管理员");
		return messageBean;
	}
}
