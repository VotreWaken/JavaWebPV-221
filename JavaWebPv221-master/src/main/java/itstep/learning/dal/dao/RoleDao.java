package itstep.learning.dal.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dto.Role;
import itstep.learning.dal.dto.User;

import java.sql.*;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class RoleDao {
    private final Connection connection;
    private final Logger logger;

    @Inject
    public RoleDao(Connection connection, Logger logger) {
        this.connection = connection;
        this.logger = logger;
    }
    public Role getById(UUID id){
        String sql = String.format(Locale.ROOT,"SELECT * FROM roles WHERE role_id = '%s'", id.toString());
        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()){
                return new Role(resultSet);
            }
        }catch (SQLException ex){
            logger.log(Level.WARNING, ex.getMessage(), ex);
        }
        return null;
    }

    public boolean hasRole(UUID userId, String roleName) {
        String sql = "SELECT 1 FROM user_roles ur " +
                "JOIN roles r ON ur.role_id = r.role_id " +
                "WHERE ur.user_id = ? AND r.role_name = ? LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId.toString());
            stmt.setString(2, roleName);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Если роль найдена, возвращаем true
            }
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "Error checking user role: " + ex.getMessage(), ex);
            return false;
        }
    }

    public Role add(Role role) {
        String sql = "INSERT INTO roles(role_id,role_name,can_create,can_edit,can_read,can_delete,can_ban,can_block) VALUES(?,?,?,?,?,?,?,?)";
        try(PreparedStatement prop = connection.prepareStatement(sql)) {
            prop.setString(1, role.getId().toString());
            prop.setString(2, role.getRoleName());
            prop.setBoolean(3, role.isCanCreate());
            prop.setBoolean(4, role.isCanEdit());
            prop.setBoolean(5, role.isCanRead());
            prop.setBoolean(6, role.isCanDelete());
            prop.setBoolean(7, role.isCanBan());
            prop.setBoolean(8, role.isCanBlock());
            prop.executeUpdate();
            return role;
        }catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage() + " -- " + sql, ex);
        }

        return null;
    }

    public boolean installTables() {
        String sql = "CREATE TABLE IF NOT EXISTS roles (" +
                "role_id CHAR(36) PRIMARY KEY DEFAULT(UUID())," +
                "role_name VARCHAR(64) NOT NULL," +
                "can_create BOOLEAN," +
                "can_edit BOOLEAN," +
                "can_read BOOLEAN," +
                "can_delete BOOLEAN," +
                "can_ban BOOLEAN," +
                "can_block BOOLEAN"+
                ") ENGINE=InnoDB, DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            return true;

        } catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
            return false;
        }
    }
}
