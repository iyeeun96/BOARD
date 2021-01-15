<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>글쓰기</title>
<link rel="stylesheet" href="resources/css/style.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script type="text/javascript">
	$(function() {
		var identy = "${mb.m_name}"
		$("#mname").html(identy + "님");
		$(".suc").css("display", "block");
		$(".bef").css("display", "none");

		//컨트롤러에서 전달하는 메시지 출력.

	});
</script>
</head>
<body>
	<div class="wrap">
		<header>
			<jsp:include page="header.jsp" /></header>
		<section>
			<div class="content">
				<form action="./boardWrite" class="write-form" method="post" enctype="multipart/form-data">
					<div class="user-info">
						<div class="user-info-sub">
							<p class="grade">등급 [${mb.g_name}]</p>
							<p class="point">POINT [${mb.m_point}]</p>
						</div>
					</div>
					<h2 class="login-header">글쓰기</h2>
					<!-- name - BoardDto의 아이디 변수이름을 넣는다. -->
					<input type="hidden" name="bid" value="${mb.m_id}"> <input
						type="text" class="write-input" name="btitle" autofocus
						placeholder="제목" required>
					<textarea rows="15" name="bcontents" placeholder="내용을 작성해주세요."
						class="write-input ta"></textarea>
					<!-- 줄바꿈 금지 -->
					<div class="filebox">
						<label for="file">업로드</label> <input type="file" name="files"
							id="file" multiple> <input class="upload-name"
							value="파일 선택" readonly> <input type="hidden"
							id="filecheck" value="0" name="fileCheck">
					</div>
					<div class="btn-area">
						<input class="btn-write" type="submit" value="W"> 
						<input class="btn-write" type="reset" value="R">
						<input class="btn-write" type="button" value="B"
							onclick="location.href='./list?pageNum=${pageNum}'">
					</div>
				</form>
			</div>
		</section>
		<footer>
			<jsp:include page="footer.jsp"/>
		</footer>
	</div>
</body>
<script type="text/javascript">
	$("#file").on('change', function() {
		var fileName = $("#file").val();
		$(".upload-name").val(fileName);

		if (fileName == "") {
			console.log("empty");
			$("#filecheck").val(0);
			$(".upload-name").val("파일 선택");
		} else {
			console.log("not empty");
			$("#filecheck").val(1);
		}
	});
</script>
</html>