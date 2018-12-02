package com.tedu;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tedu.context.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * WebServer web服务端主类
 * 
 * 
 * 
 * 
 */

// 这是一个git测试

public class WebServer {
	/*
	 * 负责与客户端（浏览器）进行TCP连接的ServerSocket
	 */
	private ServerSocket server;
	/*
	 * 日志系统
	 */
	private static final Logger LOGGER =  LoggerFactory.getLogger(WebServer.class);
	
	/**
	 * 线程池 负责管理处理客户端请求的线程
	 */
	private ExecutorService threadPool;

	/**
	 * 构造方法，用来初始化服务端
	 */
	public WebServer() {
		try {
			
			server=new ServerSocket(ServletContext.port);
			threadPool=Executors.newFixedThreadPool(ServletContext.threadPoolSum);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 服务端开始工作的方法
	 */
	public void start() {
		
		try {
			while(true) {
				
				Socket socket=server.accept();
				LOGGER.info("-有一个-"+socket+"-连接上-");
				//重用线程
				//控制线程数量
				ClientHandler handler=new ClientHandler(socket);
				threadPool.execute(handler);

			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void main(String[] args) {
		WebServer server=new WebServer();
		server.start();
		
	}
}
