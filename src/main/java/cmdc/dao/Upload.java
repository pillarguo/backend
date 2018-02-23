package cmdc.dao;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;


/**
 * @author GLZ
 *	用于上传图片
 */
@Path("upload")
public class Upload {

	private static final String ARTICLE_IMAGES_PATH = "images/";

	@POST
	@LoginVerify
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public void uploadimage(@FormDataParam("myFileName") InputStream fileInputStream,
			@FormDataParam("myFileName") FormDataContentDisposition disposition, 
			@CookieParam("cmdcAdminId") String cmdcAdminId,
			@Context HttpServletRequest req,
			@Context HttpServletResponse res) throws IOException {
		String realPath=req.getSession().getServletContext().getRealPath("/");
		
		// 判断文件格式,也就是后缀名
		String suffix = disposition.getFileName().substring(disposition.getFileName().lastIndexOf(".") + 1);
		// 允许的图片格式（扩展名）
		String types_allow = "jpg,jpeg,png,gif,bmp";
		if (!types_allow.toLowerCase().contains(suffix.toLowerCase())) {
			String errMsg = "error|图片格式不正确";
			res.setContentType("text/text;charset=utf-8");
			PrintWriter out = res.getWriter();
			out.print(errMsg); // 返回错误信息
			out.flush();
			out.close();
			return;
		}
		
		Random random = new Random();
		//用户id+时间戳(微秒)+随机数+后缀
		String imageName = cmdcAdminId+Calendar.getInstance().getTimeInMillis() +random.nextInt(10000)+"."+ suffix;//disposition.getFileName()
		
		File file = new File(realPath+ARTICLE_IMAGES_PATH + imageName);

		try {
			// 使用common io的文件写入操作
			FileUtils.copyInputStreamToFile(fileInputStream, file);
			if (file.length() > 2 * 1024 * 1024) {
				file.delete();
				String errMsg = "error|图片不能超过2M";
				res.setContentType("text/text;charset=utf-8");
				PrintWriter out = res.getWriter();
				out.print(errMsg); // 返回错误信息
				out.flush();
				out.close();
				return;
			}
		} catch (IOException ex) {
			String errMsg = "error|" + ex.getMessage();
			res.setContentType("text/text;charset=utf-8");
			PrintWriter out = res.getWriter();
			out.print(errMsg); // 返回错误信息
			out.flush();
			out.close();
			return;
		}
	
		String imgUrl = "/backend/images/" + imageName;
		res.setContentType("text/text;charset=utf-8");
		PrintWriter out = res.getWriter();
		out.print(imgUrl); // 返回url地址
		out.flush();
		out.close();
		return;
	}

}