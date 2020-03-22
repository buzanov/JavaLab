import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.itis.servlets.config.ApplicationContextConfig;
import ru.itis.servlets.models.FileInfo;
import ru.itis.servlets.repositories.FileRepository;

public class HibernateTest {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationContextConfig.class);
        FileRepository fileRepository = (FileRepository) context.getBean("fileRepository");
        FileInfo fileInfo = FileInfo.builder()
                .type("afsasfa")
                .originalFileName("asdsad")
                .storageFileName("asfasgfasfasfas")
                .url("afsafs")
                .extension(".ffs")
                .build();
        System.out.println(fileInfo.getStorageFileName());
        System.out.println(fileInfo.getExtension());
        FileInfo f = fileRepository.findById(1L).get();
        if (f == null) {
            System.out.println("null");
        } else {
            System.out.println(f.getId());
        }
        fileRepository.saveAndFlush(fileInfo);

    }
}
