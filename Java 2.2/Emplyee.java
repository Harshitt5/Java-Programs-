import java.util.*;

// Employee class
class Employee {
    String name;
    int age;
    double salary;

    public Employee(String name, int age, double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return name + " | Age: " + age + " | Salary: " + salary;
    }
}

public class EmployeeSortingDemo {
    public static void main(String[] args) {
        // Sample employee data
        List<Employee> employees = new ArrayList<>(Arrays.asList(
            new Employee("Alice", 28, 70000),
            new Employee("Bob", 35, 90000),
            new Employee("Charlie", 25, 60000),
            new Employee("David", 30, 85000)
        ));

        Scanner sc = new Scanner(System.in);
        System.out.println("--- Employee Sorting ---");
        System.out.println("Sort employees by:");
        System.out.println("1. Name");
        System.out.println("2. Age");
        System.out.println("3. Salary (Descending)");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                // Sort by name alphabetically
                employees.sort((e1, e2) -> e1.name.compareTo(e2.name));
                System.out.println("\nEmployees sorted by Name:");
                break;
            case 2:
                // Sort by age ascending
                employees.sort((e1, e2) -> Integer.compare(e1.age, e2.age));
                System.out.println("\nEmployees sorted by Age:");
                break;
            case 3:
                // Sort by salary descending
                employees.sort((e1, e2) -> Double.compare(e2.salary, e1.salary));
                System.out.println("\nEmployees sorted by Salary (Descending):");
                break;
            default:
                System.out.println("Invalid choice! Showing original list.");
        }

        // Display sorted employees
        employees.forEach(System.out::println);
        sc.close();
    }
}