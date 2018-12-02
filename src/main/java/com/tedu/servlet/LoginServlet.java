package com.tedu.servlet;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.tedu.dao.UserInfoDAO;
import com.tedu.http.HttpRequest;
import com.tedu.http.HttpResponse;
import com.tedu.vo.UserInfo;

public class LoginServlet extends HttpServlet {

	/**
	 * 处理请求的方法
	 * @param request
	 * @param response
	 */
	public void service(HttpRequest request,HttpResponse response) {
		
		try(
				//
				RandomAccessFile raf=new RandomAccessFile("user.dat", "r");
		) {
			
			UserInfoDAO dao=new UserInfoDAO();
			


			//获取用户登录信息
			String username=request.getParameter("username");
			String password=request.getParameter("password");
			
			//先根据用户名获取该用户信息
			UserInfo userinfo=dao.findByUsername(username);

		
			if(userinfo!=null && userinfo.getPassword().equals(password)) {
				
				response.sendRedirect("login_suc.html");

			}else {
				response.sendRedirect("login_fail.html");
			
			}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
