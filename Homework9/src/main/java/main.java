import dao.CourseRepositoryImpl;
import dao.DBConnector;
import model.Course;
import model.Lesson;

import java.util.Optional;

public class main {
    public static void main(String[] args) {
//        System.out.println(DBConnector.getInstance().getConnection());
        CourseRepositoryImpl repository = new CourseRepositoryImpl();
        Optional<Course> course = repository.read(3);
        if (course.isPresent()) {
            for (Lesson lesson : course.get().getLessonList()) {
                System.out.println(lesson);
            }
        }
    }
}
