package itstep.learning.dal.dao.shop;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dto.shop.Category;
import itstep.learning.dal.dto.shop.Product;
import itstep.learning.models.form.ShopCategoryFormModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class CategoryDao {
    private final Connection connection;
    private final Logger logger;

    @Inject
    public CategoryDao( Connection connection, Logger logger ) {
        this.connection = connection;
        this.logger = logger;
    }

    public List<Category> all() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories c WHERE c.delete_dt IS NULL";
        try( Statement statement = connection.createStatement() ) {
            ResultSet resultSet = statement.executeQuery( sql ) ;
            while( resultSet.next() ) {
                categories.add( new Category( resultSet ) ) ;
            }
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql, ex );
        }
        return categories ;
    }

    public Category getCategoryByIdOrSlug( String id ) {
        if( id == null || id.isEmpty() ) {
            return null;
        }
        String sql = "SELECT * FROM categories WHERE ";
        try {
            UUID.fromString( id );
            // шукаємо як id
            sql += "category_id = ?";
        }
        catch( IllegalArgumentException ignored ) {
            // шукаємо як slug
            sql += "category_slug = ?";
        }
        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString( 1, id );
            ResultSet rs = prep.executeQuery();
            if( rs.next() ) {
                return new Category( rs ) ;
            }
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql, ex );
        }
        return null;
    }

    public boolean isSlugFree( String slug ) {
        String sql = "SELECT COUNT(*) FROM categories c WHERE c.category_slug = ?";
        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString( 1, slug );
            ResultSet resultSet = prep.executeQuery();
            if( resultSet.next() ) {
                return resultSet.getInt( 1 ) == 0;
            }
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql, ex );
        }
        return false;
    }

    public Category add( ShopCategoryFormModel formModel ) {
        Category category = new Category()
                .setId( UUID.randomUUID() )
                .setName( formModel.getName() )
                .setSlug( formModel.getSlug() )
                .setDescription( formModel.getDescription() )
                .setImageUrl( formModel.getSavedFilename() );
        String sql = "INSERT INTO categories (category_id, name, description, image_url, category_slug) " +
                " VALUES (?, ?, ?, ?, ?)";
        try ( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString( 1, category.getId().toString() );
            prep.setString( 2, category.getName() );
            prep.setString( 3, category.getDescription() );
            prep.setString( 4, category.getImageUrl() );
            prep.setString( 5, category.getSlug() );
            prep.executeUpdate();
            return category;
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() + " -- " + sql, ex );
            return null;
        }
    }

    public boolean installTables() {
        String sql =
                "CREATE TABLE IF NOT EXISTS categories (" +
                        "category_id    CHAR(36)      PRIMARY KEY  DEFAULT( UUID() )," +
                        "name           VARCHAR(128)  NOT NULL," +
                        "image_url      VARCHAR(512)  NOT NULL," +
                        "description    TEXT              NULL," +
                        "delete_dt      DATETIME          NULL," +
                        "category_slug  VARCHAR(64)       NULL," +
                        "UNIQUE (category_slug)" +
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
