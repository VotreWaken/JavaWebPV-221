package itstep.learning.filters.auth;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.UserDao;
import itstep.learning.dal.dto.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@Singleton
public class SessionAuthFilter implements Filter {
    private final UserDao userDao;

    @Inject
    public SessionAuthFilter(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        String qs = req.getQueryString();
        if( "logout".equals( qs ) ) {
            session.removeAttribute( "userId" );
            ((HttpServletResponse) response).sendRedirect( req.getContextPath() + "/" );
            // немає chain.doFilter(request, response);
        }
        else {
            UUID userId = (UUID) session.getAttribute("userId");
            if (userId != null) {
                User user = userDao.getUserById(userId);
                if (user != null) {
                    req.setAttribute("Claim.Sid", userId);
                    req.setAttribute("Claim.Name", user.getName());
                    req.setAttribute("Claim.Avatar", user.getAvatar());
                }
            }
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
/*
Реалізувати вихід з авторизованого режиму:
- перед виходом видавати погодження "Вийти з системи?"
- аналізувати не лише рівність "logout", а вміст цього параметра,
   але не просто contains, тому що це буде пошук у значеннях
   (?search=logout) або у складових інших параметрів (geo-logout)
 */