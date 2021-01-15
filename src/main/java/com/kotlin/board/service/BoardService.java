package com.kotlin.board.service;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kotlin.board.dao.BoardDao;
import com.kotlin.board.dao.MemberDao;
import com.kotlin.board.dto.BfileDto;
import com.kotlin.board.dto.BoardDto;
import com.kotlin.board.dto.MemberDto;
import com.kotlin.board.dto.ReplyDto;
import com.kotlin.board.util.Paging;

import lombok.extern.java.Log;

@Service
@Log
public class BoardService {
	//DAO
	@Autowired
	private BoardDao bDao;
	@Autowired
	private MemberDao mDao;
	
	private ModelAndView mv;

	@Autowired
	private HttpSession session;

	//게시글 목록 가져오는 메소드
	public ModelAndView getBoardList(Integer pageNum) {//pageNum은 첨에 null로 들어옴.
		log.info("getBoardList()- pageNum : " +pageNum);
		mv = new ModelAndView();
		int num = (pageNum==null)? 1: pageNum;
		List<BoardDto> bList = bDao.getList(num);
		//DB에서 조회한 데이터 목록을 모델에 추가
		mv.addObject("bList", bList);//("bList"->jsp와 같은 이름, bList->만든 변수 )		

		//페이징 처리
		mv.addObject("paging", getPaging(num));
		//세션에 페이지 번호 저장
		session.setAttribute("pageNum", num);

		mv.setViewName("boardList");

		return mv;
	}//method end

	private String getPaging(int num) {
		//전체 글 갯수 구하기(from DB)
		int maxNum = bDao .getBoardCnt();
		//설정값(페이지 당 글 개수, 그룹 당 페이지 개수, 게시판 이름)
		int listCnt = 10; 
		int pageCnt = 2;
		String listName = "list";

		Paging paging = new Paging(maxNum, num, listCnt, pageCnt, listName);

		String pagingHtml = paging.makePaging();

		return pagingHtml;
	}
	//게시글 등록 서비스 메소드
	@Transactional
	public String boardInsert(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
		log.info("boardInsert()");
		String view = null;
		String id = multi.getParameter("bid");
		String title = multi.getParameter("btitle");
		String contents = multi.getParameter("bcontents");
		String check = multi.getParameter("fileCheck");
		
		//textarea는 입력한 문자열 앞뒤로 공백이 발생.
		//문자열 앞 뒤 공백 제거. trim()
		contents = contents.trim();

		BoardDto board = new BoardDto();
		board.setBid(id);
		board.setBtitle(title);
		board.setBcontents(contents);

		try {
			bDao.boardInsert(board);
			
			//파일 업로드 처리 메소드 호출
			if(check.equals("1")) {
				fileUp(multi,board.getBnum());
			}
			//포인트 증가 메소드 호출
			//회원 포인트가 100보다 작을 때 
			//글을 작성하면 포인트를 증가시키고 
			//증가된 포인트를 담은 회원 정보를 다시 받아서
			//세션에 추가 (기존 mb를 덮어씀)
			MemberDto member = (MemberDto)session.getAttribute("mb");
			if(member.getM_point()<100) {
				mDao.pointUpdate(id);
				member = mDao.getMemberInfo(id);
				session.setAttribute("mb", member);
			}
			
			view ="redirect:list";
			rttr.addFlashAttribute("msg", "글 등록 성공");
		}catch(Exception e) {
			//e.printStackTrace();
			view = "redirect:writeFrm";
			rttr.addFlashAttribute("msg", "글 등록 실패");
		}

		return view; 
	}

	//파일 업로드 처리 메소드
	public boolean fileUp(MultipartHttpServletRequest multi, int bnum)throws Exception {
		//저장공간에 대한 절대 경로 구하기
		String path = multi.getSession().getServletContext().getRealPath("/");

		path += "resources/upload/";
		log.info(path);

		File dir = new File(path);

		if(dir.isDirectory()==false) {
			dir.mkdir();//폴더가 없을 경우 폴더 생성.
		}
		//실제 파일명과 저장 파일명을 함께 관리
		Map<String, String> fmap = new HashMap<String, String>();

		fmap.put("bnum", String.valueOf(bnum));  

		//파일 전송시 기본 값을 파일 다중 선택임.
		//멀티파트 요청 객체에서 꺼내올 때는 List를 사용.
		List<MultipartFile> fList = multi.getFiles("files");
		for(int i = 0; i<fList.size(); i++) {
			MultipartFile mf = fList.get(i);
			String on = mf.getOriginalFilename();
			fmap.put("oriName", on);

			//변경된 파일 이름 저장
			String sn = System.currentTimeMillis()
					+"."//확장자 구분 점
					+ on.substring(on.lastIndexOf(".")+1);
			fmap.put("sysName", sn);

			//해당 폴더에 파일 저장하기 
			mf.transferTo(new File(path+sn));	   

			bDao.fileInsert(fmap);
		}
		return true;
	}
	//게시글 내용(1행) 가져오기
	public ModelAndView getContents(Integer bnum) {
		mv = new ModelAndView();
		//조회수 1 증가
		bDao.viewUpdate(bnum);
		//글 내용 가져오기
		BoardDto board = bDao.getContents(bnum);
		//파일목록 가져오기
		List<BfileDto> bfList = bDao.getBfList(bnum);
		//댓글 목록 가져오기
		List<ReplyDto> rList = bDao.getReplyList(bnum);
		//모델 데이터 담기
		mv.addObject("board", board);
		mv.addObject("bfList", bfList);
		mv.addObject("rList", rList);
		//뷰 이름 지정하기
		mv.setViewName("boardContents");

		return mv;
	}
	@Transactional
	public Map<String, List<ReplyDto>> replyInsert(ReplyDto reply){
		Map<String, List<ReplyDto>> rMap = null;
		try {
			//댓글을 DB에 입력
			bDao.replyInsert(reply);
			//댓글 목록을 다시 검색
			List<ReplyDto> rList = bDao.getReplyList(reply.getR_bnum());
			rMap = new HashMap<String, List<ReplyDto>>();
			rMap.put("rList", rList);
			
		} catch (Exception e) {
			//e.printStackTrace();
			rMap = null;
		}
		return rMap;
	}

	public void fileDown(String sysName, HttpServletRequest request, HttpServletResponse response) {
		//서버의 파일 위치의 절대 경로 구하기 -request 객체 이용/ 브라우저로 가는 response 객체 이용
		String path = request.getSession().getServletContext().getRealPath("/");
		
		path += "resources/upload/";
		log.info(path);
		
		String oriName = bDao.getOriName(sysName);
		path += sysName;//다운로드할 파일경로 + 파일명
		
		//서버저장장치 (디스크)에서 저장된 파일을 읽어오는 통로
		InputStream is = null;
		//사용자 컴퓨터에 파일을 보내는 통로
		OutputStream os = null;
		
		try {
			//파일명 인코딩(파일명이 한글일 때 깨짐을 방지)
			String dFileName = URLEncoder.encode(oriName, "UTF-8");
			//파일 객체 생성
			File file = new File(path);
			is = new FileInputStream(file);
			//응답 객체(response)의 헤더 설정
			//파일전송용 contentType 설정
			response.setContentType("application/octet-stream");
			response.setHeader("content-Disposition", "attachment; filename=\""+dFileName+"\"");
			//attachment; filename="가나다라.jpg"
			
			//응답 객체와 보내는 통로 연결
			os = response.getOutputStream();
			
			//파일 전송(byte 단위로 전송)
			byte[] buffer = new byte[1024];
			int length;//read가 얼마 읽어오는지 알려줌. 
			while((length = is.read(buffer))!= -1) {
				os.write(buffer, 0, length);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				os.flush();//os에 남아있을지 모르는 데이터를
						   //강제로 전부 보내도록 처리.
				
				os.close();
				is.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	public ModelAndView updateFrm(int bnum, RedirectAttributes rttr) {
		mv = new ModelAndView();
		//수정할 내용 가져오기
		BoardDto board = bDao.getContents(bnum);
		
		mv.addObject("board", board);
		List<BfileDto> bfList = bDao.getBfList(bnum);
		mv.addObject("bfList", bfList);
		mv.setViewName("updateFrm");
		
		return mv;
	}
	@Transactional
	public String boardUpdate(MultipartHttpServletRequest multi,
			RedirectAttributes rttr) {
		String view = null;
		
		String bnum= multi.getParameter("bnum");
		String title = multi.getParameter("btitle");
		String contents = multi.getParameter("bcontents");
		String id = multi.getParameter("bid");
		String check = multi.getParameter("fileCheck");
		
		contents = contents.trim();
		
		log.info("boardUpdate() t: "+title+", c: "+contents);
		
		BoardDto board = new BoardDto();
		board.setBnum(Integer.parseInt(bnum));
		board.setBtitle(title);
		board.setBcontents(contents);
		board.setBid(id);
		
		try {
			bDao.boardUpdate(board);
			//파일 업로드 메소드 호출
			if(check.equals("1")) {
				fileUp(multi, board.getBnum());
			}
			rttr.addFlashAttribute("msg", "수정 성공");
		} catch (Exception e) {
			rttr.addFlashAttribute("msg", "수정 실패");
		} 
		
		view= "redirect:contents?bnum="+bnum;
		
		return view;
	}
	//데이터베이스 트랜잭션을 설정하고 싶은 메서드에
	//어노테이션을 적용하면 메서드 내부에서 일어나는 데이터베이스
	//로직이 전부 성공하게되거나 데이터베이스 접근중 하나라도 실패하면
	//다시 롤백할 수 있게 해주는 어노테이션

	@Transactional
	public Map<String, List<BfileDto>> fileDelete(String sysname, int bnum){
		Map<String, List<BfileDto>> rMap = null;
		log.info("fileDelete()- sysname : "+sysname+", bnum : "+bnum);
		//절대 경로구하기 (세션으로)
		String path = session.getServletContext().getRealPath("/");
		
		log.info(path);
		path+="resources/upload/"+sysname;
		
		try {
			bDao.fileDelete(sysname);
			File file = new File(path);
			if(file.exists()) {
				if(file.delete()) {
					List<BfileDto> fList = bDao.getBfList(bnum);
					rMap = new HashMap<String, List<BfileDto>>();
					rMap.put("fList", fList);
				}
				else {
					rMap = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			rMap=null;
		}
		return rMap;	
	}
	@Transactional
	public String boardDelete(int bnum, RedirectAttributes rttr) {
		log.info("boardDelete()- bnum : "+bnum);
		
		String view = null;
		try {
			//댓글 삭제
			bDao.replyDelete(bnum);
			//파일 삭제
			bDao.filesListDelete(bnum);
			//게시글 삭제
			bDao.boardDelete(bnum);
			
			view = "redirect:list";
			rttr.addFlashAttribute("msg", "삭제 성공");
		} catch (Exception e) {
			//e.printStackTrace();
			view= "redirect:contents?bnum="+bnum;
			rttr.addFlashAttribute("msg", "삭제 실패");
		}
		return view;
	}
}//class end
