package dao;

import model.Course;
import model.Lesson;

import java.sql.*;
import java.util.*;

public class CourseRepositoryImpl implements CourseRepository {
    private Connection connection;
    LessonsRepositoryJdbcImpl lessonsRepositoryJdbc;

    private Map<Long, Course> map = new HashMap<>();

    private static final String SQL_UPDATE = "UPDATE project.course as c SET c.name = ? WHERE c.id = ?";
    private static final String SQL_DELETE = "DELETE FROM project.course WHERE id = ?";
    private static final String SQL_FIND_BY_ID = "SELECT c.id, c.name as course_name, l.name as lesson_name FROM project.course as c LEFT JOIN project.lesson l on l.course_id = c.id WHERE c.id = ?";
    private static final String SQL_FIND_BY_ALL = "SELECT c.id, c.name as course_name, l.name as lesson_name FROM project.course as c LEFT JOIN project.lesson l on l.course_id = c.id";
    private static final String SQL_ADD_COURSE = "INSERT INTO project.course(name) VALUES (?)";


    public CourseRepositoryImpl() {
        this.connection = DBConnector.getInstance().getConnection();
        lessonsRepositoryJdbc = new LessonsRepositoryJdbcImpl(connection);
    }

    public RowMapper<Course> mapRow = row -> {
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


    public Course create(Course model) {
        try (PreparedStatement stmt = connection.prepareStatement(SQL_ADD_COURSE, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, model.getName());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                for (Lesson lesson : model.getLessonList()) {
                    lessonsRepositoryJdbc.create(lesson);
                }
            }
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                model.setId(generatedKeys.getLong(1));
                return model;
            } else {
                throw new SQLException("Crating lesson failed");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Optional<Course> read(Long id) {
        try (PreparedStatement stmt = connection.prepareStatement(SQL_FIND_BY_ID)) {
            Optional<Course> result = Optional.empty();
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result = Optional.ofNullable(mapRow.mapRow(rs));
            }
            return result;
        } catch (
                SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void update(Course model) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setString(1, model.getName());
            statement.setLong(2, model.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        try (PreparedStatement stmt = connection.prepareStatement(SQL_DELETE)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Course> findAll() {
        try (PreparedStatement stmt = connection.prepareStatement(SQL_FIND_BY_ALL)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                mapRow.mapRow(rs);
            }
            return new ArrayList<>(map.values());
        } catch (
                SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
