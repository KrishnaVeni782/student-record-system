package dao;

import db.DBConnection;
import model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // ─── CREATE ──────────────────────────────────────────────────────────────

    public boolean addStudent(Student s) {
        String sql = "INSERT INTO students (name, age, course, marks) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setInt(2, s.getAge());
            ps.setString(3, s.getCourse());
            ps.setDouble(4, s.getMarks());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("  [ERROR] Could not add student: " + e.getMessage());
            return false;
        }
    }

    // ─── READ ALL ─────────────────────────────────────────────────────────────

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.out.println("  [ERROR] Could not fetch students: " + e.getMessage());
        }
        return list;
    }

    // ─── READ BY ID ───────────────────────────────────────────────────────────

    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.out.println("  [ERROR] Could not find student: " + e.getMessage());
        }
        return null;
    }

    // ─── SEARCH BY NAME ──────────────────────────────────────────────────────

    public List<Student> searchByName(String keyword) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE name LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.out.println("  [ERROR] Search failed: " + e.getMessage());
        }
        return list;
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────

    public boolean updateStudent(Student s) {
        String sql = "UPDATE students SET name=?, age=?, course=?, marks=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setInt(2, s.getAge());
            ps.setString(3, s.getCourse());
            ps.setDouble(4, s.getMarks());
            ps.setInt(5, s.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("  [ERROR] Could not update student: " + e.getMessage());
            return false;
        }
    }

    // ─── DELETE ──────────────────────────────────────────────────────────────

    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("  [ERROR] Could not delete student: " + e.getMessage());
            return false;
        }
    }

    // ─── STATS ───────────────────────────────────────────────────────────────

    public void printStats() {
        String sql = "SELECT COUNT(*) AS total, AVG(marks) AS avg, " +
                     "MAX(marks) AS highest, MIN(marks) AS lowest FROM students";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                System.out.println("  Total Students : " + rs.getInt("total"));
                System.out.printf("  Average Marks  : %.2f%n", rs.getDouble("avg"));
                System.out.printf("  Highest Marks  : %.2f%n", rs.getDouble("highest"));
                System.out.printf("  Lowest Marks   : %.2f%n", rs.getDouble("lowest"));
            }

        } catch (SQLException e) {
            System.out.println("  [ERROR] Could not compute stats: " + e.getMessage());
        }
    }

    // ─── HELPER ──────────────────────────────────────────────────────────────

    private Student mapRow(ResultSet rs) throws SQLException {
        return new Student(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getInt("age"),
            rs.getString("course"),
            rs.getDouble("marks")
        );
    }
}
