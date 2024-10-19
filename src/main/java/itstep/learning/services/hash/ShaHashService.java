package itstep.learning.services.hash;

import com.google.inject.Singleton;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Singleton
public class ShaHashService  implements HashService {
    @Override
    public String digest(String message) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update( message.getBytes( StandardCharsets.UTF_8 ) );
            for( byte b : md.digest() ) {
                sb.append( String.format("%02x", b) );
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
