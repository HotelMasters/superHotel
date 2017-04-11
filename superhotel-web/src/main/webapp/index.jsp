<%@ page import="pv168.hotelmasters.superhotel.web.RoomServlet" %>
<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SuperHotel</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/index.css"
</head>
<body>
    <h1>SuperHotel</h1>
    <p>Hello there.</p>
    <a href="/guest"><h2>Správa hostí</h2></a>
    <a href="/room"><h2>Správa izieb</h2></a>
</body>
</html>
