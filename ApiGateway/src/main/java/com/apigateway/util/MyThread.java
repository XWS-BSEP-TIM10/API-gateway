package com.apigateway.util;

import io.micrometer.core.instrument.Counter;

public class MyThread extends Thread {

   /* private HttpServletRequest request;
    private String status;
    private String timestamp;
    private static final String HTTP_STATUS_TAG = "http_status";
    private static final String IP_ADDR_TAG = "ip_addr";
    private static final String WEB_BROWSER_TAG = "web_browser";
    private static final String TIMESTAMP_TAG = "timestamp";
    private static final String ENDPOINT_TAG = "endpoint";

    public MyThread(HttpServletRequest request,String status, String timestamp) {
        this.request = request;
        this.status = status;
        this.timestamp = timestamp;
    }*/
	
	public MyThread(Counter tempCounter) {
		this.tempCounter = tempCounter;
	}
	private Counter tempCounter;

    @Override
    public void run() {
    	try {
			Thread.sleep(16000);
			tempCounter.increment();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
