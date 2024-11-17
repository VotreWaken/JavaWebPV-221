package itstep.learning.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import itstep.learning.services.stream.StringReader;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DbModule extends AbstractModule {
    private final StringReader stringReader;
    private Connection connection = null;
    private Driver mysqlDriver = null;

    public DbModule( StringReader stringReader ) {
        this.stringReader = stringReader;
    }

    @Provides
    private Connection getConnection() {
        if(connection == null) {
            Map<String, String> ini = new HashMap<>();
            try( InputStream rs = this
                                 .getClass()
                                 .getClassLoader()
                                 .getResourceAsStream("db.ini")
            ) {
                String[] lines = stringReader.read( rs ).split("\n");
                for(String line : lines) {
                    String[] parts = line.split("=");
                    ini.put( parts[0].trim(), parts[1].trim() );
                }
            }
            catch (IOException ex) {
                System.err.println( ex.getMessage() );
            }

            try {
                mysqlDriver = new com.mysql.cj.jdbc.Driver();
                DriverManager.registerDriver( mysqlDriver );
                connection = DriverManager.getConnection(
                        String.format( "jdbc:%s://%s:%s/%s?useUnicode=true&characterEncoding=utf8",
                                ini.get("dbms"),
                                ini.get("host"),
                                ini.get("port"),
                                ini.get("schema")
                        ),
                        ini.get("user"),
                        ini.get("password")
                );
            }
            catch( SQLException ex ) {
                System.err.println( "DbModule::getConnection " + ex.getMessage() );
            }
        }
        return connection;
    }
}
