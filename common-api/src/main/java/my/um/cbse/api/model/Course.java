package my.um.cbse.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Course entity - Enhanced for Course Management Module
 */
public class Course {
    private String courseId;
    private String courseName;
    private String courseCode;
    private String description;
    private String lecturerId;
    private String ownerId;
    private int maxStudents;
    private List<String> prerequisites;
    
    // New fields for Course Management Module
    private CourseDesign design;
    private CourseStatus status;
    private String semester;
    private Integer credits;
    private String license;
    private String institution;
    private Date startDate;
    private Date endDate;
    private Date createdDate;
    private Date lastModified;
    private boolean lectureManagementEnabled;
    private boolean absenceManagementEnabled;
    private String accessConfig;
    private boolean selfEnrollmentAllowed;
    private String rootElementId;
    private Map<String, String> metadata;
    
    public Course() {
        this.prerequisites = new ArrayList<>();
        this.status = CourseStatus.DRAFT;
        this.design = CourseDesign.CONVENTIONAL;
        this.metadata = new HashMap<>();
        this.lectureManagementEnabled = false;
        this.absenceManagementEnabled = false;
        this.selfEnrollmentAllowed = false;
    }
    
    public Course(String courseId, String courseName) {
        this();
        this.courseId = courseId;
        this.courseName = courseName;
    }
    
    public Course(String courseId, String courseName, String ownerId) {
        this();
        this.courseId = courseId;
        this.courseName = courseName;
        this.ownerId = ownerId;
    }
    
    public Course(String courseId, String courseName, String courseCode, 
                  String lecturerId, int maxStudents) {
        this();
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.lecturerId = lecturerId;
        this.maxStudents = maxStudents;
    }
    
    // Getters and Setters
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
    
    public String getCourseCode() {
        return courseCode;
    }
    
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getLecturerId() {
        return lecturerId;
    }
    
    public void setLecturerId(String lecturerId) {
        this.lecturerId = lecturerId;
    }
    
    public String getOwnerId() {
        return ownerId;
    }
    
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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
    
    public CourseDesign getDesign() {
        return design;
    }
    
    public void setDesign(CourseDesign design) {
        this.design = design;
    }
    
    public CourseStatus getStatus() {
        return status;
    }
    
    public void setStatus(CourseStatus status) {
        this.status = status;
    }
    
    public String getSemester() {
        return semester;
    }
    
    public void setSemester(String semester) {
        this.semester = semester;
    }
    
    public Integer getCredits() {
        return credits;
    }
    
    public void setCredits(Integer credits) {
        this.credits = credits;
    }
    
    public String getLicense() {
        return license;
    }
    
    public void setLicense(String license) {
        this.license = license;
    }
    
    public String getInstitution() {
        return institution;
    }
    
    public void setInstitution(String institution) {
        this.institution = institution;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public boolean isLectureManagementEnabled() {
        return lectureManagementEnabled;
    }
    
    public void setLectureManagementEnabled(boolean lectureManagementEnabled) {
        this.lectureManagementEnabled = lectureManagementEnabled;
    }
    
    public boolean isAbsenceManagementEnabled() {
        return absenceManagementEnabled;
    }
    
    public void setAbsenceManagementEnabled(boolean absenceManagementEnabled) {
        this.absenceManagementEnabled = absenceManagementEnabled;
    }
    
    public String getAccessConfig() {
        return accessConfig;
    }
    
    public void setAccessConfig(String accessConfig) {
        this.accessConfig = accessConfig;
    }
    
    // Alias for compatibility
    public void setAccessConfiguration(String accessConfig) {
        this.accessConfig = accessConfig;
    }
    
    public Date getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    public Date getLastModified() {
        return lastModified;
    }
    
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
    
    public boolean isSelfEnrollmentAllowed() {
        return selfEnrollmentAllowed;
    }
    
    public void setSelfEnrollmentAllowed(boolean selfEnrollmentAllowed) {
        this.selfEnrollmentAllowed = selfEnrollmentAllowed;
    }
    
    public String getRootElementId() {
        return rootElementId;
    }

    public void setRootElementId(String rootElementId) {
        this.rootElementId = rootElementId;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public void addPrerequisite(String prerequisite) {
        if (this.prerequisites == null) {
            this.prerequisites = new ArrayList<>();
        }
        this.prerequisites.add(prerequisite);
    }

    public void addMetadata(String key, String value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", status=" + status +
                ", design=" + design +
                ", ownerId='" + ownerId + '\'' +
                '}';
    }
}
