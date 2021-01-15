package com.kotlin.board.dto;

import java.sql.Timestamp;
import lombok.Data;

@Data //Dto로 만들어서 데이터베이스에 저장
public class BoardDto {
	private int bnum;
	private String btitle;
	private String bcontents;
	private String bid;
	private String mname;
	private Timestamp bdate;
	private int bviews;
}
