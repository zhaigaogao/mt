package cmcc.mobile.maintain.util;

import java.util.UUID;

/**
 *
 * @author renlinggao
 * @Date 2016年6月28日
 */
public class IdCreateUtil {

	/**
	 * 创建集团id
	 * 
	 * @return
	 */
	public static String createGroupId() {
		UUID uuid = UUID.randomUUID();
		return "C_" + uuid.toString();
	}
	
	/**
	 * adminuserId
	 * 
	 * @return
	 */
	public static String createAdminUserId() {
		UUID uuid = UUID.randomUUID();
		return "AU_" + uuid.toString();
	}
}
