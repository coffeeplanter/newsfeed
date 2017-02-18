<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<spring:url value="/resources/css/news.css"/>" type="text/css">
<title>
<c:choose>
		<c:when test="${empty news}">Добавить свежую новость</c:when>
		<c:otherwise>Отредактировать новость</c:otherwise>
		</c:choose>
</title>
</head>
<body>

	<div class="header">
		<h1>
		<c:choose>
		<c:when test="${empty news}">Добавить свежую новость</c:when>
		<c:otherwise>Отредактировать новость</c:otherwise>
		</c:choose>
		</h1>
	</div>

	<div>
	
		<form id="save_news" action="<spring:url value="/save"/>" method="post" accept-charset="UTF-8">
		
		<div class="category_select" style="padding-top: 7px; padding-bottom: 7px; padding-left: 15px;">
			<select style="font-size: medium;" name="category.id">
				<c:forEach items="${allCategories}" var="categories">
					<option value="${categories.id}" <c:if test="${categoryId == categories.id}">selected</c:if> style="font-size: medium; padding-top: 10px; padding-bottom: 10px;">${categories.name}</option>
				</c:forEach>
			</select>
		</div>
		
		<strong>Заголовок:</strong><br/>
		<input type="text" size="150" value="${news.header}" name="header"><br/><br/>

		<strong>Дата</strong> (в формате дд.мм.гггг)<strong>:</strong><br/>
		<input type="text" size="150" name="date_published" value="<c:if test="${empty news}"><%= new java.text.SimpleDateFormat("dd.MM.yyyy").format(new java.util.Date()) %></c:if><fmt:formatDate pattern="dd.MM.yyyy" value="${news.date_published}" />"><br/><br/>
		<strong>Содержание новости:</strong><br/>
		<textarea cols="150" rows="20" name="content">${news.content}</textarea><br/><br/><br/>

		<a href="<spring:url value="/save"/>" class="button" style="margin-left: 0px; margin-top: 10px;" onclick="document.getElementById('save_news').submit(); return false;">
		<c:choose>
		<c:when test="${empty news}">Сохранить новость</c:when>
		<c:otherwise>Сохранить изменения</c:otherwise>
		</c:choose>
		</a>
		
		<c:if test="${!empty news}">
		<input type="hidden" value="${news.id}" name="id">
		
		</c:if>
		
		<c:choose>
		<c:when test="${empty news}"><input type="hidden" value="add" name="mode"></c:when>
		<c:otherwise><input type="hidden" value="edit" name="mode"></c:otherwise>
		</c:choose>
		
		<input type="submit" value="Отправить" style="position: absolute; left: -9999px; width: 1px; height: 1px;" tabindex="-1"/>
		
		</form>

	</div>

	<div class="footer">
	</div>

</body>
</html>
