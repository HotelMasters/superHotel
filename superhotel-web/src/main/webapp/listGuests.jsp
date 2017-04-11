<%--
  Created by IntelliJ IDEA.
  User: rainbowdash
  Date: 5.4.17
  Time: 13:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="java" %>
<html>
<head>
    <link href="${pageContext.request.contextPath}/style.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=News+Cycle|Open+Sans" rel="stylesheet">
    <title>Guest management</title>
</head>
<body>
<h1>Správa hostí</h1>
    <table id="tab1">
        <thead>
        <tr>
            <th>Meno</th>
            <th>Adresa</th>
            <th>Dátum narodenia</th>
            <th>Číslo kreditnej karty</th>
        </tr>
        </thead>
        <java:forEach items="${guests}" var = "guest">
            <tr>
            <td> <java:out value="${guest.name}"/></td>
            <td> <java:out value="${guest.address}"/></td>
            <td> <java:out value="${guest.birthDay}"/></td>
            <td> <java:out value="${guest.crCardNumber}"/></td>
                <td>
                    <form method="post" action="${pageContext.request.contextPath}/guest/delete?id=${guest.id}">
                        <input id="del" type="Submit" value="Vymazať">
                    </form>
                </td>
            </tr>
        </java:forEach>
    </table>

<h2>Zadajte hosťa:</h2>
<java:if test="${not empty error}">
    <div class="error">
        <java:out value="${error}"/>
    </div>
</java:if>
<form action="${pageContext.request.contextPath}/guest/add" method="post">
    <table id="tab2">
        <tr>
            <th>Meno:</th>
            <td><input type="text" name="name" value="<java:out value="${param.name}"/>"/></td>
        </tr>
        <tr>
            <th>Adresa:</th>
            <td><input type="text" name="address" value="<java:out value="${param.address}"/>"/></td>
        </tr>
        <tr>
            <th>Dátum narodenia:</th>
            <td><input type="text" name="birthDay" value="<java:out value="${param.birthday}"/>"/></td>
        </tr>
        <tr>
            <th>Číslo kreditnej karty:</th>
            <td><input type="text" name="crCardNumber" value="<java:out value="${param.crCardNum}"/>"/></td>
        </tr>

    </table>

    <input id="add" type="Submit" value="Vložiť hosťa"/>


</form>

</body>
</html>
