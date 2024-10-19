package itstep.learning.dal.dto.shop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class Category {
    private UUID   id;
    private String name;
    private String description;
    private String imageUrl;
    private String slug;
    private Date   deleteDt;

    public Category() {}

    public Category( ResultSet resultSet ) throws SQLException {
        this.setId( UUID.fromString( resultSet.getString( "category_id" ) ) )
                .setName( resultSet.getString( "name" ) )
                .setSlug( resultSet.getString( "category_slug" ) )
                .setDescription( resultSet.getString( "description" ) )
                .setImageUrl( resultSet.getString( "image_url" ) );
        Timestamp timestamp = resultSet.getTimestamp( "delete_dt" );
        if( timestamp != null ) {
            this.setDeleteDt( new Date( timestamp.getTime() ) ) ;
        }
    }

    public String getSlug() {
        return slug;
    }

    public Category setSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public Date getDeleteDt() {
        return deleteDt;
    }

    public void setDeleteDt(Date deleteDt) {
        this.deleteDt = deleteDt;
    }

    public UUID getId() {
        return id;
    }

    public Category setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Category setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Category setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Category setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}
