package itstep.learning.dal.dto.shop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CartItem {
    private UUID cartId;
    private UUID productId;
    private int quantity;
    // private double price;

    private Product product;

    public CartItem() {
    }

    public CartItem( ResultSet rs ) throws SQLException {
        this.setCartId( UUID.fromString( rs.getString("cart_id") ) ) ;
        this.setProductId( UUID.fromString( rs.getString("product_id") ) ) ;
        this.setQuantity( rs.getInt( "cnt" ) ) ;

        try {
            product = new Product( rs );
        }
        catch( Exception ignored ) {  }
    }


    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
