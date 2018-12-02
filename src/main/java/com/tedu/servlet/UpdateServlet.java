package com.tedu.servlet;

import com.tedu.dao.UserInfoDAO;
import com.tedu.http.HttpRequest;
import com.tedu.http.HttpResponse;
import com.tedu.vo.UserInfo;

public class UpdateServlet extends HttpServlet{

	//入口
	public void service(HttpRequest request,HttpResponse response) {
		
		try {
			//获取用户修改信息
			String username=request.getParameter("username");
			String password=request.getParameter("password");
			String nickname=request.getParameter("nickname");
			String phonenumber=request.getParameter("phonenumber");
			
			UserInfoDAO dao=new UserInfoDAO();
			
			
			/*
			 * 通过名字找数据库，如果有则修改
			 * 如果没有，则返回修改失败
			 */
			
			
			if(dao.findByUsername(username)==null) {
				response.sendRedirect("update_fail.html");
				
				
			}else {
				UserInfo user=new UserInfo(username, password, nickname, phonenumber);
				dao.update(user);
				response.sendRedirect("update_success.html");
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	
		
		
	
		
		
	}
	

}
