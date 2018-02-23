package cmdc.cmdc_users;

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

import cmdc.bean.CmdcUser;
import cmdc.bean.MessageBean;
import cmdc.dao.Constant;
import cmdc.dao.LoginVerify;


@Path("cmdc_user")
@Singleton
public class CmdcUsers {

	@GET
	@LoginVerify
	@Produces(MediaType.APPLICATION_JSON)
	@Path("lists")
	public String lists(){
		ClientConfig clientConfig = new ClientConfig();
		// 创建Client
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget webTarget = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/cmdc_user/lists");
		
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
	public String pages(@QueryParam("pageNum") String pageNum, @QueryParam("pageSize") String pageSize, @QueryParam("roleType") String roleType,
			@QueryParam("nickname") String nickname,@QueryParam("createTime") String createTime,@Context HttpServletRequest req, @Context HttpServletResponse res) {
		
		if(pageNum==null||"".equals(pageNum)){
			pageNum="1";//默认是第1页
		}
		ClientConfig clientConfig = new ClientConfig();
		// 创建Client
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget webTarget = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/cmdc_user/pages");
		webTarget=webTarget.queryParam("pageNum", pageNum);
		webTarget=webTarget.queryParam("pageSize", pageSize);
		webTarget=webTarget.queryParam("roleType", roleType);
		webTarget=webTarget.queryParam("nickname", nickname);
		webTarget=webTarget.queryParam("createTime", createTime);
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
	@Path("{user_id}")
	public MessageBean putadminid(@PathParam("user_id") String user_id,@BeanParam CmdcUser cmdcUser, @Context HttpServletRequest req,
			@Context HttpServletResponse res){
		ClientConfig clientConfig = new ClientConfig();
		// 创建Client
		Client client = ClientBuilder.newClient(clientConfig);
		
		WebTarget webTarget = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/cmdc_user/");
		webTarget = webTarget.path(user_id);

		final Invocation.Builder invocationBuilder = webTarget.request();
		Response response = invocationBuilder.buildPut(Entity.entity(cmdcUser, MediaType.APPLICATION_JSON)).invoke();
		MessageBean aff = response.readEntity(MessageBean.class);
		response.close();		
		
		return aff;
	}
	
	
	@GET
	@LoginVerify
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{user_id}")
	public CmdcUser getadminid(@PathParam("user_id") String user_id, @Context HttpServletRequest req,
			@Context HttpServletResponse res){
		ClientConfig clientConfig = new ClientConfig();
		// 创建Client
		Client client = ClientBuilder.newClient(clientConfig);
		
		WebTarget webTarget = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/cmdc_user/");
		webTarget = webTarget.path(user_id);

		final Invocation.Builder invocationBuilder = webTarget.request();
		Response response = invocationBuilder.get();
		CmdcUser aff = response.readEntity(CmdcUser.class);
		response.close();		
		
		return aff;
	}
}
