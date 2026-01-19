package my.um.cbse.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Course entity
 */
public class Course {
    private String courseId;
    private String courseName;
    private String lecturerId;
    private int maxStudents;
    private List<String> prerequisites;
    private boolean selfEnrollmentAllowed;
    private String semester;
    
    public Course(String courseId, String courseName, String lecturerId) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.lecturerId = lecturerId;
        this.maxStudents = 50; // default
        this.prerequisites = new ArrayList<>();
        this.selfEnrollmentAllowed = true;
    }
    
    public String getCourseId() {
        return courseId;
    }
    
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public String getLecturerId() {
        return lecturerId;
    }
    
    public void setLecturerId(String lecturerId) {
        this.lecturerId = lecturerId;
    }
    
    public int getMaxStudents() {
        return maxStudents;
    }
    
    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }
    
    public List<String> getPrerequisites() {
        return prerequisites;
    }
    
    public void setPrerequisites(List<String> prerequisites) {
        this.prerequisites = prerequisites;
    }
    
    public boolean isSelfEnrollmentAllowed() {
        return selfEnrollmentAllowed;
    }
    
    public void setSelfEnrollmentAllowed(boolean selfEnrollmentAllowed) {
        this.selfEnrollmentAllowed = selfEnrollmentAllowed;
    }
    
    public String getSemester() {
        return semester;
    }
    
    public void setSemester(String semester) {
        this.semester = semester;
    }
    
    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", lecturerId='" + lecturerId + '\'' +
                ", maxStudents=" + maxStudents +
                ", selfEnrollmentAllowed=" + selfEnrollmentAllowed +
                '}';
    }
}
