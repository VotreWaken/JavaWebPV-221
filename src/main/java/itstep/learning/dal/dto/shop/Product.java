package itstep.learning.dal.dto.shop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class Product {
    private UUID id;
    private UUID categoryId;
    private String name;
    private String slug;
    private double price;
    private String description;
    private String imageUrl;
    private Date deleteDt;

    public Product() {
    }

    public Product( ResultSet rs ) throws SQLException {
        this.setId( UUID.fromString( rs.getString( "product_id" ) ) ) ;
        this.setCategoryId( UUID.fromString( rs.getString( "category_id" ) ) ) ;
        this.setName( rs.getString( "product_name" ) ) ;
        this.setSlug( rs.getString( "product_slug" ) ) ;
        this.setPrice( rs.getDouble( "product_price" ) ) ;
        this.setDescription( rs.getString( "product_description" ) ) ;
        this.setImageUrl( rs.getString( "product_image_url" ) ) ;
        Timestamp timestamp = rs.getTimestamp( "delete_dt" );
        if ( timestamp != null ) {
            this.setDeleteDt( new Date( timestamp.getTime() ) );
        }
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

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
