package com.tedu.servlet;

import java.io.File;

import com.tedu.context.HttpContext;
import com.tedu.http.HttpRequest;
import com.tedu.http.HttpResponse;

/**
 * 用来处理请求的所有Servlet的父类
 * @author soft01
 *
 */
public class HttpServlet {
	
	//入口
	public void service(HttpRequest request,HttpResponse response) {
		/*
		 * 实际在使用tomcat时，httpservlet类的service会根据请求方式：
		 * get post来调用另外的两个方法
		 * doget dopost
		 * 现在常用做法是直接继承httpservlet并重写service处理业务逻辑了
		 * 
		 */
	}

	//内部跳转 一次请求一次响应
	//重定向 
	public void forward(String uri,HttpRequest request,HttpResponse response) {
		
		File page=new File("webapps"+uri);
		response.setStatus_code(HttpContext.SATUS_CODE_OK);
		response.setContentLength(page.length());
		response.setContentType("text/html");
		response.setEntity(page);
		response.flush();

		
	}
	
}
