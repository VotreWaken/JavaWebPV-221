<%@ page import="itstep.learning.dal.dto.Token" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<h1>Home</h1>
<a href="servlets">servlets</a>

<%
    Token token = (Token) request.getAttribute("token");
    if (token != null) {
%>
<p><strong>Token ID:</strong> <%= token.getTokenId() %></p>
<p><strong>User ID:</strong> <%= token.getUserId() %></p>
<p><strong>Issued At:</strong> <%= token.getIat() %></p>
<p><strong>Expires At:</strong> <%= token.getExp() %></p>

<%
    Timestamp updated = (Timestamp) request.getAttribute("tokenUpdate");
    if (updated != null) {
        // Преобразуем Timestamp в строку
        String updatedTime = updated.toString();
%>
<p><strong>Updated After:</strong> <%= updatedTime %></p>
<%
} else {
%>
<p><strong>Updated At:</strong> No update available.</p>
<%
    }
%>
<%
} else {
%>
<p>No valid token found or an error occurred.</p>
<%
    }
%>
