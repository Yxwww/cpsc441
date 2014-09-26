package cpsc441.legacy;

import java.util.concurrent.TimeUnit;

public class ThreadExample implements Runnable {
	private String greeting;
	
	public ThreadExample(String greeting) {
		this.greeting = greeting;
	}
	
	public void run() {
		while(true) {
			System.out.println(Thread.currentThread().getName() + ": "+greeting);
			
			try {
				TimeUnit.MILLISECONDS.sleep(((long) Math.random() * 100));
			} catch(InterruptedException e) {
			}
		}
	}
	
	public static void main_(String [] args) {
		new Thread(new ThreadExample("T1")).start();
		new Thread(new ThreadExample("T2")).start();
		new Thread(new ThreadExample("T3")).start();
	}
}
