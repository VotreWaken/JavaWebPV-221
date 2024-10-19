<%@ page import="java.io.File" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    File scriptFile = new File(
            request.getServletContext().getRealPath("./js/spa.js"));
    long scriptFileLM = scriptFile.lastModified();
%>

<div id="spa-container"></div>

<!-- Завантажимо React. -->
<!-- Примітка: перед розгортанням на продакшн, замініть "development.js" на "production.min.js". -->
<script src="https://unpkg.com/react@18/umd/react.development.js" crossorigin></script>
<script src="https://unpkg.com/react-dom@18/umd/react-dom.development.js" crossorigin></script>
<script src="https://unpkg.com/babel-standalone@6/babel.min.js"></script>

<script type="text/babel" src="js/spa.js?<%=scriptFileLM%>"></script>
