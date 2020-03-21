package dao;

import model.Course;
import model.Lesson;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

public class CourseRepositoryTemplateIImpl implements CourseRepository {
    private JdbcTemplate template;
    private Map<Long, Course> map = new HashMap<>();

    private static final String SQL_UPDATE = "UPDATE project.course as c SET c.name = ? WHERE c.id = ?";
    private static final String SQL_DELETE = "DELETE FROM project.course WHERE id = ?";
    private static final String SQL_FIND_BY_ID = "SELECT c.id, c.name as course_name, l.name as lesson_name FROM project.course as c LEFT JOIN project.lesson l on l.course_id = c.id WHERE c.id = ?";
    private static final String SQL_FIND_ALL = "SELECT c.id, c.name as course_name, l.name as lesson_name FROM project.course as c LEFT JOIN project.lesson l on l.course_id = c.id";
    private static final String SQL_ADD_COURSE = "INSERT INTO project.course(name) VALUES (?)";
    private static final String SQL_ADD_LESSON = "INSERT INTO project.lesson(name, course_id) VALUES (?,?)";


    public CourseRepositoryTemplateIImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public RowMapper<Course> mapRow = (row, rowNumb) -> {
        try {
            Long id = row.getLong("id");
            Course course;
            if (!map.containsKey(id)) {
                course = new Course();
                course.setName(row.getString("course_name"));
                map.put(id, course);
            } else
                course = map.get(id);
            course.getLessonList().add(new Lesson(row.getString("lesson_name"), course));
            return course;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    };

    @Override
    public Course create(Course model) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(SQL_ADD_COURSE, model.getName(), keyHolder);
        model.setId(keyHolder.getKey().longValue());
        Long id = model.getId();
        for (Lesson lesson : model.getLessonList()) {
            template.update(SQL_ADD_LESSON, lesson.getName(), id, keyHolder);
            lesson.setId(keyHolder.getKey().longValue());
        }
        return model;
    }

    public Optional<Course> read(Long id) {
        template.query(SQL_FIND_BY_ID, mapRow, id);

        if (map.containsKey(id)) {
            return Optional.of(map.get(id));
        }
        return Optional.empty();
    }

    public void update(Course model) {
        template.update(SQL_UPDATE, model.getName(), model.getId());
    }

    public void delete(Long id)  {
        template.update(SQL_DELETE, id);
    }

    @Override
    public List<Course> findAll() {
        return template.query(SQL_FIND_ALL, mapRow);
    }
}
