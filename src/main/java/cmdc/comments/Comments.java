package cmdc.comments;

import java.util.Date;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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

import org.glassfish.jersey.client.ClientConfig;

import cmdc.bean.Comment;
import cmdc.bean.MessageBean;
import cmdc.dao.Constant;
import cmdc.dao.LoginVerify;

@Singleton
@Path("comments")
public class Comments {

	@PUT
	@LoginVerify
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{comment_id}")
	public MessageBean put_by_id(@PathParam("comment_id") String comment_id, @BeanParam Comment comment,
			@Context HttpServletRequest req, @Context HttpServletResponse res) {

		ClientConfig clientConfig = new ClientConfig();
		// 创建Client
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget webTarget = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/comments/");
		webTarget = webTarget.path(comment_id);
		final Invocation.Builder invocationBuilder = webTarget.request();
		Response response = invocationBuilder.buildPut(Entity.entity(comment, MediaType.APPLICATION_JSON)).invoke();
		MessageBean result = response.readEntity(MessageBean.class);
		response.close();

		return result;
	}

	@GET
	@LoginVerify
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{comment_id}")
	public String get_by_id(@PathParam("comment_id") String comment_id, @Context HttpServletRequest req,
			@Context HttpServletResponse res) {
		ClientConfig clientConfig = new ClientConfig();
		// 创建Client
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget webTarget = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/comments/");
		webTarget = webTarget.path(comment_id);
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
	public String pages(@QueryParam("postingId") String postingId,@QueryParam("pageNum") String pageNum, @QueryParam("pageSize") String pageSize,
			@QueryParam("displayStatus") String displayStatus, @QueryParam("postingTitle") String postingTitle,
			@QueryParam("nickname") String nickname, @QueryParam("floor") String floor, @Context HttpServletRequest req,
			@Context HttpServletResponse res) {

		ClientConfig clientConfig = new ClientConfig();
		// 创建Client
		Client client = ClientBuilder.newClient(clientConfig);
		if (pageNum == null || "".equals(pageNum)) {
			pageNum = "1";// 默认是第1页
		}
		WebTarget webTarget = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/comments/pages");
		webTarget = webTarget.queryParam("pageNum", pageNum);
		webTarget = webTarget.queryParam("pageSize", pageSize);
		if (displayStatus == null || displayStatus.length() == 0) {
			displayStatus = "0";// 只显示displayStatus=0的评论
		}
		webTarget=webTarget.queryParam("postingId", postingId);
		webTarget = webTarget.queryParam("displayStatus", displayStatus);
		webTarget = webTarget.queryParam("postingTitle", postingTitle);
		webTarget = webTarget.queryParam("nickname", nickname);
		webTarget = webTarget.queryParam("floor", floor);
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
	@Path("lists")
	public String lists(@QueryParam("displayStatus") String displayStatus, @Context HttpServletRequest req,
			@Context HttpServletResponse res) {
		ClientConfig clientConfig = new ClientConfig();
		// 创建Client
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget webTarget = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/comments/lists")
				.queryParam("displayStatus", displayStatus);

		final Invocation.Builder invocationBuilder = webTarget.request();
		Response response = invocationBuilder.get();
		String result = response.readEntity(String.class);
		response.close();
		return result;
	}
}
