package com.tedu.dao;

import java.io.RandomAccessFile;
import java.util.Arrays;

import com.tedu.vo.UserInfo;

/**
 * DAO 数据连接对象 DATA ACCESS OBJECT 数据持久层中的类的总称 DAO的主要工作是对数据进行持久化操作 对数据做实际的增删改查
 * 
 * DAO与业务逻辑层之间互相传递数据是以对象的形式传递的
 * 
 * DAO负责将业务逻辑层传递过来的对象中的所有数据保存起来 也负责将数据在转换为对象传递给业务逻辑层
 * 
 * @author soft01
 *
 */
public class UserInfoDAO {
	
	/**
	 * 保存用户信息
	 * @param userinfo
	 * @return true：保存成功 false：保存失败
	 */
	public boolean save(UserInfo userinfo) {
		/*
		 * 先使用RandomAccessFile写user.dat
		 * 
		 * 先将指针移动到文件最后
		 * 写入116字节的内容
		 * 将给定的UserInfo对象表示的用户信息保存
		 */
		try (RandomAccessFile raf=new RandomAccessFile("user.dat", "rw");){
			
			raf.seek(raf.length());
			
			String username=userinfo.getUsername();
			String password=userinfo.getPassword();
			String nickname=userinfo.getNickname();
			String phonenumber=userinfo.getPhonenumber();
			
			writeString(raf, username, 20);
			writeString(raf, password, 32 );
			writeString(raf, nickname, 32);
			writeString(raf, phonenumber, 32 );
			
			return true;
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	/**
	 * 根据给定的用户名查找该用户信息
	 * 
	 * @param username
	 * @return 若返回值为null表示没有此用户
	 */

	public UserInfo findByUsername(String username) {

		try (RandomAccessFile raf = new RandomAccessFile("user.dat", "rw");) {
			for (int i = 0; i < raf.length() / 116; i++) {
				raf.seek(i * 116);
				String name = readString(raf, 20);
				if (name.equals(username)) {
					String password = readString(raf, 32);
					String nickname = readString(raf, 32);
					String phonenumber = readString(raf, 32);
					return new UserInfo(name, password, nickname, phonenumber);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private String readString(RandomAccessFile raf, int len) {
		try {

			byte[] data = new byte[len];
			raf.read(data);
			String str = new String(data, "utf-8").trim();
			return str;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	/**
	 * 如果写入成功则返回true 写入失败返回false
	 * @param raf
	 * @param str
	 * @param len
	 * @return
	 */
	private boolean writeString(RandomAccessFile raf,String str,int len) {
		try {
			byte[] data;
			data=str.getBytes("utf-8");
			data=Arrays.copyOf(data, len);//扩容
			raf.write(data);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean update(UserInfo userinfo) {

		try (RandomAccessFile raf = new RandomAccessFile("user.dat", "rw");) {
			for (int i = 0; i < raf.length() / 116; i++) {
				raf.seek(i * 116);
				String name = readString(raf, 20);
				if (name.equals(userinfo.getUsername())) {
					String pwd=userinfo.getPassword();
					String nick=userinfo.getNickname();
					String phone=userinfo.getPhonenumber();
					writeString(raf,pwd,32);
					writeString(raf,nick,32);
					writeString(raf,phone,32);
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	
}
