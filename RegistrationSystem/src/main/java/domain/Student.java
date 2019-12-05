package domain;

import java.util.HashMap;

public class Student {
    private String no;
    private String name;
    private HashMap<String, Integer> courses;
    private String password;

    public Student(String no, String name, String password) {
        this.no = no;
        this.name = name;
        this.password = password;
        this.courses = new HashMap<String, Integer>();
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean hasCourse(String code) {
        return courses.containsKey(code);
    }

    public boolean hasSection(String code, Integer section) {
        return hasCourse(code) && courses.get(code).equals(section);
    }

    public int changeSection(String code, Integer section) {
        return courses.put(code, section);
    }

    public HashMap<String, Integer> getCourses() {
        return this.courses;
    }

    public void addCourse(String code, Integer section) {
        courses.put(code, section);
    }

    public void dropCourse(String courseCode) {
        courses.remove(courseCode);
    }
}
