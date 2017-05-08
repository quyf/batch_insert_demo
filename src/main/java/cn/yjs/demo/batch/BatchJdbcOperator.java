package cn.yjs.demo.batch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import cn.yjs.demo.DbOpterator;
import cn.yjs.demo.common.CommonUtils;
import cn.yjs.demo.common.Constant;

/**
 * 批处理,速度优于普通jdbc 
 * 优点 处理同级别记录时，和数据库交互次数明显减少，节省了连接成本
 * @author quyf
 *
 */
public class BatchJdbcOperator extends CommonUtils implements DbOpterator {

	private String userName = "user_name_".intern();
	
	public BatchJdbcOperator() {}
	
	public void doInsert(int rows) {
		Connection conn = getConnection();
		if( conn == null ){
			System.out.println( "batch jdbc doInsert can't get connection....");
			return ;
		}
		PreparedStatement ps = null;
		try {
			conn.setAutoCommit( false );
			long begin = System.currentTimeMillis();
			ps = conn.prepareStatement( Constant.SQL ) ;
			for(int i=0; i < rows; i++){
				ps.setInt(1, i );
				ps.setString(2, userName + 1);
				ps.addBatch();
				if( rows%200 == 0 ){//这个数对批处理的性能可能也有影响
					ps.executeBatch();
					ps.clearBatch();
				}
			}
			//在循环外面batch操作，会引发OOM
			ps.executeBatch();//保证最后都batch
			ps.clearBatch();
			conn.commit();
			long end = System.currentTimeMillis();
			System.out.println( "batch jdbc doInsert("+rows+") take time===="+(end-begin)/1000+"s");
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
		BatchJdbcOperator batchOp = new BatchJdbcOperator();
		batchOp.doInsert( 100 );
	}
}
