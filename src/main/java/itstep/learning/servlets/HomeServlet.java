package itstep.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import itstep.learning.dal.dao.TokenDao;
import itstep.learning.dal.dao.UserDao;
import itstep.learning.dal.dao.shop.CartDao;
import itstep.learning.dal.dao.shop.CategoryDao;
import itstep.learning.dal.dao.shop.ProductDao;
import itstep.learning.services.hash.HashService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@Singleton
public class HomeServlet extends HttpServlet {
    private final UserDao userDao;
    private final TokenDao tokenDao;
    private final CategoryDao categoryDao;
    private final ProductDao productDao;
    private final CartDao cartDao;

    @Inject
    public HomeServlet(UserDao userDao, TokenDao tokenDao, CategoryDao categoryDao, ProductDao productDao, CartDao cartDao) {
        this.userDao = userDao;
        this.tokenDao = tokenDao;
        this.categoryDao = categoryDao;
        this.productDao = productDao;
        this.cartDao = cartDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute( "hash",
                userDao.installTables() &&
                tokenDao.installTables() &&
                productDao.installTables() &&
                cartDao.installTables() &&
                categoryDao.installTables()
                        ? "Tables OK" : "Tables Fail" );
        // ~ return View()
        req.setAttribute( "page", "home" );
        req.getRequestDispatcher("WEB-INF/views/_layout.jsp").forward(req, resp);
    }
}
/*
Д.З. Після надсилання форми реєстрації користувача вивести (замість
порожньої сторінки) передані дані (для файлу - ім'я та *розмір)
 */