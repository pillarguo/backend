package cmdc.dao;

import javax.inject.Singleton;

/**
 * @author glz
 *设置jwt签名的秘钥
 */
@Singleton 
public class KeyUtil {
	private static String key="secret_by_cmdc_forum";
	public static String getKey(){
		return key;
	}
}
