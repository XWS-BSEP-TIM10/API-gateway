package com.apigateway.security.config;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.catalina.connector.ResponseFacade;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.apigateway.controller.AuthController;
import com.apigateway.util.MyThread;

import io.micrometer.core.instrument.Counter;

public class LengthInterceptor implements HandlerInterceptor {
	
	private static final String COUNTER_NAME_TRAFFIC = "network_traffic";
	private static final String COUNTER_NAME = "http_requests";
    private static final String HTTP_STATUS_TAG = "http_status";
    private static final String IP_ADDR_TAG = "ip_addr";
    private static final String WEB_BROWSER_TAG = "web_browser";
    private static final String TIMESTAMP_TAG = "timestamp";
    private static final String ENDPOINT_TAG = "endpoint";
	
	private final Counter getTraffic;
	private final SimpleDateFormat iso8601Formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public LengthInterceptor() {
		this.getTraffic = Counter.builder(COUNTER_NAME_TRAFFIC)
                .description("Network traffic")
                .register(AuthController.registry);
	}
	
	 @Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		 System.out.println(request.getContentLength());
		 System.out.println((request.getContentLength()/(double)(1024*1024*2014)));
		 getTraffic.increment((request.getContentLength()/(double)(1024*1024*1024)));
		 return true;
	 }

	 @Override
	 public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
				@Nullable ModelAndView modelAndView) throws Exception {
		 Field field = ReflectionUtils.findField(ResponseFacade.class, "response");
         ReflectionUtils.makeAccessible(field);
         Field fils = field;
		 ResponseFacade responseFacade = getResponseFacade(response);
		 org.apache.catalina.connector.Response connectorResponse = (org.apache.catalina.connector.Response)
                 ReflectionUtils.getField(fils, responseFacade);
		     
		  Long responseSize = connectorResponse.getCoyoteResponse().getBytesWritten(true);
		  System.out.println(responseSize);
		  System.out.println((responseSize/(double)(1024*1024*1024)));
		  getTraffic.increment((responseSize/(double)(1024*1024*1024)));
		  
		  Counter tempCounter = Counter.builder(COUNTER_NAME)
                  .description("Number of HTTP requests for server endpoints")
                  .tag(HTTP_STATUS_TAG, String.valueOf(response.getStatus()))
                  .tag( IP_ADDR_TAG, request.getRemoteAddr())
                  .tag(WEB_BROWSER_TAG, request.getHeader("User-Agent"))
                  .tag(TIMESTAMP_TAG, iso8601Formatter.format(new Date()))
                  .tag(ENDPOINT_TAG, request.getRequestURI())
                  .register(AuthController.registry); 
                  new MyThread(tempCounter).start();
		}
	 
	 private static ResponseFacade getResponseFacade(HttpServletResponse response) {
         if (response instanceof ResponseFacade) {
             return (ResponseFacade) response;
         }
         else if (response instanceof HttpServletResponseWrapper) {
             HttpServletResponseWrapper wrapper = (HttpServletResponseWrapper) response;
             HttpServletResponse wrappedResponse = (HttpServletResponse) wrapper.getResponse();
             return getResponseFacade(wrappedResponse);
         }
         else {
             throw new IllegalArgumentException("Cannot convert [" + response.getClass() +
                     "] to org.apache.catalina.connector.ResponseFacade");
         }
     }
 
}