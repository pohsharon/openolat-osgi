package my.um.cbse.course;

import my.um.cbse.api.model.*;
import my.um.cbse.api.service.CourseService;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Arrays;
import java.util.List;

/**
 * Comprehensive Test Component for Course Management Service
 * Demonstrates all 5 Use Cases
 */
@Component(immediate = true)
public class CourseTestRunner {
    
    private CourseService courseService;
    
    @Reference
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║    COURSE SERVICE TEST RUNNER - INJECTED SUCCESSFULLY    ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");
    }
    
    @Activate
    public void activate() {
        System.out.println("Starting Course Management tests in 2 seconds...");
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
        System.out.println("       COURSE MANAGEMENT SYSTEM - COMPREHENSIVE TEST SUITE");
        System.out.println(repeatString("=", 70) + "\n");
        
        // Run test suites for each Use Case
        testUC1_CreateCourse();
        testUC2_ManageCourseSettings();
        testUC3_AssignRemoveStaff();
        testUC4_DesignCourseStructure();
        testUC5_ManageLearningMaterials();
        
        System.out.println("\n" + repeatString("=", 70));
        System.out.println("                    ALL TESTS COMPLETED");
        System.out.println(repeatString("=", 70) + "\n");
    }
    
    // ==================== UC1: CREATE COURSE ====================
    private void testUC1_CreateCourse() {
        printSection("UC1: CREATE COURSE");
        
        // Test 1.1: Create a new course with Learning Path design
        System.out.println("\nTest 1.1: Create new course (Learning Path)");
        CourseResult result = courseService.createCourse(
            "Web Development Fundamentals",
            "Learn HTML, CSS, and JavaScript basics",
            CourseDesign.LEARNING_PATH,
            "2024-2",
            "COACH001"
        );
        printResult(result);
        String newCourseId = null;
        if (result.isSuccess() && result.getData() instanceof Course) {
            newCourseId = ((Course) result.getData()).getCourseId();
        }
        
        // Test 1.2: Create course with Conventional design
        System.out.println("\nTest 1.2: Create new course (Conventional)");
        result = courseService.createCourse(
            "Advanced Java Programming",
            CourseDesign.CONVENTIONAL,
            "COACH002"
        );
        printResult(result);
        
        // Test 1.3: Copy an existing course
        System.out.println("\nTest 1.3: Copy existing course CS101");
        result = courseService.copyCourse("CS101", "CS101 - Copy for 2024-2", "COACH001");
        printResult(result);
        
        // Test 1.4: Try to create course with missing title (should fail)
        System.out.println("\nTest 1.4: Create course without title (expect failure)");
        result = courseService.createCourse(null, CourseDesign.LEARNING_PATH, "COACH001");
        printResult(result);
        
        // Test 1.5: Delete course
        if (newCourseId != null) {
            System.out.println("\nTest 1.5: Delete course " + newCourseId);
            result = courseService.deleteCourse(newCourseId, "COACH001");
            printResult(result);
        }
        
        // Test 1.6: List all courses
        System.out.println("\nTest 1.6: List all courses");
        List<Course> courses = courseService.getAllCourses();
        System.out.println("  Found " + courses.size() + " courses:");
        for (Course c : courses) {
            System.out.println("    - " + c.getCourseId() + ": " + c.getCourseName() + " [" + c.getStatus() + "]");
        }
    }
    
    // ==================== UC2: MANAGE COURSE DETAILS ====================
    private void testUC2_ManageCourseSettings() {
        printSection("UC2: MANAGE COURSE DETAILS AND SETTINGS");
        
        String courseId = "CS101";
        String userId = "COACH001";
        
        // Test 2.1: Get course info
        System.out.println("\nTest 2.1: Get course info for CS101");
        Course course = courseService.getCourse(courseId);
        if (course != null) {
            System.out.println("  ✓ Found: " + course);
        } else {
            System.out.println("  ✗ Course not found");
        }
        
        // Test 2.2: Update course info
        System.out.println("\nTest 2.2: Update course title and description");
        CourseResult result = courseService.updateCourseInfo(
            courseId,
            "Introduction to Programming (Updated)",
            "Updated description: Learn programming fundamentals with Java",
            "CS101-NEW",
            userId
        );
        printResult(result);
        
        // Test 2.3: Update course metadata
        System.out.println("\nTest 2.3: Update course metadata");
        result = courseService.updateCourseMetadata(
            courseId,
            "2024-2",
            4,
            "CC BY-SA 4.0",
            "Computer Science Department",
            userId
        );
        printResult(result);
        
        // Test 2.4: Update execution settings
        System.out.println("\nTest 2.4: Enable lecture and absence management");
        result = courseService.updateExecutionSettings(courseId, true, true, userId);
        printResult(result);
        
        // Test 2.5: Update course status (publish)
        System.out.println("\nTest 2.5: Publish course");
        result = courseService.updateCourseStatus(courseId, CourseStatus.PUBLISHED, userId);
        printResult(result);
        
        // Test 2.6: Update access configuration
        System.out.println("\nTest 2.6: Update access configuration");
        result = courseService.updateAccessConfiguration(courseId, "registered_users", true, userId);
        printResult(result);
        
        // Test 2.7: Search courses
        System.out.println("\nTest 2.7: Search courses by keyword 'Programming'");
        List<Course> searchResults = courseService.searchCourses("Programming");
        System.out.println("  Found " + searchResults.size() + " course(s):");
        for (Course c : searchResults) {
            System.out.println("    - " + c.getCourseId() + ": " + c.getCourseName());
        }
    }
    
    // ==================== UC3: ASSIGN/REMOVE COURSE STAFF ====================
    private void testUC3_AssignRemoveStaff() {
        printSection("UC3: ASSIGN/REMOVE COURSE STAFF");
        
        String courseId = "CS101";
        String ownerId = "COACH001";
        
        // Test 3.1: Add a coach
        System.out.println("\nTest 3.1: Add coach to course");
        CourseResult result = courseService.addCourseMember(
            courseId, "COACH002", "Dr. Jane Wilson", "jane@university.edu", CourseRole.COACH, ownerId
        );
        printResult(result);
        
        // Test 3.2: Add participants
        System.out.println("\nTest 3.2: Add participant to course");
        result = courseService.addCourseMember(
            courseId, "STU001", "Alice Johnson", "alice@university.edu", CourseRole.PARTICIPANT, ownerId
        );
        printResult(result);
        
        System.out.println("\nTest 3.3: Add another participant");
        result = courseService.addCourseMember(
            courseId, "STU002", "Bob Smith", "bob@university.edu", CourseRole.PARTICIPANT, ownerId
        );
        printResult(result);
        
        // Test 3.4: List all members
        System.out.println("\nTest 3.4: List all course members");
        List<CourseMember> members = courseService.getCourseMembers(courseId);
        System.out.println("  Found " + members.size() + " members:");
        for (CourseMember m : members) {
            System.out.println("    - " + m.getUserId() + ": " + m.getUserName() + " [" + m.getRole() + "]");
        }
        
        // Test 3.5: Filter by role
        System.out.println("\nTest 3.5: List coaches only");
        List<CourseMember> coaches = courseService.getMembersByRole(courseId, CourseRole.COACH);
        System.out.println("  Found " + coaches.size() + " coach(es):");
        for (CourseMember m : coaches) {
            System.out.println("    - " + m.getUserName());
        }
        
        // Test 3.6: Search members
        System.out.println("\nTest 3.6: Search members by 'alice'");
        List<CourseMember> searchResults = courseService.searchMembers(courseId, "alice");
        System.out.println("  Found " + searchResults.size() + " match(es):");
        for (CourseMember m : searchResults) {
            System.out.println("    - " + m.getUserName() + " (" + m.getEmail() + ")");
        }
        
        // Test 3.7: Update member role
        System.out.println("\nTest 3.7: Promote STU001 to GROUP_COACH");
        result = courseService.updateMemberRole(courseId, "STU001", CourseRole.GROUP_COACH, ownerId);
        printResult(result);
        
        // Test 3.8: Grant rights
        System.out.println("\nTest 3.8: Grant rights to coach");
        result = courseService.grantMemberRights(
            courseId, "COACH002", 
            Arrays.asList("COURSE_EDITOR", "GROUP_MANAGEMENT", "MEMBER_MANAGEMENT"),
            ownerId
        );
        printResult(result);
        
        // Test 3.9: Remove member
        System.out.println("\nTest 3.9: Remove STU002 from course");
        result = courseService.removeCourseMember(courseId, "STU002", ownerId);
        printResult(result);
        
        // Test 3.10: Try to remove owner (should fail)
        System.out.println("\nTest 3.10: Try to remove owner (expect failure)");
        result = courseService.removeCourseMember(courseId, ownerId, ownerId);
        printResult(result);
    }
    
    // ==================== UC4: DESIGN COURSE STRUCTURE ====================
    private void testUC4_DesignCourseStructure() {
        printSection("UC4: DESIGN COURSE STRUCTURE");
        
        String courseId = "CS101";
        String userId = "COACH001";
        
        // Test 4.1: Get current structure
        System.out.println("\nTest 4.1: Get current course structure");
        List<CourseElement> elements = courseService.getCourseStructure(courseId);
        System.out.println("  Found " + elements.size() + " element(s)");
        
        // Get root element ID
        Course course = courseService.getCourse(courseId);
        String rootId = course != null ? course.getRootElementId() : null;
        
        // Test 4.2: Add structure element (Week 1)
        System.out.println("\nTest 4.2: Add 'Week 1' structure element");
        CourseResult result = courseService.addCourseElement(
            courseId, rootId, "Week 1: Introduction", CourseElementType.STRUCTURE, userId
        );
        printResult(result);
        String week1Id = null;
        if (result.isSuccess() && result.getData() instanceof CourseElement) {
            week1Id = ((CourseElement) result.getData()).getElementId();
        }
        
        // Test 4.3: Add content elements under Week 1
        System.out.println("\nTest 4.3: Add folder under Week 1");
        result = courseService.addCourseElement(
            courseId, week1Id, "Course Materials", CourseElementType.FOLDER, userId
        );
        printResult(result);
        
        System.out.println("\nTest 4.4: Add test element under Week 1");
        result = courseService.addCourseElement(
            courseId, week1Id, "Quiz 1: Basics", CourseElementType.TEST, userId
        );
        printResult(result);
        String quizId = null;
        if (result.isSuccess() && result.getData() instanceof CourseElement) {
            quizId = ((CourseElement) result.getData()).getElementId();
        }
        
        // Test 4.5: Add Week 2
        System.out.println("\nTest 4.5: Add 'Week 2' structure element");
        result = courseService.addCourseElement(
            courseId, rootId, "Week 2: Variables and Data Types", CourseElementType.STRUCTURE, userId
        );
        printResult(result);
        String week2Id = null;
        if (result.isSuccess() && result.getData() instanceof CourseElement) {
            week2Id = ((CourseElement) result.getData()).getElementId();
        }
        
        // Test 4.6: Add more elements
        System.out.println("\nTest 4.6: Add task element under Week 2");
        result = courseService.addCourseElement(
            courseId, week2Id, "Assignment 1", CourseElementType.TASK, userId
        );
        printResult(result);
        
        System.out.println("\nTest 4.7: Add wiki element under Week 2");
        result = courseService.addCourseElement(
            courseId, week2Id, "Class Wiki", CourseElementType.WIKI, userId
        );
        printResult(result);
        
        // Test 4.8: Update element
        if (quizId != null) {
            System.out.println("\nTest 4.8: Update quiz element title and description");
            result = courseService.updateCourseElement(
                courseId, quizId, "Quiz 1: Programming Basics (Updated)", 
                "Test your understanding of basic concepts", null, userId
            );
            printResult(result);
        }
        
        // Test 4.9: Set element visibility
        if (week2Id != null) {
            System.out.println("\nTest 4.9: Hide Week 2 (set visible=false)");
            result = courseService.updateCourseElement(courseId, week2Id, null, null, false, userId);
            printResult(result);
        }
        
        // Test 4.10: Set access condition
        if (quizId != null) {
            System.out.println("\nTest 4.10: Set access condition on quiz");
            result = courseService.setElementAccessCondition(
                courseId, quizId, "completed(folder_materials)", userId
            );
            printResult(result);
        }
        
        // Test 4.11: Validate structure
        System.out.println("\nTest 4.11: Validate course structure");
        result = courseService.validateCourseStructure(courseId);
        printResult(result);
        
        // Test 4.12: Print final structure
        System.out.println("\nTest 4.12: Final course structure");
        elements = courseService.getCourseStructure(courseId);
        System.out.println("  Total elements: " + elements.size());
        for (CourseElement e : elements) {
            String visibility = e.isVisible() ? "" : " [HIDDEN]";
            System.out.println("    - " + e.getElementId() + ": " + e.getTitle() + 
                " [" + e.getType() + "]" + visibility);
        }
        
        // Test 4.13: Delete element
        if (quizId != null) {
            System.out.println("\nTest 4.13: Delete quiz element");
            result = courseService.deleteCourseElement(courseId, quizId, userId);
            printResult(result);
        }
        
        // Test 4.14: Try to delete root (should fail)
        if (rootId != null) {
            System.out.println("\nTest 4.14: Try to delete root element (expect failure)");
            result = courseService.deleteCourseElement(courseId, rootId, userId);
            printResult(result);
        }
    }
    
    // ==================== UC5: MANAGE LEARNING MATERIALS ====================
    private void testUC5_ManageLearningMaterials() {
        printSection("UC5: MANAGE COURSE LEARNING MATERIALS");
        
        String courseId = "CS101";
        String userId = "COACH001";
        
        // Test 5.1: Create learning resources
        System.out.println("\nTest 5.1: Create a TEST learning resource");
        CourseResult result = courseService.createLearningResource(
            "Java Basics Quiz",
            "Multiple choice quiz on Java fundamentals",
            LearningResourceType.TEST,
            userId
        );
        printResult(result);
        String testResourceId = null;
        if (result.isSuccess() && result.getData() instanceof LearningResource) {
            testResourceId = ((LearningResource) result.getData()).getResourceId();
        }
        
        System.out.println("\nTest 5.2: Create a WIKI learning resource");
        result = courseService.createLearningResource(
            "Programming Concepts Wiki",
            "Collaborative wiki for programming terminology",
            LearningResourceType.WIKI,
            userId
        );
        printResult(result);
        String wikiResourceId = null;
        if (result.isSuccess() && result.getData() instanceof LearningResource) {
            wikiResourceId = ((LearningResource) result.getData()).getResourceId();
        }
        
        System.out.println("\nTest 5.3: Create a BLOG learning resource");
        result = courseService.createLearningResource(
            "Course Blog",
            "Weekly course updates and tips",
            LearningResourceType.BLOG,
            userId
        );
        printResult(result);
        
        // Test 5.4: List all resources
        System.out.println("\nTest 5.4: List all learning resources");
        List<LearningResource> resources = courseService.getAllLearningResources();
        System.out.println("  Found " + resources.size() + " resource(s):");
        for (LearningResource r : resources) {
            System.out.println("    - " + r.getResourceId() + ": " + r.getTitle() + " [" + r.getType() + "]");
        }
        
        // Test 5.5: Filter by type
        System.out.println("\nTest 5.5: List resources of type TEST");
        List<LearningResource> tests = courseService.getLearningResourcesByType(LearningResourceType.TEST);
        System.out.println("  Found " + tests.size() + " test resource(s)");
        
        // Test 5.6: Filter by owner
        System.out.println("\nTest 5.6: List resources by owner COACH001");
        List<LearningResource> ownerResources = courseService.getLearningResourcesByOwner(userId);
        System.out.println("  Found " + ownerResources.size() + " resource(s)");
        
        // Test 5.7: Link resource to course element
        // First, get a course element
        List<CourseElement> elements = courseService.getCourseStructure(courseId);
        String targetElementId = null;
        for (CourseElement e : elements) {
            if (e.getType() == CourseElementType.WIKI || e.getType() == CourseElementType.STRUCTURE) {
                if (!e.getElementId().startsWith("ROOT_")) {
                    targetElementId = e.getElementId();
                    break;
                }
            }
        }
        
        if (targetElementId != null && wikiResourceId != null) {
            System.out.println("\nTest 5.7: Link wiki resource to element " + targetElementId);
            result = courseService.linkResourceToElement(courseId, targetElementId, wikiResourceId, userId);
            printResult(result);
            
            // Test 5.8: Verify link
            System.out.println("\nTest 5.8: Verify resource link");
            CourseElement element = courseService.getCourseElement(courseId, targetElementId);
            if (element != null) {
                System.out.println("  Element: " + element.getTitle());
                System.out.println("  Linked Resource: " + element.getLinkedResourceId());
            }
            
            // Test 5.9: Unlink resource
            System.out.println("\nTest 5.9: Unlink resource from element");
            result = courseService.unlinkResourceFromElement(courseId, targetElementId, userId);
            printResult(result);
        }
        
        // Test 5.10: Update resource
        if (testResourceId != null) {
            System.out.println("\nTest 5.10: Update learning resource");
            result = courseService.updateLearningResource(
                testResourceId,
                "Java Basics Quiz (Updated)",
                "Updated description with more details",
                userId
            );
            printResult(result);
        }
        
        // Test 5.11: Delete resource
        if (testResourceId != null) {
            System.out.println("\nTest 5.11: Delete learning resource");
            result = courseService.deleteLearningResource(testResourceId, userId);
            printResult(result);
        }
        
        // Test 5.12: Try to delete resource owned by another user (should fail)
        System.out.println("\nTest 5.12: Try to delete resource owned by another (expect failure)");
        if (wikiResourceId != null) {
            result = courseService.deleteLearningResource(wikiResourceId, "OTHER_USER");
            printResult(result);
        }
    }
    
    // ==================== HELPER METHODS ====================
    
    private void printSection(String title) {
        System.out.println("\n" + repeatString("-", 70));
        System.out.println("  " + title);
        System.out.println(repeatString("-", 70));
    }
    
    private void printResult(CourseResult result) {
        if (result.isSuccess()) {
            System.out.println("  ✓ SUCCESS: " + result.getMessage());
        } else {
            System.out.println("  ✗ FAILED: " + result.getMessage() + 
                (result.getErrorCode() != null ? " [" + result.getErrorCode() + "]" : ""));
        }
    }
    
    private String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}
