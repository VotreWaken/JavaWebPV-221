package itstep.learning.filters;

import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Singleton
public class CharsetFilter implements Filter {
    private FilterConfig filterConfig;

    @Override
    public void init( FilterConfig filterConfig ) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException, ServletException {
        /*
        Особливість кодування символів у JSP полягає у тому, що її неможливо
        переключити після першого звернення на читання/запис з req/resp.
        Відповідно, перемикання кодування має здійснюватись якомога раніше,
        у первинних фільтрах системи.
         */
        request.setCharacterEncoding( "UTF-8" );
        request.setAttribute( "charset", "UTF-8" );

        response.setCharacterEncoding( "UTF-8" );

        System.out.println( "Filter works for " +
                ((HttpServletRequest) request).getRequestURI() );

        // chain - ланцюг фільтрів, передача управління наступному з них -
        // не автоматична, а здійснюється програмно. Якщо не передати, то
        // оброблення запиту припиняється
        chain.doFilter(request, response);   // ~Next()

    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
/*
Аналогічно до сервлетів, фільтри треба реєструвати і також є три способи
- web.xml
- @WebFilter - не гарантується порядок роботи фільтрів, тому не є поширеним
- IoC (Guice)

IoC: відмінності від консольного застосунку
Загальна схема
 1. Конфігурація (реєстрація сервісів) - одноразово
 2. Resolve - створення об'єктів з включенням до них залежностей (інжекція) -
     багаторазово

У консолі п.2 доволі часто теж одноразовий - всі сутності створюються
 при запуску.
У веб-застосунку навпаки, кожен запит ПОВИНЕН створювати новий об'єкт
 сервлета/контролера. Відповідно п.2 виконується постійно
Відповідно, конфігурація інжектора має виконуватись при старті веб-проєкту
(при деплої), а використання - при кожному запиті. Оскільки кожен запит
проходить через фільтри, інжектор вбудовується у проєкт в якості фільтру.
 */