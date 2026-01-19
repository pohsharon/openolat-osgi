package my.um.cbse.enrollment;

import my.um.cbse.api.Enrollment;
import my.um.cbse.api.EnrollmentService;
import org.osgi.service.component.annotations.Component;
import java.util.*;

/**
 * Enrollment Service Implementation
 * Registered as an OSGi Declarative Service
 */
@Component(
    service = EnrollmentService.class,
    immediate = true
)
public class EnrollmentServiceImpl implements EnrollmentService {
    
    // In-memory storage for enrollments (replace with database in production)
    private Map<String, Set<String>> studentCourses = new HashMap<>();
    private Map<String, Set<String>> courseStudents = new HashMap<>();
    
    public EnrollmentServiceImpl() {
        System.out.println("EnrollmentServiceImpl created");
    }
    
    @Override
    public boolean enrollStudent(String studentId, String courseId) {
        if (studentId == null || courseId == null) {
            System.err.println("Invalid studentId or courseId: " + studentId + ", " + courseId);
            return false;
        }
        
        if (isEnrolled(studentId, courseId)) {
            System.out.println("Student " + studentId + " already enrolled in course " + courseId);
            return false;
        }
        
        // Add student to course
        studentCourses.computeIfAbsent(studentId, k -> new HashSet<>()).add(courseId);
        
        // Add course to student's enrollment list
        courseStudents.computeIfAbsent(courseId, k -> new HashSet<>()).add(studentId);
        
        System.out.println("✓ Student " + studentId + " enrolled in course " + courseId);
        return true;
    }
    
    @Override
    public boolean unenrollStudent(String studentId, String courseId) {
        if (studentId == null || courseId == null) {
            return false;
        }
        
        if (!isEnrolled(studentId, courseId)) {
            System.out.println("Student " + studentId + " is not enrolled in course " + courseId);
            return false;
        }
        
        studentCourses.getOrDefault(studentId, new HashSet<>()).remove(courseId);
        courseStudents.getOrDefault(courseId, new HashSet<>()).remove(studentId);
        
        System.out.println("✓ Student " + studentId + " unenrolled from course " + courseId);
        return true;
    }
    
    @Override
    public boolean isEnrolled(String studentId, String courseId) {
        if (studentId == null || courseId == null) {
            return false;
        }
        
        return studentCourses.getOrDefault(studentId, new HashSet<>()).contains(courseId);
    }
    
    @Override
    public String[] getStudentCourses(String studentId) {
        if (studentId == null) {
            return new String[0];
        }
        
        Set<String> courses = studentCourses.getOrDefault(studentId, new HashSet<>());
        return courses.toArray(new String[0]);
    }
    
    @Override
    public String[] getEnrolledStudents(String courseId) {
        if (courseId == null) {
            return new String[0];
        }
        
        Set<String> students = courseStudents.getOrDefault(courseId, new HashSet<>());
        return students.toArray(new String[0]);
    }
}
