package com.kotlin.board.dao;

import java.util.*;

import com.kotlin.board.dto.BfileDto;
import com.kotlin.board.dto.BoardDto;
import com.kotlin.board.dto.ReplyDto;

public interface BoardDao {
	//게시글 목록 구하기 
	public List<BoardDto> getList(int pageNum);
	//게시글 전체 개수 구하기
	public int getBoardCnt();
	//게시글 저장 메소드
	public boolean boardInsert(BoardDto board);
	//게시글 파일 저장용 메소드
	public boolean fileInsert(Map<String, String> fmap);
	//게시글 내용(레코드 1행) 가져오기
	public BoardDto getContents(Integer bnum);
	//게시글 해당 파일 목록 가져오기
	public List<BfileDto> getBfList(Integer bnum);
	//게시글 해당 댓글 목록 가져오기
	public List<ReplyDto> getReplyList(Integer bnum);
	//댓글 저장 메소드
	public boolean replyInsert(ReplyDto reply);
	//원래 파일 이름 구하기
	public String getOriName(String sysName);
	//조회수 증가
	public void viewUpdate(Integer bnum);
	//게시글 업데이트
	public boolean boardUpdate(BoardDto board);
	//개별 파일 삭제(게시글 수정 시)
	public boolean fileDelete(String sysName);
	//게시글의 댓글 삭제
	public boolean replyDelete(Integer bnum);
	//모든 파일 삭제(같은 게시물의 모든 파일 삭제)
	public boolean filesListDelete(Integer bnum);
	//게시글 삭제
	public boolean boardDelete(Integer bnum);
}
