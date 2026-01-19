package my.um.cbse.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Student entity
 */
public class Student {
    private String studentId;
    private String name;
    private String email;
    private List<String> completedCourses;
    private UserRole role = UserRole.STUDENT;
    
    public Student(String studentId, String name, String email) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.completedCourses = new ArrayList<>();
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public List<String> getCompletedCourses() {
        return completedCourses;
    }
    
    public void setCompletedCourses(List<String> completedCourses) {
        this.completedCourses = completedCourses;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
