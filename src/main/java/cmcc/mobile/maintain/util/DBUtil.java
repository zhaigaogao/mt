package cmcc.mobile.maintain.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author renlinggao
 * @Date 2016年6月28日
 */
public class DBUtil {
	private static DBUtil instance;

	private String[] names;

	private DBUtil() {

	}

	/**
	 * 单例模式
	 * 
	 * @return
	 */
	public static DBUtil getInstance() {
		if (instance == null) {
			instance = new DBUtil();
			String dbNames = PropertiesUtil.getDbByKey("DBNAMES");
			if (dbNames != null)
				instance.setNames(dbNames.split(","));
		}

		return instance;
	}
	
	/**
	 * 获取dbname方法
	 * @return
	 */
	public String getDbName() {
		return instance.getNames()[0];
	}

	public String[] getNames() {
		return names;
	}

	public void setNames(String[] names) {
		this.names = names;
	}

}
