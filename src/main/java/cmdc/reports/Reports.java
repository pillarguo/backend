package cmdc.reports;

import java.util.ArrayList;
import java.util.List;

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
import cmdc.bean.Posting;
import cmdc.bean.Report;
import cmdc.dao.Constant;
import cmdc.dao.LoginVerify;


/**
 * @author GLZ
 *	举报的增删改查
 */
@Singleton
@Path("reports")
public class Reports {

	@GET
	@LoginVerify
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{report_id}")
	public String get_by_id(@PathParam("report_id") String report_id, @Context HttpServletRequest req,
			@Context HttpServletResponse res){
		ClientConfig clientConfig = new ClientConfig();
		// 创建Client
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget webTarget = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/reports");
		webTarget = webTarget.path(report_id);
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
	@Path("{report_id}")
	public MessageBean put_by_id(@PathParam("report_id") String report_id,@BeanParam Report report, @Context HttpServletRequest req,
			@Context HttpServletResponse res){
		ClientConfig clientConfig = new ClientConfig();
		// 创建Client
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget webTarget = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/reports/");
		webTarget = webTarget.path(report_id);
		final Invocation.Builder invocationBuilder = webTarget.request();
		Response response = invocationBuilder.buildPut(Entity.entity(report, MediaType.APPLICATION_JSON)).invoke();
		MessageBean result = response.readEntity(MessageBean.class);
		response.close();

		return result;
	}
	
	@PUT
	@LoginVerify
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("renew_report")
	public MessageBean renew_report(@BeanParam Report report, @Context HttpServletRequest req,
			@Context HttpServletResponse res){
		Report report2=new Report();//report2作为条件		
		report2.setCommentId(report.getCommentId());
		report2.setPostingId(report.getPostingId());
		report2.setDisplayStatus("0");
		List<Report> reportList=new ArrayList<Report>();
		reportList.add(report);//report作为更改参照
		reportList.add(report2);
		ClientConfig clientConfig = new ClientConfig();
		// 创建Client
		Client client = ClientBuilder.newClient(clientConfig);
		
		if(report.getDisplayStatus().equals("1")){//displaystatus等于1，表示删除相应的帖子或者评论
			if(report.getCommentId()!=null&&report.getCommentId().toString().length()!=0){//删除评论
				Comment comment=new Comment();
				comment.setDisplayStatus("1");
				WebTarget wt = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/comments/"+report.getCommentId().toString());
				//wt.path(report.getCommentId().toString());
				final Invocation.Builder ib = wt.request();
				Response resp = ib.buildPut(Entity.entity(comment, MediaType.APPLICATION_JSON)).invoke();
				MessageBean result = resp.readEntity(MessageBean.class);
				resp.close();				
				if(result.getResultCode().equals("0")){
					return result;
				}
				
			}else if(report.getPostingId()!=null&&report.getPostingId().toString().length()!=0){//删除帖子
				Posting posting=new Posting();
				posting.setDisplayStatus("1");
				WebTarget wt = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/postings/"+report.getPostingId().toString());
			
				final Invocation.Builder ib = wt.request();
				Response resp = ib.buildPut(Entity.entity(posting, MediaType.APPLICATION_JSON)).invoke();
				MessageBean result = resp.readEntity(MessageBean.class);				
				resp.close();
				if(result.getResultCode().equals("0")){
					return result;
				}
			}
		}
		
		WebTarget webTarget = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/reports/renew_report");
		final Invocation.Builder invocationBuilder = webTarget.request();
		Response response = invocationBuilder.buildPut(Entity.entity(reportList, MediaType.APPLICATION_JSON)).invoke();
		MessageBean result = response.readEntity(MessageBean.class);
		System.out.println(result);
		response.close();

		return result;
	}
	
	@GET
	@LoginVerify
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("pages")
	public String pages(@QueryParam("pageNum") String pageNum, @QueryParam("pageSize") String pageSize,
			@QueryParam("displayStatus") String displayStatus, @Context HttpServletRequest req, @Context HttpServletResponse res) {
		ClientConfig clientConfig = new ClientConfig();
		// 创建Client
		Client client = ClientBuilder.newClient(clientConfig);
		if (pageNum == null || "".equals(pageNum)) {
			pageNum = "1";// 默认是第1页
		}
		WebTarget webTarget = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/reports/pages");
		
		webTarget=webTarget.queryParam("pageNum", pageNum);
		webTarget=webTarget.queryParam("pageSize", pageSize);
		if(displayStatus==null||displayStatus.length()==0){
			displayStatus="0";
		}
		webTarget=webTarget.queryParam("displayStatus", displayStatus);
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
		WebTarget webTarget = client.target("http://"+Constant.getDBHost()+"/cmdcforumdb/v1/reports/lists")
				.queryParam("displayStatus", displayStatus);

		final Invocation.Builder invocationBuilder = webTarget.request();
		Response response = invocationBuilder.get();
		String result = response.readEntity(String.class);
		response.close();
		return result;

	}
	
}
