package model;

public class Student {

    private int    id;
    private String name;
    private int    age;
    private String course;
    private double marks;

    public Student() {}

    public Student(String name, int age, String course, double marks) {
        this.name   = name;
        this.age    = age;
        this.course = course;
        this.marks  = marks;
    }

    public Student(int id, String name, int age, String course, double marks) {
        this.id     = id;
        this.name   = name;
        this.age    = age;
        this.course = course;
        this.marks  = marks;
    }

    // Getters
    public int    getId()     { return id; }
    public String getName()   { return name; }
    public int    getAge()    { return age; }
    public String getCourse() { return course; }
    public double getMarks()  { return marks; }

    // Setters
    public void setId(int id)         { this.id = id; }
    public void setName(String name)  { this.name = name; }
    public void setAge(int age)       { this.age = age; }
    public void setCourse(String c)   { this.course = c; }
    public void setMarks(double m)    { this.marks = m; }

    public String getGrade() {
        if (marks >= 90) return "A+";
        if (marks >= 80) return "A";
        if (marks >= 70) return "B";
        if (marks >= 60) return "C";
        if (marks >= 50) return "D";
        return "F";
    }

    @Override
    public String toString() {
        return String.format("| %-4d | %-20s | %-3d | %-15s | %-6.1f | %-5s |",
                id, name, age, course, marks, getGrade());
    }
}
