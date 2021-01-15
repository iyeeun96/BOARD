package com.kotlin.board;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kotlin.board.dto.BfileDto;
import com.kotlin.board.dto.ReplyDto;
import com.kotlin.board.service.BoardService;

import lombok.extern.java.Log;

@Controller
@Log
public class BoardController {
	//서비스 객체 변수
	@Autowired
	private BoardService bServ;
	//ModelAndView 객체 (데이터 전송용)
	private ModelAndView mv;
	
	//게시글 목록 화면 전환
	@GetMapping("list")//memberService에서 넘어옴
	public ModelAndView boardList(Integer pageNum) {
		log.info("boardList()");
		mv = bServ.getBoardList(pageNum); 

		return mv;
	}
	//게시글 작성 폼 화면 전환
	@GetMapping("writeFrm")//boardList.jsp에서 넘어옴
	public String writeFrm() {
		log.info("writeFrm()");
		return "writeFrm";
	}
	//게시글 작성 처리
	@PostMapping("boardWrite")
	public String boardWrite(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
		log.info("boardWrite()");

		String view = bServ.boardInsert(multi, rttr);

		return view;
	}
	//해당 게시글 가져오기 처리
	@GetMapping("contents")//boardList.jsp
	public ModelAndView boardContents(Integer bnum) {
		log.info("boardContents()-bnum: " +bnum);
		mv = bServ.getContents(bnum);

		return mv;
	}
	//댓글 등록 처리
	@PostMapping(value="replyIns", produces= "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, List<ReplyDto>> replyInsert(ReplyDto reply){
		Map<String, List<ReplyDto>> rMap = bServ.replyInsert(reply);
		
		return rMap;
	}
	//첨부파일 다운로드 처리
	@GetMapping("download")
	public void fileDownLoad(String sysName, HttpServletRequest request, HttpServletResponse response) {
		log.info("fileDownLoad() file : "+sysName);
		
		bServ.fileDown(sysName, request, response);
		
	}
	//수정할 게시글 불러오기
	@GetMapping("updateFrm")
	public ModelAndView updateFrm(int bnum, RedirectAttributes rttr) {
		log.info("updateFrm()-bnum : " +bnum);
		mv = bServ.updateFrm(bnum, rttr);
		return mv;
	}
	//게시글 수정 처리
	@PostMapping("boardUpdate")
	public String boardUpdate(MultipartHttpServletRequest multi,
			RedirectAttributes rttr) {
		String view= bServ.boardUpdate(multi, rttr);
		return view;
	}
	//첨부파일 삭제 처리
	@PostMapping(value="delfile", produces="application/json; charset=utf-8")
	@ResponseBody
	public Map<String, List<BfileDto>> delFile(String sysname, int bnum){
		Map<String, List<BfileDto>> rMap = null;
		rMap = bServ.fileDelete(sysname, bnum);
		return rMap;
	}
	//게시글 삭제 처리
	@GetMapping("delete")
	public String boardDelete(int bnum, RedirectAttributes rttr) {
		log.info("boardDelete() - bnum : "+bnum);
		String view = bServ.boardDelete(bnum, rttr);
		return view;
	}
}//class end
