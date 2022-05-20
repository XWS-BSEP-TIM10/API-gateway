package com.apigateway.security.config;

import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.apigateway.security.auth.XSSRequestWrapper;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class XSSFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public XSSFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		// Deny ClickJacking Attacks from URL's outside our application domain
		HttpServletResponse res = (HttpServletResponse) response;
		//String context2=request.get
		XSSRequestWrapper reqWrapper = new XSSRequestWrapper((HttpServletRequest) request);
		

		if(!reqWrapper.getRequestURI().contains("error")){
			if (reqWrapper.isMatch()) {
				res.reset();
				res.addHeader("X-FRAME-OPTIONS", "SAMEORIGIN");
				res.addHeader("X-XSS-Protection", "1; mode=block");
				res.addHeader("X-Content-Type-Options", "nosniff");
				res.setStatus(HttpServletResponse.SC_BAD_REQUEST);	
				res.sendError(HttpServletResponse.SC_BAD_REQUEST);
				//request.getRequestDispatcher("error").forward(request, res);
				//res.sendRedirect(request.getServletContext().getContextPath() + "/jsp/exception/403.jsp");
				return;
			}
		}
		res.addHeader("X-FRAME-OPTIONS", "SAMEORIGIN");
		res.addHeader("X-XSS-Protection", "1; mode=block");
		res.addHeader("X-Content-Type-Options", "nosniff");
		chain.doFilter(request, res);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}
}
