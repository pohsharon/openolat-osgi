package my.um.cbse.enrollment;

import my.um.cbse.api.model.AttendanceRecord;
import my.um.cbse.api.model.Course;
import my.um.cbse.api.model.Enrollment;
import my.um.cbse.api.model.EnrollmentResult;
import my.um.cbse.api.model.EnrollmentStatus;
import my.um.cbse.api.model.Student;
import my.um.cbse.api.model.StudentGroup;
import my.um.cbse.api.model.UserRole;
import my.um.cbse.api.service.EnrollmentService;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Comprehensive Enrollment Service Implementation
 * Registered as an OSGi Declarative Service
 */
@Component(
    service = EnrollmentService.class,
    immediate = true
)
public class EnrollmentServiceImpl implements EnrollmentService {
    
    // Data stores (replace with database in production)
    private final Map<String, Enrollment> enrollments = new HashMap<>(); // key: studentId_courseId
    private final Map<String, Course> courses = new HashMap<>();
    private final Map<String, Student> students = new HashMap<>();
    private final Map<String, StudentGroup> groups = new HashMap<>();
    private final Map<String, AttendanceRecord> attendanceRecords = new HashMap<>();
    
    @Activate
    public void activate() {
        System.out.println("=== EnrollmentServiceImpl ACTIVATED ===");
        initializeSampleData();
    }
    
    @Deactivate
    public void deactivate() {
        System.out.println("=== EnrollmentServiceImpl DEACTIVATED ===");
    }
    
    private void initializeSampleData() {
        // Sample courses
        Course cs101 = new Course("CS101", "Introduction to Programming", "LEC001");
        cs101.setMaxStudents(30);
        cs101.setSelfEnrollmentAllowed(true);
        cs101.setSemester("2024-1");
        registerCourse(cs101);
        
        Course cs102 = new Course("CS102", "Data Structures", "LEC001");
        cs102.setMaxStudents(25);
        cs102.getPrerequisites().add("CS101");
        cs102.setSelfEnrollmentAllowed(true);
        registerCourse(cs102);
        
        Course cs201 = new Course("CS201", "Algorithms", "LEC002");
        cs201.setMaxStudents(20);
        cs201.getPrerequisites().add("CS102");
        cs201.setSelfEnrollmentAllowed(false); // Requires manual enrollment
        registerCourse(cs201);
        
        System.out.println("✓ Sample courses initialized: CS101, CS102, CS201");
    }
    
    // ========== STUDENT SELF-ENROLLMENT ==========
    
    @Override
    public EnrollmentResult selfEnroll(String studentId, String courseId) {
        System.out.println("\n[SELF-ENROLL] Student: " + studentId + ", Course: " + courseId);
        
        // Validation
        if (studentId == null || courseId == null) {
            return new EnrollmentResult(false, "Invalid student ID or course ID", "INVALID_INPUT");
        }
        
        Student student = students.get(studentId);
        if (student == null) {
            return new EnrollmentResult(false, "Student not found: " + studentId, "STUDENT_NOT_FOUND");
        }
        
        Course course = courses.get(courseId);
        if (course == null) {
            return new EnrollmentResult(false, "Course not found: " + courseId, "COURSE_NOT_FOUND");
        }
        
        // Check if self-enrollment is allowed
        if (!course.isSelfEnrollmentAllowed()) {
            return new EnrollmentResult(false, "Self-enrollment not allowed for this course. Contact lecturer.", "SELF_ENROLL_DISABLED");
        }
        
        // Check if already enrolled
        String key = makeKey(studentId, courseId);
        if (enrollments.containsKey(key)) {
            Enrollment existing = enrollments.get(key);
            if (existing.getStatus() == EnrollmentStatus.ACTIVE) {
                return new EnrollmentResult(false, "Already enrolled in this course", "ALREADY_ENROLLED");
            }
        }
        
        // Check prerequisites
        if (!checkPrerequisites(studentId, courseId)) {
            return new EnrollmentResult(false, "Prerequisites not met: " + course.getPrerequisites(), "PREREQUISITES_NOT_MET");
        }
        
        // Check enrollment limit
        if (isCourseEnrollmentFull(courseId)) {
            return new EnrollmentResult(false, "Course is full (max: " + course.getMaxStudents() + ")", "COURSE_FULL");
        }
        
        // Create enrollment
        Enrollment enrollment = new Enrollment(studentId, courseId);
        enrollment.setSelfEnrolled(true);
        enrollment.setEnrolledBy(studentId);
        enrollments.put(key, enrollment);
        
        System.out.println("✓ Self-enrollment successful!");
        return new EnrollmentResult(true, "Successfully enrolled in " + course.getCourseName());
    }
    
    @Override
    public EnrollmentResult selfUnenroll(String studentId, String courseId) {
        System.out.println("\n[SELF-UNENROLL] Student: " + studentId + ", Course: " + courseId);
        
        String key = makeKey(studentId, courseId);
        Enrollment enrollment = enrollments.get(key);
        
        if (enrollment == null || enrollment.getStatus() != EnrollmentStatus.ACTIVE) {
            return new EnrollmentResult(false, "Not enrolled in this course", "NOT_ENROLLED");
        }
        
        enrollment.setStatus(EnrollmentStatus.DROPPED);
        enrollment.addToHistory("Self-unenrolled");
        
        System.out.println("✓ Self-unenrollment successful!");
        return new EnrollmentResult(true, "Successfully unenrolled from course");
    }
    
    // ========== LECTURER/ADMIN MANUAL ENROLLMENT ==========
    
    @Override
    public EnrollmentResult manualEnroll(String studentId, String courseId, String enrolledBy, UserRole role) {
        System.out.println("\n[MANUAL-ENROLL] Student: " + studentId + ", Course: " + courseId + ", By: " + enrolledBy + " (" + role + ")");
        
        // Authorization check
        if (role != UserRole.LECTURER && role != UserRole.ADMIN) {
            return new EnrollmentResult(false, "Insufficient permissions", "UNAUTHORIZED");
        }
        
        // Validation
        if (studentId == null || courseId == null) {
            return new EnrollmentResult(false, "Invalid student ID or course ID", "INVALID_INPUT");
        }
        
        Student student = students.get(studentId);
        if (student == null) {
            return new EnrollmentResult(false, "Student not found: " + studentId, "STUDENT_NOT_FOUND");
        }
        
        Course course = courses.get(courseId);
        if (course == null) {
            return new EnrollmentResult(false, "Course not found: " + courseId, "COURSE_NOT_FOUND");
        }
        
        // Check if already enrolled
        String key = makeKey(studentId, courseId);
        if (enrollments.containsKey(key)) {
            Enrollment existing = enrollments.get(key);
            if (existing.getStatus() == EnrollmentStatus.ACTIVE) {
                return new EnrollmentResult(false, "Already enrolled in this course", "ALREADY_ENROLLED");
            }
        }
        
        // Check enrollment limit (admin can override, lecturer cannot)
        if (role == UserRole.LECTURER && isCourseEnrollmentFull(courseId)) {
            return new EnrollmentResult(false, "Course is full. Contact admin for override.", "COURSE_FULL");
        }
        
        // Create enrollment
        Enrollment enrollment = new Enrollment(studentId, courseId, enrolledBy);
        enrollments.put(key, enrollment);
        
        System.out.println("✓ Manual enrollment successful!");
        return new EnrollmentResult(true, "Student manually enrolled by " + role);
    }
    
    @Override
    public EnrollmentResult manualUnenroll(String studentId, String courseId, String unenrolledBy, UserRole role) {
        System.out.println("\n[MANUAL-UNENROLL] Student: " + studentId + ", Course: " + courseId + ", By: " + unenrolledBy);
        
        if (role != UserRole.LECTURER && role != UserRole.ADMIN) {
            return new EnrollmentResult(false, "Insufficient permissions", "UNAUTHORIZED");
        }
        
        String key = makeKey(studentId, courseId);
        Enrollment enrollment = enrollments.get(key);
        
        if (enrollment == null || enrollment.getStatus() != EnrollmentStatus.ACTIVE) {
            return new EnrollmentResult(false, "Student not enrolled in this course", "NOT_ENROLLED");
        }
        
        enrollment.setStatus(EnrollmentStatus.DROPPED);
        enrollment.addToHistory("Manually unenrolled by " + unenrolledBy + " (" + role + ")");
        
        System.out.println("✓ Manual unenrollment successful!");
        return new EnrollmentResult(true, "Student manually unenrolled");
    }
    
    // ========== VIEW AND FILTER ENROLLED STUDENTS ==========
    
    @Override
    public List<Student> getEnrolledStudents(String courseId) {
        return enrollments.values().stream()
            .filter(e -> e.getCourseId().equals(courseId))
            .filter(e -> e.getStatus() == EnrollmentStatus.ACTIVE)
            .map(e -> students.get(e.getStudentId()))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Student> getEnrolledStudentsByStatus(String courseId, EnrollmentStatus status) {
        return enrollments.values().stream()
            .filter(e -> e.getCourseId().equals(courseId))
            .filter(e -> e.getStatus() == status)
            .map(e -> students.get(e.getStudentId()))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Course> getStudentCourses(String studentId) {
        return enrollments.values().stream()
            .filter(e -> e.getStudentId().equals(studentId))
            .filter(e -> e.getStatus() == EnrollmentStatus.ACTIVE)
            .map(e -> courses.get(e.getCourseId()))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Student> searchEnrolledStudents(String courseId, String searchTerm) {
        String lowerSearch = searchTerm.toLowerCase();
        return getEnrolledStudents(courseId).stream()
            .filter(s -> s.getStudentId().toLowerCase().contains(lowerSearch) ||
                        s.getName().toLowerCase().contains(lowerSearch) ||
                        s.getEmail().toLowerCase().contains(lowerSearch))
            .collect(Collectors.toList());
    }
    
    @Override
    public Enrollment getEnrollment(String studentId, String courseId) {
        return enrollments.get(makeKey(studentId, courseId));
    }
    
    // ========== PREREQUISITES, LIMITS, RESTRICTIONS ==========
    
    @Override
    public boolean checkPrerequisites(String studentId, String courseId) {
        Course course = courses.get(courseId);
        if (course == null || course.getPrerequisites().isEmpty()) {
            return true;
        }
        
        Student student = students.get(studentId);
        if (student == null) {
            return false;
        }
        
        // Check if student has completed all prerequisites
        for (String prereq : course.getPrerequisites()) {
            if (!student.getCompletedCourses().contains(prereq)) {
                System.out.println("✗ Prerequisite not met: " + prereq);
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isCourseEnrollmentFull(String courseId) {
        Course course = courses.get(courseId);
        if (course == null) {
            return true;
        }
        
        long activeEnrollments = enrollments.values().stream()
            .filter(e -> e.getCourseId().equals(courseId))
            .filter(e -> e.getStatus() == EnrollmentStatus.ACTIVE)
            .count();
        
        return activeEnrollments >= course.getMaxStudents();
    }
    
    @Override
    public int getAvailableSpots(String courseId) {
        Course course = courses.get(courseId);
        if (course == null) {
            return 0;
        }
        
        long activeEnrollments = enrollments.values().stream()
            .filter(e -> e.getCourseId().equals(courseId))
            .filter(e -> e.getStatus() == EnrollmentStatus.ACTIVE)
            .count();
        
        return Math.max(0, course.getMaxStudents() - (int)activeEnrollments);
    }
    
    @Override
    public boolean isSelfEnrollmentAllowed(String courseId) {
        Course course = courses.get(courseId);
        return course != null && course.isSelfEnrollmentAllowed();
    }
    
    // ========== GROUP MANAGEMENT ==========
    
    @Override
    public StudentGroup createGroup(String groupId, String groupName, String courseId, int maxSize) {
        StudentGroup group = new StudentGroup(groupId, groupName, courseId);
        group.setMaxSize(maxSize);
        groups.put(groupId, group);
        System.out.println("✓ Group created: " + groupName);
        return group;
    }
    
    @Override
    public boolean assignStudentToGroup(String studentId, String groupId) {
        StudentGroup group = groups.get(groupId);
        if (group == null) {
            System.err.println("✗ Group not found: " + groupId);
            return false;
        }
        
        if (group.isFull()) {
            System.err.println("✗ Group is full");
            return false;
        }
        
        // Check if student is enrolled in the course
        String key = makeKey(studentId, group.getCourseId());
        Enrollment enrollment = enrollments.get(key);
        if (enrollment == null || enrollment.getStatus() != EnrollmentStatus.ACTIVE) {
            System.err.println("✗ Student not enrolled in course");
            return false;
        }
        
        group.getStudentIds().add(studentId);
        enrollment.setGroupId(groupId);
        System.out.println("✓ Student " + studentId + " assigned to group " + group.getGroupName());
        return true;
    }
    
    @Override
    public boolean removeStudentFromGroup(String studentId, String groupId) {
        StudentGroup group = groups.get(groupId);
        if (group == null) {
            return false;
        }
        
        boolean removed = group.getStudentIds().remove(studentId);
        
        // Update enrollment
        String key = makeKey(studentId, group.getCourseId());
        Enrollment enrollment = enrollments.get(key);
        if (enrollment != null) {
            enrollment.setGroupId(null);
        }
        
        return removed;
    }
    
    @Override
    public List<StudentGroup> getCourseGroups(String courseId) {
        return groups.values().stream()
            .filter(g -> g.getCourseId().equals(courseId))
            .collect(Collectors.toList());
    }
    
    @Override
    public StudentGroup getStudentGroup(String studentId, String courseId) {
        String key = makeKey(studentId, courseId);
        Enrollment enrollment = enrollments.get(key);
        
        if (enrollment != null && enrollment.getGroupId() != null) {
            return groups.get(enrollment.getGroupId());
        }
        
        return null;
    }
    
    // ========== ATTENDANCE TRACKING ==========
    
    @Override
    public AttendanceRecord markAttendance(String studentId, String courseId, String sessionDate, boolean present) {
        String recordId = studentId + "_" + courseId + "_" + sessionDate;
        AttendanceRecord record = new AttendanceRecord(recordId, studentId, courseId, sessionDate);
        record.setPresent(present);
        attendanceRecords.put(recordId, record);
        
        System.out.println((present ? "✓ Present" : "✗ Absent") + " - " + studentId + " in " + courseId + " on " + sessionDate);
        return record;
    }
    
    @Override
    public List<AttendanceRecord> getStudentAttendance(String studentId, String courseId) {
        return attendanceRecords.values().stream()
            .filter(r -> r.getStudentId().equals(studentId) && r.getCourseId().equals(courseId))
            .collect(Collectors.toList());
    }
    
    @Override
    public double getAttendanceRate(String studentId, String courseId) {
        List<AttendanceRecord> records = getStudentAttendance(studentId, courseId);
        if (records.isEmpty()) {
            return 0.0;
        }
        
        long presentCount = records.stream().filter(AttendanceRecord::isPresent).count();
        return (presentCount * 100.0) / records.size();
    }
    
    @Override
    public List<AttendanceRecord> getSessionAttendance(String courseId, String sessionDate) {
        return attendanceRecords.values().stream()
            .filter(r -> r.getCourseId().equals(courseId) && r.getSessionDate().equals(sessionDate))
            .collect(Collectors.toList());
    }
    
    // ========== PARTICIPATION TRACKING ==========
    
    @Override
    public boolean updateParticipationScore(String studentId, String courseId, int score) {
        String key = makeKey(studentId, courseId);
        Enrollment enrollment = enrollments.get(key);
        
        if (enrollment == null) {
            return false;
        }
        
        enrollment.setParticipationScore(score);
        enrollment.addToHistory("Participation score updated to " + score);
        return true;
    }
    
    @Override
    public int getParticipationScore(String studentId, String courseId) {
        String key = makeKey(studentId, courseId);
        Enrollment enrollment = enrollments.get(key);
        return enrollment != null ? enrollment.getParticipationScore() : 0;
    }
    
    // ========== ENROLLMENT HISTORY ==========
    
    @Override
    public List<String> getEnrollmentHistory(String studentId, String courseId) {
        String key = makeKey(studentId, courseId);
        Enrollment enrollment = enrollments.get(key);
        return enrollment != null ? enrollment.getEnrollmentHistory() : new ArrayList<>();
    }
    
    @Override
    public List<Enrollment> getAllEnrollments(String courseId) {
        return enrollments.values().stream()
            .filter(e -> e.getCourseId().equals(courseId))
            .collect(Collectors.toList());
    }
    
    // ========== COURSE AND STUDENT MANAGEMENT ==========
    
    @Override
    public boolean registerCourse(Course course) {
        if (course == null || course.getCourseId() == null) {
            return false;
        }
        courses.put(course.getCourseId(), course);
        System.out.println("✓ Course registered: " + course.getCourseId() + " - " + course.getCourseName());
        return true;
    }
    
    @Override
    public boolean registerStudent(Student student) {
        if (student == null || student.getStudentId() == null) {
            return false;
        }
        students.put(student.getStudentId(), student);
        System.out.println("✓ Student registered: " + student.getStudentId() + " - " + student.getName());
        return true;
    }
    
    @Override
    public Course getCourse(String courseId) {
        return courses.get(courseId);
    }
    
    @Override
    public Student getStudent(String studentId) {
        return students.get(studentId);
    }
    
    // ========== HELPER METHODS ==========
    
    private String makeKey(String studentId, String courseId) {
        return studentId + "_" + courseId;
    }
}
