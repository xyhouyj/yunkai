package com.hyj.distribute.lock;

public class ThreadJoinTest {
public static void main(String[] args) throws InterruptedException {
	Thread t1 = new Thread(new Runnable() {
		
		public void run() {
			for (int i = 0; i < 10; i++) {
				System.out.println(Thread.currentThread().getName());
			}
			
		}
	});
	
Thread t2 = new Thread(new Runnable() {
		
		public void run() {
			for (int i = 0; i < 10; i++) {
				System.out.println(Thread.currentThread().getName());
			}
			
		}
	});

	
	t1.start();
	t2.start();
	for (int i = 0; i < 10; i++) {
		if (i==5) {
			t2.join();
		}
	}
//	t2.join();
}
}
