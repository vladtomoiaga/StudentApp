package database;

public interface DataBaseActions {

    void printActions();
    void action();
    void createStudentTable();
    void insertStudent();
    void updateStudent();
    void deleteStudent();
    void findStudentByID();
    void findStudentByFirstName();
    void findStudentByLastName();
    void createCourseTable();
    void insertCourse();
    void updateCourse();
    void deleteCourse();
    void findCourseByID();
    void findCourseByName();
    void findCourseByOwner();
    void findCourseByRoom();
    void selectOneTable();

}
