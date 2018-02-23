package cmdc.admins;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.security.RolesAllowed;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

import org.glassfish.jersey.client.ClientConfig;

import cmdc.bean.Admin;
import cmdc.bean.MessageBean;
import cmdc.dao.AdminsAOP;
import cmdc.dao.Constant;
import cmdc.dao.ExpirationTime;
import cmdc.dao.KeyUtil;
import cmdc.dao.LoginVerify;
import cmdc.dao.TokenUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Path("admins")
@XmlRootElement(name = "admin")
@Singleton
public class Admins {

	@POST
	@Path("admin_login")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public MessageBean postIt(@BeanParam Admin admin, @FormParam("identify") String identify,
			@Context HttpServletRequest req, @Context HttpServletResponse res) throws UnsupportedEncodingException {

		MessageBean messageBean = new MessageBean();
		// 验证码
		String rand = (String) req.getSession().getAttribute("rand");
		if (identify == null || identify.length() <= 0) {
			messageBean.setResultCode("-2");
			messageBean.setResultMsg("验证码为空");
			return messageBean;
		}
		// 将前端传来的验证码转化为大写字母（数字不变），然后比较
		if (!identify.toUpperCase().equals(rand)) {
			messageBean.setResultCode("-1");
			messageBean.setResultMsg("验证码不正确");
			return messageBean;
		}

		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget webTarget = client.target("http://" + Constant.getDBHost() + "/cmdcforumdb/v1/cmdc_admin/lists");

		if (admin.getPhoneNumber() != null && admin.getPhoneNumber().trim().length() != 0
				&& !"".equals(admin.getPhoneNumber().trim())) {
			webTarget = webTarget.queryParam("phoneNumber", admin.getPhoneNumber());
		} else {
			webTarget = webTarget.queryParam("cmdcAdminName", admin.getCmdcAdminName());
		}

		final Invocation.Builder invocationBuilder = webTarget.request();
		Response response = invocationBuilder.get();
		@SuppressWarnings("unchecked")
		List<Admin> adminList = response.readEntity(List.class);
		response.close();
		if (adminList.size() != 0) {
			JSONArray json = JSONArray.fromObject(adminList);
			JSONObject job = json.getJSONObject(0);
			if (job.getString("password").equals(admin.getPassword())) {
				if (job.getString("roleCancelStatus").equals("1")) {
					messageBean.setResultCode("-3");
					messageBean.setResultMsg("用户已被注销");
					return messageBean;
				}

				String key = KeyUtil.getKey();
				// 这是一个时间戳，代表token的过期时间
				ExpirationTime exp = new ExpirationTime();
				Date expiration = exp.getExpiration();
				// 生成token

				String jwtString = TokenUtil.getJWTString(job.getString("cmdcAdminId"), job.getString("roleType"),
						expiration, key);
				// 生成cookie
				Cookie cookie = new Cookie("jwt", jwtString);
				// cookie在客户端存储3600*12秒
				cookie.setMaxAge(3600 * 12);
				// 设置cookie的应用路径
				cookie.setPath("/");
				// String domain = DomainUtil.getDomain();
				// String domain = req.getHeader("host");
				// System.out.println("domain"+domain);
				// cookie.setDomain(req.getHeader("host"));
				cookie.setHttpOnly(true);
				res.addCookie(cookie);

				// 生成cookie,存储真实姓名
				// Cookie cookie2 =new Cookie("realName",
				// URLEncoder.encode(job.getString("realName"), "UTF-8"));
				Cookie cookie2 = new Cookie("realName", URLEncoder.encode(job.getString("realName"), "UTF-8"));
				// cookie在客户端存储3600*12秒
				cookie2.setMaxAge(3600 * 12);
				// 设置cookie的应用路径
				cookie2.setPath("/");
				// cookie2.setDomain(domain);
				cookie2.setHttpOnly(false);
				res.addCookie(cookie2);

				// 生成cookie,存储用户id
				Cookie cookie3 = new Cookie("cmdcAdminId", job.getString("cmdcAdminId"));
				// cookie在客户端存储3600*12秒
				cookie3.setMaxAge(3600 * 12);
				// 设置cookie的应用路径
				cookie3.setPath("/");
				// cookie3.setDomain(domain);
				cookie3.setHttpOnly(false);
				res.addCookie(cookie3);

				// 生成cookie,存储用户角色
				Cookie cookie4 = new Cookie("roleType", job.getString("roleType"));
				// cookie在客户端存储3600秒
				cookie4.setMaxAge(3600 * 12);
				// 设置cookie的应用路径
				cookie4.setPath("/");
				// cookie4.setDomain(domain);
				cookie4.setHttpOnly(false);
				res.addCookie(cookie4);

				// 更新“登录时间”字段
				Admin admin_new = new Admin();
				admin_new.setLoginTime(new Date());
				WebTarget wt = client.target("http://" + Constant.getDBHost() + "/cmdcforumdb/v1/cmdc_admin/");
				wt = wt.path((job.getString("cmdcAdminId")));
				final Invocation.Builder ib = wt.request();
				Response resp = ib.buildPut(Entity.entity(admin_new, MediaType.APPLICATION_JSON)).invoke();
				MessageBean aff = resp.readEntity(MessageBean.class);
				resp.close();

				messageBean.setResultCode("1");
				messageBean.setResultMsg("登录成功");
				return messageBean;

			}

		}
		messageBean.setResultCode("0");
		messageBean.setResultMsg("用户名或密码不正确");
		return messageBean;
	}

	@SuppressWarnings("unchecked")
	@POST
	@LoginVerify
	@RolesAllowed({ "S" })
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("new_admin")
	public MessageBean postIt(@BeanParam Admin admin, @Context HttpServletRequest req,
			@Context HttpServletResponse res) {
		MessageBean messageBean = new MessageBean();
		String regEx = "1{1}[0-9]{10}";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(admin.getPhoneNumber());
		if (matcher.matches() == false || admin.getPhoneNumber().trim().length() != 11
				|| admin.getPhoneNumber().charAt(0) != '1') {
			messageBean.setResultCode("-1");
			messageBean.setResultMsg("手机格式有误");
			return messageBean;
		} else if (admin.getCmdcAdminName().length() > 50 || admin.getCmdcAdminName().trim().length() <= 0) {
			messageBean.setResultCode("-1");
			messageBean.setResultMsg("用户名长度有误");
			return messageBean;
		} else if (admin.getPassword().length() != 32) {
			messageBean.setResultCode("-1");
			messageBean.setResultMsg("密码长度有误");
			return messageBean;
		} else if (admin.getDepartment().length() > 15 || admin.getDepartment().length() <= 0) {
			messageBean.setResultCode("-1");
			messageBean.setResultMsg("部门长度有误");
			return messageBean;
		} else if (admin.getRealName().length() > 15 || admin.getRealName().length() <= 0) {
			messageBean.setResultCode("-1");
			messageBean.setResultMsg("真实姓名长度有误");
			return messageBean;
		}

		admin.setCreateTime(new Date());
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget webTarget = client.target("http://" + Constant.getDBHost() + "/cmdcforumdb/v1/cmdc_admin/new_admin");
		final Invocation.Builder invocationBuilder = webTarget.request();
		Response response = invocationBuilder.buildPost(Entity.entity(admin, MediaType.APPLICATION_JSON)).invoke();
		MessageBean result = response.readEntity(MessageBean.class);

		if (result.getResultCode().equals("0")) {
			WebTarget wt1 = client.target("http://" + Constant.getDBHost() + "/cmdcforumdb/v1/cmdc_admin/lists")
					.queryParam("cmdcAdminName", admin.getCmdcAdminName());
			final Invocation.Builder ib1 = wt1.request();
			Response resp1 = ib1.get();
			List<Admin> adminlist1 = resp1.readEntity(List.class);
			resp1.close();
			if (adminlist1.size() > 0) {
				messageBean.setResultCode("-2");
				messageBean.setResultMsg("用户名已存在");
				return messageBean;
			}

			WebTarget wt2 = client.target("http://" + Constant.getDBHost() + "/cmdcforumdb/v1/cmdc_admin/lists")
					.queryParam("phoneNumber", admin.getPhoneNumber());
			final Invocation.Builder ib2 = wt2.request();
			Response resp2 = ib2.get();
			List<Admin> adminlist2 = resp2.readEntity(List.class);
			resp2.close();
			if (adminlist2.size() > 0) {
				messageBean.setResultCode("-2");
				messageBean.setResultMsg("手机号已存在");
				return messageBean;
			}
		}
		return result;

	}

	@GET
	@LoginVerify
	@RolesAllowed({ "S" })
	@Produces(MediaType.APPLICATION_JSON)
	@Path("admin_list")
	public String admin_list(@QueryParam("cmdcAdminName") String cmdcAdminName,
			@QueryParam("phoneNumber") String phoneNumber, @Context HttpServletRequest req,
			@Context HttpServletResponse res) throws ServletException, IOException {

		ClientConfig clientConfig = new ClientConfig();
		// 创建Client
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget webTarget = client.target("http://" + Constant.getDBHost() + "/cmdcforumdb/v1/cmdc_admin/lists")
				.queryParam("cmdcAdminName", cmdcAdminName);
		webTarget = webTarget.queryParam("phoneNumber", phoneNumber);
		final Invocation.Builder invocationBuilder = webTarget.request();
		Response response = invocationBuilder.get();
		String result = response.readEntity(String.class);

		response.close();
		return result;

	}

	@GET
	@LoginVerify
	@RolesAllowed({ "S", "N" })
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{admin_id}")
	public String getadminid(@PathParam("admin_id") String admin_id, @CookieParam("jwt") String tokenstr,
			@Context HttpServletRequest req, @Context HttpServletResponse res) throws ServletException, IOException {

		ClientConfig clientConfig = new ClientConfig();
		// 创建Client
		Client client = ClientBuilder.newClient(clientConfig);

		AdminsAOP adminsAOP = new AdminsAOP();
		MessageBean messageBean = adminsAOP.get_auth(admin_id, tokenstr);
		MessageBean message = new MessageBean();
		if (messageBean.getResultCode().equals("0")) {
			message.setResultCode("0");
			message.setResultMsg("无权限查看");
			return String.valueOf(message);
		}

		WebTarget webTarget = client.target("http://" + Constant.getDBHost() + "/cmdcforumdb/v1/cmdc_admin/");
		webTarget = webTarget.path(admin_id);
		final Invocation.Builder invocationBuilder = webTarget.request();
		Response response = invocationBuilder.get();
		String result = response.readEntity(String.class);
		response.close();

		return result;

	}

	@PUT
	@LoginVerify
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{admin_id}")
	public MessageBean put(@PathParam("admin_id") String admin_id, @BeanParam Admin admin,
			@CookieParam("jwt") String tokenstr, @Context HttpServletRequest req, @Context HttpServletResponse res)
			throws ServletException, IOException {
		MessageBean messageBean = new MessageBean();
		if (admin.getPhoneNumber() != null) {
			String regEx = "1{1}[0-9]{10}";
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher = pattern.matcher(admin.getPhoneNumber());
			if (matcher.matches() == false || admin.getPhoneNumber().trim().length() != 11
					|| admin.getPhoneNumber().charAt(0) != '1') {
				messageBean.setResultCode("-1");
				messageBean.setResultMsg("手机格式有误");
				return messageBean;
			}
		} else if (admin.getCmdcAdminName() != null
				&& (admin.getCmdcAdminName().length() > 50 || admin.getCmdcAdminName().trim().length() <= 0)) {
			messageBean.setResultCode("-1");
			messageBean.setResultMsg("用户名长度有误");
			return messageBean;
		} // 密码后台都是加密后的代码，不能直接计算长度
		else if (admin.getPassword() != null && admin.getPassword().length() != 32) {
			messageBean.setResultCode("-1");
			messageBean.setResultMsg("密码长度有误");
			return messageBean;
		} else if (admin.getDepartment() != null
				&& (admin.getDepartment().length() > 15 || admin.getDepartment().length() <= 0)) {
			messageBean.setResultCode("-1");
			messageBean.setResultMsg("部门长度有误");
			return messageBean;
		} else if (admin.getRealName() != null
				&& (admin.getRealName().length() > 15 || admin.getRealName().length() <= 0)) {
			messageBean.setResultCode("-1");
			messageBean.setResultMsg("真实姓名长度有误");
			return messageBean;
		}

		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		// 验证admin-id和jwt的id是否一致，或者是否是超级管理员

		AdminsAOP adminsAOP = new AdminsAOP();
		messageBean = adminsAOP.put_auth(admin_id, tokenstr);

		if (messageBean.getResultCode().equals("0")) {
			MessageBean message = new MessageBean();
			message.setResultCode("0");
			message.setResultMsg("失败");
			return message;
		}

		WebTarget webTarget = client.target("http://" + Constant.getDBHost() + "/cmdcforumdb/v1/cmdc_admin/");
		webTarget = webTarget.path(admin_id);

		final Invocation.Builder invocationBuilder = webTarget.request();
		Response response = invocationBuilder.buildPut(Entity.entity(admin, MediaType.APPLICATION_JSON)).invoke();
		MessageBean aff = response.readEntity(MessageBean.class);
		response.close();
		if (admin.getRealName() != null) {
			// 生成cookie,存储真实姓名
			Cookie cookie2 = new Cookie("realName", URLEncoder.encode("\"" + admin.getRealName() + "\"", "UTF-8"));
			// cookie在客户端存储3600*12秒
			cookie2.setMaxAge(3600 * 12);
			// 设置cookie的应用路径
			cookie2.setPath("/");
			// cookie2.setDomain(domain);
			cookie2.setHttpOnly(false);
			res.addCookie(cookie2);

		}
		return aff;
	}

	@POST
	@LoginVerify
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("reset_password")
	public MessageBean reset_password(@FormParam("pw_old") String pw_old, @FormParam("pw_new") String pw_new,
			@CookieParam("jwt") String tokenstr, @Context HttpServletRequest req, @Context HttpServletResponse res) {
		MessageBean messageBean = new MessageBean();
		if (pw_new.equals(pw_old)) {
			messageBean.setResultCode("-3");
			messageBean.setResultMsg("新密码不可与原密码相同");
			return messageBean;
		}

		if (pw_new.length() != 32) {
			messageBean.setResultCode("-1");
			messageBean.setResultMsg("修改失败");
			// messageBean.setResultMsg("新密码长度有误");
			return messageBean;
		}

		messageBean = new MessageBean();
		String admin_id = "";// 从token中获取用户名
		String key = KeyUtil.getKey();// 获取秘钥

		admin_id = TokenUtil.getId(tokenstr, key);// 获取用户名
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget webTarget = client.target("http://" + Constant.getDBHost() + "/cmdcforumdb/v1/cmdc_admin/");
		webTarget = webTarget.path(admin_id);
		final Invocation.Builder invocationBuilder = webTarget.request();
		Response response = invocationBuilder.get();
		Admin admin = response.readEntity(Admin.class);

		if (admin.getPassword().equals(pw_old)) {
			Admin admin_new = new Admin();
			admin_new.setPassword(pw_new);
			admin_new.setUpdateTime(new Date());
			WebTarget wt = client.target("http://" + Constant.getDBHost() + "/cmdcforumdb/v1/cmdc_admin/");
			webTarget = wt.path(admin_id);
			final Invocation.Builder ib = webTarget.request();
			Response result = ib.buildPut(Entity.entity(admin_new, MediaType.APPLICATION_JSON)).invoke();
			MessageBean aff = result.readEntity(MessageBean.class);
			if (aff.getResultCode().equals("1")) {
				messageBean.setResultCode("1");
				messageBean.setResultMsg("修改成功");
				return messageBean;
			}
		} else {
			messageBean.setResultCode("0");
			messageBean.setResultMsg("原密码错误");
			return messageBean;
		}
		messageBean.setResultCode("-2");
		messageBean.setResultMsg("修改失败");
		return messageBean;

	}

}
