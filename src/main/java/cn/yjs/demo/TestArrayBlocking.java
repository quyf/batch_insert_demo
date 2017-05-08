package cn.yjs.demo;

public class TestArrayBlocking {

	public static void main(String[] args) throws Exception {
		final ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(5);
		queue.offer( 1 );queue.offer( 2 );queue.offer( 3 );queue.offer( 4 );
		queue.offer( 9 );
		new Thread(new Runnable() {
			
			public void run() {
				try {
					queue.put( 10 );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		queue.take();
		queue.take();
		queue.offer( 5 );
		queue.offer( 7 );
		queue.take();
		queue.offer( 6 );
		queue.take();
		queue.take();
		queue.take();
		queue.take();
		queue.take();
		queue.take();
	}
}
