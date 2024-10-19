package itstep.learning.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestServlet extends HttpServlet {
    protected final static Gson gson = new GsonBuilder().serializeNulls().create();

    protected RestResponse restResponse;
    protected HttpServletResponse resp;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.resp = resp;
        if( this.restResponse == null ) {
            this.restResponse = new RestResponse();
        }
        super.service(req, resp);
    }

    protected void sendRest(int statusCode, Object data) throws IOException {
        restResponse
                .setStatus( statusCode )
                .setData( data );
        sendRest(0);
    }
    protected void sendRest() throws IOException {
        sendRest(0);
    }
    protected void sendRest(int maxAge) throws IOException {
        resp.setContentType( "application/json" );
        resp.setHeader( "Cache-Control", maxAge == 0 ? "no-cache" : "max-age=" + maxAge );
        resp.getWriter().print( gson.toJson( restResponse ) );
    }
}
/*
Д.З. Реалізувати скасування кошику - кнопку, яка видаляє
кошик (всі позиції) без оформлення оплати
 */