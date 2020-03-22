import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonBuilderUtils;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

public class MediaTypeTest {
    public static void main(String[] args) throws MimeTypeParseException {

        MediaType mediaType = MediaType.parseMediaType("image/jpeg");
        System.out.println( mediaType);

    }


}
