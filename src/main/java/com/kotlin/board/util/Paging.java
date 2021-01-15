package com.kotlin.board.util;

public class Paging {
	private int maxNum;//전체 글 개수
	private int pageNum;//현재 페이지 번호
	private int listCnt;//페이지 당 글 개수
	private int pageCnt;//보여질 페이지번호 개수
	private String listName;//목록 페이지의 이름(종류)
	
	//생성자로 초기 필수 데이터 저장 
	public Paging(int maxNum, int pageNum, int listCnt, int pageCnt, String listName) {
		this.maxNum= maxNum;
		this.pageNum = pageNum;
		this.listCnt = listCnt;
		this.pageCnt = pageCnt;
		this.listName= listName;
	}
	
	public String makePaging() {
		//전체 페이지 개수
		//게시글이 9개 이하 - 1개의 페이지 
		//게시글이 11개 이상 - 2개의 페이지
		int totalPage =(maxNum % listCnt > 0)?
				maxNum/listCnt +1:
				maxNum/listCnt;
		//현재 페이지가 속해 있는 그룹 번호
		int curGroup = (pageNum % pageCnt > 0)?
				pageNum/pageCnt +1 :
				pageNum/pageCnt;
		
		StringBuffer sb = new StringBuffer();
		//현재 그룹의 시작 페이지 번호
		int start = (curGroup*pageCnt) - (pageCnt-1);
		//현재 그룹의 끝 페이지 번호
		int end = (curGroup * pageCnt>= totalPage)?
				totalPage : curGroup * pageCnt;
		if(start != 1) {
			sb.append("<a class='pno' href='"+listName+"?pageNum="+(start-1)+"'>");
			sb.append("&nbsp;이전&nbsp;</a>");
		}//<a class ='pno' href='list?pageNum=5'> 이전 </a>
		
		//이전과 다음 버튼 사이의 페이지 번호 처리
		for(int i = start; i<=end; i++) {
			if(pageNum!=i) {
				//페이지 이동을 위한 링크를 걸어줌.
				sb.append("<a class='pno' href='"+listName+"?pageNum="+i+"'>");
				sb.append("&nbsp;"+i+"&nbsp;</a>");
			}
			else {
				//현재 페이지라서 링크를 걸지 않음.
				sb.append("<font class='pno' style='color:red;'>");
				sb.append("&nbsp;"+i+"&nbsp;</font>");
			}
			//현재 보고 있는 페이지 번호 : 7
			//<a class='pno' href='list?pageNum=6'> 6 </a>
			//<font class='pno' style='color:red;'> 7 </font>
			//<a class='pno' href='list?pageNum=8'> 8 </a>
			//<a class='pno' href='list?pageNum=9'> 9 </a>
			//<a class='pno' href='list?pageNum=10'> 10 </a>
		}//for end
		
		//'다음' 버튼 처리
		if(end!=totalPage) {
			sb.append("<a class='pno' href='"+listName+"?pageNum="+(end+1)+"'>");
			sb.append("&nbsp;다음&nbsp;</a>");
		}
		//<a class='pno' href='list?pageNum=6'> 다음 </a>
		return sb.toString();//문자열로 완성 
	}//makePaging() end
}//class end
