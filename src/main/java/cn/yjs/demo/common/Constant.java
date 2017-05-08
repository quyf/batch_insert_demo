package cn.yjs.demo.common;

public class Constant {

	//jedis相关
	public static final String JEDIS_HOST = "192.168.10.129";//vm虚拟机安装的redis
	public static final int    JEDIS_PORT =  6379;
	public static final String JEDIS_PWD = "a123456";
	//mysql相关
	public static final String MYSQL_URL = "jdbc:mysql://127.0.0.1:3306/S61?autoReconnect=true";//测试库
	public static final String MYSQL_USER = "xxx";
	public static final String MYSQL_PWD =  "a123456";
	
	public static final String RMQ_NAME_SERVER = "192.168.10.129:9876";//vm开的mq
	
	public static final String SQL = "INSERT INTO S61.T_TEST_INSERT(`u_id`,`user_name`,`create_time`) VALUES(?,?,NOW());";
	
	
}
