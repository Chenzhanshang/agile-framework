package com.agile.framework.config;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

@Slf4j
public class JsonRequestWrapperFilter implements Filter {

	public JsonRequestWrapperFilter() {
	}

	@Override
	public void doFilter(ServletRequest request,
						 ServletResponse response,
						 FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		if (log.isDebugEnabled()) {
			requestHeaders(req);
		}
		if (JsonRequestWrapper.isJson(req)) {
			log.debug("JsonRequestWrapperFilter JsonRequest wrapper");
			JsonRequestWrapper wrapper = new JsonRequestWrapper(req);
			chain.doFilter(wrapper, response);
		} else {
			chain.doFilter(request, response);
		}
	}

	private void requestHeaders(HttpServletRequest request) {
		Enumeration<String> headers = request.getHeaderNames();
		while(headers.hasMoreElements()) {
			String header = headers.nextElement();
			log.debug("[header]{}: {}", header, request.getHeader(header));
		}
	}
}
