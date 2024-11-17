<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String pageName = (String) request.getAttribute( "page" ) ;
    // contextPath -- /Java221 - контекстний шлях нашого застосунку
    String contextPath = request.getContextPath();
    boolean isAuthenticated = request.getAttribute("Claim.Sid") != null;
    String avatar = (String) request.getAttribute("Claim.Avatar");
    String userName = (String) request.getAttribute("Claim.Name");
%>
<html>
<head>
    <title>PV-221</title>
    <!--Import Google Icon Font-->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <!-- Compiled and minified CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <link rel="stylesheet" href="<%=contextPath%>/css/site.css" />
</head>
<body>
    <header>
        <nav>
            <div class="nav-wrapper light-blue">
                <a href="<%=contextPath%>/" class="site-logo left">
                    <img src="<%=contextPath%>/img/Java_Logo.png" alt="Logo" />
                    PV-221
                </a>
                <ul id="nav-mobile" class="left">
                    <li><a href="<%=contextPath%>/spa">SPA</a></li>
                    <li><a href="#">Components</a></li>
                    <li><a href="#">JavaScript</a></li>
                </ul>
                <% if( isAuthenticated ) { %>
                    <img src="<%=contextPath%>/file/<%=avatar%>"
                         alt="avatar"
                         title="<%=userName%>"
                         class="nav-addon right nav-avatar"/>
                    <a class="nav-addon right" href="?logout"><i class="material-icons">logout</i></a>
                <% } else { %>
                    <a class="nav-addon right" href="<%=contextPath%>/signup"><i class="material-icons">person_add</i></a>
                    <!-- Modal Trigger -->
                    <a class="nav-addon right modal-trigger" href="#auth-modal"><i class="material-icons">login</i></a>
                <% } %>

            </div>
        </nav>
    </header>
    <!-- RenderBody -->
    <main class="container"><jsp:include page='<%= pageName + ".jsp" %>' /></main>
    <div class="spacer"></div>
    <footer class="page-footer light-blue">
        <div class="container">
            <div class="row">
                <div class="col l6 s12">
                    <h5 class="white-text">Footer Content</h5>
                    <p class="grey-text text-lighten-4">You can use rows and columns here to organize your footer content.</p>
                </div>
                <div class="col l4 offset-l2 s12">
                    <h5 class="white-text">Links</h5>
                    <ul>
                        <li><a class="grey-text text-lighten-3" href="#!">Link 1</a></li>
                        <li><a class="grey-text text-lighten-3" href="#!">Link 2</a></li>
                        <li><a class="grey-text text-lighten-3" href="#!">Link 3</a></li>
                        <li><a class="grey-text text-lighten-3" href="#!">Link 4</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="footer-copyright">
            <div class="container">
                © 2024 ITSTEP PV-221
                <a class="grey-text text-lighten-4 right" href="#!">More Links</a>
            </div>
        </div>
    </footer>


    <!-- Modal Structure -->
    <div id="auth-modal" class="modal">
        <div class="modal-content">
            <h4>Автентифікація</h4>
            <form id="modal-auth-form" action="<%=contextPath%>/signup">
            <div class="row">
                <div class="input-field col s6">
                    <i class="material-icons prefix">alternate_email</i>
                    <input id="auth-user-email" name="user-email" type="email" class="validate">
                    <label for="auth-user-email">E-mail</label>
                </div>
                <div class="input-field col s6">
                    <i class="material-icons prefix">lock</i>
                    <input id="auth-user-password" name="user-password" type="password" class="validate">
                    <label for="auth-user-password">Пароль</label>
                </div>
            </div>
            </form>
        </div>
        <div class="modal-footer">
            <button class="modal-close waves-effect waves-green btn-flat">Закрити</button>
            <button form="modal-auth-form" type="submit" class="waves-effect waves-green btn-flat">Вхід</button>
        </div>
    </div>


    <!-- Compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
<script>

</script>
<script src="<%=contextPath%>/js/site.js"></script>
</body>
</html>
