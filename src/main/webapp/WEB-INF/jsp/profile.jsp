<%--
  Created by IntelliJ IDEA.
  User: romankukin
  Date: 15.12.2021
  Time: 19:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <h1>Profile</h1>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <style>
      td {
        padding: 0 15px;
      }
    </style>
</head>
<body>
<div style="display: flex;">
    <div>
        <div>
            <img src="${profileImage}" alt="profile image"
                 style="max-width: 200px;max-height: 350px">
        </div>
        <div>
            <form action="${pageContext.request.contextPath}/uploadImage" enctype="multipart/form-data" method="post">
                <input type="file" id="image" name="image" accept="image/*">
                <button type="submit">Upload</button>
            </form>
        </div>
        <form class="form-test" action="${pageContext.request.contextPath}/logout" method="post">
            <h2><input type="submit" value="logout"></h2>
        </form>
    </div>
    <div style="margin-left: 100px">
        <h2>Hello!</h2>
        <h2>${userEmail}</h2>
    </div>
</div>
<div style="display: flex;margin-top: 100px">
    <div>
        <table>
            <tr>
                <th>File name</th>
                <th>Size</th>
                <th>MIME</th>
            </tr>
            <jsp:useBean id="imagesHistoryList" scope="request" type="java.util.List"/>
            <c:forEach items="${imagesHistoryList}" var="imagesHistoryList" varStatus="status">
                <tr>
                    <td>
                        <c:choose>
                            <c:when test="${imagesHistoryList.filename=='shades.png'}">
                                <a href="getImage/${imagesHistoryList.filename}" target="_blank">${imagesHistoryList.filename}</a>
                                <br />
                            </c:when>
                            <c:otherwise>
                                <a href="getImage/${userId}/${imagesHistoryList.filename}" target="_blank">${imagesHistoryList.filename}</a>
                                <br />
                            </c:otherwise>
                        </c:choose>
                        </td>
                    <td>${imagesHistoryList.size}</td>
                    <td>${imagesHistoryList.mime}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <div style="margin-left: 100px">
        <table>
            <tr>
                <th>DateTime</th>
                <th>Type</th>
                <th>IP</th>
            </tr>
            <jsp:useBean id="authEvents" scope="request" type="java.util.List"/>
            <c:forEach items="${authEvents}" var="authEvents" varStatus="status">
                <tr>
                    <td>${authEvents.eventTime}</td>
                    <td>${authEvents.eventType}</td>
                    <td>${authEvents.ipAddress}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>

</body>
</html>
