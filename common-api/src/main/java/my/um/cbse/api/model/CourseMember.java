package my.um.cbse.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Course Member entity representing a user's membership in a course
 */
public class CourseMember {
    private String memberId;
    private String courseId;
    private String userId;
    private String userName;
    private String email;
    private CourseRole role;
    private Date joinedDate;
    private List<String> rights;
    private boolean active;
    
    public CourseMember() {
        this.rights = new ArrayList<>();
        this.joinedDate = new Date();
        this.active = true;
    }
    
    public CourseMember(String courseId, String userId, String userName, CourseRole role) {
        this();
        this.memberId = courseId + "_" + userId;
        this.courseId = courseId;
        this.userId = userId;
        this.userName = userName;
        this.role = role;
    }
    
    public String getMemberId() {
        return memberId;
    }
    
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
    
    public String getCourseId() {
        return courseId;
    }
    
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public CourseRole getRole() {
        return role;
    }
    
    public void setRole(CourseRole role) {
        this.role = role;
    }
    
    public Date getJoinedDate() {
        return joinedDate;
    }
    
    public void setJoinedDate(Date joinedDate) {
        this.joinedDate = joinedDate;
    }
    
    public List<String> getRights() {
        return rights;
    }
    
    public void setRights(List<String> rights) {
        this.rights = rights;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    @Override
    public String toString() {
        return "CourseMember{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", role=" + role +
                ", active=" + active +
                '}';
    }
}
