package dao;

import model.Course;
import model.Lesson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CourseRepositoryImpl implements CourseRepository {
    private Connection connection;
    private Map<Integer, Course> map = new HashMap<>();

    private static final String SQL_UPDATE = "UPDATE project.course as c SET c.name = ? WHERE c.id = ?";
    private static final String SQL_DELETE = "DELETE FROM project.course WHERE id = ?";
    private static final String SQL_FIND_BY_ID = "SELECT c.id, c.name as course_name, l.name as lesson_name FROM project.course as c LEFT JOIN project.lesson l on l.course_id = c.id WHERE c.id = ?";
    private static final String SQL_FIND_BY_ALL = "SELECT c.id, c.name as course_name, l.name as lesson_name FROM project.course as c LEFT JOIN project.lesson l on l.course_id = c.id";
    private static final String SQL_ADD_COURSE = "INSERT INTO project.course(name) VALUES (?) RETURNING id";
    private static final String SQL_ADD_LESSON = "INSERT INTO project.lesson(name, course_id) VALUES (?,?)";


    public CourseRepositoryImpl() {
        this.connection = DBConnector.getInstance().getConnection();
    }

    public RowMapper<Course> mapRow = row -> {
        try {
            int id = row.getInt("id");
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


    public void create(Course model) {
        try (PreparedStatement stmt = connection.prepareStatement(SQL_ADD_COURSE)) {
            stmt.setString(1, model.getName());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                //Лучше приготовить statement и несколько раз выполгить или открывать и закрывать по очереди?
                for (Lesson lesson : model.getLessonList()) {
                    try (PreparedStatement statement = connection.prepareStatement(SQL_ADD_LESSON)) {
                        statement.setString(1, lesson.getName());
                        statement.setInt(2, id);
                        statement.executeUpdate();
                    }
                }
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
