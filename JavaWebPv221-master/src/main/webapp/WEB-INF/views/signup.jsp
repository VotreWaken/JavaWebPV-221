<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String contextPath = request.getContextPath();
%>
<h1>Реєстрація користувача</h1>
<form class="card-panel grey lighten-5"
      id="signup-form"
      action="<%=contextPath%>/signup"
      enctype="multipart/form-data"
      method="post">
    <div class="row">
        <div class="input-field col s6">
            <i class="material-icons prefix">badge</i>
            <input id="user-name" name="user-name" type="text" class="validate">
            <label for="user-name">Ім'я</label>
        </div>
        <div class="input-field col s6">
            <i class="material-icons prefix">cake</i>
            <input id="user-birthdate" name="user-birthdate" type="date" class="validate">
            <label for="user-birthdate">Дата народження</label>
        </div>
    </div>

    <div class="row">
        <div class="input-field col s6">
            <i class="material-icons prefix">alternate_email</i>
            <input id="user-email" name="user-email" type="email" class="validate">
            <label for="user-email">E-mail</label>
        </div>
        <div class="file-field input-field col s6">
            <div class="btn light-blue">
                <i class="material-icons">account_circle</i>
                <input type="file" name="user-avatar">
            </div>
            <div class="file-path-wrapper">
                <input class="file-path validate" type="text">
            </div>
        </div>
    </div>

    <div class="row">
        <div class="input-field col s6">
            <i class="material-icons prefix">lock</i>
            <input id="user-password" name="user-password" type="password" class="validate">
            <label for="user-password">Пароль</label>
        </div>
        <div class="input-field col s6">
            <i class="material-icons prefix">lock_open</i>
            <input id="user-repeat" name="user-repeat" type="password" class="validate">
            <label for="user-repeat">Повтор</label>
        </div>
    </div>

    <div class="row">
        <button class="btn waves-effect waves-light  light-blue darken-2 right" type="submit">Реєстрація
            <i class="material-icons right">send</i>
        </button>
    </div>
</form>

<div style="height: 40px"></div>
<h2>Розбір даних форм</h2>
<p>
    Форми передаються двома видами представлень:
    <code>application/x-www-form-urlencoded</code> або
    <code>multipart/form-data</code>.
    Перший включає лише поля (ключ=значення) та може бути як в query-параметрах,
    так і в тілі пакету.
    Другий може передавати файли і має значно складнішу структуру:
    multipart - такий, що складається з кількох частин, кожна з яких - це
    самостійний НТТР пакет, тільки без статус-рядка. Кожне поле форми передається
    окремою частиною, яка своїми заголовками визначає що це - файл або поле.
</p>
<pre>
    POST /JavaWeb/signup HTTP/1.1\r\n
    Host: localhost:8080\r\n
    Connection: close\r\n
    Content-Type: application/x-www-form-urlencoded; charset=utf-8\r\n
    \r\n
    user-name=%D0%9F%D0%B5%D1%82%D1%80%D0%BE%D0%B2%D0%B8%D1%87&user-email=user@i.ua
    (user-name=Петрович&user-email=user@i.ua)



    POST /JavaWeb/signup HTTP/1.1
    Host: localhost:8080
    Connection: close
    Delimiter: 1234
    Content-Type: multipart/form-data; charset=utf-8

    1234--
    Content-Type: text/plain; charset=utf-8
    Content-Disposition: form-field; name=user-name

    Петрович
    1234--
    Content-Type: text/plain; charset=utf-8
    Content-Disposition: form-field; name=user-email

    user@i.ua
    1234--
    Content-Type: image/png
    Content-Disposition: attachment; filename=photo.png

    PNG1l;jnvo[im3perindb'k,
    --1234--
</pre>
<div style="height: 40px"></div>