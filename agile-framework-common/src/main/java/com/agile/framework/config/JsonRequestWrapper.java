package com.agile.framework.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 专为json请求增加一级wrapper, 避免ServletInputStream只能读取一次的问题
 * wrapper采用ByteArrayInputStream暂存input stream
 * ByteArrayInputStream因为内存流可能会占不少空间, 但不需要用户自行回收内存
 *
 * @author chenzhanshang
 */
@Slf4j
public class JsonRequestWrapper extends HttpServletRequestWrapper implements HttpServletRequest {
	private byte[] bytes;

	public JsonRequestWrapper(HttpServletRequest request) {
		super(request);
		log.debug("JsonHttpServletRequestWrapper");
		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				log.debug("JsonHttpServletRequestWrapper getInputStream");
				bytes = StreamUtils.copyToByteArray(inputStream);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		log.debug("JsonHttpServletRequestWrapper getInputStream");
		return new ServletInputStream() {
			ByteArrayInputStream inputStr = new ByteArrayInputStream(bytes);
			@Override
			public int read() {
				return inputStr.read();
			}

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
			}
		};
	}

	@Override
	public BufferedReader getReader() throws IOException {
		log.debug("JsonHttpServletRequestWrapper getReader");
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	public static boolean isJson(HttpServletRequest request) {
		String contentType = request.getHeader("Content-Type");
		if (! StringUtils.isEmpty(contentType) && contentType.toLowerCase().matches(".*json.*")) {
			return true;
		}
		return false;
	}
}
