<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h1>Реєстрація користувача</h1>

<form class="card-panel grey lighten-5" enctype="multipart/form-data"  method="post" >
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
            <div class="btn green darken-1">
                <i class="material-icons">account_circle</i>
                <input type="file" name="user-avatar">
            </div>
            <div class="file-path-wrapper">
                <input class="file-path validate" type="text" >
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
            <label for="user-repeat">Повторити пароль</label>
        </div>
    </div>
    <div class="row">
        <button class="btn waves-effect waves-light green darken-3 right" type="submit" name="action">Реєстрація
            <i class="material-icons right">send</i>
        </button>
    </div>

</form>
<div style="height: 40px"></div>
<h2>Розбір даних форм</h2>
<p>
    Форми передаються двома видами представлень:
    <code>application/x-www-form-urlencoded</code>
    <code>multipart/form-data</code>
    Перший включає лише поля (ключ=значення) та може бути як в query-параметра,
    так і в тілі пакета.
    Другий може передавати файли і має значно складнішу структуру:
    multipart - такий, що складається з кількох частин, кожна з яких - це
    самостійний HTTP пакет, тільки без статус-рядка. Кожне поле форми передається
    окремою частиною, яка своїми заголовками визначає що це - файл або поле
</p>
<pre>
    HTTP/1.1 200 OK
    Connection: close
    Delimiter: 1234

    1234--
    Content-type: text/plain
    Content-Disposition: form-field; name=user-name

    Петрович
    1234--
    Content-type: image/png
    Content-Disposition: attachment; filename=photo.png

    PNG1l;jvno[imp3perindb'k
    --1234--
</pre>
<div style="height: 40px"></div>