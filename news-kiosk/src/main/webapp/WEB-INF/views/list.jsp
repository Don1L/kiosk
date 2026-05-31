<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Газетный киоск</title>
</head>
<body>

<h1>Газетный киоск</h1>

<c:if test="${not empty error}">
    <p style="color:red"><b>Ошибка:</b> ${error}</p>
</c:if>

<p>
    Добавить:
    <a href="${pageContext.request.contextPath}/publications?action=newForm&type=NEWSPAPER">Газету</a> |
    <a href="${pageContext.request.contextPath}/publications?action=newForm&type=MAGAZINE">Журнал</a> |
    <a href="${pageContext.request.contextPath}/publications?action=newForm&type=BOOK">Книгу</a>
</p>

<table border="1" cellpadding="6" cellspacing="0">
    <thead>
        <tr>
            <th>ID</th>
            <th>Тип</th>
            <th>Название</th>
            <th>Цена</th>
            <th>Кол-во</th>
            <th>Детали</th>
            <th>Действия</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${publications}" var="p">
            <tr>
                <td>${p.id}</td>
                <td>
                    <c:choose>
                        <c:when test="${p.type == 'NEWSPAPER'}">Газета</c:when>
                        <c:when test="${p.type == 'MAGAZINE'}">Журнал</c:when>
                        <c:when test="${p.type == 'BOOK'}">Книга</c:when>
                    </c:choose>
                </td>
                <td>${p.title}</td>
                <td>${p.price}</td>
                <td>${p.quantity}</td>
                <td>
                    <c:if test="${p.type == 'NEWSPAPER'}">
                        №${p.issueNumber} от ${p.date}
                    </c:if>
                    <c:if test="${p.type == 'MAGAZINE'}">
                        №${p.issueNumber}, ${p.monthYear}
                    </c:if>
                    <c:if test="${p.type == 'BOOK'}">
                        ${p.author}, ISBN: ${p.isbn}
                    </c:if>
                </td>
                <td>
                    <form method="post" action="${pageContext.request.contextPath}/publications"
                          style="display:inline">
                        <input type="hidden" name="action" value="sell"/>
                        <input type="hidden" name="id" value="${p.id}"/>
                        <input type="number" name="sellQty" value="1" min="1"
                               style="width:45px"/>
                        <button type="submit">Продать</button>
                    </form>
                    &nbsp;
                    <a href="${pageContext.request.contextPath}/publications?action=edit&id=${p.id}">
                        Изменить
                    </a>
                    &nbsp;
                    <form method="post" action="${pageContext.request.contextPath}/publications"
                          style="display:inline"
                          onsubmit="return confirm('Удалить?')">
                        <input type="hidden" name="action" value="delete"/>
                        <input type="hidden" name="id" value="${p.id}"/>
                        <button type="submit">Удалить</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty publications}">
            <tr><td colspan="7" style="text-align:center">Список пуст</td></tr>
        </c:if>
    </tbody>
</table>

</body>
</html>
