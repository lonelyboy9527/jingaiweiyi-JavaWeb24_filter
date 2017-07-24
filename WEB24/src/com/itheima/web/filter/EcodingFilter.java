package com.itheima.web.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class EcodingFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		
		//post乱码解码
		//arg0.setCharacterEncoding("UTF-8");
		//arg2.doFilter(arg0, arg1);
		
		//get乱码
		//在传递arg0之前对arg0.getParameter方法进行增强
		/**
		 * 装饰者模式（包装）
		 * 1、增强类与被增强的类要实现同一个接口
		 * 2、在增强类中传入被增强的类
		 * 3、需要增强的方法重写，不需要增强的方法调用被增强对象的
		 * 
		 * */
		
//		//被增强的对象
//		HttpServletRequest req = (HttpServletRequest) arg0;
//		//增强对象
//		EnhanceRequest  enhanceRequest = new EnhanceRequest(req);
//		arg2.doFilter(enhanceRequest, arg1);
		
		
		/*
		 * WEB25使用动态代理解决乱码问题
		 * */
		final HttpServletRequest req1 = (HttpServletRequest) arg0;
		HttpServletRequest newProxyInstance = (HttpServletRequest)Proxy.newProxyInstance(
				req1.getClass().getClassLoader(), 
				req1.getClass().getInterfaces(), 
				new InvocationHandler() {
					
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						
						//对getParameter方法进行增强
						String name = method.getName();
						if ("getParameter".equals(name)) {
							String invoke = (String)method.invoke(req1, args); //乱码
							//转码
							invoke = new String(invoke.getBytes("iso8859-1"), "UTF-8");
							return invoke;
						}
						return method.invoke(req1, args);
					}
				});
		
		arg2.doFilter(newProxyInstance, arg1);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}

class EnhanceRequest extends HttpServletRequestWrapper{

	private HttpServletRequest request;
	
	public EnhanceRequest(HttpServletRequest request) {
		super(request);
		this.request = request;
	}

	//对 getParameter 增强
	@Override
	public String getParameter(String name) {
		String parameter = request.getParameter(name); //获得乱码
		System.out.println("开始转码...");
		try {
			parameter = new String(parameter.getBytes("iso8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return parameter;
	}
}