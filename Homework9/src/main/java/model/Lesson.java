package model;

public class Lesson {
    String name;
    Course course;

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "name='" + name + '\'' +
                ", course=" + course.getName() +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Lesson(String name, Course course) {
        this.name = name;
        this.course = course;
        //this.course.getLessonList().add(this);
    }
}
