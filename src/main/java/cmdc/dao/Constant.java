package cmdc.dao;

public class Constant {

	private static String lucenePath="G:\\eclipse_work\\";
	private static String DBHost="192.168.23.1:8088";	
	
	public static String getDBHost() {
		return DBHost;
	}


	public static void setDBHost(String dBHost) {
		DBHost = dBHost;
	}


	public static void setLucenePath(String lucenePath) {
		Constant.lucenePath = lucenePath;
	}


	public static String getLucenePath(){
		return lucenePath;
	}	
	
	
	
	
}
