<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<spring:url value="/resources/css/news.css"/>" type="text/css">
<title>Лента новостей</title>
</head>
<body>

	<div class="header">
		<h1>
		<c:choose>
		<c:when test="${!empty mainPage}">Лента новостей</c:when>
		<c:otherwise><a href="<spring:url value="/"/>">Лента новостей</a></c:otherwise>
		</c:choose>
		</h1>
	</div>

	<div class="header_buttons">
				<a class="button" style="margin-left: 0px;" href="<spring:url value="/add"/>">Добавить новость</a>
				<div class="category_select" style="padding-top: 7px; padding-bottom: 7px; padding-left: 15px;">
					<select onchange="top.location=this.value" style="font-size: medium;">
						<option value="<spring:url value="/category"/>" style="font-size: medium; padding-top: 10px; padding-bottom: 10px;">Все категории</option>
						<c:forEach items="${allCategories}" var="categories">
							<option value="<spring:url value="/category/"/>${categories.id}" <c:if test="${categoryId == categories.id}">selected</c:if> style="font-size: medium; padding-top: 10px; padding-bottom: 10px;">${categories.name}</option>
						</c:forEach>
					</select>
				</div>
				<form action="<spring:url value="/search"/>" id="search_news" method="post" accept-charset="UTF-8">
				<input type="text" name="query" size="70" value="${param.query}">
				<input type="radio" name="search_area" value="body" <c:if test="${empty param.search_area}">checked</c:if><c:if test="${param.search_area == 'body'}">checked</c:if>><span class="radio_text">По всему тексту</span>
				<input type="radio" name="search_area" value="header" <c:if test="${param.search_area == 'header'}">checked</c:if>><span class="radio_text">По заголовкам</span>
				
				<a href="<spring:url value="/search"/>" class="button" onclick="document.getElementById('search_news').submit(); return false;">Поиск</a>
				<input type="submit" value="Поиск" style="position: absolute; left: -9999px; width: 1px; height: 1px;" tabindex="-1"/>
				</form>
				
	</div>

	<c:forEach items="${allNews}" var="news">
		<div class="news">
		<div class="category">
			<span><a href="<spring:url value="/category/"/>${news.category.id}">${news.category.name}</a></span>
		</div>
		<div class="title">
			<h2>${news.header}</h2>
		</div>
		<div class="date_published">
			<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${news.date_published}" />
		</div>
		<div class="content">
			<div class="news_text">
				${news.content}
			</div>
			<div class="news_buttons">
				<a href="<spring:url value="/edit/"/>${news.id}" class="button">Редактировать новость</a>
				<a href="<spring:url value="/delete/"/>${news.id}" class="button">Удалить новость</a>
			</div>
		</div>
	</div>
	</c:forEach>

	<div class="footer">
	</div>

</body>
</html>
