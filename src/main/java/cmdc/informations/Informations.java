package cmdc.informations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
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

import cmdc.bean.Information;
import cmdc.bean.MessageBean;
import cmdc.dao.Constant;
import cmdc.dao.InformationsAOP;
import cmdc.dao.KeyUtil;
import cmdc.dao.LoginVerify;
import cmdc.dao.TokenUtil;



/**
 * @author GLZ
 *	资讯的所有方法（增删改查）
 */
@Path("informations")
@XmlRootElement(name = "information")
@Singleton
public class Informations {

	@POST
	@LoginVerify
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("new_info")
	public MessageBean postIt(@BeanParam Information information, @Context HttpServletRequest req,
			@CookieParam("jwt") String tokenstr,@Context HttpServletResponse res) {
		MessageBean messageBean = new MessageBean();
		if (information.getInformationTitle().length() > 50 || information.getInformationTitle().length() <= 0) {
			messageBean.setResultCode("-1");
			messageBean.setResultMsg("标题长度有误");
			return messageBean;
		} else if (information.getInformationSummary().length() > 150
				|| information.getInformationSummary().length() <= 0) {
			messageBean.setResultCode("-1");
			messageBean.setResultMsg("概要长度有误");
			return messageBean;
		} else if (information.getInformationImage() == null || information.getInformationImage().length() == 0) {
			messageBean.setResultCode("-1");
			messageBean.setResultMsg("封面图片不能为空");
			return messageBean;
		} else if (information.getInformationContent().length() == 0) {
			messageBean.setResultCode("-1");
			messageBean.setResultMsg("内容不能为空");
			return messageBean;
		}

		
		String key = KeyUtil.getKey();// 获取秘钥
		String id = TokenUtil.getId(tokenstr, key);// 获取用户id

		information.setCreateTime(new Date());

		information.setCmdcAdminId(Integer.parseInt(id));
		information.setUpdateCmdcAdminId(Integer.parseInt(id));
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget webTarget = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/informations/new_info");

		final Invocation.Builder invocationBuilder = webTarget.request();
		Response response = invocationBuilder.buildPost(Entity.entity(information, MediaType.APPLICATION_JSON))
				.invoke();
		MessageBean aff = response.readEntity(MessageBean.class);
		response.close();
		if (aff.getResultCode().equals("1")) {
			messageBean.setResultCode("1");
			messageBean.setResultMsg("新建成功");
			return messageBean;
		}

		return aff;
	}

	@GET
	@LoginVerify
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("info_list")
	public String getIt(@QueryParam("displayStatus") String displayStatus, @Context HttpServletRequest req,
			@Context HttpServletResponse res) throws ServletException, IOException {

		ClientConfig clientConfig = new ClientConfig();
		// 创建Client
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget webTarget = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/informations/lists")
				.queryParam("displayStatus", displayStatus);

		final Invocation.Builder invocationBuilder = webTarget.request();
		Response response = invocationBuilder.get();
		String result = response.readEntity(String.class);
		response.close();
		return result;

	}

	@GET
	@LoginVerify	
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("pages")
	public String pages(@QueryParam("pageNum") String pageNum, @QueryParam("pageSize") String pageSize,
			@QueryParam("displayStatus") String displayStatus, @QueryParam("informationTitle") String informationTitle,
			@QueryParam("createTime") String createTime, @CookieParam("jwt") String tokenstr,@Context HttpServletRequest req,
			@Context HttpServletResponse res) throws ServletException, IOException {
		if (displayStatus == null || displayStatus.length() == 0) {
			displayStatus = ">-1";
		}
		/*// 获取jwt
		Cookie[] cookies = req.getCookies();
		String tokenstr = "";
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			String cookieName = cookie.getName();
			if (cookieName.equals("jwt")) {
				tokenstr = cookie.getValue();
				break;
			}
		}*/
		ClientConfig clientConfig = new ClientConfig();
		// 创建Client
		Client client = ClientBuilder.newClient(clientConfig);

		// 超级管理员返回所有资讯，普通管理员返回自己的资讯
		InformationsAOP informationsAOP = new InformationsAOP();
		MessageBean messageBean = informationsAOP.getrole(tokenstr);

		if (pageNum == null || "".equals(pageNum)) {
			pageNum = "1";// 默认是第1页
		}

		WebTarget webTarget = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/informations/pages");
		webTarget = webTarget.queryParam("displayStatus", displayStatus);
		webTarget = webTarget.queryParam("pageNum", pageNum);
		webTarget = webTarget.queryParam("pageSize", pageSize);
		webTarget = webTarget.queryParam("informationTitle", informationTitle);
		webTarget = webTarget.queryParam("createTime", createTime);
		if (messageBean.getResultCode().equals("0")) {
			String key = KeyUtil.getKey();// 获取秘钥
			String cmdcAdminId = TokenUtil.getId(tokenstr, key);
			webTarget = webTarget.queryParam("cmdcAdminId", cmdcAdminId);
		}
		final Invocation.Builder invocationBuilder = webTarget.request();
		Response response = invocationBuilder.get();
		String result = response.readEntity(String.class);
		response.close();
		return result;

	}

	@GET
	@LoginVerify
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{info_id}")
	public String infoid(@PathParam("info_id") String info_id, @Context HttpServletRequest req,
			@Context HttpServletResponse res) throws ServletException, IOException {

		ClientConfig clientConfig = new ClientConfig();
		// 创建Client
		Client client = ClientBuilder.newClient(clientConfig);

		WebTarget webTarget = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/informations/");
		webTarget = webTarget.path(info_id);
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
	@Path("{info_id}")
	public MessageBean put(@PathParam("info_id") String info_id, @BeanParam Information information,@CookieParam("jwt") String tokenstr,
			@Context HttpServletRequest req, @Context HttpServletResponse res) throws ServletException, IOException {

		MessageBean messageBean = new MessageBean();
		if (information.getInformationTitle() != null && (information.getInformationTitle().length() > 50
				|| information.getInformationTitle().length() <= 0)) {
			messageBean.setResultCode("-1");
			messageBean.setResultMsg("标题长度有误");
			return messageBean;
		} else if (information.getInformationSummary() != null && (information.getInformationSummary().length() > 150
				|| information.getInformationSummary().length() <= 0)) {
			messageBean.setResultCode("-1");
			messageBean.setResultMsg("概要长度有误");
			return messageBean;
		} else if (information.getInformationImage() != null
				&& information.getInformationImage().trim().length() == 0) {
			messageBean.setResultCode("-1");
			messageBean.setResultMsg("背景图不能为空");
			return messageBean;
		} else if (information.getInformationContent() != null && information.getInformationContent().length() == 0) {
			messageBean.setResultCode("-1");
			messageBean.setResultMsg("内容不能为空");
			return messageBean;
		}

		
		String key = KeyUtil.getKey();// 获取秘钥
		
		String id = TokenUtil.getId(tokenstr, key);// 获取用户名
		
		information.setUpdateCmdcAdminId(Integer.parseInt(id));
		information.setUpdateTime(new Date());
		information.setCmdcAdminId(Integer.parseInt(id));
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);

		// 验证admin-id和jwt的id是否一致，或者是否是超级管理员
		InformationsAOP informationsAOP = new InformationsAOP();
		messageBean = informationsAOP.put_author_id(information.getCmdcAdminId().toString(), tokenstr);
		
		if (messageBean.getResultCode().equals("0")) {
			MessageBean message = new MessageBean();
			message.setResultCode("0");
			message.setResultMsg("失败");
			return message;
		}

		WebTarget webTarget = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/informations/");
		webTarget = webTarget.path(info_id);

		final Invocation.Builder invocationBuilder = webTarget.request();
		Response response = invocationBuilder.buildPut(Entity.entity(information, MediaType.APPLICATION_JSON)).invoke();
		MessageBean aff = response.readEntity(MessageBean.class);
		response.close();
		return aff;
	}

	@PUT
	@LoginVerify
	@RolesAllowed({ "S" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("resort")
	public MessageBean resort(List<Information> informationlist, @Context HttpServletRequest req,
			@Context HttpServletResponse res) {
		MessageBean messageBean = new MessageBean();

		// 先把DisplayStatus大于0的设为等于0
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		Information information1 = new Information();
		Information information2 = new Information();
		information1.setDisplayStatus("0");
		information2.setDisplayStatus(">0");
		List<Information> informationli = new ArrayList<Information>();
		informationli.add(information1);
		informationli.add(information2);
		WebTarget wt = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/informations/renew_info");
		final Invocation.Builder ib = wt.request();

		Response resp = ib.buildPut(Entity.entity(informationli, MediaType.APPLICATION_JSON)).invoke();
		MessageBean result = resp.readEntity(MessageBean.class);
		resp.close();
		if (result.getResultCode().equals("0")) {
			return result;
		}
		// 再把前端的数据（DisplayStatus大于0的）传到数据库
		Information information = new Information();
		for (Integer i = 0; i < informationlist.size(); i++) {
			information = informationlist.get(i);
			WebTarget webTarget = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/informations/");
			webTarget = webTarget.path(information.getInformationId().toString());
			final Invocation.Builder invocationBuilder = webTarget.request();
			Response response = invocationBuilder.buildPut(Entity.entity(information, MediaType.APPLICATION_JSON))
					.invoke();
			MessageBean aff = response.readEntity(MessageBean.class);
			response.close();
			if (aff.getResultCode().equals("0")) {
				break;
			}
			if (i == informationlist.size() - 1) {
				messageBean.setResultCode("1");
				messageBean.setResultMsg("排序成功");
				return messageBean;
			}
		}

		messageBean.setResultCode("1");
		messageBean.setResultMsg("排序成功");
		return messageBean;
	}
}
