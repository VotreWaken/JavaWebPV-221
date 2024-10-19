package itstep.learning.servlets.shop;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.shop.CategoryDao;
import itstep.learning.dal.dao.shop.ProductDao;
import itstep.learning.dal.dto.shop.Category;
import itstep.learning.dal.dto.shop.Product;
import itstep.learning.rest.*;
import itstep.learning.services.files.FileService;
import itstep.learning.services.formparse.FormParseResult;
import itstep.learning.services.formparse.FormParseService;
import org.apache.commons.fileupload.FileItem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Singleton
public class ProductServlet  extends RestServlet {
    private final FormParseService formParseService;
    private final FileService fileService;
    private final ProductDao productDao;
    private final CategoryDao categoryDao;

    @Inject
    public ProductServlet(FormParseService formParseService, FileService fileService, ProductDao productDao, CategoryDao categoryDao) {
        this.formParseService = formParseService;
        this.fileService = fileService;
        this.productDao = productDao;
        this.categoryDao = categoryDao;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.restResponse = new RestResponse().setMeta(
                new RestMetaData()
                        .setUri( "/shop/products" )
                        .setMethod( req.getMethod() )
                        .setLocale( "uk-UA" )
                        .setServerTime( new Date() )
                        .setName( "Shop Product API" )
                        .setAcceptMethods(new String[]{ "GET", "POST"})
        );
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getParameter( "id" );
        if( productId != null ) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", productId);
            super.restResponse.getMeta().setParams(params);
            getProductById( productId, req, resp );
            return;
        }

        String categoryId = req.getParameter( "categoryId" );
        if( categoryId != null ) {
            Map<String, Object> params = new HashMap<>();
            params.put("categoryId", categoryId);
            super.restResponse.getMeta().setParams(params);
            getProductsByCategoryId(categoryId, req, resp);
            return;
        }

        super.sendRest( 400, "Missing one of the required parameters: 'id' or 'categoryId'" );
    }

    private void getProductsByCategoryId( String categoryId, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Category category = categoryDao.getCategoryByIdOrSlug( categoryId );
        if( category == null ) {
            super.sendRest( 404, "Invalid category id: " + categoryId );
            return;
        }
        super.sendRest( 200, productDao.allFromCategory( category.getId() ) );
    }

    private void getProductById( String id, HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
        Product product = productDao.getProductByIdOrSlug( id );
        if( product != null ) {
            super.sendRest( 200, product ) ;
        }
        else {
            super.sendRest( 404, "Product not found: " + id );
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if( req.getAttribute( "Claim.Sid" ) == null ) {
            super.sendRest( 401, "Unauthorized. Token empty or rejected" );
            return;
        }
        try {
            Product product = getModelFromRequest( req ) ;
            product = productDao.add( product );
            if( product == null ) {
                super.sendRest( 500, "Server Error" );
            }
            else {
                super.sendRest( 200, product );
            }
        }
        catch( Exception ex ) {
            super.sendRest( 422, ex.getMessage() );
            // return;
        }
    }

    private Product getModelFromRequest( HttpServletRequest req ) throws Exception {
        Product product = new Product();
        FormParseResult formParseResult = formParseService.parse( req );

        String slug = formParseResult.getFields().get( "product-slug" );
        if( slug != null && ! slug.isEmpty() ) {
            slug = slug.trim();
            if( slug.isEmpty() ||
                    ! productDao.isSlugFree( slug ) ) {
                throw new Exception( "Slug '" + slug + "' is empty or not free" );
            }
            product.setSlug( slug );
        }

        String categoryId = formParseResult.getFields().get( "product-category-id" );
        Category category = categoryDao.getCategoryByIdOrSlug( categoryId );
        if( category == null ) {
            throw new Exception( "Missing or empty or incorrect required field: 'product-category-id'" );
        }
        product.setCategoryId( category.getId() );

        try {
            product.setPrice(
                    Double.parseDouble(
                            formParseResult.getFields().get( "product-price" )
                    )
            );
        }
        catch( Exception ignored ) {
            throw new Exception( "Missing or empty or incorrect required field: 'product-price'" );
        }

        product.setName( formParseResult.getFields().get( "product-name" ) );
        if( product.getName() == null || product.getName().isEmpty() ) {
            throw new Exception( "Missing or empty required field: 'product-name'" );
        }
        product.setDescription( formParseResult.getFields().get( "product-description" ) );
        if( product.getDescription() == null || product.getDescription().isEmpty() ) {
            throw new Exception( "Missing or empty required field: 'product-description'" );
        }

        FileItem avatar = formParseResult.getFiles().get( "product-img" );
        if( avatar.getSize() > 0 ) {
            int dotPosition = avatar.getName().lastIndexOf(".");
            if( dotPosition == -1 ) {
                throw new Exception( "Rejected file without extension: 'product-img'" );
            }
            String ext = avatar.getName().substring( dotPosition );
            String[] extensions = { ".jpg", ".jpeg", ".png", ".svg", ".bmp" };
            if( Arrays.stream(extensions).noneMatch( (e) -> e.equals(ext) ) ) {
                throw new Exception( "Rejected file with non-image extension: 'product-img'" );
            }
            product.setImageUrl( fileService.upload( avatar ) );
        }
        else {
            throw new Exception( "Missing or empty required file: 'product-img'" );
        }
        return product;
    }
}
