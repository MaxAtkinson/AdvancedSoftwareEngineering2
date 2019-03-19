package model;

import java.util.Observable;

public class Qthread extends Observable implements Runnable {
	
	private CustomerQueue cq = CustomerQueue.getInstance();

	@Override
	public void run() {
		
		System.out.println(cq.checkBuffer());
		
		while(!cq.checkBuffer()){
			cq.popPush(cq.getBuffer(), cq.getQueue());
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Thread Terminated");
	}


}
