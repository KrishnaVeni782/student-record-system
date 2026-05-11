import dao.StudentDAO;
import model.Student;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final StudentDAO dao     = new StudentDAO();
    private static final Scanner    scanner = new Scanner(System.in);

    public static void main(String[] args) {
        printBanner();
        int choice;
        do {
            printMenu();
            choice = readInt("  Enter choice: ");
            System.out.println();
            switch (choice) {
                case 1  -> addStudent();
                case 2  -> viewAllStudents();
                case 3  -> searchStudent();
                case 4  -> updateStudent();
                case 5  -> deleteStudent();
                case 6  -> viewStats();
                case 0  -> System.out.println("  Goodbye! See you soon.");
                default -> System.out.println("  [!] Invalid option. Try again.");
            }
        } while (choice != 0);

        scanner.close();
    }

    // ─── MENU ────────────────────────────────────────────────────────────────

    private static void printBanner() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║     STUDENT RECORD SYSTEM  v1.0      ║");
        System.out.println("  ║        Java + MySQL (JDBC)            ║");
        System.out.println("  ╚══════════════════════════════════════╝");
        System.out.println();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("  ┌─────────────────────────────┐");
        System.out.println("  │          MAIN MENU          │");
        System.out.println("  ├─────────────────────────────┤");
        System.out.println("  │  1. Add New Student         │");
        System.out.println("  │  2. View All Students       │");
        System.out.println("  │  3. Search Student          │");
        System.out.println("  │  4. Update Student          │");
        System.out.println("  │  5. Delete Student          │");
        System.out.println("  │  6. View Statistics         │");
        System.out.println("  │  0. Exit                    │");
        System.out.println("  └─────────────────────────────┘");
    }

    // ─── 1. ADD ──────────────────────────────────────────────────────────────

    private static void addStudent() {
        System.out.println("  ── Add New Student ──────────────────");
        String name   = readString("  Name   : ");
        int    age    = readInt(   "  Age    : ");
        String course = readString("  Course : ");
        double marks  = readDouble("  Marks  : ");

        Student s = new Student(name, age, course, marks);
        if (dao.addStudent(s)) {
            System.out.println("  ✔ Student added successfully!");
        } else {
            System.out.println("  ✘ Failed to add student.");
        }
    }

    // ─── 2. VIEW ALL ─────────────────────────────────────────────────────────

    private static void viewAllStudents() {
        System.out.println("  ── All Students ─────────────────────");
        List<Student> list = dao.getAllStudents();
        if (list.isEmpty()) {
            System.out.println("  No records found.");
            return;
        }
        printTableHeader();
        list.forEach(s -> System.out.println("  " + s));
        printTableFooter();
        System.out.println("  Total: " + list.size() + " record(s).");
    }

    // ─── 3. SEARCH ───────────────────────────────────────────────────────────

    private static void searchStudent() {
        System.out.println("  ── Search Student ───────────────────");
        System.out.println("  1. Search by ID");
        System.out.println("  2. Search by Name");
        int opt = readInt("  Option: ");

        if (opt == 1) {
            int id = readInt("  Enter Student ID: ");
            Student s = dao.getStudentById(id);
            if (s != null) {
                printTableHeader();
                System.out.println("  " + s);
                printTableFooter();
            } else {
                System.out.println("  No student found with ID " + id);
            }

        } else if (opt == 2) {
            String keyword = readString("  Enter name keyword: ");
            List<Student> list = dao.searchByName(keyword);
            if (list.isEmpty()) {
                System.out.println("  No results for \"" + keyword + "\".");
            } else {
                printTableHeader();
                list.forEach(s -> System.out.println("  " + s));
                printTableFooter();
            }

        } else {
            System.out.println("  [!] Invalid option.");
        }
    }

    // ─── 4. UPDATE ───────────────────────────────────────────────────────────

    private static void updateStudent() {
        System.out.println("  ── Update Student ───────────────────");
        int id = readInt("  Enter Student ID to update: ");

        Student existing = dao.getStudentById(id);
        if (existing == null) {
            System.out.println("  No student found with ID " + id);
            return;
        }

        System.out.println("  Current record:");
        printTableHeader();
        System.out.println("  " + existing);
        printTableFooter();
        System.out.println("  (Press Enter to keep current value)");

        String name   = readStringOptional("  New Name   [" + existing.getName()   + "]: ", existing.getName());
        String ageStr = readStringOptional("  New Age    [" + existing.getAge()    + "]: ", String.valueOf(existing.getAge()));
        String course = readStringOptional("  New Course [" + existing.getCourse() + "]: ", existing.getCourse());
        String mrkStr = readStringOptional("  New Marks  [" + existing.getMarks()  + "]: ", String.valueOf(existing.getMarks()));

        Student updated = new Student(id, name, Integer.parseInt(ageStr), course, Double.parseDouble(mrkStr));
        if (dao.updateStudent(updated)) {
            System.out.println("  ✔ Student updated successfully!");
        } else {
            System.out.println("  ✘ Update failed.");
        }
    }

    // ─── 5. DELETE ───────────────────────────────────────────────────────────

    private static void deleteStudent() {
        System.out.println("  ── Delete Student ───────────────────");
        int id = readInt("  Enter Student ID to delete: ");

        Student s = dao.getStudentById(id);
        if (s == null) {
            System.out.println("  No student found with ID " + id);
            return;
        }

        System.out.println("  Record to delete: " + s.getName() + " (ID: " + id + ")");
        String confirm = readString("  Are you sure? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes")) {
            if (dao.deleteStudent(id)) {
                System.out.println("  ✔ Student deleted.");
            } else {
                System.out.println("  ✘ Deletion failed.");
            }
        } else {
            System.out.println("  Deletion cancelled.");
        }
    }

    // ─── 6. STATS ────────────────────────────────────────────────────────────

    private static void viewStats() {
        System.out.println("  ── Class Statistics ─────────────────");
        dao.printStats();
    }

    // ─── DISPLAY HELPERS ─────────────────────────────────────────────────────

    private static void printTableHeader() {
        System.out.println("  +------+----------------------+-----+-----------------+--------+-------+");
        System.out.println("  | ID   | Name                 | Age | Course          | Marks  | Grade |");
        System.out.println("  +------+----------------------+-----+-----------------+--------+-------+");
    }

    private static void printTableFooter() {
        System.out.println("  +------+----------------------+-----+-----------------+--------+-------+");
    }

    // ─── INPUT HELPERS ───────────────────────────────────────────────────────

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a valid integer.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(scanner.nextLine().trim());
                if (val < 0 || val > 100) {
                    System.out.println("  [!] Marks must be between 0 and 100.");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a valid number.");
            }
        }
    }

    private static String readStringOptional(String prompt, String defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? defaultValue : input;
    }
}
