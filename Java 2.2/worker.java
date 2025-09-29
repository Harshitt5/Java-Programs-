import java.sql.*;
import java.util.*;

// ----------- Employee Part -----------
class Employee {
    int empID;
    String name;
    double salary;

    public Employee(int empID, String name, double salary) {
        this.empID = empID;
        this.name = name;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return empID + " | " + name + " | " + salary;
    }
}

// ----------- Product Part -----------
class Product {
    int productID;
    String name;
    double price;
    int quantity;

    public Product(int productID, String name, double price, int quantity) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}

// ----------- Student Model -----------
class Student {
    int studentID;
    String name;
    String department;
    double marks;

    public Student(int studentID, String name, String department, double marks) {
        this.studentID = studentID;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }
}

// ----------- Main Application -----------
public class DatabaseDemoApp {

    // Database credentials
    static final String URL = "jdbc:mysql://localhost:3306/your_database";
    static final String USER = "root";
    static final String PASS = "password";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean exitApp = false;

        while (!exitApp) {
            System.out.println("\n--- Database Demo App ---");
            System.out.println("1. Fetch Employee Data");
            System.out.println("2. Product CRUD Operations");
            System.out.println("3. Student Management");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            int mainChoice = sc.nextInt();

            switch (mainChoice) {
                case 1 -> fetchEmployees();
                case 2 -> productCRUD(sc);
                case 3 -> studentManagement(sc);
                case 4 -> exitApp = true;
                default -> System.out.println("Invalid choice!");
            }
        }

        sc.close();
    }

    // ----------- Part A: Fetch Employees -----------
    static void fetchEmployees() {
        String query = "SELECT EmpID, Name, Salary FROM Employee";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nEmpID | Name | Salary");
            while (rs.next()) {
                int id = rs.getInt("EmpID");
                String name = rs.getString("Name");
                double salary = rs.getDouble("Salary");
                System.out.println(id + " | " + name + " | " + salary);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching employees: " + e.getMessage());
        }
    }

    // ----------- Part B: Product CRUD -----------
    static void productCRUD(Scanner sc) {
        boolean exit = false;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            conn.setAutoCommit(false);

            while (!exit) {
                System.out.println("\n--- Product CRUD ---");
                System.out.println("1. Create Product");
                System.out.println("2. Read Products");
                System.out.println("3. Update Product");
                System.out.println("4. Delete Product");
                System.out.println("5. Back to Main Menu");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1 -> {
                        System.out.print("Product Name: ");
                        String name = sc.nextLine();
                        System.out.print("Price: ");
                        double price = sc.nextDouble();
                        System.out.print("Quantity: ");
                        int qty = sc.nextInt();
                        String insertSQL = "INSERT INTO Product (ProductName, Price, Quantity) VALUES (?, ?, ?)";
                        try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                            ps.setString(1, name);
                            ps.setDouble(2, price);
                            ps.setInt(3, qty);
                            ps.executeUpdate();
                            conn.commit();
                            System.out.println("Product inserted!");
                        } catch (SQLException e) {
                            conn.rollback();
                            System.out.println("Insert failed: " + e.getMessage());
                        }
                    }
                    case 2 -> {
                        String selectSQL = "SELECT * FROM Product";
                        try (Statement stmt = conn.createStatement();
                             ResultSet rs = stmt.executeQuery(selectSQL)) {
                            System.out.println("ProductID | Name | Price | Quantity");
                            while (rs.next()) {
                                System.out.println(rs.getInt("ProductID") + " | " +
                                        rs.getString("ProductName") + " | " +
                                        rs.getDouble("Price") + " | " +
                                        rs.getInt("Quantity"));
                            }
                        }
                    }
                    case 3 -> {
                        System.out.print("ProductID to update: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("New Name: ");
                        String name = sc.nextLine();
                        System.out.print("New Price: ");
                        double price = sc.nextDouble();
                        System.out.print("New Quantity: ");
                        int qty = sc.nextInt();
                        String updateSQL = "UPDATE Product SET ProductName=?, Price=?, Quantity=? WHERE ProductID=?";
                        try (PreparedStatement ps = conn.prepareStatement(updateSQL)) {
                            ps.setString(1, name);
                            ps.setDouble(2, price);
                            ps.setInt(3, qty);
                            ps.setInt(4, id);
                            ps.executeUpdate();
                            conn.commit();
                            System.out.println("Product updated!");
                        } catch (SQLException e) {
                            conn.rollback();
                            System.out.println("Update failed: " + e.getMessage());
                        }
                    }
                    case 4 -> {
                        System.out.print("ProductID to delete: ");
                        int id = sc.nextInt();
                        String deleteSQL = "DELETE FROM Product WHERE ProductID=?";
                        try (PreparedStatement ps = conn.prepareStatement(deleteSQL)) {
                            ps.setInt(1, id);
                            ps.executeUpdate();
                            conn.commit();
                            System.out.println("Product deleted!");
                        } catch (SQLException e) {
                            conn.rollback();
                            System.out.println("Delete failed: " + e.getMessage());
                        }
                    }
                    case 5 -> exit = true;
                    default -> System.out.println("Invalid choice!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    // ----------- Part C: Student Management -----------
    static void studentManagement(Scanner sc) {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n--- Student Management ---");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
                conn.setAutoCommit(false);

                switch (choice) {
                    case 1 -> {
                        System.out.print("Name: ");
                        String name = sc.nextLine();
                        System.out.print("Department: ");
                        String dept = sc.nextLine();
                        System.out.print("Marks: ");
                        double marks = sc.nextDouble();
                        String insertSQL = "INSERT INTO Student (Name, Department, Marks) VALUES (?, ?, ?)";
                        try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                            ps.setString(1, name);
                            ps.setString(2, dept);
                            ps.setDouble(3, marks);
                            ps.executeUpdate();
                            conn.commit();
                            System.out.println("Student added!");
                        } catch (SQLException e) {
                            conn.rollback();
                            System.out.println("Insert failed: " + e.getMessage());
                        }
                    }
                    case 2 -> {
                        String selectSQL = "SELECT * FROM Student";
                        try (Statement stmt = conn.createStatement();
                             ResultSet rs = stmt.executeQuery(selectSQL)) {
                            System.out.println("StudentID | Name | Department | Marks");
                            while (rs.next()) {
                                System.out.println(rs.getInt("StudentID") + " | " +
                                        rs.getString("Name") + " | " +
                                        rs.getString("Department") + " | " +
                                        rs.getDouble("Marks"));
                            }
                        }
                    }
                    case 3 -> {
                        System.out.print("StudentID to update: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("New Name: ");
                        String name = sc.nextLine();
                        System.out.print("New Department: ");
                        String dept = sc.nextLine();
                        System.out.print("New Marks: ");
                        double marks = sc.nextDouble();
                        String updateSQL = "UPDATE Student SET Name=?, Department=?, Marks=? WHERE StudentID=?";
                        try (PreparedStatement ps = conn.prepareStatement(updateSQL)) {
                            ps.setString(1, name);
                            ps.setString(2, dept);
                            ps.setDouble(3, marks);
                            ps.setInt(4, id);
                            ps.executeUpdate();
                            conn.commit();
                            System.out.println("Student updated!");
                        } catch (SQLException e) {
                            conn.rollback();
                            System.out.println("Update failed: " + e.getMessage());
                        }
                    }
                    case 4 -> {
                        System.out.print("StudentID to delete: ");
                        int id = sc.nextInt();
                        String deleteSQL = "DELETE FROM Student WHERE StudentID=?";
                        try (PreparedStatement ps = conn.prepareStatement(deleteSQL)) {
                            ps.setInt(1, id);
                            ps.executeUpdate();
                            conn.commit();
                            System.out.println("Student deleted!");
                        } catch (SQLException e) {
                            conn.rollback();
                            System.out.println("Delete failed: " + e.getMessage());
                        }
                    }
                    case 5 -> exit = true;
                    default -> System.out.println("Invalid choice!");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }
}