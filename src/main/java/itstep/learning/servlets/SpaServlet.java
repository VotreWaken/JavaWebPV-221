package itstep.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.TokenDao;
import itstep.learning.dal.dto.User;
import itstep.learning.rest.RestServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

@Singleton
public class SpaServlet extends RestServlet {
    private final Logger logger;
    private final TokenDao tokenDao;

    @Inject
    public SpaServlet(Logger logger, TokenDao tokenDao) {
        this.logger = logger;
        this.tokenDao = tokenDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // ~ return View()
        req.setAttribute( "page", "spa" );
        req.getRequestDispatcher("WEB-INF/views/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authHeader = req.getHeader( "Authorization" );
        if( authHeader == null ) {
            super.sendRest( 401, "Missing Authorization header" );
            return;
        }
        if( ! authHeader.startsWith( "Bearer " ) ) {
            super.sendRest( 401, "Bearer Authorization scheme only" );
            return;
        }
        String token = authHeader.substring( 7 );
        UUID tokenId;
        try {
            tokenId = UUID.fromString( token );
        }
        catch( IllegalArgumentException ex ) {
            logger.warning( ex.getMessage() );
            super.sendRest( 401, "Illegal token format" );
            return;
        }
        try {
            User user = tokenDao.getUserByTokenId( tokenId ) ;
            super.sendRest( 200, user );
        }
        catch( Exception ex ) {
            super.sendRest( 500, ex.getMessage() );
        }
    }

}
/*
Д.З. Забезпечити збереження даних про користувача, які надходять при
одержані токену (автентифікації) та відображати їх на сторінці сайту
(ім'я користувача та/або аватарку)
Після виходу або вичерпання терміну токену прибирати ці дані
 */