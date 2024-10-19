<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Java 221</title>
</head>
<body>
<h1>Java web</h1>
<h2>JSP</h2>
<p>
    Java Server Pages - технологія формування динамічних сторінок (сайтів)
    за допомогою серверної активності.
</p>
<h3>Вирази</h3>
<p>
    Інструкції, що мають результат, причому мається на увазі що цей результат
    стає частиною HTML.<br/>
    &lt;%= вираз мовою Java %&gt;<br/>
    Наприклад, <code>&lt;%= 2 + 3 %&gt; = <%= 2 + 3 %></code>
</p>
<h3>Інструкції</h3>
<p>
    Директиви, що не мають результату, або результат яких ігнорується.<br/>
    &lt;% інструкція мовою Java %&gt;
    <br/>
    Наприклад, <code>&lt;% int x = 10; %&gt;  <% int x = 10; %></code>
    <br/>
    <code>&lt;%= x %&gt; = <%= x %></code>
</p>
<h3>Умовна верстка</h3>
<p>
    Умовне формування HTML коду, причому негативне плече умовного оператора
    взагалі не потрапляє до HTML.<br/>
</p>
<pre>
&lt;% if(Умова) { %&gt;
    HTML-якщо-true
&lt;% } else { %&gt;
    HTML-якщо-false
&lt;% } %&gt;
</pre>

<br/>
<% if( x % 2 == 0 ) { %>
    <b>x - парне число</b>
<% } else { %>
    <i>х - непарне число</i>
<% } %>

<h3>Цикли</h3>
<p>
    Повторне включення до HTML однакових (або майже) блоків верстки.
</p>
<pre>
&lt;% for (int i = 0; i < 10; i++) { %&gt;
    HTML, що повторюється, за потреби з виразом &lt;%= i %&gt;
&lt;% } %&gt;
</pre>

<% for (int i = 0; i < 10; i++) { %>
    <span><%= i %></span>&emsp;
<% } %>

<%
    String[] arr = { "Product 1", "Product 2", "Product 3", "Product 4", "Product 5" };
%>
<ul>
    <% for(String str : arr) { %>
        <li><%= str %></li>
    <% } %>
</ul>
<h3>Взаємодія з файлами HTML/JSP</h3>
<p>
    Реалізація відображення одного файлу як частини іншого файлу.
</p>
&lt;jsp:include page="fragment.jsp"/&gt;
<jsp:include page="fragment.jsp"/>

<pre>
    Browser        Tomcat
     (URL)      (Listen:8080)      CGI
   HTTP->8080   -->  Parse   [Req, Resp] -> [python.exe index.py]
        <-----------  HTTP  <--------------- print '< html> ... Hello... < /html>';

</pre>
Д.З. Реалізувати відображення масиву рядків у вигляді HTML-таблиці,
______________
1 | String 1 |
2 | String 2 |
3 | String 3 |
...
* додати до "масиву" відомості про "ціну товару" та також вивести її в таблиці
</body>
</html>
