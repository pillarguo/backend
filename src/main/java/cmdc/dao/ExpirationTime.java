package cmdc.dao;

import java.util.Date;

import javax.inject.Singleton;
@Singleton 
public class ExpirationTime {	
	public Date getExpiration(){
		//现在过期时间是3600000*12微秒，也就是1*12小时
		Date expiration=new Date(System.currentTimeMillis() + 12*3600000);
		return expiration;
	}
}
