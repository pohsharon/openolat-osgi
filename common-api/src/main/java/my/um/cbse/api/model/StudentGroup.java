package my.um.cbse.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Student group for projects/tutorials
 */
public class StudentGroup {
    private String groupId;
    private String groupName;
    private String courseId;
    private List<String> studentIds;
    private int maxSize;
    private String purpose; // PROJECT, TUTORIAL, LAB, etc.
    
    public StudentGroup(String groupId, String groupName, String courseId) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.courseId = courseId;
        this.studentIds = new ArrayList<>();
        this.maxSize = 5; // default
    }
    
    public String getGroupId() {
        return groupId;
    }
    
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    public String getGroupName() {
        return groupName;
    }
    
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public String getCourseId() {
        return courseId;
    }
    
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    
    public List<String> getStudentIds() {
        return studentIds;
    }
    
    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }
    
    public int getMaxSize() {
        return maxSize;
    }
    
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
    
    public String getPurpose() {
        return purpose;
    }
    
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    
    public boolean isFull() {
        return studentIds.size() >= maxSize;
    }
    
    @Override
    public String toString() {
        return "StudentGroup{" +
                "groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", courseId='" + courseId + '\'' +
                ", size=" + studentIds.size() +
                "/" + maxSize +
                '}';
    }
}
