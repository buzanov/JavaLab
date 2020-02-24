package dao;

import model.Course;

import java.util.List;

public interface CourseRepository extends CrudRepository<Course, Long> {
    List<Course> findAll();
}
