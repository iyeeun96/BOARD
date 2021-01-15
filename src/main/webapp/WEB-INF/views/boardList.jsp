<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>게시글 목록</title>
<link rel="stylesheet" href="resources/css/style.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script type="text/javascript">
$(function(){
	var identy = "${mb.m_name}"
	$("#mname").html(identy+"님");
	$(".suc").css("display", "block");
	$(".bef").css("display", "none");
	
	//컨트롤러에서 전달하는 메시지 출력.
	
});
</script>
</head>
<body>
<div class="wrap">
	<header><jsp:include page = "header.jsp"/></header>
	<section>
	<div class="content">
		<div class="board-form">
			<div class="user-info">
				<div class="user-info-sub">
					<p class="grade">등급 [${mb.g_name}]</p>
					<p class="point">POINT [${mb.m_point}]</p>
				</div>
			</div>
			<h2 class="login-header">게시글 목록</h2>
			<div class="data-area">
				<div class="title-row">
					<div class="t-no p-10">번호</div>
					<div class="t-title p-30">제목</div>
					<div class="t-name p-15">작성자</div>
					<div class="t-date p-30">작성일</div>
					<div class="t-view p-15">조회수</div>
				</div>
				<c:forEach var="bitem" items="${bList}">
					<div class="data-row">
						<!-- dto에 있는 이름(=만들어진 컬럼이름)과 같게 -->
						<div class="t-no p-10">${bitem.bnum}</div>
						<div class="t-title p-30"><a href="contents?bnum=${bitem.bnum}">${bitem.btitle}</a></div>
						<div class="t-name p-15">${bitem.mname}</div>
						<div class="t-date p-30"><fmt:formatDate value="${bitem.bdate}" pattern="yyyy-MM-dd hh:mm:ss"/></div>
						<div class="t-view p-15">${bitem.bviews}</div>
					</div>
				</c:forEach>
			</div>
			<div class="btn-area">
				<div class="paging">${paging}</div>
				<!-- 버튼 누르면 페이지 이동시키기 location.href(Get방식)-컨트롤러에서 처리.. -->
				<button class="wr-btn" onclick="location.href='./writeFrm'">글쓰기</button>
			</div>
		</div>
	</div>
	</section>
	<footer><jsp:include page = "footer.jsp"/></footer>
</div>
</body>
</html>