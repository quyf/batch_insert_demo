package cn.yjs.demo;

import cn.yjs.demo.batch.BatchJdbcOperator;
import cn.yjs.demo.jdbc.BaseJdbcOperator;
import cn.yjs.demo.jedis.JedisOperator;
import cn.yjs.demo.mq.RocketMqOperator;

/**
 * 测试类
 * @author quyf
 *
 */
public class TestInsert {

	public static void main(String[] args) {
		int rows = 10000;
		
		BaseJdbcOperator t = new BaseJdbcOperator();
		t.doInsert( rows );
		
		BatchJdbcOperator batchOp = new BatchJdbcOperator();
		batchOp.doInsert( rows );
		
		JedisOperator jedisOp = new JedisOperator();
		jedisOp.readyData( rows );
		
		jedisOp.doInsert( rows );
		
		RocketMqOperator mqOp = new RocketMqOperator();
		mqOp.readyData( rows );
		mqOp.doInsert( rows );
		
	}
}
