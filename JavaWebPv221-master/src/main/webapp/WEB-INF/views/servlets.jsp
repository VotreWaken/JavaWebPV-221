<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>Servlets</h1>
<p>
    Сервлети у Java - спеціалізовані класи, призначені для мережних задач.
    Зокрема, HttpServlet - для веб-задач, аналог контролерів.
</p>
<p>
    Роботу з сервлетами забезпечує Servlet-API, що встановлюється як
    окремі бібліотеки (залежності). Для Maven: https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api
</p>
<p>
    Порядок роботи з сервлетами наступний
</p>
<ul>
    <li>Описуємо клас сервлету.</li>
    <li>Якщо це сервлет з представленням (View), то створюємо JSP файл</li>
    <li>У сервлеті переадресовуємо (forward) на представлення</li>
    <li>
        Реєструємо сервлет (зв'язуємо його з веб-адресою)
        одним з наступних способів
        <ul>
            <li>За допомогою web.xml</li>
            <li>Анотацією @WebServlet</li>
            <li>Інструментами IoC</li>
        </ul>
    </li>
</ul>
</body>
</html>
