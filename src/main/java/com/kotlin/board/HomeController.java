package com.kotlin.board;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kotlin.board.dto.MemberDto;
import com.kotlin.board.service.MemberService;


@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private MemberService mServ;
	
	private ModelAndView mv;
	
	@GetMapping("/")
	public String home() {
		logger.info("home()");
		return "home";
	}
	@GetMapping("joinFrm")
	public String joinFrm() {
		logger.info("joinFrm()");
		return "joinFrm";
	}
	@PostMapping("memInsert")
	//서비스에 리턴타입 맞춰주기. 데이터를 담을 필요가 없어서 viewname만 String타입으로 전달한다.
	public String memInsert(MemberDto member, RedirectAttributes rttr) {
		logger.info("memInsert()");
		String view = mServ.memberInsert(member, rttr);
		return view;
	}
	@GetMapping("loginFrm")
	public String loginFrm() {//화면전환하는 메소드
		logger.info("loginFrm()");
		return "loginFrm";
	}
	@PostMapping("login")//form의 action에서 넘어오는 값 처리
	public String loginProc(MemberDto member, RedirectAttributes rttr) {
		logger.info("loginProc()");
		String view = mServ.loginProc(member, rttr);
		return view;
	}
	
	@GetMapping(value="idCheck", produces="application/text; charset=utf-8")
	@ResponseBody
	public String idCheck(String mid) {
		logger.info("idCheck() id:"+mid);
		String result = mServ.idCheck(mid);
		return result;
	}
	@GetMapping("logout")
	public String logout() {
		return mServ.logout();
	}
}
