<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="java" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Správa izieb</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/rooms.css"/>
</head>
<body>
    <h1>Správa izieb</h1>
    <p>Táto stránka umožňuje vytvárať, zobrazovať, upravovať a mazať izby vášho hotela.</p>
    <div id="roomList">
        <h2>Zoznam izieb</h2>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Kapacita</th>
                <th>Cena</th>
            </tr>
            </thead>
            <java:forEach items="${rooms}" var="room">
                <tr>
                    <td><java:out value="${room.id}"/></td>
                    <td><java:out value="${room.capacity}"/>
                    <java:choose>
                        <java:when test="${room.capacity == 1}">osoba</java:when>
                        <java:otherwise>
                            <java:choose>
                                <java:when test="${room.capacity < 5}">osoby</java:when>
                                <java:otherwise>osôb</java:otherwise>
                            </java:choose>
                        </java:otherwise>
                    </java:choose>
                    </td>
                    <td><java:out value="${room.price}"/> Kč</td>
                    <td>
                        <form action="${pageContext.request.contextPath}/room/delete?id=${room.id}" method="post">
                            <input type="Submit" value="Vymazať"/>
                        </form>
                    </td>
                </tr>
            </java:forEach>
        </table>
    </div>
    <div id="roomCreate">
        <h2>Pridať izbu</h2>
        <java:if test="${not empty error}">
            <p class="error"><java:out value="${error}"/></p>
        </java:if>
        <form action="${pageContext.request.contextPath}/room/create" method="post">
            <table style="margin-top: -2em">
                <tr>
                    <th>Kapacita:</th>
                    <td><input type="text" name="capacity" value="<java:out value='${capacity}'/>"/> osôb</td>
                </tr><br/>
                <tr>
                    <th>Cena:</th>
                    <td><input type="text" name="price" value="<java:out value='${price}'/>"/> Kč</td>
                </tr>
            </table>
            <input type="Submit" value="Vytvoriť" id="roomCreateSubmit"/>
        </form>
    </div>
</body>
</html>
