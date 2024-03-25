package model;

public class Student {

    private String firstName;
    private String lastName;
    private int favoriteIDCourse;

    public Student(String firstName, String lastName, int favoriteIDCourse) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.favoriteIDCourse = favoriteIDCourse;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getFavoriteIDCourse() {
        return favoriteIDCourse;
    }

    public void setFavoriteIDCourse(int favoriteIDCourse) {
        this.favoriteIDCourse = favoriteIDCourse;
    }

    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", favoriteIDCourse='" + favoriteIDCourse + '\'' +
                '}';
    }
}
