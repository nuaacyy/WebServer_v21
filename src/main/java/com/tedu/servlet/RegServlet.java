package com.tedu.servlet;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.tedu.dao.UserInfoDAO;
import com.tedu.http.HttpRequest;
import com.tedu.http.HttpResponse;
import com.tedu.vo.UserInfo;

/**
 * 用来处理/myweb/reg请求，完成注册业务
 * 
 * @author soft01
 *
 */
public class RegServlet extends HttpServlet{

	/**
	 * 处理请求的方法
	 * @param request
	 * @param response
	 */
	public void service(HttpRequest request,HttpResponse response) {
		try {
			//获取用户注册信息
			String username=request.getParameter("username");
			String password=request.getParameter("password");
			String nickname=request.getParameter("nickname");
			String phonenumber=request.getParameter("phonenumber");
			
			UserInfoDAO dao=new UserInfoDAO();
			
			
			/*
			 * 首先检查该用户是否存在，不存在才写入文件
			 * 存在则跳转一个显示该用户已存在的页面
			 */
			
			
			if(dao.findByUsername(username)==null) {
				
				UserInfo user=new UserInfo(username, password, nickname, phonenumber);
				
				dao.save(user);
				response.sendRedirect("reg_success.html");
				
				
			}else {
				response.sendRedirect("reg_fail.html");
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	
		
		
	}
}
