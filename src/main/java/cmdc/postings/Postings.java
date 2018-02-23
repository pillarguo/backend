package cmdc.postings;

import java.io.IOException;
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

import org.apache.lucene.queryparser.classic.ParseException;
import org.glassfish.jersey.client.ClientConfig;

import cmdc.bean.MessageBean;
import cmdc.bean.Posting;
import cmdc.dao.Constant;
import cmdc.dao.LoginVerify;
import cmdc.lucenes.Lucenes;


/**
 * @author glz
 *	帖子的增删改查等
 *
 */
@Path("postings")
@Singleton
public class Postings {

	@GET
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{posting_id}")
	public String get_by_id(@PathParam("posting_id") String posting_id, @Context HttpServletRequest req,
			@Context HttpServletResponse res) {
		ClientConfig clientConfig = new ClientConfig();
		// 创建Client
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget webTarget = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/postings/");
		webTarget = webTarget.path(posting_id);
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
	@Path("{posting_id}")
	public MessageBean put_by_id(@PathParam("posting_id") String posting_id, @BeanParam Posting posting,
			@Context HttpServletRequest req, @Context HttpServletResponse res) throws IOException, ParseException {
		MessageBean messageBean = new MessageBean();
		if (posting.getPostingTitle() != null
				&& (posting.getPostingTitle().length() > 100 || posting.getPostingTitle().length() <= 0)) {
			messageBean.setResultCode("-1");
			messageBean.setResultMsg("标题长度有误");
			return messageBean;
		}
		posting.setUpdateTime(new Date());
		ClientConfig clientConfig = new ClientConfig();
		// 创建Client
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget webTarget = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/postings/");
		webTarget = webTarget.path(posting_id);
		final Invocation.Builder invocationBuilder = webTarget.request();
		Response response = invocationBuilder.buildPut(Entity.entity(posting, MediaType.APPLICATION_JSON)).invoke();
		messageBean = response.readEntity(MessageBean.class);
		response.close();
		
		Lucenes lucene = new Lucenes();// 更新索引
		if (messageBean.getResultCode().equals("1") && posting.getPostingTitle() != null) {	
			posting.setPostingId(Integer.valueOf(posting_id));
			if (posting.getDisplayStatus().length() == 0) {//更新索引内容
				// String
				// realPath=req.getSession().getServletContext().getRealPath("/");
				lucene.update(posting);
			} 
		}else if (messageBean.getResultCode().equals("1") &&posting.getDisplayStatus()!=null&& posting.getDisplayStatus().equals("1")) {//不再显示，就删除索引
			lucene.delete(posting_id);
		}
		return messageBean;
	}

	@GET
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("pages")
	public String pages(@QueryParam("pageNum") String pageNum, @QueryParam("pageSize") String pageSize,
			@QueryParam("displayStatus") String displayStatus, @QueryParam("postingTitle") String postingTitle,
			@QueryParam("postingOrderBy") String postingOrderBy, @QueryParam("postingWhether") String postingWhether,@Context HttpServletRequest req, @Context HttpServletResponse res) {

		ClientConfig clientConfig = new ClientConfig();
		// 创建Client
		Client client = ClientBuilder.newClient(clientConfig);
		if (pageNum == null || "".equals(pageNum)) {
			pageNum = "1";// 默认是第1页
		}
		WebTarget webTarget = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/postings/pages");
		webTarget = webTarget.queryParam("pageNum", pageNum);
		webTarget = webTarget.queryParam("pageSize", pageSize);
		if (displayStatus == null || displayStatus.length() == 0) {
			displayStatus = "0";
		}
		webTarget = webTarget.queryParam("displayStatus", displayStatus);
		webTarget = webTarget.queryParam("postingTitle", postingTitle);
		webTarget = webTarget.queryParam("postingOrderBy", postingOrderBy);//排序规则	
		webTarget = webTarget.queryParam("postingWhether", postingWhether);		
		final Invocation.Builder invocationBuilder = webTarget.request();
		Response response = invocationBuilder.get();
		String result = response.readEntity(String.class);
		response.close();
		return result;
	}

	@GET
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("lists")
	public String lists(@QueryParam("displayStatus") String displayStatus, @Context HttpServletRequest req,
			@Context HttpServletResponse res) {
		ClientConfig clientConfig = new ClientConfig();
		// 创建Client
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget webTarget = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/postings/lists")
				.queryParam("displayStatus", displayStatus);

		final Invocation.Builder invocationBuilder = webTarget.request();
		Response response = invocationBuilder.get();
		String result = response.readEntity(String.class);
		response.close();
		return result;

	}

}
