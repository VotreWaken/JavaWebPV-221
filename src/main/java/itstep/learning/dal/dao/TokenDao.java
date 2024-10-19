package itstep.learning.dal.dao;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import itstep.learning.dal.dto.Token;
import itstep.learning.dal.dto.User;
import itstep.learning.services.hash.HashService;

import java.sql.*;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TokenDao {
    private final Connection connection;
    private final Logger logger;

    @Inject
    public TokenDao( Connection connection, Logger logger ) {
        this.connection = connection;
        this.logger = logger;
    }

    public User getUserByTokenId( UUID tokenId ) throws Exception {
        String sql = "SELECT * FROM tokens t JOIN users u ON t.user_id = u.id WHERE t.token_id = ?";
        try( PreparedStatement prep = connection.prepareStatement(sql) ) {
            prep.setString( 1, tokenId.toString() );
            ResultSet rs = prep.executeQuery();
            if( rs.next() ) {
                Token token = new Token( rs );
                if( token.getExp().before( new Date() ) ) {
                    throw new Exception( "Token expired" ) ;
                }
                return new User( rs );
            }
            else {
                throw new Exception( "Token rejected" ) ;
            }
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql, ex );
            throw new Exception( "Server error. Details on server logs" ) ;
        }
    }

    public Token create( User user ) {
        /*
        Д.З. Перед створенням нового токену для користувача
        виконати перевірку того, що в даного користувача вже
        є активний токен (дата ехр якого ще не настала).
        У такому разі подовжити токен на половину типового
        терміну дії токену. Новий токен при цьому не створювати.
         */
        Token token = new Token();
        token.setTokenId( UUID.randomUUID() );
        token.setUserId( user.getId() );
        token.setIat( new Date( System.currentTimeMillis() ) );
        token.setExp( new Date( System.currentTimeMillis() + 1000 * 3600 * 3 ) );
        String sql = "INSERT INTO tokens (token_id, user_id, iat, exp) VALUES (?, ?, ?, ?)";
        try( PreparedStatement prep = connection.prepareStatement( sql )) {
            prep.setString( 1, token.getTokenId().toString() );
            prep.setString( 2, token.getUserId().toString() );
            prep.setTimestamp( 3, new Timestamp( token.getIat().getTime() ) );
            prep.setTimestamp( 4, new Timestamp( token.getExp().getTime() ) );
            prep.executeUpdate();
            return token;
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql, ex );
            return null;
        }
    }

    public boolean installTables() {
        String sql =
                "CREATE TABLE IF NOT EXISTS tokens (" +
                        "token_id  CHAR(36)  PRIMARY KEY  DEFAULT( UUID() )," +
                        "user_id   CHAR(36)  NOT NULL," +
                        "exp       DATETIME      NULL," +
                        "iat       DATETIME  NOT NULL   DEFAULT CURRENT_TIMESTAMP" +
                        ") ENGINE = InnoDB, DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci";

        try( Statement stmt = connection.createStatement() ) {
            stmt.executeUpdate( sql );
            return true;
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql, ex );
            return false;
        }
    }
}
