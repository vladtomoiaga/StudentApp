package model;

public class Course {
    private String courseName;
    private String courseOwner;
    private int courseRoom;

    public Course(String courseName, String courseOwner, int courseRoom) {
        this.courseName = courseName;
        this.courseOwner = courseOwner;
        this.courseRoom = courseRoom;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseOwner() {
        return courseOwner;
    }

    public void setCourseOwner(String courseOwner) {
        this.courseOwner = courseOwner;
    }

    public int getCourseRoom() {
        return courseRoom;
    }

    public void setCourseRoom(int courseRoom) {
        this.courseRoom = courseRoom;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseName='" + courseName + '\'' +
                ", courseOwner='" + courseOwner + '\'' +
                ", courseRoom=" + courseRoom +
                '}';
    }
}
