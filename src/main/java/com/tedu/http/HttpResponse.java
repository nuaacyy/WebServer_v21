package com.tedu.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.b3log.latke.logging.Logger;


import com.tedu.context.HttpContext;
import com.tedu.context.ServletContext;

/**
 * 该类表示一个具体的http响应信息 一个标准的http响应应该包含三部分 状态行 响应头 响应正文
 * 
 * @author soft01
 *
 */
public class HttpResponse {
	/**
	 * Logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(HttpRequest.class);
	/*
	 * 通过这个输出流将响应信息发送给客户端
	 */
	OutputStream out;

	/*
	 * 响应实体 实际发送给客户端的文件
	 */
	private File entity;
	
	/*
	 * 响应头信息
	 */
	private Map<String, String> headers=new HashMap<String, String>();
	

	/*
	 * 状态状态
	 * 
	 */
	private int status_code;
	
	/**
	 * 设置状态代码
	 * @param code
	 */
	public void setStatus_code(int code) {
		this.status_code = code;
	}

	public HttpResponse(OutputStream out) {
		super();
		this.out = out;

	}

	/*
	 * 回复客户端
	 */
	public void flush() {
		/*
		 * 1 发送状态行 2 发送响应头 3 发送响应正文
		 */

		sendStatusLine();
		sendHeaders();
		sendContent();

	}

	/*
	 * 发送状态行
	 */
	private void sendStatusLine() {
		try {
			String statueLine = ServletContext.protocol+" "+status_code+" "+HttpContext.getStatusReasonByStatusCode(status_code);
			println(statueLine);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * 发送响应头
	 */
	private void sendHeaders() {
		
		for(Entry<String, String> e : headers.entrySet()) {
			String line=e.getKey()+":"+e.getValue();
			println(line);
		}

	/*	System.out.println("发送响应头");
		String line = HttpContext.HEADER_CONTENT_TYPE+":image/png";
		System.out.println("header:"+line);

		println(line);

		line = HttpContext.HEADER_CONTENT_LENGTH + ":" + entity.length();
		System.out.println("header:"+line);
		println(line);
*/
		// 单独发送CRLF
		println("");

	}

	/*
	 * 发送响应正文
	 */
	private void sendContent() {
		if(entity==null) {
			return ;
		}
		try (
				FileInputStream fis = new FileInputStream(entity);
				
				BufferedInputStream bis = new BufferedInputStream(fis);
		){
			byte[] buf = new byte[1024 * 10];
			int len = -1;
			while ((len = bis.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向客户端发送一行字符串,发送该字符串后会自动发送 CRLF.
	 * 
	 * @param line
	 *            给定的字符串末尾不需要CRLF
	 */
	private void println(String line) {
		try {
			out.write(line.getBytes("iso8859-1"));
			out.write(HttpContext.CR);
			out.write(HttpContext.LF);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public File getEntity() {
		return entity;
	}

	public void setEntity(File entity) {
		this.entity = entity;
	}
	
	public void setContentType(String contentType) {
		this.headers.put(HttpContext.HEADER_CONTENT_TYPE, contentType);
	}
	
	public void setContentLength(long length) {
		this.headers.put(HttpContext.HEADER_CONTENT_LENGTH, length+"");
	}
	
	/**
	 * 让客户端重定向到指定地址
	 */
	public void sendRedirect(String url) {
		
		
		//设置状态代码为重定向
		this.setStatus_code(HttpContext.SATUS_CODE_REDIRECT);
		//设置响应头：Location
		this.headers.put(HttpContext.HEADER_LACATION, url);
		this.flush();

		
	}
	

}
