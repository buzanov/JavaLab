import java.util.UUID;

public class EmailSendTest {
    public static void main(String[] args) {
        System.out.println(generateRandomStringByUUID());
    }
    public static String generateRandomStringByUUID() {
        return UUID.randomUUID().toString();
    }
}
