package my.um.cbse.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Enrollment Data Transfer Object (DTO)
 */
public class Enrollment {
    private String enrollmentId;
    private String studentId;
    private String courseId;
    private long enrollmentDate;
    private EnrollmentStatus status;
    private String enrolledBy; // studentId for self-enrollment, adminId for manual
    private boolean isSelfEnrolled;
    private String groupId; // Optional: assigned group
    private int participationScore;
    private List<String> enrollmentHistory;
    
    public Enrollment(String studentId, String courseId) {
        this.enrollmentId = generateId(studentId, courseId);
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentDate = System.currentTimeMillis();
        this.status = EnrollmentStatus.ACTIVE;
        this.isSelfEnrolled = true;
        this.participationScore = 0;
        this.enrollmentHistory = new ArrayList<>();
        addToHistory("Enrolled");
    }
    
    public Enrollment(String studentId, String courseId, String enrolledBy) {
        this(studentId, courseId);
        this.enrolledBy = enrolledBy;
        this.isSelfEnrolled = false;
        addToHistory("Manually enrolled by " + enrolledBy);
    }
    
    private String generateId(String studentId, String courseId) {
        return studentId + "_" + courseId + "_" + System.currentTimeMillis();
    }
    
    public void addToHistory(String action) {
        String entry = System.currentTimeMillis() + ": " + action;
        enrollmentHistory.add(entry);
    }
    
    // Getters and Setters
    
    public String getEnrollmentId() {
        return enrollmentId;
    }
    
    public void setEnrollmentId(String enrollmentId) {
        this.enrollmentId = enrollmentId;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getCourseId() {
        return courseId;
    }
    
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    
    public long getEnrollmentDate() {
        return enrollmentDate;
    }
    
    public void setEnrollmentDate(long enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
    
    public EnrollmentStatus getStatus() {
        return status;
    }
    
    public void setStatus(EnrollmentStatus status) {
        this.status = status;
        addToHistory("Status changed to " + status);
    }
    
    public String getEnrolledBy() {
        return enrolledBy;
    }
    
    public void setEnrolledBy(String enrolledBy) {
        this.enrolledBy = enrolledBy;
    }
    
    public boolean isSelfEnrolled() {
        return isSelfEnrolled;
    }
    
    public void setSelfEnrolled(boolean selfEnrolled) {
        isSelfEnrolled = selfEnrolled;
    }
    
    public String getGroupId() {
        return groupId;
    }
    
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    public int getParticipationScore() {
        return participationScore;
    }
    
    public void setParticipationScore(int participationScore) {
        this.participationScore = participationScore;
    }
    
    public List<String> getEnrollmentHistory() {
        return enrollmentHistory;
    }
    
    public void setEnrollmentHistory(List<String> enrollmentHistory) {
        this.enrollmentHistory = enrollmentHistory;
    }
    
    @Override
    public String toString() {
        return "Enrollment{" +
                "enrollmentId='" + enrollmentId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", courseId='" + courseId + '\'' +
                ", status=" + status +
                ", enrollmentDate=" + enrollmentDate +
                ", isSelfEnrolled=" + isSelfEnrolled +
                '}';
    }
}
