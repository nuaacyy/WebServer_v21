package com.tedu.context;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;



/**
 * 服务端配置信息 上下文 
 * @author soft01
 *
 */
public class ServletContext {
	
	//服务端口
	public static int port;
	//协议版本
	public static String protocol;
	//线程池线程数量
	public static int threadPoolSum;
	//URI字符集
	public static String URIEncoding;
	
	
	
	
	
	
	/*
	 * Servlet与请求的映射关系
	 * key： 请求uri
	 * value： Servlet的名字
	 */
	private static Map<String, String> servletMapping=new HashMap<String, String>();
	
	static {
		init();
	}

	private static void init() {
		//初始化Servlet映射信息
		initServletMapping();
		//初始化服务端基础配置信息
		initServerConfig();
	}
	
	
	
	
	/**
	 * 初始化服务端基础配置信息
	 */
	private static void initServerConfig() {
		try {
			SAXReader reader=new SAXReader();
			Document doc=reader.read(new File("./conf/config.xml"));
			//获取根标签
			Element root=doc.getRootElement();
			Element con=root.element("connector");
			//
			port=Integer.parseInt(con.attributeValue("port"));
			protocol=con.attributeValue("protocol");
			threadPoolSum=Integer.parseInt(con.attributeValue("threadPool"));
			URIEncoding=con.attributeValue("URIEncoding");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	

	/**
	 * 
	 */
	private static void initServletMapping() {
		/*
		 * 解析servlet.xml文件，将每个<servlet>标签中的属性uri的值作为key，将
		 * 属性class的值作为value存入到servletMapping中
		 */
		try {
			SAXReader reader=new SAXReader();
			Document doc=reader.read(new File("./conf/servlet.xml"));
			//获取根元素
			Element root=doc.getRootElement();
			List<Element> subElements=root.elements("servlet");
			for (Element element : subElements) {
				String uri=element.attributeValue("uri");
				String xxServlet=element.attributeValue("class");
				servletMapping.put(uri, xxServlet);
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * 根据给定的uri获取对应的Servlet的名字
	 * @param uri
	 * @return
	 */
	public static String getServletNameByURI(String uri) {
		
		return servletMapping.get(uri);
	}
	
	
}
