package model;

import java.util.ArrayList;
import java.util.List;

public class Course {
    Long id;
    String name;
    List<Lesson> lessonList;

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lessonList=" + lessonList +
                '}';
    }

    public Course() {
        lessonList = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course(String name) {
        this.name = name;
        lessonList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Lesson> getLessonList() {
        return lessonList;
    }

    public void setLessonList(List<Lesson> lessonList) {
        this.lessonList = lessonList;
    }
}
