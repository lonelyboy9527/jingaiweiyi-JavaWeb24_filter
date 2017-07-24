package com.itheima.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class QuickFilter1 implements Filter {

	@Override
	// doFilter是Filter的核心过滤的方法
	
	/* ServletRequest:内部封装的是客户端的请求的内容
	 * ServletResponse:代表是响应
	 * FilterChain:过滤器链对象
	 * */
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		
		//截住
		System.out.println("quick1 running...");
		
		//请求 放行
		arg2.doFilter(arg0, arg1);	
	}

	@Override
	//Filter创建的时候执行init方法
	public void init(FilterConfig arg0) throws ServletException {
		//1.获得web.xml中，filter名字<filter-name>QuickFilter1</filter-name>
		String filterName = arg0.getFilterName();
		System.out.println(filterName);
		
		//2.获得当前filter的初始化参数
		/*
		 <init-param>
    		<param-name>aaa</param-name>
    		<param-value>AAA</param-value>
    	</init-param>
		 * */
		String initParameter = arg0.getInitParameter("aaa");
		System.out.println(initParameter);
		
		//3.获得servletContext
		ServletContext servletContext = arg0.getServletContext();
		
		System.out.println("quick1 init ...");
	}
	
	@Override
	//Filter销毁的时候执行destroy方法
	public void destroy() {
		System.out.println("quick1 destroy ...");
	}
	
	
}
