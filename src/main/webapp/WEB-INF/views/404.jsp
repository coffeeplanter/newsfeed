<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<spring:url value="/resources/css/news.css"/>" type="text/css">
<title>Ошибка 404</title>
</head>
<body>
<h2>Ошибка!</h2>
<p>Страница не существует.</p>
<p><a href="<spring:url value="/"/>">Перейдите на главную страницу</a></p>
</body>
</html>
