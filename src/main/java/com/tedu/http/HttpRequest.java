package com.tedu.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

import java.util.HashMap;
import java.util.Map;

import com.tedu.model.Cookie;
import com.tedu.model.HTTPSession;
import org.b3log.latke.logging.Logger;

import com.tedu.context.HttpContext;
import com.tedu.context.ServletContext;



/**
 * 当前类的每个实例用于表示一个具体的客户端发送过来的HTTP请求
 * @author soft01
 *
 */
public class HttpRequest {
	/**
	 * Logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(HttpRequest.class);
	
	/*
	 * 用来读取客户端发送过来数据的输入流，
	 * 该流应该是通过Socket获取的输入流
	 */
	private InputStream in;
	/*
	 * 请求行相关信息
	 */
	
	
	//请求方式
	private String method;
	//请求资源的路径
	private String url;
	//请求使用的http协议版本
	private String protocol;
	
	//具体的请求资源路径（？前面的内容）
	private String requestURI;
	
	//具体的请求资源路径（？后面的内容）
	private String queryString;
	
	//存储所有参数
	private Map<String, String> parameters=new HashMap<>();
	
	/*
	 * 消息头相关信息
	 */
	private Map<String, String> headers=new HashMap<String, String>();
	
	private Cookie[] cookies;
	private HTTPSession httpSession;
	
	
	
	
	
	/**
	 * 实例化HttpRequest
	 * @param in
	 */
	public HttpRequest (InputStream in) throws EmptyRequestException{
		this.in=in;
		/*
		 * 每个请求包含三部分解析
		 * 1 解析请求行
		 * 2 解析消息头
		 * 3 解析消息正文
		 */
		//1
		parseRequestLine();
		//2
		parseHeaders();
		
		parseContent();
		
	}
	
	/**
	 * 解析消息正文
	 */
	private void parseContent() {
		/*
		 * 查看消息头中有没有Content-Length.若没有
		 * 则表示没有消息正文部分
		 * 
		 */
		if(headers.containsKey(HttpContext.HEADER_CONTENT_LENGTH)) {
			
			try {
				/*
				 * 判断是否为form表单数据
				 */
				String contentType=headers.get(HttpContext.HEADER_CONTENT_TYPE);
				if("application/x-www-form-urlencoded".equals(contentType)) {
					//开始处理form表单数据
					//读取正文中的所有字节
					int length=Integer.parseInt(headers.get(HttpContext.HEADER_CONTENT_LENGTH));
					byte [] data=new byte[length];
					in.read(data);
					
					String line=new String(data, "utf-8");
//					//转码
//					line=URLDecoder.decode(line, "utf-8");
//					System.out.println("++++++++++++++++"+line);
//					/*
//					 * 解析表单内容，并设置到paramenters属性中
//					 */
					parseQueryString(line);
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	
	/**
	 * 解析客户端传递过来的参数
	 * @param line
	 */
	private void parseQueryString(String line) {
		try {
			
			line=URLDecoder.decode(line, ServletContext.URIEncoding);
			String [] paraArray=line.split("&");
			for (String para : paraArray) {
				String [] strings=para.split("=");
				if(strings.length==2) {
					this.parameters.put(strings[0], strings[1]);
				}else {
					this.parameters.put(strings[0], "");
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 解析请求行
	 */
	private void parseRequestLine() throws EmptyRequestException{
		//打桩
		//读取请求行内容
		String requestLine = readLine();
		
		//判断是否为空请求
		if(requestLine.length()==0) {
			throw new EmptyRequestException("空请求");
		}
		//打桩
		
		//将请求行中的三部分分别存到method url protocol
		String[] array=requestLine.split("\\s");
		method=array[0];
		url=array[1];
		
		protocol=array[2];
		
		//详细解析地址请求部分
		parseUrl();
		
		
	}
	
	
	
	
	
	
	
	
	/**
	 * 
	 */
	private void parseUrl() {
		/*
		 * 请求行中地址部分（this.url）有两种情况
		 * 1 直接请求资源
		 * 	例如：/myweb/reg.html
		 * 2 请求功能并附带参数
		 * 	例如：/myweb/reg?username=&password=&nickname=&phonenumber=
		 * 该功能要实现：
		 * 1 将url中"?"（若有“？”）之前的内容设置到属性requestURI中
		 * 2 将url中“？”（若有“？”）之后的内容设置到属性queryString中
		 * 3 解析queryString，将每个参数取出，将参数名作为key
		 * 	参数值作为value存入属性parameters这个map中
		 */
		
		int index=this.url.indexOf('?');
		if(index==-1) {
			this.requestURI=this.url;
		}else {
			this.requestURI=this.url.substring(0, index);
			this.queryString=this.url.substring(index+1);
			
			/*
			 * 将queryString转码
			 * URLDecoder可以对浏览器地址栏发送过来的内容进行转码
			 * http协议要求地址栏传递的参数只能符合ISO8859-1编码内容
			 * 所以像中文这样的字符是不能直接通过地址栏传递的，现在的解决办法是将中文以
			 * utf-8编码转换为字节，
			 * 再将字节以16进制形式表示，前面以%开始，这样每个中文是三个字节，地址栏表示形式类似：
			 * %E8%8C%83%
			 * 注意：两位16进制可以表示一个8位二进制（一个字节）
			 * URLDecoder的decode方法可以对含有上述的字符按照指定的字符集转码
			 * 
			 */
//			try {
//				queryString=URLDecoder.decode(queryString, ServletContext.URIEncoding);
//				
//			}catch (Exception e) {
//				e.printStackTrace();
//			}
//			System.out.println("queryString"+queryString);
//			
//			/*
//			 * 拆分所有参数
//			 */
//			// 1 按照“&”拆分出每个参数
			parseQueryString(queryString);
		
		}
	
	}
	
	
	
	
	
	
	
	/**
	 * 解析消息头
	 */
	private void parseHeaders() {
		//完整请求
		
//		GET /myweb/index.html/ HTTP/1.1
//		Host: localhost:8088
//		Connection: keep-alive
//		Cache-Control: max-age=0
//		Upgrade-Insecure-Requests: 1
//		User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36
//		Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
//		Accept-Encoding: gzip, deflate, sdch, br
//		Accept-Language: zh-CN,zh;q=0.8
	
		//完整请求
		
		
		
		
		/**
		 * 循环读取若干行，每一行是一个消息头
		 * 将消息头“：”左面的内容作为key，右面内容作为value存入到headers这个属性
		 * 中保存 当读取一行返回的是空字符串，说明只读取到了一个CRLF，这标志着请求中的消息头部分读取
		 * 完毕了。
		 */
		//打桩
		
	
		while(true) {
			String line=readLine();
			if("".equals(line)) {
				break;
			}
			int index=line.indexOf(':');
			String name=line.substring(0, index).trim();
			String value=line.substring(index+1).trim();
			
			headers.put(name, value);
			
		}
		
		
	}
	
	
	/**
	 * 读取一行字符串（以crlf作为结尾），返回的字符串不包含最后的crlf
	 * @return
	 */
	private String readLine() {
		
		StringBuilder builder=new StringBuilder();
		
		//c1保存上个字符，c2保存本次读到的字符
		char c1='a';
		char c2='a';
		
		int d=-1;
		try {
			while((d=in.read())!=-1) {
				c2=(char)d;
				if(c1==HttpContext.CR&&c2==HttpContext.LF) {
					break;
				}
				builder.append(c2);
				c1=c2;
			}
			return builder.toString().trim();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	public String getMethod() {
		return method;
	}
	public String getUrl() {
		return url;
	}
	public String getProtocol() {
		return protocol;
	}
	
	public String getHeader(String name) {
		return headers.get(name);
	}
	
	public String getRequestURI() {
		return this.requestURI;
	}
	
	public String getQueryString() {
		return this.queryString;
	}
	
	
	public String getParameter(String name) {
		return this.parameters.get(name);
	}
	

}
