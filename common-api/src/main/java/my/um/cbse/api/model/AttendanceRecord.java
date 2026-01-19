package my.um.cbse.api.model;

/**
 * Attendance record for a student in a course session
 */
public class AttendanceRecord {
    private String recordId;
    private String studentId;
    private String courseId;
    private String sessionDate;
    private boolean present;
    private String notes;
    private long timestamp;
    
    public AttendanceRecord(String recordId, String studentId, String courseId, String sessionDate) {
        this.recordId = recordId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.sessionDate = sessionDate;
        this.present = false;
        this.timestamp = System.currentTimeMillis();
    }
    
    public String getRecordId() {
        return recordId;
    }
    
    public void setRecordId(String recordId) {
        this.recordId = recordId;
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
    
    public String getSessionDate() {
        return sessionDate;
    }
    
    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }
    
    public boolean isPresent() {
        return present;
    }
    
    public void setPresent(boolean present) {
        this.present = present;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "AttendanceRecord{" +
                "studentId='" + studentId + '\'' +
                ", courseId='" + courseId + '\'' +
                ", sessionDate='" + sessionDate + '\'' +
                ", present=" + present +
                '}';
    }
}
