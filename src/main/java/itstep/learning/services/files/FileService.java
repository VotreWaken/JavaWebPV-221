package itstep.learning.services.files;

import org.apache.commons.fileupload.FileItem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileService {
    String upload( FileItem fileItem );
    InputStream download( String fileName ) throws IOException;
}
