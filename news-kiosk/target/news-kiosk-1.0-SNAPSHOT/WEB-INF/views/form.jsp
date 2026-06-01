<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Газетный киоск — Товар</title>
</head>
<body>

<h1>
    <c:choose>
        <c:when test="${formAction == 'update'}">Редактировать товар</c:when>
        <c:otherwise>Новый товар</c:otherwise>
    </c:choose>
</h1>

<form method="post" action="${pageContext.request.contextPath}/publications">
    <input type="hidden" name="action" value="${formAction}"/>
    <input type="hidden" name="type" value="${type}"/>
    <c:if test="${publication != null}">
        <input type="hidden" name="id" value="${publication.id}"/>
    </c:if>

    <p>
        <b>Тип:</b>
        <c:choose>
            <c:when test="${type == 'NEWSPAPER'}">Газета</c:when>
            <c:when test="${type == 'MAGAZINE'}">Журнал</c:when>
            <c:when test="${type == 'BOOK'}">Книга</c:when>
        </c:choose>
    </p>

    <p>
        <label>Название:
            <input type="text" name="title" value="${publication.title}" required/>
        </label>
    </p>
    <p>
        <label>Цена:
            <input type="number" name="price" step="0.01" min="0"
                   value="${publication.price}" required/>
        </label>
    </p>
    <p>
        <label>Количество:
            <input type="number" name="quantity" min="0"
                   value="${publication.quantity}" required/>
        </label>
    </p>

    <c:if test="${type == 'NEWSPAPER'}">
        <p>
            <label>Номер выпуска:
                <input type="number" name="issueNumber"
                       value="${publication.issueNumber}" required/>
            </label>
        </p>
        <p>
            <label>Дата (DD.MM.YYYY):
                <input type="text" name="pubDate"
                       value="${publication.date}" required/>
            </label>
        </p>
    </c:if>

    <c:if test="${type == 'MAGAZINE'}">
        <p>
            <label>Номер выпуска:
                <input type="number" name="issueNumber"
                       value="${publication.issueNumber}" required/>
            </label>
        </p>
        <p>
            <label>Месяц/Год (MM/YYYY):
                <input type="text" name="monthYear"
                       value="${publication.monthYear}" required/>
            </label>
        </p>
    </c:if>

    <c:if test="${type == 'BOOK'}">
        <p>
            <label>Автор:
                <input type="text" name="author"
                       value="${publication.author}" required/>
            </label>
        </p>
        <p>
            <label>ISBN:
                <input type="text" name="isbn"
                       value="${publication.isbn}" required/>
            </label>
        </p>
    </c:if>

    <button type="submit">Сохранить</button>
    <a href="${pageContext.request.contextPath}/publications">Отмена</a>
</form>

</body>
</html>
