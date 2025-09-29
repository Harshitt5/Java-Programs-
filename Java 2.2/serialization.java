import java.io.*;

// Step 1: Define Student class implementing Serializable
class Student implements Serializable {
    private static final long serialVersionUID = 1L; // for version control

    int id;
    String name;
    double gpa;

    public Student(int id, String name, double gpa) {
        this.id = id;
        this.name = name;
        this.gpa = gpa;
    }

    // Method to display student details
    public void display() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("GPA: " + gpa);
    }
}

public class StudentSerializationDemo {

    public static void main(String[] args) {
        // Step 2: Create a Student object
        Student student = new Student(101, "Alice", 9.1);

        // File to store serialized object
        String filename = "student.ser";

        // --- Serialization ---
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(student); // serialize object
            System.out.println("Student serialized successfully!");

        } catch (IOException e) {
            System.out.println("IOException during serialization: " + e.getMessage());
        }

        // --- Deserialization ---
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            Student deserializedStudent = (Student) ois.readObject(); // deserialize object
            System.out.println("\nStudent deserialized:");
            deserializedStudent.display();

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException during deserialization: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
        }
    }
}