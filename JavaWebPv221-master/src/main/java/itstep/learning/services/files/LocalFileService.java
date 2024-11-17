package itstep.learning.services.files;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.services.stream.StringReader;
import org.apache.commons.fileupload.FileItem;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class LocalFileService implements FileService {
    private final String uploadPath;

    @Inject
    public LocalFileService(StringReader stringReader) {
        Map<String, String> ini = new HashMap<>();
        try( InputStream rs = this
                .getClass()
                .getClassLoader()
                .getResourceAsStream("files.ini")
        ) {
            String[] lines = stringReader.read( rs ).split("\n");
            for(String line : lines) {
                String[] parts = line.split("=");
                ini.put( parts[0].trim(), parts[1].trim() );
            }
        }
        catch (IOException ex) {
            System.err.println( ex.getMessage() );
            throw new RuntimeException( ex );
        }
        this.uploadPath = ini.get( "upload_path" );
    }

    @Override
    public String upload( FileItem fileItem ) {
        String formFilename = fileItem.getName();
        // відокремлюємо розширення
        int dotPosition = formFilename.lastIndexOf( '.' );
        String extension = formFilename.substring( dotPosition );
        // генеруємо ім'я зберігаючи розширення
        String filename;
        File file;
        do {
            filename = UUID.randomUUID() + extension;
            file = new File( this.uploadPath, filename );
        } while( file.exists() );  // гарантуємо новизну імені файлу

        try {
            fileItem.write( file );
        }
        catch( Exception ex ) {
            System.err.println( ex.getMessage() );
            return null;
        }

        return filename;
    }

    @Override
    public InputStream download( String fileName ) throws IOException {
        File file = new File( this.uploadPath, fileName );
        if( file.isFile() && file.canRead() ) {
            return Files.newInputStream( file.toPath() );
        }
        return null;
    }
}
/*
Д.З. Забезпечити валідацію файлів, що завантажуються для аватарки,
на предмет дозволених розширень (взяти перелік для зображень).
У випадку відхилень відмовляти у реєстрації
 */