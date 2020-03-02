package dao;

import model.Lesson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LessonsRepositoryJdbcImpl implements LessonsRepository {

    //language=SQL
    private static final String SQL_SELECT_BY_ID = "select * from lesson where id = ?";
    //language=SQL
    private static final String SQL_SELECT_ALL = "select * from lesson";
    //language=SQL
    private static final String SQL_INSERT = "insert into lesson(name) values (?)";

    private Connection connection;

    private RowMapper<Lesson> lessonRowMapper = row ->
            Lesson.builder()
                    .id(row.getLong("id"))
                    .name(row.getString("name"))
                    .build();


    public LessonsRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    public Optional<Lesson> read(Long id) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID);
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();
            Optional<Lesson> lessonResult = Optional.empty();
            if (result.next()) {
                Lesson lesson = lessonRowMapper.mapRow(result);
                lessonResult = Optional.of(lesson);
            }
            result.close();
            statement.close();
            return lessonResult;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void update(Lesson model) {

    }

    public List<Lesson> findAll() {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
            ResultSet result = statement.executeQuery();
            List<Lesson> lessons = new ArrayList<>();
            while (result.next()) {
                Lesson lesson = lessonRowMapper.mapRow(result);
                lessons.add(lesson);
            }
            result.close();
            statement.close();
            return lessons;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Lesson create(Lesson lesson) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, lesson.getName());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                lesson.setId(generatedKeys.getLong(1));
                return lesson;
            } else {
                throw new SQLException("Crating lesson failed");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }


    public void delete(Long id) {

    }

}
