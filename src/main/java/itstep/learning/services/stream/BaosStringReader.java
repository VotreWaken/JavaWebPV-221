package itstep.learning.services.stream;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class BaosStringReader implements StringReader {
    @Override
    public String read( InputStream inputStream ) throws IOException {
        byte[] buffer = new byte[4096];
        try(
                ByteArrayOutputStream byteBuilder = new ByteArrayOutputStream();
                BufferedInputStream bis = new BufferedInputStream( inputStream )
        ) {
            int len;
            while( (len = bis.read(buffer) ) != -1 ) {
                byteBuilder.write( buffer, 0, len );
            }
            return byteBuilder.toString( StandardCharsets.UTF_8.name() );
        }
    }
}
