<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>상세 보기</title>
<link rel="stylesheet" href="resources/css/style.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script type="text/javascript">
$(function(){
	var identy = "${mb.m_name}";
	$("#mname").html(identy + "님");
	$(".suc").css("display", "block");
	$(".bef").css("display", "none");
	
	//컨트롤러에서 전달하는 메시지 출력.
	var msg = "${msg}";
	
	if(msg != ""){
		alert(msg);
		location.reload(true);
	}
	//작성자만 수정 삭제할 수 있게 하기.
	$('#upbtn').hide();
	$('#delbtn').hide();
	
	var mid = "${mb.m_id}";//로그인한 아이디
	var bid = "${board.bid}";//작성자 아이디
	
	if(mid==bid){
		$('#upbtn').show();
		$('#delbtn').show();
	}
	
});
</script>
</head>
<body>
<div class="wrap">
	<header>
	<jsp:include page="header.jsp"/>
	</header>
	<section>
	<div class="content">
		<div class="write-form">
			<div class="user-info">
				<div class="user-info-sub">
					<p class="grade">등급 [${mb.g_name}]</p>
					<p class="point">POINT [${mb.m_point}]</p>
				</div>
			</div>
			<h2 class="login-header">상세 보기</h2>
			<table>
				<tr height="30">
					<td width="100" bgcolor="pink" align="center">NUM</td>
					<td colspan="5">${board.bnum}</td>
				</tr>
				<tr height="30">
					<td bgcolor="pink" align="center">WRITER</td>
					<td width="150">${board.mname}</td>
					<td bgcolor="pink" align="center">DATE</td>
					<td width="200"><fmt:formatDate value="${board.bdate}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
					<td bgcolor="pink" align="center">VIEWS</td>
					<td width="100">${board.bviews}</td>
				</tr>
				<tr height="30">
					<td bgcolor="pink" align="center">TITLE</td>
					<td colspan="5">${board.btitle}</td>
				</tr>
				<tr height="200">
					<td bgcolor="pink" align="center">CONTENTS</td>
					<td colspan="5">${board.bcontents}</td>
				</tr>
				<tr>
					<th>첨부파일</th>
					<td colspan="5">
						<c:if test="${empty bfList}">
							첨부된 파일이 없습니다.
						</c:if>
						<c:if test="${!empty bfList}">
							<c:forEach var="file" items="${bfList}">
							<a href="./download?sysName=${file.bf_sysname}">
							<span class="">${file.bf_oriname}</span></a>&nbsp;&nbsp;
							</c:forEach>
						</c:if>
					</td>
				</tr>
				<c:if test="${!empty bfList}">
				<tr>
					<th>PREVIEW</th>
					<td colspan="5">
					<c:forEach var="f" items="${bfList}">
						<c:if test="${fn:contains(f.bf_sysname, '.jpg')}">
							<img src="resources/upload/${f.bf_sysname}" 
								width="100">
						</c:if>
						<c:if test="${fn:contains(f.bf_sysname, '.png')}">
							<img src="resources/upload/${f.bf_sysname}" 
								width="100">
						</c:if>
						<c:if test="${fn:contains(f.bf_sysname, '.gif')}">
							<img src="resources/upload/${f.bf_sysname}" 
								width="100">
						</c:if>
					</c:forEach>
					</td>
				</tr>
				</c:if>
				<tr>
					<td colspan="6" align="center">
						<button class="btn-write" id="upbtn" onclick="location.href='./updateFrm?bnum=${board.bnum}'">U</button>
						<button class="btn-write" id="delbtn" onclick="location.href='./delete?bnum=${board.bnum}'">D</button>
						<button class="btn-sub" onclick="location.href='./list?pageNum=${pageNum}'">B</button>
					</td>
				</tr>
			</table>
			<!-- 댓글 작성 양식 -->
			<form id="rFrm">
				<textarea rows="3" class="write-input ta" name="r_contents" id="comment" placeholder="댓글을 적어주세요."></textarea>
				<input type="button" value="댓글전송" class="btn-write" 
				onclick="replyInsert(${board.bnum})" style="width:100%; margin-bottom:30px;">
			</form>
			<!-- 댓글 목록 출력 부분 -->
			<table style="width:100%">
				<tr bgcolor="pink" align="center" height="30">
					<td width="20%">Writer</td>
					<td width="50%">Contents</td>
					<td width="30%">Date</td>
				</tr>
			</table>
				<table id="rtable" style="width:100%;">
					<c:forEach var="r" items="${rList}">
					<tr height="25" align="center">
						<td width="20%">${r.r_id}</td>
						<td width="50%">${r.r_contents }</td>
						<td width="30%">${r.r_date}</td>
					</tr>
					</c:forEach>
				</table>
		</div>
	</div>
	</section>
	<footer>
	<jsp:include page="footer.jsp"/>
	</footer>
</div>
</body>
<script src="resources/js/jquery.serializeObject.js"></script>
<script type="text/javascript">
function replyInsert(bnum){
	//form데이터를 json으로 변환
	var replyFrm =$("#rFrm").serializeObject();
	replyFrm.r_bnum = bnum;//글번호 추가
	replyFrm.r_id = "${mb.m_id}";//작성자(로그인한) 아이디
	
	console.log(replyFrm);
	
	$.ajax({
		url:"replyIns",
		type:"post",
		data: replyFrm,
		dataType: "json",
		success: function(data){
			var rlist =""; 
			var dlist = data.rList;
			
			for(var i=0; i<dlist.length; i++){
				rlist += '<tr height="25" align="center">'
				+'<td width="20%">'+dlist[i].r_id+'</td>'
				+'<td width="50%">'+dlist[i].r_contents+'</td>'
				+'<td width="30%">'+dlist[i].r_date+'</td>'
			}
			$('#rtable').html(rlist);
		},
		error: function(error){
			console.log(error);
			alert("댓글 입력 실패");
		}
	});
	$("#comment").val("");//댓글 전송 후 칸 비우기
}
</script>
</html>




