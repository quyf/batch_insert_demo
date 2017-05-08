package cn.yjs.demo.common;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 把公共方法封装抽出来
 * @author quyf
 *
 */
public class CommonUtils {

	Connection connection = null;

	public CommonUtils() {
	}

	// init jdbc connection
	public void init() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = (Connection) DriverManager.getConnection(Constant.MYSQL_URL, Constant.MYSQL_USER,
					Constant.MYSQL_PWD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		init();
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
}

