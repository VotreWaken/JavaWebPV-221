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

    public Timestamp getTimeUntilTokenExpirationUpdate(Token token) {
        long currentTimeMillis = System.currentTimeMillis();
        long remainingTimeMillis = token.getExp().getTime() - currentTimeMillis;

        // Если осталось меньше 10 минут, токен можно обновить
        if (remainingTimeMillis > 0 && remainingTimeMillis <= 10 * 60 * 1000) {
            long typicalTokenDurationMillis = 1000 * 60 * 60 * 3; // 3 часа
            long additionalTimeMillis = typicalTokenDurationMillis / 2;

            // Новый Timestamp с учетом времени обновления токена
            long newExpirationTimeMillis = currentTimeMillis + additionalTimeMillis;
            return new Timestamp(newExpirationTimeMillis);
        }

        // Если обновление не требуется, возвращаем текущий Timestamp
        return new Timestamp(currentTimeMillis);
    }

    public Token getNotExpiredTokenByUserId(UUID userId) throws SQLException {
        Token token = new Token();
        String sql = "SELECT * FROM tokens WHERE user_id = ? AND exp > ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userId.toString());
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Извлекаем данные токена
                token.setTokenId(UUID.fromString(rs.getString("token_id")));
                token.setUserId(UUID.fromString(rs.getString("user_id")));
                token.setIat(rs.getTimestamp("iat"));
                token.setExp(rs.getTimestamp("exp"));

                long currentTimeMillis = System.currentTimeMillis();
                long remainingTimeMillis = token.getExp().getTime() - currentTimeMillis;

                if (remainingTimeMillis > 0) {
                    long typicalTokenDurationMillis = 1000 * 60 * 60 * 3;
                    long additionalTimeMillis = typicalTokenDurationMillis / 2;

                    token.setExp(new Timestamp(currentTimeMillis + additionalTimeMillis));

                    String updateSql = "UPDATE tokens SET exp = ? WHERE token_id = ?";
                    try (PreparedStatement updatePs = connection.prepareStatement(updateSql)) {
                        updatePs.setTimestamp(1, new Timestamp(token.getExp().getTime()));
                        updatePs.setString(2, token.getTokenId().toString());
                        updatePs.executeUpdate();
                    }
                    return token;
                }
            }
        } catch (SQLException e) {
            // Логирование и обработка ошибки
            e.printStackTrace();
            throw e;
        }
        return null; // Если нет активного токена
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
