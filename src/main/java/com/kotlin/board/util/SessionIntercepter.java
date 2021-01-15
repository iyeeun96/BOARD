package com.kotlin.board.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.java.Log;

@Log
public class SessionIntercepter extends HandlerInterceptorAdapter {
	@Autowired
	HttpSession session;
	
	//컨트롤러로 요청이 전달되기 전에 처리하는 메소드
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("preHandler()-인터셉트");
		
		//세션에 mb가 없으면 첫번째 페이지로 이동
		if(session.getAttribute("mb")==null) {
			response.sendRedirect("../");
			return false;
		}
		
		return true;
		
	}
	//로그아웃 후 뒤로 가기 막기
	//postHandle 메소드를 재정의하여 브라우저의 캐쉬 제거
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if(request.getProtocol().equals("HTTP/1.1")) {
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		}
		else {//프로토콜이 "HTTP/1.0"일 때,
			response.setHeader("Pragma", "no-cache");
		}
		response.setDateHeader("Expires", 0);
	}
}
