package com.tedu.http;
/**
 * 空请求异常
 * 当客户端连接后发送的是空请求，则httprequest在初始化时抛出该异常
 * @author soft01
 *
 */
public class EmptyRequestException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;//版本号

	public EmptyRequestException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmptyRequestException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public EmptyRequestException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public EmptyRequestException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public EmptyRequestException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	

}
