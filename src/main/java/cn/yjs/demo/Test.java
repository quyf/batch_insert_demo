package cn.yjs.demo;

import java.util.concurrent.locks.ReentrantLock;

public class Test {

	public static void main(String[] args) {
		ReentrantLock lock = new ReentrantLock();
		lock.lock();
		lock.lock();
	}
	
	protected static boolean tryAcquire(int arg) {
        throw new UnsupportedOperationException();
    }
}
