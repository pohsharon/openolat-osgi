package my.um.cbse.api;

/**
 * Enrollment Service Interface
 * Defines the contract for enrollment operations
 */
public interface EnrollmentService {
    
    /**
     * Enroll a student in a course
     * @param studentId the student identifier
     * @param courseId the course identifier
     * @return true if enrollment was successful, false otherwise
     */
    boolean enrollStudent(String studentId, String courseId);
    
    /**
     * Unenroll a student from a course
     * @param studentId the student identifier
     * @param courseId the course identifier
     * @return true if unenrollment was successful, false otherwise
     */
    boolean unenrollStudent(String studentId, String courseId);
    
    /**
     * Check if a student is enrolled in a course
     * @param studentId the student identifier
     * @param courseId the course identifier
     * @return true if student is enrolled, false otherwise
     */
    boolean isEnrolled(String studentId, String courseId);
    
    /**
     * Get all courses a student is enrolled in
     * @param studentId the student identifier
     * @return array of course IDs
     */
    String[] getStudentCourses(String studentId);
    
    /**
     * Get all students enrolled in a course
     * @param courseId the course identifier
     * @return array of student IDs
     */
    String[] getEnrolledStudents(String courseId);
}
