package com.itheima.web.filter;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itheima.domain.User;
import com.itheima.service.UserService;

public class AutoLoginFilter implements Filter {

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest)arg0;
		HttpServletResponse resp = (HttpServletResponse)arg1;
		
		// 获得cookie中的用户名和密码，进行登录操作
		//获得cookie
		Cookie[] cookies = req.getCookies();
		
		String cookie_username = null;
		String cookie_password = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				// 获得名字是 username的password的cookie
				if (cookie.getName().equals("cookie_username")) {
					cookie_username = cookie.getValue();
					//恢复中文用户名
					cookie_username = URLDecoder.decode(cookie_username, "UTF-8");
				}
				if (cookie.getName().equals("cookie_password")) {
					cookie_password = cookie.getValue();
					cookie_password = URLDecoder.decode(cookie_password, "UTF-8");
				}
			}
		}
		
		//判断是否为空
		if (cookie_username != null && cookie_password != null) {
			//登录的代码
			UserService service = new UserService();
			User user = null;
			try {
				user = service.login(cookie_username, cookie_password);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			HttpSession session = req.getSession();
			session.setAttribute("user", user);
		}
		//放行
		arg2.doFilter(arg0, arg1);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
