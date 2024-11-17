package itstep.learning.servlets.shop;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.shop.CartDao;
import itstep.learning.rest.RestMetaData;
import itstep.learning.rest.RestResponse;
import itstep.learning.rest.RestServlet;
import itstep.learning.services.stream.StringReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

@Singleton
public class CartServlet extends RestServlet {
    private final CartDao cartDao;
    private final StringReader stringReader;
    private final Logger logger;

    @Inject
    public CartServlet(CartDao cartDao, StringReader stringReader, Logger logger) {
        this.cartDao = cartDao;
        this.stringReader = stringReader;
        this.logger = logger;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.restResponse = new RestResponse().setMeta(
                new RestMetaData()
                        .setUri( "/shop/cart" )
                        .setMethod( req.getMethod() )
                        .setLocale( "uk-UA" )
                        .setServerTime( new Date() )
                        .setName( "Shop Cart API" )
                        .setAcceptMethods(new String[]{ "GET", "POST", "PUT", "DELETE" })
        );
        if( req.getMethod().equals( "PATCH" ) ) {
            super.resp = resp;
            this.doPatch( req, resp );
        }
        else {
            super.service(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Перевіряємо токен (за результатом фільтру)
        String userId = (String) req.getAttribute( "Claim.Sid" );
        if( userId == null ) {
            super.sendRest( 401, "Auth token required" );
            return;
        }
        JsonObject json;
        try { json = parseBodyAsJsonObject(req); }
        catch( ParseException ex ) {
            super.sendRest( ex.getErrorOffset(), ex.getMessage() );
            return;
        }
        JsonElement element = json.get( "userId" );
        if( element == null ) {
            super.sendRest( 422, "JSON must have 'userId' field" );
            return;
        }
        String cartUserId = element.getAsString();
        if( ! userId.equals( cartUserId ) ) {
            super.sendRest( 403, "Authorization mismatch" );
            return;
        }
        element = json.get( "productId" );
        if( element == null ) {
            super.sendRest( 422, "JSON must have 'productId' field" );
            return;
        }
        UUID cartProductId;
        try {
            cartProductId = UUID.fromString( element.getAsString() );
        }
        catch( IllegalArgumentException ignored ) {
            super.sendRest( 422, "'productId' field must be a valid UUID" );
            return;
        }
        if( cartDao.add( UUID.fromString( userId ), cartProductId, 1 ) ) {
            super.sendRest( 201, cartProductId );
        }
        else {
            super.sendRest( 500, "See server log for details" );
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Перевіряємо токен (за результатом фільтру)
        String userId = (String) req.getAttribute( "Claim.Sid" );
        if( userId == null ) {
            super.sendRest( 401, "Auth token required" );
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put( "userId", userId );
        super.restResponse.getMeta().setParams( params );

        super.sendRest( 200, cartDao.getCart( userId ) );
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject json;
        try { json = parseBodyAsJsonObject(req); }
        catch( ParseException ex ) {
            super.sendRest( ex.getErrorOffset(), ex.getMessage() );
            return;
        }
        JsonElement element = json.get( "cartId" );
        if( element == null ) {
            super.sendRest( 422, "JSON must have 'cartId' field" );
            return;
        }
        String str = element.getAsString();
        UUID cartId;
        try { cartId = UUID.fromString( str ); }
        catch( IllegalArgumentException ignored ) {
            super.sendRest( 422, "'cartId' field must be a valid UUID" );
            return;
        }
        element = json.get( "productId" );
        if( element == null ) {
            super.sendRest( 422, "JSON must have 'productId' field" );
            return;
        }
        str = element.getAsString();
        UUID productId;
        try { productId = UUID.fromString( str ); }
        catch( IllegalArgumentException ignored ) {
            super.sendRest( 422, "'productId' field must be a valid UUID" );
            return;
        }
        element = json.get( "delta" );
        if( element == null ) {
            super.sendRest( 422, "JSON must have 'delta' field" );
            return;
        }
        int delta = element.getAsInt();
        try {
            if( cartDao.update( cartId, productId, delta ) ) {
                super.sendRest( 200, "Updated" );
            }
            else {
                super.sendRest( 409, "Update failed" );
            }
        }
        catch( Exception ignored ) {
            super.sendRest( 500, "See server log for details" );
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID cartId;
        try { cartId = UUID.fromString( req.getParameter("cart-id") ); }
        catch( IllegalArgumentException ignored ) {
            super.sendRest( 400, "'cart-id' parameter must be a valid UUID" );
            return;
        }
        super.sendRest( 202, cartDao.close( cartId, false ) );
    }

    private JsonObject parseBodyAsJsonObject( HttpServletRequest req ) throws ParseException {
        JsonElement json = parseBody( req );
        if( ! json.isJsonObject() ) {
            throw new ParseException( "JSON root must be an object", 422 );
        }
        return json.getAsJsonObject();
    }

    private JsonElement parseBody( HttpServletRequest req ) throws ParseException {
        if( ! req.getContentType().startsWith( "application/json" ) ) {
            throw new ParseException( "'application/json' expected", 415 );
        }
        String jsonString;
        try {
            jsonString = stringReader.read( req.getInputStream() );
        }
        catch( IOException ex ) {
            logger.warning( ex.getMessage() );
            throw new ParseException( "JSON could not be extracted", 400 );
        }
        JsonElement json;
        try {
            json = gson.fromJson( jsonString, JsonElement.class );
        }
        catch( JsonSyntaxException ex ) {
            throw new ParseException( "JSON could not be parsed", 400 );
        }
        return json;
    }
}
