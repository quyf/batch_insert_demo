package cn.yjs.demo.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import cn.yjs.demo.DbOpterator;
import cn.yjs.demo.common.CommonUtils;
import cn.yjs.demo.common.Constant;

/**
 * 原始的数据库操作，一次连接操作一次db
 * @author quyf
 *
 */
public class BaseJdbcOperator extends CommonUtils implements DbOpterator {

	private String userName = "user_name_".intern();
	
	public BaseJdbcOperator() {}
	
	public void doInsert(int rows) {
		Connection conn = getConnection();
		if( conn == null ){
			System.out.println( "jdbc doInsert can't get connection....");
			return ;
		}
		PreparedStatement ps = null;
		try {
			long begin = System.currentTimeMillis();
			for(int i=0; i < rows; i++){
				ps = conn.prepareStatement( Constant.SQL ) ;
				ps.setInt(1,  i);
				ps.setString(2, userName + 1);
				ps.executeUpdate();
			}
			long end = System.currentTimeMillis();
			System.out.println( "base jdbc doInsert("+rows+") take time===="+(end-begin)/1000+"s");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				ps.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		BaseJdbcOperator t = new BaseJdbcOperator();
		t.doInsert(50);
	}
}
