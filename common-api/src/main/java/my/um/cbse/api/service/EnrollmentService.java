package my.um.cbse.api.service;

import java.util.List;

import my.um.cbse.api.model.AttendanceRecord;
import my.um.cbse.api.model.Course;
import my.um.cbse.api.model.Enrollment;
import my.um.cbse.api.model.EnrollmentResult;
import my.um.cbse.api.model.EnrollmentStatus;
import my.um.cbse.api.model.Student;
import my.um.cbse.api.model.StudentGroup;
import my.um.cbse.api.model.UserRole;

/**
 * Comprehensive Enrollment Service Interface
 * Defines the contract for enrollment operations
 */
public interface EnrollmentService {
    
    // ========== STUDENT SELF-ENROLLMENT ==========
    
    /**
     * Student self-enrollment in a course
     * @param studentId the student identifier
     * @param courseId the course identifier
     * @return EnrollmentResult with success status and message
     */
    EnrollmentResult selfEnroll(String studentId, String courseId);
    
    /**
     * Student self-unenrollment from a course
     * @param studentId the student identifier
     * @param courseId the course identifier
     * @return EnrollmentResult with success status and message
     */
    EnrollmentResult selfUnenroll(String studentId, String courseId);
    
    // ========== LECTURER/ADMIN MANUAL ENROLLMENT ==========
    
    /**
     * Manual enrollment by lecturer or admin
     * @param studentId the student identifier
     * @param courseId the course identifier
     * @param enrolledBy the lecturer/admin identifier
     * @param role the role of the person enrolling (LECTURER or ADMIN)
     * @return EnrollmentResult with success status and message
     */
    EnrollmentResult manualEnroll(String studentId, String courseId, String enrolledBy, UserRole role);
    
    /**
     * Manual unenrollment by lecturer or admin
     * @param studentId the student identifier
     * @param courseId the course identifier
     * @param unenrolledBy the lecturer/admin identifier
     * @param role the role of the person unenrolling
     * @return EnrollmentResult with success status and message
     */
    EnrollmentResult manualUnenroll(String studentId, String courseId, String unenrolledBy, UserRole role);
    
    // ========== VIEW AND FILTER ENROLLED STUDENTS ==========
    
    /**
     * Get all students enrolled in a course
     * @param courseId the course identifier
     * @return list of enrolled students
     */
    List<Student> getEnrolledStudents(String courseId);
    
    /**
     * Get enrolled students filtered by status
     * @param courseId the course identifier
     * @param status the enrollment status to filter by
     * @return list of students with matching status
     */
    List<Student> getEnrolledStudentsByStatus(String courseId, EnrollmentStatus status);
    
    /**
     * Get all courses a student is enrolled in
     * @param studentId the student identifier
     * @return list of courses
     */
    List<Course> getStudentCourses(String studentId);
    
    /**
     * Search enrolled students by name or ID
     * @param courseId the course identifier
     * @param searchTerm the search term
     * @return list of matching students
     */
    List<Student> searchEnrolledStudents(String courseId, String searchTerm);
    
    /**
     * Get enrollment details
     * @param studentId the student identifier
     * @param courseId the course identifier
     * @return enrollment object or null if not found
     */
    Enrollment getEnrollment(String studentId, String courseId);
    
    // ========== PREREQUISITES, LIMITS, RESTRICTIONS ==========
    
    /**
     * Check if student meets course prerequisites
     * @param studentId the student identifier
     * @param courseId the course identifier
     * @return true if prerequisites are met
     */
    boolean checkPrerequisites(String studentId, String courseId);
    
    /**
     * Check if course has reached enrollment limit
     * @param courseId the course identifier
     * @return true if course is full
     */
    boolean isCourseEnrollmentFull(String courseId);
    
    /**
     * Get available spots in a course
     * @param courseId the course identifier
     * @return number of available spots
     */
    int getAvailableSpots(String courseId);
    
    /**
     * Check if self-enrollment is allowed for a course
     * @param courseId the course identifier
     * @return true if self-enrollment is allowed
     */
    boolean isSelfEnrollmentAllowed(String courseId);
    
    // ========== GROUP MANAGEMENT ==========
    
    /**
     * Create a student group for projects/tutorials
     * @param groupId the group identifier
     * @param groupName the group name
     * @param courseId the course identifier
     * @param maxSize maximum group size
     * @return the created group
     */
    StudentGroup createGroup(String groupId, String groupName, String courseId, int maxSize);
    
    /**
     * Assign student to a group
     * @param studentId the student identifier
     * @param groupId the group identifier
     * @return true if assignment was successful
     */
    boolean assignStudentToGroup(String studentId, String groupId);
    
    /**
     * Remove student from a group
     * @param studentId the student identifier
     * @param groupId the group identifier
     * @return true if removal was successful
     */
    boolean removeStudentFromGroup(String studentId, String groupId);
    
    /**
     * Get all groups in a course
     * @param courseId the course identifier
     * @return list of groups
     */
    List<StudentGroup> getCourseGroups(String courseId);
    
    /**
     * Get student's group in a course
     * @param studentId the student identifier
     * @param courseId the course identifier
     * @return the group or null if not assigned
     */
    StudentGroup getStudentGroup(String studentId, String courseId);
    
    // ========== ATTENDANCE TRACKING ==========
    
    /**
     * Mark student attendance for a session
     * @param studentId the student identifier
     * @param courseId the course identifier
     * @param sessionDate the session date
     * @param present true if present, false if absent
     * @return the attendance record
     */
    AttendanceRecord markAttendance(String studentId, String courseId, String sessionDate, boolean present);
    
    /**
     * Get attendance records for a student in a course
     * @param studentId the student identifier
     * @param courseId the course identifier
     * @return list of attendance records
     */
    List<AttendanceRecord> getStudentAttendance(String studentId, String courseId);
    
    /**
     * Get attendance rate for a student in a course
     * @param studentId the student identifier
     * @param courseId the course identifier
     * @return attendance percentage (0-100)
     */
    double getAttendanceRate(String studentId, String courseId);
    
    /**
     * Get all attendance records for a course session
     * @param courseId the course identifier
     * @param sessionDate the session date
     * @return list of attendance records
     */
    List<AttendanceRecord> getSessionAttendance(String courseId, String sessionDate);
    
    // ========== PARTICIPATION TRACKING ==========
    
    /**
     * Update student participation score
     * @param studentId the student identifier
     * @param courseId the course identifier
     * @param score the participation score
     * @return true if update was successful
     */
    boolean updateParticipationScore(String studentId, String courseId, int score);
    
    /**
     * Get student participation score
     * @param studentId the student identifier
     * @param courseId the course identifier
     * @return participation score
     */
    int getParticipationScore(String studentId, String courseId);
    
    // ========== ENROLLMENT HISTORY ==========
    
    /**
     * Get enrollment history for a student in a course
     * @param studentId the student identifier
     * @param courseId the course identifier
     * @return list of history entries
     */
    List<String> getEnrollmentHistory(String studentId, String courseId);
    
    /**
     * Get all enrollments with their status
     * @param courseId the course identifier
     * @return list of all enrollments
     */
    List<Enrollment> getAllEnrollments(String courseId);
    
    // ========== COURSE AND STUDENT MANAGEMENT ==========
    
    /**
     * Register a new course
     * @param course the course to register
     * @return true if registration was successful
     */
    boolean registerCourse(Course course);
    
    /**
     * Register a new student
     * @param student the student to register
     * @return true if registration was successful
     */
    boolean registerStudent(Student student);
    
    /**
     * Get course information
     * @param courseId the course identifier
     * @return course object or null if not found
     */
    Course getCourse(String courseId);
    
    /**
     * Get student information
     * @param studentId the student identifier
     * @return student object or null if not found
     */
    Student getStudent(String studentId);
}
