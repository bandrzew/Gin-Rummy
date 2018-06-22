<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style><%@include file="/WEB-INF/css/style.css"%></style>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script><%@include file="/WEB-INF/js/app.js"%></script>
</head>
<body>
	<div id="ai" class="hand">
		<c:forEach var="i" begin="1" end="10">
			<img src="<c:url value='/resources/cards/blackback.png'/>"style="height:140px">
		</c:forEach>
	</div>
	<div id="piles">
		<img id="discard" src="<c:url value='/resources/cards/${discard}.jpg'/>"style="height:150px">
		<img id="stock" src="<c:url value='/resources/cards/blackback.png'/>"style="height:150px">
	</div>
	<div id="hint">Wybierz stos</div>
	<div id="player" class="hand">
		<c:forEach var="card" items="${hand}">
			<a id="${card}" class="hand">
				<img src="<c:url value='/resources/cards/${card}.jpg'/>"style="height:150px">
			</a>
		</c:forEach>
	</div>
</body>
</html>