package data;

import domain.Course;
import domain.Student;
import org.json.JSONObject;

import java.util.HashMap;

public class Database {
    private static HashMap<String, Course> courses;
    private static HashMap<String, Student> students;

    public static void init() {
        initCourses();
        initStudents();
    }

    public static void initCourses() {
        courses = new HashMap<String, Course>();
        courses.put("CMPE150", new Course("CMPE150", 1, 1));
        courses.put("CMPE160", new Course("CMPE160", 1, 2));
        courses.put("CMPE220", new Course("CMPE220", 1, 10));
        courses.put("CMPE240", new Course("CMPE240", 2, 5));
        courses.put("CMPE436", new Course("CMPE436", 1, 1));
        courses.put("GER101", new Course("GER101", 2, 3));
        courses.put("FR101", new Course("FR101", 2, 3));
        courses.put("JP101", new Course("JP101", 1, 3));
        courses.put("SPA101", new Course("SPA101", 2, 3));
    }

    public static void initStudents() {
        students = new HashMap<String, Student>();
        students.put("0", new Student("0", "Alper Sen", "alper"));
        students.put("1", new Student("1", "Burak Yuksel", "burak"));
        students.put("2", new Student("2", "Hatice Melike Ecevit", "hatice"));
        students.put("3", new Student("3", "Burak Ikan Yildiz", "ikan"));
        students.put("4", new Student("4", "Elif Caliskan", "elif"));
        students.put("5", new Student("5", "Enes Ozcan", "enes"));
        students.put("6", new Student("6", "Fahri Can Sanli", "fahri"));
        students.put("7", new Student("7", "Gurkan Demir", "gurkan"));
        students.put("8", new Student("8", "Mehmet Berk Kemaloglu", "kemaloglu"));
        students.put("9", new Student("9", "Merve Ilik", "merve"));
        students.put("10", new Student("10", "Ufuk Yilmaz", "ufuk"));
        students.put("11", new Student("11", "Yagmur Kahyaoglu", "yagmur"));
        students.put("12", new Student("12", "Yunus Emre Inci", "yunus"));
        students.put("13", new Student("13", "Bera Kaya", "bera"));
        students.put("14", new Student("14", "Serkan Ozel", "serkan"));
        students.put("15", new Student("15", "Ismet Dagli", "ismet"));
        students.put("16", new Student("16", "Joseph", "joseph"));
        students.put("17", new Student("17", "Student 17", "stu17"));
        students.put("18", new Student("18", "Student 18", "stu18"));
    }

    public static Course getCourse(String courseCode) {
        return courses.get(courseCode);
    }

    public static Student getStudent(String studentNo) {
        return students.get(studentNo);
    }

    public static JSONObject login(String studentNo, String password) {
        JSONObject body = new JSONObject();
        JSONObject response = new JSONObject();

        if (students.containsKey(studentNo) && password.equals(students.get(studentNo).getPassword())) {
            body.put("name", students.get(studentNo).getName());

            response.put("isSuccess", true);
            response.put("message", "Logged in as " + students.get(studentNo).getName());
            response.put("body", body);
        } else {
            response.put("isSuccess", false);
            response.put("message", "Invalid credentials, please try again!");
        }

        return response;
    }

    /*
        Student with number <studentNo> adding section <section> of coures <courseCode>
     */
    public static JSONObject quickAdd(String studentNo, String courseCode, Integer section) {
        JSONObject response = new JSONObject();

        if (studentNo == null || courseCode == null || section == null) {
            response.put("isSuccess", false);
            response.put("message", "System error, please try again!");
            return response;
        }

        // section numbers stored are starting from 0
        section--;

        if (courses.get(courseCode) == null) {
            response.put("isSuccess", false);
            response.put("message", "No such course");
        } else if (!courses.get(courseCode).hasSection(section)) {
            response.put("isSuccess", false);
            response.put("message", "Wrong section number");
        } else if (students.get(studentNo).hasSection(courseCode, section)) {
            response.put("isSuccess", false);
            response.put("message", "You have already added this course");
        } else if (students.get(studentNo) != null) {

            courses.get(courseCode).lock.P();

            if (courses.get(courseCode).getQuota(section) > 0) {

                if (students.get(studentNo).hasCourse(courseCode)) {
                    Integer oldSection = students.get(studentNo).changeSection(courseCode, section);
                    courses.get(courseCode).changeSection(oldSection, section);

                    response.put("isSuccess", true);
                    response.put("message", "Changed section of " + courseCode + " successfully");
                } else {
                    courses.get(courseCode).addCourse(section);
                    students.get(studentNo).addCourse(courseCode, section);

                    response.put("isSuccess", true);
                    response.put("message", "Added " + courseCode + " successfully");
                }
            } else {
                response.put("isSuccess", false);
                response.put("message", "Could not add " + courseCode + " due to quota restrictions");
            }

            courses.get(courseCode).lock.V();

        } else {
            response.put("isSuccess", false);
            response.put("message", "Invalid information");
        }

        return response;
    }

    /*
        Get courses of student with number <studentNo>
        Response Body:
        {
            "<courseCode1>" : <section1>,
            "<courseCode2>" : <section2>,
            ..
        }
     */
    public static JSONObject getMyCourses(String studentNo) {
        JSONObject response = new JSONObject();

        if (studentNo != null && students.get(studentNo) != null) {
            response.put("isSuccess", true);
            response.put("message", "Success");

            JSONObject body = new JSONObject(students.get(studentNo).getCourses());
            response.put("body", body);

        } else {
            response.put("isSuccess", false);
            response.put("message", "Invalid student number");
        }

        return response;
    }

    /*
        Get all courses
        Response Body:
        {
            "<courseCode>.<section>" : <quota>
        }
     */
    public static JSONObject getCourses() {
        JSONObject response = new JSONObject();

        response.put("isSuccess", true);
        response.put("message", "Success");

        JSONObject body = new JSONObject();

        for (String code : courses.keySet()) {
            HashMap<Integer, Integer> sections = courses.get(code).getSections();

            for (Integer sectionNo: sections.keySet()) {
                body.put(code + "." + (sectionNo+1), sections.get(sectionNo));
            }
        }

        response.put("body", body);

        return response;
    }

    /*
        Student with number <studentNo> dropping section <section> of course <courseCode>
     */
    public static JSONObject dropCourse(String studentNo, String courseCode, Integer section) {
        section--;
        JSONObject response = new JSONObject();

        if (studentNo == null || students.get(studentNo) == null) {
            response.put("isSuccess", false);
            response.put("message", "No such student");
        } else if (courseCode == null || !students.get(studentNo).hasCourse(courseCode)) {
            response.put("isSuccess", false);
            response.put("message", "Course already dropped or never added");
        } else {
            response.put("isSuccess", true);
            response.put("message", "Dropped " + courseCode + " successfully");

            students.get(studentNo).dropCourse(courseCode);
            courses.get(courseCode).dropCourse(section);
        }

        return response;
    }

}
