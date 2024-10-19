package itstep.learning.services.formparse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class MixedFormParseService implements FormParseService {
    private static final int MEMORY_THRESHOLD = 10 * 1024 * 1024 ;  // 10 MB - file in memory
    private static final int MAX_FILE_SIZE    = 10 * 1024 * 1024 ;  // максимальний розмір одного файлу
    private static final int MAX_FORM_SIZE    = 20 * 1024 * 1024 ;  // макс. розмір всіх даних від форми

    private final ServletFileUpload fileUpload ;

    @Inject
    public MixedFormParseService() {
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory() ;
        fileItemFactory.setSizeThreshold( MEMORY_THRESHOLD ) ;
        fileItemFactory.setRepository( new File( System.getProperty( "java.io.tmpdir" ) ) ) ;

        fileUpload = new ServletFileUpload( fileItemFactory ) ;
        fileUpload.setFileSizeMax( MAX_FILE_SIZE ) ;
        fileUpload.setSizeMax( MAX_FORM_SIZE ) ;
    }

    @Override
    public FormParseResult parse( HttpServletRequest request ) {
        final Map<String, String> fields = new HashMap<>() ;
        final Map<String, FileItem> files = new HashMap<>() ;

        boolean isMultipart = request.getHeader("Content-Type") != null &&
                request.getHeader("Content-Type").startsWith("multipart/form-data");

        String charset = (String) request.getAttribute( "charset" ) ;
        if( charset == null ) {
            charset = StandardCharsets.UTF_8.name() ;
        }

        if( isMultipart ) {   // apache.upload
            try {
                for( FileItem item : fileUpload.parseRequest( request ) ) {
                    if( item.isFormField() ) {
                        fields.put( item.getFieldName(), item.getString(charset) ) ;
                    }
                    else {
                        files.put( item.getFieldName(), item ) ;
                    }
                }
            }
            catch( Exception ex ) {
                throw new RuntimeException( ex ) ;
            }
        }
        else {   // javax.servlet API
            for( Map.Entry<String, String[]> entry :
                    request.getParameterMap().entrySet() ) {
                fields.put(
                        entry.getKey(),
                        entry.getValue()[0]
                );
            }
        }

        return new FormParseResult() {
            @Override public Map<String, String> getFields() { return fields; }
            @Override public Map<String, FileItem> getFiles() { return files; }
        };
    }
}

