package com.apigateway.util;

import io.micrometer.core.instrument.Counter;

public class MyThread extends Thread {

	
	public MyThread(Counter tempCounter) {
		this.tempCounter = tempCounter;
	}
	private Counter tempCounter;

    @Override
    public void run() {
    	try {
			Thread.sleep(8000);
			tempCounter.increment();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
