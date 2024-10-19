package itstep.learning.dal.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import itstep.learning.dal.dto.User;
import itstep.learning.models.form.UserSignupFormModel;
import itstep.learning.services.hash.HashService;

import java.sql.*;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class UserDao {
    private final Connection connection;
    private final Logger logger;
    private final HashService hashService;

    @Inject
    public UserDao(Connection connection, Logger logger, @Named("digest") HashService hashService) {
        this.connection = connection;
        this.logger = logger;
        this.hashService = hashService;
    }

    public User getUserById( UUID id ) {
        String sql = String.format(
                Locale.ROOT,
                "SELECT * FROM users WHERE id = '%s'",
                id.toString());
        try( Statement statement = connection.createStatement() ) {
            ResultSet resultSet = statement.executeQuery( sql );
            if( resultSet.next() ) {
                return new User( resultSet );
            }
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql, ex );
        }
        return null;
    }

    public User authenticate( String login, String password ) {
        String sql = "SELECT * FROM users JOIN users_security " +
                " ON users.id = users_security.user_id " +
                " WHERE users_security.login = ? ";

        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString( 1, login );
            ResultSet res = prep.executeQuery();
            if( res.next() ) {
                String salt = res.getString("salt");
                String dk = res.getString("dk");
                if( hashService.digest( salt + password ).equals( dk ) ) {
                    return new User( res );
                }
            }
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql, ex );
        }
        return null;
    }

    public User signup( UserSignupFormModel model ) {
        if( model == null ) {
            return null;
        }
        User user = new User();

        user.setName( model.getName() );
        user.setEmail( model.getEmail() );
        user.setAvatar( model.getAvatar() );
        user.setBirthdate( model.getBirthdate() );
        user.setSignupDt( new Date() );
        user.setId( UUID.randomUUID() );

        String sql = "INSERT INTO users(id,name,email,avatar,birthdate,signup_dt)" +
                "VALUES(?,?,?,?,?,?)";
        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString( 1, user.getId().toString() );
            prep.setString( 2, user.getName() );
            prep.setString( 3, user.getEmail() );
            prep.setString( 4, user.getAvatar() );
            prep.setTimestamp( 5, user.getBirthdate() == null ? null :
                    new Timestamp( user.getBirthdate().getTime() ) );
            prep.setTimestamp( 6, new Timestamp( user.getSignupDt().getTime() ) );

            prep.executeUpdate();
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql, ex );
            return null;
        }

        sql = "INSERT INTO users_security(user_id, login, salt, dk) VALUES(?,?,?,?)";
        String salt = hashService.digest(
                UUID.randomUUID().toString()
        ).substring(0, 32);
        String dk = hashService.digest( salt + model.getPassword() );
        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString( 1, user.getId().toString() );
            prep.setString( 2, model.getEmail() );
            prep.setString( 3, salt );
            prep.setString( 4, dk );
            prep.executeUpdate();
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql, ex );
            return null;
        }
        return user;
    }

    public boolean installTables() {
        String sql =
            "CREATE TABLE IF NOT EXISTS users (" +
                "id        CHAR(36)     PRIMARY KEY  DEFAULT( UUID() )," +
                "name      VARCHAR(128) NOT NULL," +
                "email     VARCHAR(128) NOT NULL," +
                "avatar    VARCHAR(128)     NULL," +
                "birthdate DATETIME         NULL," +
                "signup_dt DATETIME     NOT NULL   DEFAULT CURRENT_TIMESTAMP," +
                "delete_dt DATETIME         NULL" +
            ") ENGINE = InnoDB, DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci";

        try( Statement stmt = connection.createStatement() ) {
            stmt.executeUpdate( sql );
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql, ex );
            return false;
        }
        sql =
            "CREATE TABLE IF NOT EXISTS users_security (" +
                "id        CHAR(36)     PRIMARY KEY  DEFAULT( UUID() )," +
                "user_id   CHAR(36)     NOT NULL," +
                "login     VARCHAR(64)  NOT NULL," +
                "salt      CHAR(32)     NOT NULL," +
                "dk        CHAR(32)     NOT NULL," +
                "role_id   CHAR(36)         NULL" +
            ") ENGINE = InnoDB, DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci";
        try( Statement stmt = connection.createStatement() ) {
            stmt.executeUpdate( sql );
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql, ex );
            return false;
        }
        return true;
    }
}
