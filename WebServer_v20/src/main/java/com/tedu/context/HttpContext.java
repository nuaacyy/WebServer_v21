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
	
	/*
	 * HTTP协议中头信息定义
	 */
	public static final String HEADER_CONTENT_TYPE="Content-Type";
	public static final String HEADER_CONTENT_LENGTH="Content-Length";
	
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
			System.out.println("标签数目："+subElements.size());
			
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
	
	
}
