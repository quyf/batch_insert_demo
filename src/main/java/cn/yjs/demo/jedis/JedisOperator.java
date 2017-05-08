package cn.yjs.demo.jedis;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.yjs.demo.DbOpterator;
import cn.yjs.demo.common.Constant;
import cn.yjs.demo.common.InsertThread;
import redis.clients.jedis.Jedis;

/**
 * redis是Nosql缓存的一种实现，作用无非就是存储，redis支持的数据类型丰富，list数据结构可以用来做队列，
 * 对DB的操作最终还是要数据落地 ，如果用redis在中间，可以其list来实现对数据库操作的异步落地
 * 策略1、list中批量获取数据更新到数据库 2、定时去list获取数据入库
 *
 * 时间换空间
 * 
 * @author quyf
 *
 */
public class JedisOperator implements DbOpterator {
	
	public int coreSize = Runtime.getRuntime().availableProcessors() + 1 ;
	public ExecutorService threadPool = Executors.newFixedThreadPool( coreSize );
	
	private Jedis jedis = null;
	
	
	public JedisOperator() {
		jedis = new Jedis( Constant.JEDIS_HOST, Constant.JEDIS_PORT);
		//jedis.auth( Constant.JEDIS_PWD );
	}
	
	public void doInsert(int rows) {
		long length = jedis.llen("list");//队列长度
		long step = 200;//步长 每次获取多少
		long start = 0;
		long stop = step;
		long begin = System.currentTimeMillis();
		for(;  ;){
			if( start>= length ){
				break;
			}
			List<String> ll = jedis.lrange("list", start, stop);
			insert( ll );
			start = stop + 1;
			stop = stop + step;
		}
		long end = System.currentTimeMillis();
		System.out.println( "jedis doInsert("+rows+") take time===="+(end-begin)/1000+"s");
		jedis.ltrim("list", 0, 0);//clear redis list data ,but left first
		jedis.lpop("list");// del the first
		jedis.close();
		jedis.quit();
	}

	private void insert(List<String> ll) {
		if( null==ll || ll.size() == 0)
			return ;
		
		threadPool.submit( new InsertThread("jedis",ll));
	}
	
	public Jedis getJedis() {
		return jedis;
	}

	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}
	
	public void readyData(int rows){
		for( int i=0;i<rows; i++){
			jedis.lpush("list", i+"");
		}
	}
	
	public static void main(String[] args) {
//		JedisOperator opt = new JedisOperator();
//		String v = opt.getJedis().get("hello");
//		System.out.println( v );
//		opt.getJedis().set("a", "foo");
//		System.out.println( opt.getJedis().get("a") );
//		System.out.println( opt.getJedis().del("a") );
		
		JedisOperator mqOp = new JedisOperator();
		//mqOp.readyData(500);
		mqOp.doInsert(5);
	}
	
}
