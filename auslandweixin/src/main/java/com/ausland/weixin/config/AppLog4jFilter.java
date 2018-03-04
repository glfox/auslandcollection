package com.ausland.weixin.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

 
@Component
public class AppLog4jFilter extends OncePerRequestFilter {

	private final static Logger logger = Logger.getLogger(AppLog4jFilter.class);
	
	private static final String TRXID = "TrxId";
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if(response.getHeader(TRXID) != null)
		{
			MDC.put(TRXID, TRXID + ":" + response.getHeader(TRXID));
		}
		else
		{
			MDC.put(TRXID, TRXID + ":" + AppThreadLocal.getId());
			request.setAttribute(TRXID, AppThreadLocal.getId());
			response.addHeader(TRXID, AppThreadLocal.getId());
		}
		try {
			filterChain.doFilter(request, response);
		} finally {
			MDC.remove(TRXID);
			AppThreadLocal.clear();
		}
	}

}
