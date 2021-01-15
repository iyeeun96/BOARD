package com.kotlin.board.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kotlin.board.dao.MemberDao;
import com.kotlin.board.dto.MemberDto;

import lombok.extern.java.Log;

@Service
@Log
public class MemberService {
	//DAO 객체
	@Autowired
	private MemberDao mDao;
	@Autowired
	private HttpSession session;
	//ModelAndView
	private ModelAndView mv;
	
	@Transactional
	public String memberInsert(MemberDto member, RedirectAttributes rttr) {
		String view = null;
		
		//비밀번호 암호화 처리
		//스프링 프레임워크에서 제공하는 암호화 인코더 사용
		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
		//암호화된 패스워드
		String encPwd = pwdEncoder.encode(member.getM_pwd());
		//다시 member에 셋
		member.setM_pwd(encPwd);
		
		try {
			mDao.memberInsert(member);
			view = "redirect:/";
			rttr.addFlashAttribute("msg", "가입 성공");
			
		}catch (Exception e) {
			//e.printStackTrace();
			view = "redirect:joinFrm";
			rttr.addFlashAttribute("msg", "가입 실패");
		}
		return view;
	}
	@Transactional
	public String loginProc(MemberDto member, RedirectAttributes rttr) {
		String view = null;
		
		//job1.DB에서 암호화된 비밀번호 구하기
		String encPwd = mDao.getEncPwd(member.getM_id());
		
		//암호화된 비밀번호와 입력한 비밀번호의 비교 처리를 위한 인코더 생성
		BCryptPasswordEncoder pwdEncode = new BCryptPasswordEncoder();
		
		if(encPwd != null) {
			//아이디 있음.
			if(pwdEncode.matches(member.getM_pwd(), encPwd)) {
				//(입력한 패스워드 값, 암호화된 패스워드 값)같은지 비교
				//같으면 true, 틀리면 false를 반환하는 메소드 - matches
				
				//job2.화면에 출력할 사용자 정보 가져오기
				member = mDao.getMemberInfo(member.getM_id());
				//나중에 화면에 출력할때 쓰기위해 세션에 다시 저장
				session.setAttribute("mb", member);
				//로그인 성공시 게시판 컨트롤러로 이동.
				view = "redirect:list";
			}
			else {
				//비밀번호 틀린 경우
				view = "redirect:loginFrm";
				rttr.addFlashAttribute("msg","비밀번호 오류");
			}
		}else {
			//아이디 없음.
			view = "redirect:loginFrm";
			rttr.addFlashAttribute("msg","아이디 없음");
		}
		
		return view;
	}
	
	public String idCheck(String id) {
		log.info("idCheck() id: "+id);
		String result = null;
		
		int cnt = mDao.idCheck(id);
		if(cnt == 0) {
			//사용 가능 아이디 
			result = "success";
		}
		else {
			//중복된 아이디
			result = "fail";
		}
		return result;
	}
	public String logout() {
		session.invalidate();
		return "home";
	}
}//class end
