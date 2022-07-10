package com.apigateway.security.config;

import java.lang.reflect.Field;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.catalina.connector.Response;
import org.apache.catalina.connector.ResponseFacade;
import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.util.ReflectionUtils;

public class LengthInterceptor implements HandlerInterceptor {
	
	 @Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		 System.out.println(request.getContentLength());
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
		     
		  
		  System.out.println(connectorResponse.getCoyoteResponse().getBytesWritten(true));

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