package com.tedu;
/**
 * 该线程任务供WebServer使用，负责处理指定客户端的交互
 * @author soft01
 *
 */

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;

import com.tedu.context.HttpContext;
import com.tedu.context.ServletContext;
import com.tedu.http.EmptyRequestException;
import com.tedu.http.HttpRequest;
import com.tedu.http.HttpResponse;
import com.tedu.servlet.HttpServlet;
import com.tedu.servlet.LoginServlet;
import com.tedu.servlet.RegServlet;
import com.tedu.servlet.UpdateServlet;

public class ClientHandler implements Runnable{
	
	private Socket socket;
	
	public ClientHandler(Socket socket) {
		this.socket=socket;
	}

	public void run() {
		try {
			
			//打桩
			System.out.println("ClientHandler:开始处理请求");
			
			
			//创建响应对象
			HttpResponse response=new HttpResponse(socket.getOutputStream());
			
			/*
			 * 解析请求信息
			 */
			HttpRequest request=new HttpRequest(socket.getInputStream());
			
			//获取请求路径
			String requestURI=request.getRequestURI();
			//判断是否为注册功能 
			
			//获取URI对应的Servlet名字
			String servletName=ServletContext.getServletNameByURI(requestURI);
			
			if(servletName!=null) {
				//处理业务
				Class cls=Class.forName(servletName);
				
//				Object obj=cls.newInstance();
//				Method method=cls.getDeclaredMethod("service", new Class[] {HttpRequest.class,HttpResponse.class});
//				method.invoke(obj, new Object[] {request,response});
				
				//面向接口编程
				HttpServlet servlet=(HttpServlet)cls.newInstance();
				servlet.service(request, response);
				
			}else {
				
				File file=new File("webapps"+requestURI);
				//判断客户端请求的文件是否存在，该文件可能是.html .jpg .png .ico 等
				//文件名从http的请求行中通过解析获得
				/*
				 * 浏览器通过发送请求给服务器请求资源，该资源可能是各种文件
				 * 请求次数的多少取决于html文件标签
				 * 通过请求资源，服务器首先判断该文件资源是否存在 如果存在则进行一次响应
				 * 一次响应包含若干步骤：
				 * 状态行的写出
				 * 响应头的写出
				 * 响应正文的写出
				 * 一次请求一次响应，如此往复
				 * 
				 * 步骤：
				 * 1 服务器拿到serversocket对象，初始化该对象 监控8088端口，浏览器通过该端口访问服务器后，则服务器内存创建socket对象，服务器通过该对象和浏览器进行会话
				 * 2 服务器通过socket对象进一步拿到具体的outputstream流对象进行字符和正文的写出 通过拿到inputstream对象进行浏览器请求信息的读入
				 * 3 通过inputstream进行请求行的解析，解析出请求行 请求头 ，通过请求行拿到资源文件的名字等信息 并在响应中写出状态行 响应头 响应正文 
				 * 4 如何解析请求 作出响应下放到具体的实现类中，总体思想：自顶向下 逐层细化 提供服务 封装细节
				 * 
				 * 
				 */
				
				
				
					if(file.exists()) {
						
						String fileName=file.getName();
						System.out.println("文件名字："+fileName);
						
						//获取后缀名
						int ind=fileName.lastIndexOf(".");
						String extension=fileName.substring(ind+1);
						System.out.println(extension);
						
						String contentType=HttpContext.getMimeType(extension);
						System.out.println("文件后缀所对应的介质类型："+contentType);
						
						//设置响应头
						response.setContentType(contentType);
						response.setContentLength(file.length());
						response.setEntity(file);
						response.flush();
						
					}else {
						System.out.println("文件不存在");
					}
				}
					
		}catch(EmptyRequestException e) {
			
			System.out.println(e.getMessage());
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			try {
				socket.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	


}
