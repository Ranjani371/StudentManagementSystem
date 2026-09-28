import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static ArrayList<Student> studentList = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);


    private static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {

        loadDataFromFile();

        while (true) {
            System.out.println("\n=== STUDENT MANAGEMENT SYSTEM ===");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": addStudent();
                          break;
                case "2": viewStudents();
                          break;
                case "3": updateStudent();
                          break;
                case "4": deleteStudent();
                          break;
                case "5":
                    System.out.println("Exiting system. Data saved ");
                    System.exit(0);
                default:
                    System.out.println("Invalid option! chose a number from 1 to 5.");
            }
        }
    }


    private static void addStudent() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();

        if (findStudentIndex(id) != -1) {
            System.out.println("Error: A student with this ID already exists!");
            return;
        }

        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Age: ");
        int age = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter Course: ");
        String course = scanner.nextLine().trim();

        studentList.add(new Student(id, name, age, course));
        saveDataToFile(); // Instantly save changes to text file
        System.out.println("Success: Student record added successfully!");
    }

    //  VIEW
    private static void viewStudents() {
        if (studentList.isEmpty()) {
            System.out.println("The registry database is empty.");
            return;
        }

        System.out.println("\n--- CURRENT STUDENT ROSTER ---");
        for (Student student : studentList) {
            System.out.println(student);
        }
    }

    // 3. UPDATE
    private static void updateStudent() {
        System.out.print("Enter Student ID to update: ");
        String id = scanner.nextLine().trim();

        int index = findStudentIndex(id);
        if (index == -1) {
            System.out.println("Error: Student record not found!");
            return;
        }

        Student student = studentList.get(index);

        System.out.print("Enter New Name (Press Enter to keep '" + student.getName() + "'): ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) student.setName(name);

        System.out.print("Enter New Age (Press Enter to keep '" + student.getAge() + "'): ");
        String ageInput = scanner.nextLine().trim();
        if (!ageInput.isEmpty()) student.setAge(Integer.parseInt(ageInput));

        System.out.print("Enter New Course (Press Enter to keep '" + student.getCourse() + "'): ");
        String course = scanner.nextLine().trim();
        if (!course.isEmpty()) student.setCourse(course);

        saveDataToFile();
        System.out.println("Success: Student details modified.");
    }

    // 4. DELETE
    private static void deleteStudent() {
        System.out.print("Enter Student ID to delete: ");
        String id = scanner.nextLine().trim();

        int index = findStudentIndex(id);
        if (index == -1) {
            System.out.println("Error: Student record not found!");
            return;
        }

        studentList.remove(index);
        saveDataToFile();
        System.out.println("Success: Student deleted successfully.");
    }


    private static int findStudentIndex(String id) {
        for (int i = 0; i < studentList.size(); i++) {
            if (studentList.get(i).getId().equalsIgnoreCase(id)) {
                return i;
            }
        }
        return -1;
    }


    private static void saveDataToFile() {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            for (Student student : studentList) {
                // Formatting data into standard format: id,name,age,course
                writer.write(student.getId() + "," +
                        student.getName() + "," +
                        student.getAge() + "," +
                        student.getCourse() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving records to text file: " + e.getMessage());
        }
    }


    private static void loadDataFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (!line.isEmpty()) {

                    String[] data = line.split(",");
                    if (data.length == 4) {
                        String id = data[0];
                        String name = data[1];
                        int age = Integer.parseInt(data[2]);
                        String course = data[3];


                        studentList.add(new Student(id, name, age, course));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading text storage file.");
        }
    }
}
