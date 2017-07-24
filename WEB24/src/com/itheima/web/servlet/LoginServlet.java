package com.itheima.web.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itheima.domain.User;
import com.itheima.service.UserService;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LoginServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		HttpSession session = request.getSession();
		//获取数据 
		String username = request.getParameter("username");//中文张三 
		String password = request.getParameter("password");
		
		UserService service = new UserService();
		User user = null;
		try {
			user = service.login(username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (user != null) {
			//登录成功
			//判断用户是否勾选自动登录
			String autoLogin = request.getParameter("autoLogin");
			if (autoLogin != null) {
				
				// 对中文张三进行编码
				String username_code = URLEncoder.encode(username, "UTF-8");
				
				Cookie cookie_username = new Cookie("cookie_username", username_code);
				Cookie cookie_password = new Cookie("cookie_password", user.getPassword());
				
				//设置cookie的持久化时间
				cookie_username.setMaxAge(60 * 60);//1小时
				cookie_password.setMaxAge(60 * 60);//1小时
				//设置cookie的携带路径
				cookie_username.setPath(request.getContextPath());
				cookie_password.setPath(request.getContextPath());
				//发送cookie
				response.addCookie(cookie_username);
				response.addCookie(cookie_password);
			}
			
			//将登录的用户user对象存到session中
			System.out.println(user.getUsername() + "登录成功...");
			session.setAttribute("user", user);
			System.out.println(request.getContextPath());
			response.sendRedirect(request.getContextPath());
			
						
		} else {
			//失败转发到登录页面给出提示信息
			request.setAttribute("loginInfo", "用户名或密码错误");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);

	}
}
