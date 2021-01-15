package com.kotlin.board.dao;

import com.kotlin.board.dto.MemberDto;

public interface MemberDao {
	//회원가입
	public void memberInsert(MemberDto member);
	//비밀번호 구하기
	public String getEncPwd(String id);
	//로그인 후 사용할 사용자 정보 가져오기
	public MemberDto getMemberInfo(String id);
	//아이디 중복여부 확인
	public int idCheck(String id);
	//포인트 업데이트
	public void pointUpdate(String id);
}
