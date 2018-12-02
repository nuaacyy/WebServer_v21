package com.tedu.context;
/**
 * http 协议相关信息定义
 * @author soft01
 *
 */

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class HttpContext {
	public static final int CR=13;
	public static final int LF=10;
	
	/**
	 * HTTP协议中规定的状态代码
	 */
	/*
	 * 状态代码：200 正常
	 */
	public static final int SATUS_CODE_OK=200;
	/*
	 * 状态代码：404 未发现该资源
	 */
	public static final int SATUS_CODE_NOT_FOUND=404;
	/*
	 * 状态代码：500 服务器处理请求错误
	 */
	public static final int SATUS_CODE_ERROR=500;
	/*
	 * 
	 * 状态代码：302 需要客户端重定向
	 */
	public static final int SATUS_CODE_REDIRECT=302;
	
	/*
	 * 状态码对应的状态描述信息
	 */
	private static Map<Integer, String> codeReasonMap=new HashMap<>();
	
	
	
	/*
	 * HTTP协议中头信息定义
	 */
	public static final String HEADER_CONTENT_TYPE="Content-Type";
	public static final String HEADER_CONTENT_LENGTH="Content-Length";
	public static final String HEADER_LACATION="Location";
	
	/*
	 * 介质类型映射信息
	 */
	private static 	Map<String, String> mimeTypeMapping=new HashMap<>();
	
	static {
		//初始化静态资源
		init();
	}
	
	/**
	 * 初始化所有静态资源
	 */
	private static void init() {
		//1 初始化介质类型映射
		initMimeTypeMapping();
		//2 初始化状态码描述
		initCodeReason();
	}
	
	private static void initCodeReason() {
		// TODO Auto-generated method stub
		codeReasonMap.put(200, "OK");
		codeReasonMap.put(404, "NOT FOUND");
		codeReasonMap.put(302, "MOVED PERMANENTLY");
		codeReasonMap.put(500, "INTERNAL SERVER ERROR");
	}

	/**
	 * 初始化介质类型映射信息
	 */
	private static void initMimeTypeMapping() {

		/**
		 * 临时先写死几个content-type对应的介质类型
		 * 后期会改为读取web.xml文件 将所有的介质类型读取出来设置到这个map中
		 */
		/*
		 * 读取conf/web.xml文档
		 * 
		 * 将根标签<web-app>下所有名字为<mime-mapping>的子标签解析出来
		 * 
		 * 将每个<mime-mapping>标签中的子标签：
		 * <extension>中间的文本作为key
		 * <mime-type>中间的文本作为value
		 * 存入到mimeTypeMapping中
		 */
		try {
			
			SAXReader reader=new SAXReader();
			Document doc=reader.read(new FileInputStream("./conf/web.xml"));
			Element root=doc.getRootElement();
			
			List<Element> subElements=root.elements("mime-mapping");
			
			//遍历
			for (Element element : subElements) {
				Element extensionEle=element.element("extension");
				String extension=extensionEle.getText();
				Element mime_typeEle=element.element("mime-type");
				String mime_type=mime_typeEle.getText();
				
				mimeTypeMapping.put(extension, mime_type);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	
	
	}
	
	/**
	 * 根据资源后缀获取http协议规定对应的介质类型
	 * @param extension
	 * @return
	 */
	public static String getMimeType(String extension) {
		return mimeTypeMapping.get(extension);
	}
	
	
	public static String getStatusReasonByStatusCode(int code ) {
		return codeReasonMap.get(code);
	}
	
	
}
