package com.kotlin.board.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ReplyDto {
	private int r_num;
	private int r_bnum;
	private String r_contents;
	@JsonFormat(pattern ="yyyy-MM-dd hh:mm:ss")
	private Timestamp r_date;
	private String r_id;
}
//date 처리시 json 객체로 변환할 때
//시간 값에 대한 출력 형식을 pattern으로 지정.