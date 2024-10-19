package itstep.learning.services.stream;

import java.io.IOException;
import java.io.InputStream;

public interface StringReader {
    String read(InputStream inputStream) throws IOException;
}
