package cn.yjs.demo.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class InsertThread implements Runnable {
	CommonUtils utils = new CommonUtils();
	List<String> ll = new ArrayList<String>();
	String name ;
	public static ConcurrentHashMap<String, Long> timeMap = new ConcurrentHashMap<String, Long>();
	
	
	public InsertThread(String name,List<String> ll) {
		this.name = name;
		this.ll = ll;
		Thread.currentThread().setName(name);
		timeMap.put(name, 0l);
	}
	
	public void run() {
		Connection conn = utils.getConnection();
		PreparedStatement ps = null;
		try {
			long begin = System.currentTimeMillis();
			conn.setAutoCommit( false );
			ps = conn.prepareStatement( Constant.SQL ) ;
			int i = 1;
			for(String id : ll ){
				ps.setInt(1, i );
				ps.setString(2, "user_name_" + id);
				ps.addBatch();
				if( i%200 == 0 ){
					ps.executeBatch();
					ps.clearBatch();
				}
				i++;
			}
			ps.executeBatch();//保证最后都batch
			ps.clearBatch();
			conn.commit();
			long end = System.currentTimeMillis();
			Long ltime = (end-begin)/1000;
			timeMap.put(name, timeMap.get(name)+ltime);
			System.out.println( name +" take times:"+ timeMap.get(name));
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println( Thread.currentThread().getName() + " error:" + e);
		}finally {
			try {
				ps.close();
				conn.close();
				//System.out.println( Thread.currentThread().getName() + " close:");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
