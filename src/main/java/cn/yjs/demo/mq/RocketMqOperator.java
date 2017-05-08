package cn.yjs.demo.mq;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

import cn.yjs.demo.DbOpterator;
import cn.yjs.demo.common.Constant;
import cn.yjs.demo.common.InsertThread;

/**
 * DB瞬时间大量插入操作，占用IO资源很大，影响其他核心业务对数据库的资源利用，而且容易导致db宕机，
 * 所以如果对数据不是非常的实时，其实是可以选择一些高效的异步通知和同步机制来缓解流量，时间换空间的做法，
 * MQ的作用是在流量高峰的时候，让业务异步，消峰填谷的目的，
 * 尤其是RocketMq做为阿里的高性能高吞吐的消息中间件值得借鉴，demo里采用Consumer批量消费的方案，尽量让数据快速入库，又能起到对DB资源的缓和利用
 * 
 * @author quyf
 *
 */
public class RocketMqOperator implements DbOpterator {
	
	public int coreSize = Runtime.getRuntime().availableProcessors() + 1 ;
	public ExecutorService threadPool = Executors.newFixedThreadPool( coreSize );
	
	public void doInsert(final int rows) {
		try{
			DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("unique_group_name_c");  
			consumer.setNamesrvAddr( Constant.RMQ_NAME_SERVER );
	        
	        consumer.setConsumeMessageBatchMaxSize(5);//批量消费200  
	      
	        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);  
	  
	        consumer.subscribe("TopicTest", "*");  
	        final long begin = System.currentTimeMillis();
	        consumer.registerMessageListener(new MessageListenerConcurrently() {  
	  
	            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {  
	            	//System.out.println("msgs的长度" + msgs.size() );  
	            	handleMqMsg( msgs );
	            	if( msgs.size()==0 ){
	            		 System.out.println("Consumer over.");  
	            		 long end =  System.currentTimeMillis();
	            		 System.out.println( "mq doInsert("+rows+") take time===="+(end-begin)/1000+"s");
	            	}
	                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;  
	            }  
	        });  
	        
	        consumer.start();  
	  
	        System.out.println("Consumer Started.");  
		}catch (MQClientException e) {
            e.printStackTrace(); 
        }
	}
	
	private void handleMqMsg(List<MessageExt> msgs){
		if( null==msgs || msgs.size() == 0)
			return ;
		List<String> ll = new ArrayList<String>( msgs.size() );
		for(MessageExt ext:msgs ){
			ll.add( new String(ext.getBody()));
		}
		
		threadPool.submit( new InsertThread("mq",ll));
	}
	
	public void readyData(int rows){
		try {
        	DefaultMQProducer producer = new DefaultMQProducer("unique_group_name_p");
            producer.setNamesrvAddr( Constant.RMQ_NAME_SERVER );
            producer.start();

            for (int i = 0; i < rows; i++) {
                Message msg = new Message("TopicTest", "TagA", (i+"").getBytes());
                /*SendResult sendResult =*/ producer.send( msg );
               //SendResult sendResult = producer.send( msg );
              // System.out.println( "produce-"+sendResult);
            }

            producer.shutdown();
        }
        catch (MQClientException e) {
            e.printStackTrace(); 
        }
        catch (RemotingException e) {
            e.printStackTrace();
        }
        catch (MQBrokerException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
	
	public static void main(String[] args) throws InterruptedException, MQClientException {  
		RocketMqOperator mqOp = new RocketMqOperator();
		mqOp.readyData(20);
		mqOp.doInsert(2);
    }  

}
