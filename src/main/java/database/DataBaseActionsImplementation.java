package database;

import java.sql.*;
import java.util.Scanner;

public class DataBaseActionsImplementation implements DataBaseActions {
    Scanner scanner = new Scanner(System.in);

    // Data for student table
    String studentTableName;
    int idStudent;
    String firstName;
    String lastName;
    int favoriteIDCourse;

    // Data for course table
    String courseTableName;
    int idCourse;
    String courseName;
    String courseOwner;
    int courseRoom;


    @Override
    public void printActions() {

        System.out.println("\nAvailable actions:\n");
        System.out.println("0  - to shutdown\n" +
                "1  - insert a new course\n" +
                "2  - update an existing course\n" +
                "3  - delete an existing course\n" +
                "4  - find an existing course by id\n" +
                "5  - find an existing course by name\n" +
                "6  - find an existing course by course owner\n" +
                "7  - find an existing course by course room\n" +
                "8  - insert a new student\n" +
                "9  - update an existing student\n" +
                "10 - delete an existing student\n" +
                "11 - find an existing student by id\n" +
                "12 - find an existing student by first name\n" +
                "13 - find an existing student by last name\n" +
                "14 - print a list of available actions.");
        System.out.println("Info: First create the course table and after that the student table.");
        System.out.println();
        System.out.println("Choose your action: ");

    }

    @Override
    public void action() {

        boolean flag = true;
        while (flag) {

            System.out.println("\nEnter action:");

            try {
                int action = scanner.nextInt();
                scanner.nextLine();

                switch (action) {
                    case 0:
                        System.out.println("\nApp is closing...");
                        flag = false;
                        break;

                    case 1:
                        insertCourse();
                        break;

                    case 2:
                        updateCourse();
                        break;

                    case 3:
                        deleteCourse();
                        break;

                    case 4:
                        findCourseByID();
                        break;

                    case 5:
                        findCourseByName();
                        break;

                    case 6:
                        findCourseByOwner();
                        break;

                    case 7:
                        findCourseByRoom();
                        break;

                    case 8:
                        insertStudent();
                        break;

                    case 9:
                        updateStudent();
                        break;

                    case 10:
                        deleteStudent();
                        break;

                    case 11:
                        findStudentByID();
                        break;

                    case 12:
                        findStudentByFirstName();
                        break;

                    case 13:
                        findStudentByLastName();
                        break;

                    case 14:
                    default:
                        printActions();
                        break;
                }

            } catch (Exception e) {
                System.out.println("Error when typing the action: " + e.getMessage());
                flag = false;
            }
        }

    }


    /** Create the student table */
    @Override
    public void createStudentTable() {

        try {
            Connection connection = DriverManager.getConnection(DataSourceName.URL, DataSourceName.USER, DataSourceName.PASSWORD);
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            System.out.println("Enter the student table name:");
            studentTableName = scanner.nextLine().replaceAll(" ", "_").toLowerCase();

            // Create student table
            statement.execute("CREATE TABLE IF NOT EXISTS " + studentTableName + " " +
                            "(" + DataSourceName.ID_STUDENT +  " SERIAL PRIMARY KEY NOT NULL," +
                                  DataSourceName.STUDENT_FIRST_NAME + " TEXT, " +
                                  DataSourceName.STUDENT_LAST_NAME + " TEXT, " +
                                  DataSourceName.STUDENT_FAVORITE_ID_COURSE + " INT, " +
                            "CONSTRAINT fk_favorite_course " +
                            "FOREIGN KEY " + "(" + DataSourceName.STUDENT_FAVORITE_ID_COURSE + ")" +
                            "REFERENCES " + DataSourceName.COURSE_TABLE + "(" + DataSourceName.ID_COURSE + "))");

            connection.commit();
            statement.close();
            connection.close();

            System.out.println(studentTableName + " table was created.");

        } catch (SQLException e) {
            System.out.println("Error message: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    /** Insert data in the student table */
    @Override
    public void insertStudent() {

        try {
            Connection connection = DriverManager.getConnection(DataSourceName.URL, DataSourceName.USER, DataSourceName.PASSWORD);
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            System.out.println("Do you want to add a new student?");
            System.out.println("Enter first name:");
            firstName = scanner.nextLine();

            System.out.println("Enter last name:");
            lastName = scanner.nextLine();

            System.out.println("Enter favorite course:");

            // Verify if the user type from console a String and then convert to an Int
            if (scanner.hasNextLine()) {
                String idCourseString = scanner.nextLine();

                try {
                    //Convert the input to an Int
                    favoriteIDCourse = Integer.parseInt(idCourseString);

                    // Find the favorite idcourse
                    ResultSet rs = statement.executeQuery("SELECT * " +
                            "FROM " + DataSourceName.COURSE_TABLE + " " +
                            "WHERE " + DataSourceName.ID_COURSE + "=" + favoriteIDCourse);

                    if (!rs.isBeforeFirst()) {
                        System.out.println("Favorite idcourse " + favoriteIDCourse + " does not exist.");

                    } else {
                        // Insert student in the student table
                        statement.executeUpdate("INSERT INTO " + DataSourceName.STUDENT_TABLE +
                                " (" + DataSourceName.STUDENT_FIRST_NAME + ", " +
                                       DataSourceName.STUDENT_LAST_NAME + ", " +
                                       DataSourceName.STUDENT_FAVORITE_ID_COURSE + ")" +
                                "VALUES('" + firstName + "','" +
                                             lastName + "','" +
                                             favoriteIDCourse + "')");

                        connection.commit();
                        statement.close();
                        connection.close();

                        System.out.println("Student " + firstName + " " + lastName + " was inserted.");

                    }

                } catch (NumberFormatException ex) {
                    System.out.println("Invalid input for idcourse: " + ex.getMessage());
                }

            }

        } catch (SQLException e){
                System.out.println("Error message: " + e.getMessage());
                throw new RuntimeException(e);
        }

    }

    /** Update data in the student table */
    @Override
    public void updateStudent() {

        try {
            Connection connection = DriverManager.getConnection(DataSourceName.URL, DataSourceName.USER, DataSourceName.PASSWORD);
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            System.out.println("Enter the idstudent which you need to update:");

            // Verify if the user type from console a String and then convert to an Int
            if (scanner.hasNextLine()) {
                String idStudentString = scanner.nextLine();

                try {
                    // Convert the String to an Int
                    idStudent = Integer.parseInt(idStudentString);

                    // Find the idstudent
                    ResultSet rs = statement.executeQuery("SELECT * " +
                            "FROM " + DataSourceName.STUDENT_TABLE + " " +
                            "WHERE " + DataSourceName.ID_STUDENT + "=" + idStudent);

                    if (!rs.isBeforeFirst()) {
                        System.out.println("Student with idstudent " + idStudent + " does not exist.");

                    } else {

                        System.out.println("Enter the new first name: ");
                        firstName = scanner.nextLine();

                        System.out.println("Enter the new last name: ");
                        lastName = scanner.nextLine();

                        System.out.println("Enter the new favorite idcourse: ");

                        // Verify if the user type from console a String and then convert to an Int
                        if (scanner.hasNextLine()) {
                            String idCourseString = scanner.nextLine();

                            try {
                                //Convert the input to an Int
                                favoriteIDCourse = Integer.parseInt(idCourseString);

                                // Find the favorite idcourse
                                ResultSet rsIDCourse = statement.executeQuery("SELECT * " +
                                        "FROM " + DataSourceName.COURSE_TABLE + " " +
                                        "WHERE " + DataSourceName.ID_COURSE + "=" + favoriteIDCourse);

                                if (!rsIDCourse.isBeforeFirst()) {
                                    System.out.println("Favorite idcourse " + favoriteIDCourse + " does not exist.");
                                } else {

                                    // Update student
                                    statement.executeUpdate("UPDATE " + DataSourceName.STUDENT_TABLE + " " +
                                            "SET " + DataSourceName.STUDENT_FIRST_NAME + "='" + firstName + "', " +
                                                     DataSourceName.STUDENT_LAST_NAME + "='" + lastName + "', " +
                                                     DataSourceName.STUDENT_FAVORITE_ID_COURSE + "=" + favoriteIDCourse + " " +
                                            "WHERE " + DataSourceName.ID_STUDENT + "=" + idStudent);

                                    connection.commit();
                                    statement.close();
                                    connection.close();

                                    System.out.println("Student was updated.");

                                }

                            } catch (NumberFormatException ex) {
                                System.out.println("Invalid input: " + ex.getMessage());
                            }

                        }
                    }

                } catch (NumberFormatException ex) {
                    System.out.println();
                }
            } else {
                System.out.println("Please enter a correct value.");
            }

        } catch (SQLException e){
            System.out.println("Error message: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    /** Delete data from the student table */
    @Override
    public void deleteStudent() {

        try {
            Connection connection = DriverManager.getConnection(DataSourceName.URL, DataSourceName.USER, DataSourceName.PASSWORD);
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            System.out.println("Enter the idstudent which you need to delete:");

            // Verify if the user type from console a String and then convert to an Int
            if (scanner.hasNextLine()) {
                String idStudentString = scanner.nextLine();

                try {
                    // Convert the String to an Int
                    idStudent = Integer.parseInt(idStudentString);

                    // Find the idstudent
                    ResultSet rs = statement.executeQuery("SELECT * " +
                            "FROM " + DataSourceName.STUDENT_TABLE + " " +
                            "WHERE " + DataSourceName.ID_STUDENT + "=" + idStudent);

                    if (!rs.isBeforeFirst()) {
                        System.out.println("Student with idstudent " + idStudent + " does not exist.");

                    } else {
                        // Delete the student by idStudent
                        statement.executeUpdate("DELETE FROM " + DataSourceName.STUDENT_TABLE + " " +
                                "WHERE " + DataSourceName.ID_STUDENT + "=" + idStudent);

                        connection.commit();
                        statement.close();
                        connection.close();

                        System.out.println("Student with idstudent: " + idStudent + " was deleted.");
                    }

                } catch (NumberFormatException ex) {
                    System.out.println("Invalid input: " + ex.getMessage());
                }
            }

        } catch (SQLException e){
            System.out.println("Error message: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    /** Find data in the student table */
    @Override
    public void findStudentByID() {

        try {
            Connection connection = DriverManager.getConnection(DataSourceName.URL, DataSourceName.USER, DataSourceName.PASSWORD);
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            System.out.println("Enter the idstudent which you need to find:");

            // Verify if the user type from console a String and then convert to an Int
            if (scanner.hasNextLine()) {
                String idStudentString = scanner.nextLine();

                try {
                    // Convert the String to an Int
                    idStudent = Integer.parseInt(idStudentString);

                // Find the student by idStudent
                ResultSet rs = statement.executeQuery("SELECT * " +
                                                          "FROM " + DataSourceName.STUDENT_TABLE + " " +
                                                          "WHERE " + DataSourceName.ID_STUDENT + "=" + idStudent);

                if (!rs.isBeforeFirst()) {
                    System.out.println("Student with idstudent " + idStudent + " does not exist.");
                }

                // Printing the information for the student
                while (rs.next()) {
                    System.out.println("idstudent: " + rs.getString(1));
                    System.out.println("first_name: " + rs.getString(2));
                    System.out.println("last_name: " + rs.getString(3));
                    System.out.println("favorite_id_course: " + rs.getString(4));
                    System.out.println("-------------------------------------------");
                }

                connection.commit();
                statement.close();
                connection.close();

            } catch (NumberFormatException ex) {
                    System.out.println("Invalid input: " + ex.getMessage());
            }
        }

        } catch (SQLException e){
            System.out.println("Error message: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public void findStudentByFirstName() {

        try {
            Connection connection = DriverManager.getConnection(DataSourceName.URL, DataSourceName.USER, DataSourceName.PASSWORD);
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            System.out.println("Enter the first name which you need to find:");
            firstName = scanner.nextLine().toLowerCase();

            // Find the student by firstName
            ResultSet rs = statement.executeQuery("SELECT * " +
                                                      "FROM " + DataSourceName.STUDENT_TABLE + " " +
                                                      "WHERE LOWER(" + DataSourceName.STUDENT_FIRST_NAME + ")='" + firstName + "'");

            if (!rs.isBeforeFirst()) {
                System.out.println("Student with first name " + firstName + " does not exist.");
            }

            // Printing the information for the student
            while (rs.next()) {
                System.out.println("idstudent: " + rs.getString(1));
                System.out.println("first_name: " + rs.getString(2));
                System.out.println("last_name: " + rs.getString(3));
                System.out.println("favorite_id_course: " + rs.getString(4));
                System.out.println("-------------------------------------------");
            }

            connection.commit();
            statement.close();
            connection.close();

        } catch (SQLException e){
            System.out.println("Error message: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public void findStudentByLastName() {

        try {
            Connection connection = DriverManager.getConnection(DataSourceName.URL, DataSourceName.USER, DataSourceName.PASSWORD);
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            System.out.println("Enter the last name which you need to find:");
            lastName = scanner.nextLine().toLowerCase();

            // Find the student by lastName
            ResultSet rs = statement.executeQuery("SELECT * " +
                                                      "FROM " + DataSourceName.STUDENT_TABLE + " " +
                                                      "WHERE LOWER(" + DataSourceName.STUDENT_LAST_NAME + ")='" + lastName + "'");

            if (!rs.isBeforeFirst()) {
                System.out.println("Student with last name " + lastName + " does not exist.");
            }

            // Printing the information for the student
            while (rs.next()) {
                System.out.println("idstudent: " + rs.getString(1));
                System.out.println("first_name: " + rs.getString(2));
                System.out.println("last_name: " + rs.getString(3));
                System.out.println("favorite_id_course: " + rs.getString(4));
                System.out.println("-------------------------------------------");
            }

            connection.commit();
            statement.close();
            connection.close();

        } catch (SQLException e){
            System.out.println("Error message: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    /** Create the course table */
    @Override
    public void createCourseTable() {

        try {
            Connection connection = DriverManager.getConnection(DataSourceName.URL, DataSourceName.USER, DataSourceName.PASSWORD);
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            System.out.println("Enter the course table name:");
            courseTableName = scanner.nextLine().replaceAll(" ", "_").toLowerCase();

            // Create the course table
            statement.execute("CREATE TABLE IF NOT EXISTS " + courseTableName + " " +
                             "(" + DataSourceName.ID_COURSE +  " SERIAL PRIMARY KEY NOT NULL," +
                                   DataSourceName.COURSE_NAME + " TEXT, " +
                                   DataSourceName.COURSE_OWNER + " TEXT, " +
                                   DataSourceName.COURSE_ROOM + " INT)");

            connection.commit();
            statement.close();
            connection.close();

            System.out.println(courseTableName + " table was created.");

        } catch (SQLException e) {
            System.out.println("Error message: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    /** Insert data in the course table */
    @Override
    public void insertCourse() {

        try {
            Connection connection = DriverManager.getConnection(DataSourceName.URL, DataSourceName.USER, DataSourceName.PASSWORD);
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            System.out.println("Do you want to add a new course?");
            System.out.println("Enter the course name:");
            courseName = scanner.nextLine();

            System.out.println("Enter the course owner:");
            courseOwner = scanner.nextLine();

            System.out.println("Enter the course room:");

            // Verify if the user type from console a String and then to convert to an Int
            if (scanner.hasNextLine()) {
                String courseRoomString = scanner.nextLine();

                try {
                    // Convert the String into an Int
                    courseRoom = Integer.parseInt(courseRoomString);

                    // Insert the course in the course table
                    statement.executeUpdate("INSERT INTO " + DataSourceName.COURSE_TABLE +
                            " (" + DataSourceName.COURSE_NAME + ", " +
                                   DataSourceName.COURSE_OWNER + ", " +
                                   DataSourceName.COURSE_ROOM + ")" +
                            "VALUES('" + courseName + "','" +
                                         courseOwner + "','" +
                                         courseRoom + "')");

                    connection.commit();
                    statement.close();
                    connection.close();

                    System.out.println("Course " + courseName + " was inserted.");

                } catch (NumberFormatException ex) {
                    System.out.println("Invalid input for course room: " + ex.getMessage());
                }

            }

        } catch (SQLException e){
            System.out.println("Error message: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    /** Update data in the course table */
    @Override
    public void updateCourse() {

        try {
            Connection connection = DriverManager.getConnection(DataSourceName.URL, DataSourceName.USER, DataSourceName.PASSWORD);
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            System.out.println("Enter the idcourse which you need to update:");

            // Verify if the user type from console a String and then convert to an Int
            if (scanner.hasNextLine()) {
                String idCourseString = scanner.nextLine();

                try {
                    // Convert the String to an Int
                    idCourse = Integer.parseInt(idCourseString);

                    ResultSet rs = statement.executeQuery("SELECT * " +
                            "FROM " + DataSourceName.COURSE_TABLE + " " +
                            "WHERE " + DataSourceName.ID_COURSE + "=" + idCourse);

                    // Verify if the idcourse exist
                    if (!rs.isBeforeFirst()) {
                        System.out.println("Course with idcourse " + idCourse + " does not exist.");

                    } else {

                        System.out.println("Enter the new course name: ");
                        courseName = scanner.nextLine();

                        System.out.println("Enter the new course owner: ");
                        courseOwner = scanner.nextLine();

                        System.out.println("Enter the new course room: ");

                        // Verify if the user type from console a number
                        if (scanner.hasNextInt()) {
                            courseRoom = scanner.nextInt();

                            // Update the course
                            statement.executeUpdate("UPDATE " + DataSourceName.COURSE_TABLE + " " +
                                    "SET " + DataSourceName.COURSE_NAME + "='" + courseName + "', " +
                                             DataSourceName.COURSE_OWNER + "='" + courseOwner + "', " +
                                             DataSourceName.COURSE_ROOM + "=" + courseRoom + " " +
                                    "WHERE " + DataSourceName.ID_COURSE + "=" + idCourse);

                            connection.commit();
                            statement.close();
                            connection.close();

                            System.out.println("Course " + courseName + " was updated.");

                        } else {
                            System.out.println("Please enter a correct value for course room.");
                        }
                    }

                } catch (NumberFormatException ex) {
                    System.out.println("Invalid input: " + ex.getMessage());
                }

                }

            } catch(SQLException e){
                System.out.println("Error message: " + e.getMessage());
                throw new RuntimeException(e);
            }

        }

    /** Delete data from the course table */
    @Override
    public void deleteCourse() {

        try {
            Connection connection = DriverManager.getConnection(DataSourceName.URL, DataSourceName.USER, DataSourceName.PASSWORD);
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            System.out.println("Enter the idcourse which you need to delete:");

            // Verify if the user type from console a String and then convert to an Int
            if (scanner.hasNextLine()) {
                String idCourseString = scanner.nextLine();

                try {
                    // Convert the String to an Int
                    idCourse = Integer.parseInt(idCourseString);

                    ResultSet rs = statement.executeQuery("SELECT * " +
                            "FROM " + DataSourceName.COURSE_TABLE + " " +
                            "WHERE " + DataSourceName.ID_COURSE + "=" + idCourse);

                    // Verify if the idcourse exist
                    if (!rs.isBeforeFirst()) {
                        System.out.println("Course with idcourse " + idCourse + " does not exist.");

                    } else {

                        statement.executeUpdate("UPDATE " + DataSourceName.STUDENT_TABLE + " " +
                                "SET " + DataSourceName.STUDENT_FAVORITE_ID_COURSE + "=" + "null " +
                                "WHERE " + DataSourceName.STUDENT_FAVORITE_ID_COURSE + "=" + idCourse);

                        statement.executeUpdate("DELETE FROM " + DataSourceName.COURSE_TABLE + " " +
                                "WHERE " + DataSourceName.ID_COURSE + "=" + idCourse);

                        connection.commit();
                        statement.close();
                        connection.close();

                        System.out.println("Course with idCourse: " + idCourse + " was deleted.");

                    }

                } catch (NumberFormatException ex) {
                    System.out.println("Invalid input: " + ex.getMessage());
                }

            }

        } catch (SQLException e){
            System.out.println("Error message: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public void findCourseByID() {

        try {
            Connection connection = DriverManager.getConnection(DataSourceName.URL, DataSourceName.USER, DataSourceName.PASSWORD);
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            System.out.println("Enter the idcourse which you need to find:");

            // Verify if the user type from console a String and then convert to an Int
            if (scanner.hasNextLine()) {
                String idCourseString = scanner.nextLine();

                try {
                    // Convert the String into an Int
                    idCourse = Integer.parseInt(idCourseString);

                    ResultSet rs = statement.executeQuery("SELECT * " +
                            "FROM " + DataSourceName.COURSE_TABLE + " " +
                            "WHERE " + DataSourceName.ID_COURSE + "=" + idCourse);

                    // Verify if the idcourse exist
                    if (!rs.isBeforeFirst()) {
                        System.out.println("Course with idcourse " + idCourse + " does not exist.");
                    }

                    // Printing the information for the course
                    while (rs.next()) {
                        System.out.println("idcourse: " + rs.getString(1));
                        System.out.println("course_name: " + rs.getString(2));
                        System.out.println("course_owner: " + rs.getString(3));
                        System.out.println("course_room: " + rs.getString(4));
                        System.out.println("-------------------------------------------");
                    }

                    connection.commit();
                    statement.close();
                    connection.close();

                } catch (NumberFormatException ex) {
                    System.out.println("Invalid input: " + ex.getMessage());
                }

            }

        } catch (SQLException e){
            System.out.println("Error message: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public void findCourseByName() {

        try {
            Connection connection = DriverManager.getConnection(DataSourceName.URL, DataSourceName.USER, DataSourceName.PASSWORD);
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            System.out.println("Enter the course name which you need to find:");
            courseName = scanner.nextLine().toLowerCase();

            ResultSet rs = statement.executeQuery("SELECT * " +
                                                      "FROM " + DataSourceName.COURSE_TABLE + " " +
                                                      "WHERE LOWER(" + DataSourceName.COURSE_NAME + ")='" + courseName + "'");

            // Verify if the course name exist
            if (!rs.isBeforeFirst()) {
                System.out.println("Course with course name " + courseName + " does not exist.");
            }

            // Printing the information for the course
            while (rs.next()) {
                System.out.println("idcourse: " + rs.getString(1));
                System.out.println("course_name: " + rs.getString(2));
                System.out.println("course_owner: " + rs.getString(3));
                System.out.println("course_room: " + rs.getString(4));
                System.out.println("-------------------------------------------");
            }

            connection.commit();
            statement.close();
            connection.close();

        } catch (SQLException e){
            System.out.println("Error message: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public void findCourseByOwner() {

        try {
            Connection connection = DriverManager.getConnection(DataSourceName.URL, DataSourceName.USER, DataSourceName.PASSWORD);
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            System.out.println("Enter the course owner which you need to find:");
            courseOwner = scanner.nextLine().toLowerCase();

            ResultSet rs = statement.executeQuery("SELECT * " +
                                                      "FROM " + DataSourceName.COURSE_TABLE + " " +
                                                      "WHERE LOWER(" + DataSourceName.COURSE_OWNER + ")='" + courseOwner + "'");

            // Verify if the course owner exist
            if (!rs.isBeforeFirst()) {
                System.out.println("Course with course owner " + courseOwner + " does not exist.");
            }

            // Printing the information for the course
            while (rs.next()) {
                System.out.println("idcourse: " + rs.getString(1));
                System.out.println("course_name: " + rs.getString(2));
                System.out.println("course_owner: " + rs.getString(3));
                System.out.println("course_room: " + rs.getString(4));
                System.out.println("-------------------------------------------");
            }

            connection.commit();
            statement.close();
            connection.close();

        } catch (SQLException e){
            System.out.println("Error message: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public void findCourseByRoom() {

        try {
            Connection connection = DriverManager.getConnection(DataSourceName.URL, DataSourceName.USER, DataSourceName.PASSWORD);
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            System.out.println("Enter the course room which you need to find:");

            // Verify if the user type from console a String and then convert to an Int
            if (scanner.hasNextLine()) {
                String courseRoomString = scanner.nextLine();

                try {
                    // Convert the String into an Int
                    courseRoom = Integer.parseInt(courseRoomString);

                    ResultSet rs = statement.executeQuery("SELECT * " +
                                                              "FROM " + DataSourceName.COURSE_TABLE + " " +
                                                              "WHERE " + DataSourceName.COURSE_ROOM + "=" + courseRoom);

                    // Verify if the course room exist
                    if (!rs.isBeforeFirst()) {
                        System.out.println("Course with curse room " + courseRoom + " does not exist.");
                    }

                    // Printing the information for the course
                    while (rs.next()) {
                        System.out.println("idcourse: " + rs.getString(1));
                        System.out.println("course_name: " + rs.getString(2));
                        System.out.println("course_owner: " + rs.getString(3));
                        System.out.println("course_room: " + rs.getString(4));
                        System.out.println("-------------------------------------------");
                    }

                    connection.commit();
                    statement.close();
                    connection.close();

                } catch (NumberFormatException ex) {
                    System.out.println("Invalid input: " + ex.getMessage());
                }

            }

        } catch (SQLException e){
            System.out.println("Error message: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public void selectOneTable() {

        try {
            Connection connection = DriverManager.getConnection(DataSourceName.URL, DataSourceName.USER, DataSourceName.PASSWORD);
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            // FULL OUTER JOIN student and course table
            ResultSet rs = statement.executeQuery("SELECT * " +
                    "FROM " + DataSourceName.STUDENT_TABLE + " " +
                    "FULL OUTER JOIN " + DataSourceName.COURSE_TABLE + " " +
                    "ON " + DataSourceName.STUDENT_TABLE + "." + DataSourceName.STUDENT_FAVORITE_ID_COURSE + "=" +
                            DataSourceName.COURSE_TABLE + "." + DataSourceName.ID_COURSE);

            // Printing the information
            while (rs.next()) {
                System.out.println("idstudent: " + rs.getString(1));
                System.out.println("first_name: " + rs.getString(2));
                System.out.println("last_name: " + rs.getString(3));
                System.out.println("student_favorite_course: " + rs.getString(4));
                System.out.println("idcourse: " + rs.getString(5));
                System.out.println("course_name: " + rs.getString(6));
                System.out.println("course_owner: " + rs.getString(7));
                System.out.println("course_room: " + rs.getString(8));
                System.out.println("-------------------------------------------");
            }

            connection.commit();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Error message: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

}
