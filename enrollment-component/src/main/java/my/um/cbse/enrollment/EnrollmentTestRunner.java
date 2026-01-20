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
import org.osgi.service.component.annotations.Reference;

import java.util.List;

/**
 * Comprehensive Test Component for Enrollment Service
 * Demonstrates all functionality
 */
@Component(immediate = true)
public class EnrollmentTestRunner {
    
    private EnrollmentService enrollmentService;
    
    @Reference
    public void setEnrollmentService(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║  ENROLLMENT SERVICE TEST RUNNER - INJECTED SUCCESSFULLY  ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");
    }
    
    @Activate
    public void activate() {
        System.out.println("Starting comprehensive tests in 2 seconds...");
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                runAllTests();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    private void runAllTests() {
        System.out.println("\n" + repeatString("=", 70));
        System.out.println("          ENROLLMENT SYSTEM - COMPREHENSIVE TEST SUITE");
        System.out.println(repeatString("=", 70) + "\n");
        
        // Initialize test data
        initializeTestData();
        
        // Run test suites
        testSelfEnrollment();
        testManualEnrollment();
        testPrerequisitesAndLimits();
        testGroupManagement();
        testAttendanceTracking();
        testParticipationTracking();
        testViewAndFilter();
        
        System.out.println("\n" + repeatString("=", 70));
        System.out.println("                    ALL TESTS COMPLETED");
        System.out.println(repeatString("=", 70) + "\n");
    }
    
    private void initializeTestData() {
        printSection("INITIALIZING TEST DATA");
        
        // Register students
        Student alice = new Student("STU001", "Alice Johnson", "alice@university.edu");
        alice.getCompletedCourses().add("CS101"); // Has completed CS101
        enrollmentService.registerStudent(alice);
        
        Student bob = new Student("STU002", "Bob Smith", "bob@university.edu");
        // Bob hasn't completed any courses yet
        enrollmentService.registerStudent(bob);
        
        Student charlie = new Student("STU003", "Charlie Kirk", "charlie@university.edu");
        charlie.setRole(UserRole.LECTURER);
        enrollmentService.registerStudent(charlie);
        
        Student diana = new Student("STU004", "Diana Prince", "diana@university.edu");
        enrollmentService.registerStudent(diana);
        
        // Register lecturers
        Student lecturer1 = new Student("LEC001", "Dr. Emma Wilson", "emma.wilson@university.edu");
        lecturer1.setRole(UserRole.LECTURER);
        enrollmentService.registerStudent(lecturer1);
        
        Student lecturer2 = new Student("LEC002", "Prof. James Anderson", "james.anderson@university.edu");
        lecturer2.setRole(UserRole.LECTURER);
        enrollmentService.registerStudent(lecturer2);
        
        Student lecturer3 = new Student("LEC003", "Dr. Sarah Martinez", "sarah.martinez@university.edu");
        lecturer3.setRole(UserRole.LECTURER);
        enrollmentService.registerStudent(lecturer3);
        
        // Register admin
        Student admin = new Student("ADMIN001", "Admin User", "admin@university.edu");
        admin.setRole(UserRole.ADMIN);
        enrollmentService.registerStudent(admin);
        
        System.out.println("✓ Test data initialized: 4 students, 3 lecturers, 1 admin, 3 courses (from service)");
    }
    
    private void testSelfEnrollment() {
        printSection("TEST 1: STUDENT SELF-ENROLLMENT");
        
        // Test 1.1: Successful self-enrollment
        System.out.println("\nTest 1.1: Alice self-enrolls in CS101");
        EnrollmentResult result = enrollmentService.selfEnroll("STU001", "CS101");
        printResult(result);
        
        // Test 1.2: Duplicate enrollment attempt
        System.out.println("\nTest 1.2: Alice tries to enroll in CS101 again (should fail)");
        result = enrollmentService.selfEnroll("STU001", "CS101");
        printResult(result);
        
        // Test 1.3: Self-unenrollment
        System.out.println("\nTest 1.3: Alice unenrolls from CS101");
        result = enrollmentService.selfUnenroll("STU001", "CS101");
        printResult(result);
        
        // Test 1.4: Re-enroll after unenrollment
        System.out.println("\nTest 1.4: Alice re-enrolls in CS101");
        result = enrollmentService.selfEnroll("STU001", "CS101");
        printResult(result);
    }
    
    private void testManualEnrollment() {
        printSection("TEST 2: LECTURER/ADMIN MANUAL ENROLLMENT");
        
        // Test 2.1: Lecturer manually enrolls student
        System.out.println("\nTest 2.1: Lecturer manually enrolls Bob in CS101");
        EnrollmentResult result = enrollmentService.manualEnroll("STU002", "CS101", "LEC001", UserRole.LECTURER);
        printResult(result);
        
        // Test 2.2: Attempt manual enrollment without permission
        System.out.println("\nTest 2.2: Student tries manual enrollment (should fail)");
        result = enrollmentService.manualEnroll("STU003", "CS101", "STU001", UserRole.STUDENT);
        printResult(result);
        
        // Test 2.3: Admin manually unenrolls student
        System.out.println("\nTest 2.3: Admin manually unenrolls Bob from CS101");
        result = enrollmentService.manualUnenroll("STU002", "CS101", "ADMIN001", UserRole.ADMIN);
        printResult(result);
    }
    
    private void testPrerequisitesAndLimits() {
        printSection("TEST 3: PREREQUISITES, LIMITS & RESTRICTIONS");
        
        // Test 3.1: Enroll in course with prerequisites (should fail)
        System.out.println("\nTest 3.1: Bob tries to enroll in CS102 without completing CS101");
        EnrollmentResult result = enrollmentService.selfEnroll("STU002", "CS102");
        printResult(result);
        
        // Test 3.2: Enroll with prerequisites met (should succeed)
        System.out.println("\nTest 3.2: Alice enrolls in CS102 (has completed CS101)");
        result = enrollmentService.selfEnroll("STU001", "CS102");
        printResult(result);
        
        // Test 3.3: Check available spots
        System.out.println("\nTest 3.3: Check available spots in CS101");
        int spots = enrollmentService.getAvailableSpots("CS101");
        System.out.println("Available spots: " + spots + "/30");
        
        // Test 3.4: Try self-enrollment on restricted course
        System.out.println("\nTest 3.4: Alice tries self-enrollment in CS201 (manual enrollment only)");
        result = enrollmentService.selfEnroll("STU001", "CS201");
        printResult(result);
        
        // Test 3.5: Manual enrollment in restricted course
        System.out.println("\nTest 3.5: Admin manually enrolls Alice in CS201");
        result = enrollmentService.manualEnroll("STU001", "CS201", "ADMIN001", UserRole.ADMIN);
        printResult(result);
    }
    
    private void testGroupManagement() {
        printSection("TEST 4: GROUP MANAGEMENT");
        
        // Test 4.1: Create groups
        System.out.println("\nTest 4.1: Create project groups for CS101");
        StudentGroup group1 = enrollmentService.createGroup("G001", "Team Alpha", "CS101", 3);
        StudentGroup group2 = enrollmentService.createGroup("G002", "Team Beta", "CS101", 3);
        System.out.println("✓ Created: " + group1.getGroupName() + " and " + group2.getGroupName());
        
        // Test 4.2: Assign students to groups
        System.out.println("\nTest 4.2: Assign Alice and Diana to Team Alpha");
        enrollmentService.selfEnroll("STU004", "CS101"); // Diana enrolls first
        boolean assigned1 = enrollmentService.assignStudentToGroup("STU001", "G001");
        boolean assigned2 = enrollmentService.assignStudentToGroup("STU004", "G001");
        System.out.println(assigned1 && assigned2 ? "✓ Students assigned successfully" : "✗ Assignment failed");
        
        // Test 4.3: Get student's group
        System.out.println("\nTest 4.3: Get Alice's group in CS101");
        StudentGroup aliceGroup = enrollmentService.getStudentGroup("STU001", "CS101");
        if (aliceGroup != null) {
            System.out.println("✓ Alice is in: " + aliceGroup.getGroupName());
        }
        
        // Test 4.4: List all groups in course
        System.out.println("\nTest 4.4: List all groups in CS101");
        List<StudentGroup> groups = enrollmentService.getCourseGroups("CS101");
        groups.forEach(g -> System.out.println("  - " + g));
    }
    
    private void testAttendanceTracking() {
        printSection("TEST 5: ATTENDANCE TRACKING");
        
        // Test 5.1: Mark attendance
        System.out.println("\nTest 5.1: Mark attendance for Week 1");
        enrollmentService.markAttendance("STU001", "CS101", "2024-01-15", true);
        enrollmentService.markAttendance("STU004", "CS101", "2024-01-15", true);
        enrollmentService.markAttendance("STU001", "CS101", "2024-01-17", false);
        enrollmentService.markAttendance("STU004", "CS101", "2024-01-17", true);
        
        // Test 5.2: Get attendance records
        System.out.println("\nTest 5.2: Get Alice's attendance in CS101");
        List<AttendanceRecord> records = enrollmentService.getStudentAttendance("STU001", "CS101");
        System.out.println("Total sessions: " + records.size());
        records.forEach(r -> System.out.println("  - " + r.getSessionDate() + ": " + (r.isPresent() ? "Present" : "Absent")));
        
        // Test 5.3: Calculate attendance rate
        System.out.println("\nTest 5.3: Calculate Alice's attendance rate");
        double rate = enrollmentService.getAttendanceRate("STU001", "CS101");
        System.out.println("Attendance rate: " + String.format("%.1f", rate) + "%");
        
        // Test 5.4: Get session attendance
        System.out.println("\nTest 5.4: Get all attendance for session 2024-01-15");
        List<AttendanceRecord> sessionRecords = enrollmentService.getSessionAttendance("CS101", "2024-01-15");
        System.out.println("Students present: " + sessionRecords.stream().filter(AttendanceRecord::isPresent).count() + "/" + sessionRecords.size());
    }
    
    private void testParticipationTracking() {
        printSection("TEST 6: PARTICIPATION TRACKING");
        
        // Test 6.1: Update participation scores
        System.out.println("\nTest 6.1: Update participation scores");
        enrollmentService.updateParticipationScore("STU001", "CS101", 85);
        enrollmentService.updateParticipationScore("STU004", "CS101", 92);
        System.out.println("✓ Scores updated");
        
        // Test 6.2: Get participation scores
        System.out.println("\nTest 6.2: Get participation scores");
        int aliceScore = enrollmentService.getParticipationScore("STU001", "CS101");
        int dianaScore = enrollmentService.getParticipationScore("STU004", "CS101");
        System.out.println("Alice: " + aliceScore + " points");
        System.out.println("Diana: " + dianaScore + " points");
        
        // Test 6.3: View enrollment history
        System.out.println("\nTest 6.3: View Alice's enrollment history in CS101");
        List<String> history = enrollmentService.getEnrollmentHistory("STU001", "CS101");
        history.forEach(entry -> System.out.println("  " + entry));
    }
    
    private void testViewAndFilter() {
        printSection("TEST 7: VIEW AND FILTER ENROLLED STUDENTS");
        
        // Test 7.1: Get all enrolled students
        System.out.println("\nTest 7.1: Get all students enrolled in CS101");
        List<Student> enrolled = enrollmentService.getEnrolledStudents("CS101");
        System.out.println("Total enrolled: " + enrolled.size());
        enrolled.forEach(s -> System.out.println("  - " + s.getStudentId() + ": " + s.getName()));
        
        // Test 7.2: Get students by status
        System.out.println("\nTest 7.2: Get ACTIVE enrollments in CS101");
        List<Student> activeStudents = enrollmentService.getEnrolledStudentsByStatus("CS101", EnrollmentStatus.ACTIVE);
        System.out.println("Active students: " + activeStudents.size());
        
        // Test 7.3: Search students
        System.out.println("\nTest 7.3: Search for students with 'alice' in CS101");
        List<Student> searchResults = enrollmentService.searchEnrolledStudents("CS101", "alice");
        searchResults.forEach(s -> System.out.println("  Found: " + s.getName()));
        
        // Test 7.4: Get student's courses
        System.out.println("\nTest 7.4: Get all courses Alice is enrolled in");
        List<Course> courses = enrollmentService.getStudentCourses("STU001");
        courses.forEach(c -> System.out.println("  - " + c.getCourseId() + ": " + c.getCourseName()));
        
        // Test 7.5: Get all enrollments with details
        System.out.println("\nTest 7.5: Get all enrollment records for CS101");
        List<Enrollment> allEnrollments = enrollmentService.getAllEnrollments("CS101");
        System.out.println("Total enrollment records: " + allEnrollments.size());
        allEnrollments.forEach(e -> System.out.println("  - " + e));
    }
    
    // Helper methods
    
    private void printSection(String title) {
        System.out.println("\n" + repeatString("-", 70));
        System.out.println("  " + title);
        System.out.println(repeatString("-", 70));
    }
    
    private String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
    
    private void printResult(EnrollmentResult result) {
        String status = result.isSuccess() ? "✓ SUCCESS" : "✗ FAILED";
        System.out.println(status + ": " + result.getMessage());
        if (result.getErrorCode() != null) {
            System.out.println("  Error Code: " + result.getErrorCode());
        }
    }
}
