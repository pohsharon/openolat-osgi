package my.um.cbse.course;

import my.um.cbse.api.model.*;
import my.um.cbse.api.service.CourseService;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Course Management Service Implementation
 * Registered as an OSGi Declarative Service
 * 
 * Implements all 5 Use Cases:
 * - UC1: Create Course
 * - UC2: Manage Course Details and Settings
 * - UC3: Assign/Remove Course Staff
 * - UC4: Design Course Structure
 * - UC5: Manage Course Learning Materials
 */
@Component(
    service = CourseService.class,
    immediate = true
)
public class CourseServiceImpl implements CourseService {
    
    // Data stores (replace with database in production)
    private final Map<String, Course> courses = new HashMap<>();
    private final Map<String, CourseMember> courseMembers = new HashMap<>(); // key: courseId_userId
    private final Map<String, CourseElement> courseElements = new HashMap<>(); // key: courseId_elementId
    private final Map<String, LearningResource> learningResources = new HashMap<>();
    
    private int courseIdCounter = 1000;
    private int elementIdCounter = 1;
    private int resourceIdCounter = 1;
    
    @Activate
    public void activate() {
        System.out.println("=== CourseServiceImpl ACTIVATED ===");
        initializeSampleData();
    }
    
    @Deactivate
    public void deactivate() {
        System.out.println("=== CourseServiceImpl DEACTIVATED ===");
    }
    
    private void initializeSampleData() {
        // Create sample coach/owner
        String coachId = "COACH001";
        
        // Sample Course 1: Introduction to Programming
        Course cs101 = new Course("CS101", "Introduction to Programming", coachId);
        cs101.setCourseCode("CS101");
        cs101.setDescription("Learn the fundamentals of programming using Java");
        cs101.setDesign(CourseDesign.LEARNING_PATH);
        cs101.setStatus(CourseStatus.PUBLISHED);
        cs101.setSemester("2024-1");
        cs101.setCredits(3);
        cs101.setInstitution("University of Computing");
        cs101.setMaxStudents(30);
        cs101.setSelfEnrollmentAllowed(true);
        courses.put(cs101.getCourseId(), cs101);
        
        // Add owner as member
        CourseMember owner1 = new CourseMember(cs101.getCourseId(), coachId, "Dr. Smith", CourseRole.OWNER);
        owner1.setEmail("smith@university.edu");
        courseMembers.put(makeMemberKey(cs101.getCourseId(), coachId), owner1);
        
        // Create course structure for CS101
        createInitialCourseStructure(cs101.getCourseId(), "Introduction to Programming");
        
        // Sample Course 2: Data Structures
        Course cs102 = new Course("CS102", "Data Structures", coachId);
        cs102.setCourseCode("CS102");
        cs102.setDescription("Advanced data structures and algorithms");
        cs102.setDesign(CourseDesign.CONVENTIONAL);
        cs102.setStatus(CourseStatus.DRAFT);
        cs102.setSemester("2024-1");
        cs102.setCredits(4);
        cs102.setMaxStudents(25);
        cs102.getPrerequisites().add("CS101");
        courses.put(cs102.getCourseId(), cs102);
        
        CourseMember owner2 = new CourseMember(cs102.getCourseId(), coachId, "Dr. Smith", CourseRole.OWNER);
        courseMembers.put(makeMemberKey(cs102.getCourseId(), coachId), owner2);
        createInitialCourseStructure(cs102.getCourseId(), "Data Structures");
        
        // Sample Learning Resources
        LearningResource wiki1 = new LearningResource("RES001", "Programming Wiki", LearningResourceType.WIKI, coachId);
        wiki1.setDescription("Collaborative wiki for programming concepts");
        learningResources.put(wiki1.getResourceId(), wiki1);
        
        LearningResource test1 = new LearningResource("RES002", "Java Basics Quiz", LearningResourceType.TEST, coachId);
        test1.setDescription("Quiz on Java fundamentals");
        learningResources.put(test1.getResourceId(), test1);
        
        System.out.println("✓ Sample data initialized: 2 courses, 2 learning resources");
    }
    
    private void createInitialCourseStructure(String courseId, String courseTitle) {
        // Create root element
        String rootId = "ROOT_" + courseId;
        CourseElement root = new CourseElement(rootId, courseTitle, CourseElementType.STRUCTURE);
        root.setDescription("Course root element");
        root.setOrder(0);
        courseElements.put(makeElementKey(courseId, rootId), root);
        
        // Update course with root element ID
        Course course = courses.get(courseId);
        if (course != null) {
            course.setRootElementId(rootId);
        }
    }
    
    // ========== UC1: CREATE COURSE ==========
    
    @Override
    public CourseResult createCourse(String title, CourseDesign design, String ownerId) {
        return createCourse(title, null, design, null, ownerId);
    }
    
    @Override
    public CourseResult createCourse(String title, String description, CourseDesign design, 
                                     String semester, String ownerId) {
        System.out.println("\n[CREATE-COURSE] Title: " + title + ", Owner: " + ownerId);
        
        // Validation
        if (title == null || title.trim().isEmpty()) {
            return new CourseResult(false, "Course title is required", "MISSING_TITLE");
        }
        if (ownerId == null || ownerId.trim().isEmpty()) {
            return new CourseResult(false, "Owner ID is required", "MISSING_OWNER");
        }
        
        // Generate course ID
        String courseId = "CRS" + (++courseIdCounter);
        
        // Create course
        Course course = new Course(courseId, title, ownerId);
        course.setCourseCode(courseId);
        course.setDescription(description);
        course.setDesign(design != null ? design : CourseDesign.LEARNING_PATH);
        course.setSemester(semester);
        course.setStatus(CourseStatus.DRAFT);
        
        courses.put(courseId, course);
        
        // Add owner as course member
        CourseMember owner = new CourseMember(courseId, ownerId, "Owner-" + ownerId, CourseRole.OWNER);
        courseMembers.put(makeMemberKey(courseId, ownerId), owner);
        
        // Create initial course structure
        createInitialCourseStructure(courseId, title);
        
        System.out.println("✓ Course created: " + courseId);
        return new CourseResult(true, "Course created successfully with ID: " + courseId, course);
    }
    
    @Override
    public CourseResult copyCourse(String sourceCourseId, String newTitle, String ownerId) {
        System.out.println("\n[COPY-COURSE] Source: " + sourceCourseId + ", New Title: " + newTitle);
        
        Course source = courses.get(sourceCourseId);
        if (source == null) {
            return new CourseResult(false, "Source course not found: " + sourceCourseId, "COURSE_NOT_FOUND");
        }
        
        // Create new course as copy
        String newCourseId = "CRS" + (++courseIdCounter);
        Course copy = new Course(newCourseId, newTitle != null ? newTitle : source.getCourseName() + " (Copy)", ownerId);
        copy.setCourseCode(newCourseId);
        copy.setDescription(source.getDescription());
        copy.setDesign(source.getDesign());
        copy.setStatus(CourseStatus.DRAFT);
        copy.setSemester(source.getSemester());
        copy.setCredits(source.getCredits());
        copy.setMaxStudents(source.getMaxStudents());
        copy.setInstitution(source.getInstitution());
        copy.getPrerequisites().addAll(source.getPrerequisites());
        
        courses.put(newCourseId, copy);
        
        // Add owner
        CourseMember owner = new CourseMember(newCourseId, ownerId, "Owner-" + ownerId, CourseRole.OWNER);
        courseMembers.put(makeMemberKey(newCourseId, ownerId), owner);
        
        // Copy course structure
        copyCourseStructure(sourceCourseId, newCourseId, newTitle);
        
        System.out.println("✓ Course copied: " + newCourseId);
        return new CourseResult(true, "Course copied successfully with ID: " + newCourseId, copy);
    }
    
    private void copyCourseStructure(String sourceCourseId, String newCourseId, String newTitle) {
        // Get source elements
        List<CourseElement> sourceElements = getCourseStructure(sourceCourseId);
        Map<String, String> elementIdMapping = new HashMap<>();
        
        for (CourseElement source : sourceElements) {
            String newElementId = "ELEM" + (++elementIdCounter);
            CourseElement copy = new CourseElement(newElementId, source.getTitle(), source.getType());
            copy.setDescription(source.getDescription());
            copy.setOrder(source.getOrder());
            copy.setVisible(source.isVisible());
            copy.setAccessCondition(source.getAccessCondition());
            copy.setLinkedResourceId(source.getLinkedResourceId());
            copy.getConfiguration().putAll(source.getConfiguration());
            
            // Map old parent to new parent
            if (source.getParentId() != null && elementIdMapping.containsKey(source.getParentId())) {
                copy.setParentId(elementIdMapping.get(source.getParentId()));
            }
            
            elementIdMapping.put(source.getElementId(), newElementId);
            courseElements.put(makeElementKey(newCourseId, newElementId), copy);
        }
        
        // Set root element
        Course newCourse = courses.get(newCourseId);
        if (newCourse != null && !elementIdMapping.isEmpty()) {
            Course sourceCourse = courses.get(sourceCourseId);
            if (sourceCourse != null && sourceCourse.getRootElementId() != null) {
                newCourse.setRootElementId(elementIdMapping.get(sourceCourse.getRootElementId()));
            }
        }
    }
    
    @Override
    public CourseResult deleteCourse(String courseId, String userId) {
        System.out.println("\n[DELETE-COURSE] Course: " + courseId + ", By: " + userId);
        
        Course course = courses.get(courseId);
        if (course == null) {
            return new CourseResult(false, "Course not found: " + courseId, "COURSE_NOT_FOUND");
        }
        
        // Check permission
        if (!hasPermission(courseId, userId, CourseRole.OWNER)) {
            return new CourseResult(false, "Only course owner can delete the course", "UNAUTHORIZED");
        }
        
        // Remove course elements
        List<String> elementsToRemove = courseElements.keySet().stream()
            .filter(key -> key.startsWith(courseId + "_"))
            .collect(Collectors.toList());
        elementsToRemove.forEach(courseElements::remove);
        
        // Remove course members
        List<String> membersToRemove = courseMembers.keySet().stream()
            .filter(key -> key.startsWith(courseId + "_"))
            .collect(Collectors.toList());
        membersToRemove.forEach(courseMembers::remove);
        
        // Remove course
        courses.remove(courseId);
        
        System.out.println("✓ Course deleted: " + courseId);
        return new CourseResult(true, "Course deleted successfully");
    }
    
    // ========== UC2: MANAGE COURSE DETAILS AND SETTINGS ==========
    
    @Override
    public Course getCourse(String courseId) {
        return courses.get(courseId);
    }
    
    @Override
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }
    
    @Override
    public List<Course> getCoursesByOwner(String ownerId) {
        return courses.values().stream()
            .filter(c -> ownerId.equals(c.getOwnerId()))
            .collect(Collectors.toList());
    }
    
    @Override
    public CourseResult updateCourseInfo(String courseId, String title, String description, 
                                         String courseCode, String userId) {
        System.out.println("\n[UPDATE-COURSE-INFO] Course: " + courseId);
        
        Course course = courses.get(courseId);
        if (course == null) {
            return new CourseResult(false, "Course not found: " + courseId, "COURSE_NOT_FOUND");
        }
        
        if (!hasPermission(courseId, userId, CourseRole.COACH)) {
            return new CourseResult(false, "Insufficient permissions", "UNAUTHORIZED");
        }
        
        if (title != null) course.setCourseName(title);
        if (description != null) course.setDescription(description);
        if (courseCode != null) course.setCourseCode(courseCode);
        course.setLastModified(new Date());
        
        System.out.println("✓ Course info updated");
        return new CourseResult(true, "Course info updated successfully", course);
    }
    
    @Override
    public CourseResult updateCourseMetadata(String courseId, String semester, Integer credits, 
                                             String license, String institution, String userId) {
        System.out.println("\n[UPDATE-COURSE-METADATA] Course: " + courseId);
        
        Course course = courses.get(courseId);
        if (course == null) {
            return new CourseResult(false, "Course not found: " + courseId, "COURSE_NOT_FOUND");
        }
        
        if (!hasPermission(courseId, userId, CourseRole.COACH)) {
            return new CourseResult(false, "Insufficient permissions", "UNAUTHORIZED");
        }
        
        if (semester != null) course.setSemester(semester);
        if (credits != null) course.setCredits(credits);
        if (license != null) course.setLicense(license);
        if (institution != null) course.setInstitution(institution);
        course.setLastModified(new Date());
        
        System.out.println("✓ Course metadata updated");
        return new CourseResult(true, "Course metadata updated successfully", course);
    }
    
    @Override
    public CourseResult updateExecutionSettings(String courseId, boolean lectureManagementEnabled, 
                                                boolean absenceManagementEnabled, String userId) {
        System.out.println("\n[UPDATE-EXECUTION-SETTINGS] Course: " + courseId);
        
        Course course = courses.get(courseId);
        if (course == null) {
            return new CourseResult(false, "Course not found: " + courseId, "COURSE_NOT_FOUND");
        }
        
        if (!hasPermission(courseId, userId, CourseRole.COACH)) {
            return new CourseResult(false, "Insufficient permissions", "UNAUTHORIZED");
        }
        
        course.setLectureManagementEnabled(lectureManagementEnabled);
        course.setAbsenceManagementEnabled(absenceManagementEnabled);
        course.setLastModified(new Date());
        
        System.out.println("✓ Execution settings updated");
        return new CourseResult(true, "Execution settings updated successfully", course);
    }
    
    @Override
    public CourseResult updateCourseStatus(String courseId, CourseStatus status, String userId) {
        System.out.println("\n[UPDATE-COURSE-STATUS] Course: " + courseId + ", Status: " + status);
        
        Course course = courses.get(courseId);
        if (course == null) {
            return new CourseResult(false, "Course not found: " + courseId, "COURSE_NOT_FOUND");
        }
        
        if (!hasPermission(courseId, userId, CourseRole.OWNER)) {
            return new CourseResult(false, "Only owner can change publication status", "UNAUTHORIZED");
        }
        
        course.setStatus(status);
        course.setLastModified(new Date());
        
        System.out.println("✓ Course status updated to: " + status);
        return new CourseResult(true, "Course status updated to " + status, course);
    }
    
    @Override
    public CourseResult updateAccessConfiguration(String courseId, String accessConfig, 
                                                  boolean selfEnrollmentAllowed, String userId) {
        System.out.println("\n[UPDATE-ACCESS-CONFIG] Course: " + courseId);
        
        Course course = courses.get(courseId);
        if (course == null) {
            return new CourseResult(false, "Course not found: " + courseId, "COURSE_NOT_FOUND");
        }
        
        if (!hasPermission(courseId, userId, CourseRole.OWNER)) {
            return new CourseResult(false, "Only owner can change access configuration", "UNAUTHORIZED");
        }
        
        course.setAccessConfiguration(accessConfig);
        course.setSelfEnrollmentAllowed(selfEnrollmentAllowed);
        course.setLastModified(new Date());
        
        System.out.println("✓ Access configuration updated");
        return new CourseResult(true, "Access configuration updated successfully", course);
    }
    
    // ========== UC3: ASSIGN/REMOVE COURSE STAFF ==========
    
    @Override
    public CourseResult addCourseMember(String courseId, String userId, String userName, 
                                        String email, CourseRole role, String addedBy) {
        System.out.println("\n[ADD-MEMBER] Course: " + courseId + ", User: " + userId + ", Role: " + role);
        
        Course course = courses.get(courseId);
        if (course == null) {
            return new CourseResult(false, "Course not found: " + courseId, "COURSE_NOT_FOUND");
        }
        
        if (!hasPermission(courseId, addedBy, CourseRole.COACH)) {
            return new CourseResult(false, "Insufficient permissions to add members", "UNAUTHORIZED");
        }
        
        String memberKey = makeMemberKey(courseId, userId);
        if (courseMembers.containsKey(memberKey)) {
            return new CourseResult(false, "User is already a member of this course", "ALREADY_MEMBER");
        }
        
        CourseMember member = new CourseMember(courseId, userId, userName, role);
        member.setEmail(email);
        courseMembers.put(memberKey, member);
        
        System.out.println("✓ Member added: " + userName + " as " + role);
        return new CourseResult(true, "Member added successfully", member);
    }
    
    @Override
    public CourseResult removeCourseMember(String courseId, String userId, String removedBy) {
        System.out.println("\n[REMOVE-MEMBER] Course: " + courseId + ", User: " + userId);
        
        Course course = courses.get(courseId);
        if (course == null) {
            return new CourseResult(false, "Course not found: " + courseId, "COURSE_NOT_FOUND");
        }
        
        if (!hasPermission(courseId, removedBy, CourseRole.COACH)) {
            return new CourseResult(false, "Insufficient permissions to remove members", "UNAUTHORIZED");
        }
        
        String memberKey = makeMemberKey(courseId, userId);
        CourseMember member = courseMembers.get(memberKey);
        
        if (member == null) {
            return new CourseResult(false, "Member not found", "MEMBER_NOT_FOUND");
        }
        
        // Cannot remove the owner
        if (member.getRole() == CourseRole.OWNER && userId.equals(course.getOwnerId())) {
            return new CourseResult(false, "Cannot remove the course owner", "CANNOT_REMOVE_OWNER");
        }
        
        courseMembers.remove(memberKey);
        
        System.out.println("✓ Member removed: " + userId);
        return new CourseResult(true, "Member removed successfully");
    }
    
    @Override
    public CourseResult updateMemberRole(String courseId, String userId, CourseRole newRole, String updatedBy) {
        System.out.println("\n[UPDATE-MEMBER-ROLE] Course: " + courseId + ", User: " + userId + ", New Role: " + newRole);
        
        if (!hasPermission(courseId, updatedBy, CourseRole.OWNER)) {
            return new CourseResult(false, "Only owner can change member roles", "UNAUTHORIZED");
        }
        
        String memberKey = makeMemberKey(courseId, userId);
        CourseMember member = courseMembers.get(memberKey);
        
        if (member == null) {
            return new CourseResult(false, "Member not found", "MEMBER_NOT_FOUND");
        }
        
        member.setRole(newRole);
        
        System.out.println("✓ Member role updated to: " + newRole);
        return new CourseResult(true, "Member role updated successfully", member);
    }
    
    @Override
    public List<CourseMember> getCourseMembers(String courseId) {
        return courseMembers.values().stream()
            .filter(m -> courseId.equals(m.getCourseId()))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<CourseMember> getMembersByRole(String courseId, CourseRole role) {
        return courseMembers.values().stream()
            .filter(m -> courseId.equals(m.getCourseId()) && role.equals(m.getRole()))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<CourseMember> searchMembers(String courseId, String searchTerm) {
        String term = searchTerm.toLowerCase();
        return courseMembers.values().stream()
            .filter(m -> courseId.equals(m.getCourseId()))
            .filter(m -> m.getUserName().toLowerCase().contains(term) || 
                        (m.getEmail() != null && m.getEmail().toLowerCase().contains(term)))
            .collect(Collectors.toList());
    }
    
    @Override
    public CourseResult grantMemberRights(String courseId, String userId, List<String> rights, String grantedBy) {
        System.out.println("\n[GRANT-RIGHTS] Course: " + courseId + ", User: " + userId);
        
        if (!hasPermission(courseId, grantedBy, CourseRole.OWNER)) {
            return new CourseResult(false, "Only owner can grant rights", "UNAUTHORIZED");
        }
        
        String memberKey = makeMemberKey(courseId, userId);
        CourseMember member = courseMembers.get(memberKey);
        
        if (member == null) {
            return new CourseResult(false, "Member not found", "MEMBER_NOT_FOUND");
        }
        
        for (String right : rights) {
            if (!member.getRights().contains(right)) {
                member.getRights().add(right);
            }
        }
        
        System.out.println("✓ Rights granted: " + rights);
        return new CourseResult(true, "Rights granted successfully", member);
    }
    
    @Override
    public CourseResult revokeMemberRights(String courseId, String userId, List<String> rights, String revokedBy) {
        System.out.println("\n[REVOKE-RIGHTS] Course: " + courseId + ", User: " + userId);
        
        if (!hasPermission(courseId, revokedBy, CourseRole.OWNER)) {
            return new CourseResult(false, "Only owner can revoke rights", "UNAUTHORIZED");
        }
        
        String memberKey = makeMemberKey(courseId, userId);
        CourseMember member = courseMembers.get(memberKey);
        
        if (member == null) {
            return new CourseResult(false, "Member not found", "MEMBER_NOT_FOUND");
        }
        
        member.getRights().removeAll(rights);
        
        System.out.println("✓ Rights revoked: " + rights);
        return new CourseResult(true, "Rights revoked successfully", member);
    }
    
    // ========== UC4: DESIGN COURSE STRUCTURE ==========
    
    @Override
    public List<CourseElement> getCourseStructure(String courseId) {
        return courseElements.values().stream()
            .filter(e -> courseElements.containsKey(makeElementKey(courseId, e.getElementId())))
            .sorted(Comparator.comparingInt(CourseElement::getOrder))
            .collect(Collectors.toList());
    }
    
    @Override
    public CourseElement getCourseElement(String courseId, String elementId) {
        return courseElements.get(makeElementKey(courseId, elementId));
    }
    
    @Override
    public CourseResult addCourseElement(String courseId, String parentElementId, String title, 
                                         CourseElementType type, String userId) {
        System.out.println("\n[ADD-ELEMENT] Course: " + courseId + ", Type: " + type + ", Title: " + title);
        
        Course course = courses.get(courseId);
        if (course == null) {
            return new CourseResult(false, "Course not found: " + courseId, "COURSE_NOT_FOUND");
        }
        
        if (!hasPermission(courseId, userId, CourseRole.COACH)) {
            return new CourseResult(false, "Insufficient permissions", "UNAUTHORIZED");
        }
        
        // Validate parent exists if specified
        if (parentElementId != null) {
            CourseElement parent = courseElements.get(makeElementKey(courseId, parentElementId));
            if (parent == null) {
                return new CourseResult(false, "Parent element not found", "PARENT_NOT_FOUND");
            }
        }
        
        String elementId = "ELEM" + (++elementIdCounter);
        CourseElement element = new CourseElement(elementId, title, type);
        element.setParentId(parentElementId != null ? parentElementId : course.getRootElementId());
        element.setOrder(getNextElementOrder(courseId, element.getParentId()));
        
        courseElements.put(makeElementKey(courseId, elementId), element);
        
        // Update parent's child list
        if (element.getParentId() != null) {
            CourseElement parent = courseElements.get(makeElementKey(courseId, element.getParentId()));
            if (parent != null) {
                parent.getChildElementIds().add(elementId);
            }
        }
        
        System.out.println("✓ Element added: " + elementId);
        return new CourseResult(true, "Course element added with ID: " + elementId, element);
    }
    
    private int getNextElementOrder(String courseId, String parentId) {
        return (int) courseElements.values().stream()
            .filter(e -> courseElements.containsKey(makeElementKey(courseId, e.getElementId())))
            .filter(e -> Objects.equals(e.getParentId(), parentId))
            .count();
    }
    
    @Override
    public CourseResult updateCourseElement(String courseId, String elementId, String title, 
                                            String description, Boolean visible, String userId) {
        System.out.println("\n[UPDATE-ELEMENT] Course: " + courseId + ", Element: " + elementId);
        
        if (!hasPermission(courseId, userId, CourseRole.COACH)) {
            return new CourseResult(false, "Insufficient permissions", "UNAUTHORIZED");
        }
        
        CourseElement element = courseElements.get(makeElementKey(courseId, elementId));
        if (element == null) {
            return new CourseResult(false, "Element not found", "ELEMENT_NOT_FOUND");
        }
        
        if (title != null) element.setTitle(title);
        if (description != null) element.setDescription(description);
        if (visible != null) element.setVisible(visible);
        
        System.out.println("✓ Element updated");
        return new CourseResult(true, "Course element updated successfully", element);
    }
    
    @Override
    public CourseResult deleteCourseElement(String courseId, String elementId, String userId) {
        System.out.println("\n[DELETE-ELEMENT] Course: " + courseId + ", Element: " + elementId);
        
        if (!hasPermission(courseId, userId, CourseRole.COACH)) {
            return new CourseResult(false, "Insufficient permissions", "UNAUTHORIZED");
        }
        
        String elementKey = makeElementKey(courseId, elementId);
        CourseElement element = courseElements.get(elementKey);
        
        if (element == null) {
            return new CourseResult(false, "Element not found", "ELEMENT_NOT_FOUND");
        }
        
        // Check if it's the root element
        Course course = courses.get(courseId);
        if (course != null && elementId.equals(course.getRootElementId())) {
            return new CourseResult(false, "Cannot delete root element", "CANNOT_DELETE_ROOT");
        }
        
        // Delete all child elements recursively
        deleteElementAndChildren(courseId, elementId);
        
        // Remove from parent's child list
        if (element.getParentId() != null) {
            CourseElement parent = courseElements.get(makeElementKey(courseId, element.getParentId()));
            if (parent != null) {
                parent.getChildElementIds().remove(elementId);
            }
        }
        
        System.out.println("✓ Element deleted");
        return new CourseResult(true, "Course element deleted successfully");
    }
    
    private void deleteElementAndChildren(String courseId, String elementId) {
        CourseElement element = courseElements.get(makeElementKey(courseId, elementId));
        if (element == null) return;
        
        // Delete children first
        for (String childId : new ArrayList<>(element.getChildElementIds())) {
            deleteElementAndChildren(courseId, childId);
        }
        
        // Delete the element itself
        courseElements.remove(makeElementKey(courseId, elementId));
    }
    
    @Override
    public CourseResult moveCourseElement(String courseId, String elementId, String newParentId, 
                                          int newOrder, String userId) {
        System.out.println("\n[MOVE-ELEMENT] Course: " + courseId + ", Element: " + elementId);
        
        if (!hasPermission(courseId, userId, CourseRole.COACH)) {
            return new CourseResult(false, "Insufficient permissions", "UNAUTHORIZED");
        }
        
        CourseElement element = courseElements.get(makeElementKey(courseId, elementId));
        if (element == null) {
            return new CourseResult(false, "Element not found", "ELEMENT_NOT_FOUND");
        }
        
        // Remove from old parent's child list
        if (element.getParentId() != null) {
            CourseElement oldParent = courseElements.get(makeElementKey(courseId, element.getParentId()));
            if (oldParent != null) {
                oldParent.getChildElementIds().remove(elementId);
            }
        }
        
        // Update element
        element.setParentId(newParentId);
        element.setOrder(newOrder);
        
        // Add to new parent's child list
        if (newParentId != null) {
            CourseElement newParent = courseElements.get(makeElementKey(courseId, newParentId));
            if (newParent != null) {
                newParent.getChildElementIds().add(elementId);
            }
        }
        
        System.out.println("✓ Element moved");
        return new CourseResult(true, "Course element moved successfully", element);
    }
    
    @Override
    public CourseResult setElementAccessCondition(String courseId, String elementId, 
                                                  String accessCondition, String userId) {
        System.out.println("\n[SET-ACCESS-CONDITION] Course: " + courseId + ", Element: " + elementId);
        
        if (!hasPermission(courseId, userId, CourseRole.COACH)) {
            return new CourseResult(false, "Insufficient permissions", "UNAUTHORIZED");
        }
        
        CourseElement element = courseElements.get(makeElementKey(courseId, elementId));
        if (element == null) {
            return new CourseResult(false, "Element not found", "ELEMENT_NOT_FOUND");
        }
        
        element.setAccessCondition(accessCondition);
        
        System.out.println("✓ Access condition set");
        return new CourseResult(true, "Access condition set successfully", element);
    }
    
    // ========== UC5: MANAGE COURSE LEARNING MATERIALS ==========
    
    @Override
    public CourseResult createLearningResource(String title, String description, 
                                               LearningResourceType type, String ownerId) {
        System.out.println("\n[CREATE-RESOURCE] Title: " + title + ", Type: " + type);
        
        if (title == null || title.trim().isEmpty()) {
            return new CourseResult(false, "Resource title is required", "MISSING_TITLE");
        }
        
        String resourceId = "RES" + String.format("%03d", ++resourceIdCounter);
        LearningResource resource = new LearningResource(resourceId, title, type, ownerId);
        resource.setDescription(description);
        
        learningResources.put(resourceId, resource);
        
        System.out.println("✓ Learning resource created: " + resourceId);
        return new CourseResult(true, "Learning resource created with ID: " + resourceId, resource);
    }
    
    @Override
    public LearningResource getLearningResource(String resourceId) {
        return learningResources.get(resourceId);
    }
    
    @Override
    public List<LearningResource> getAllLearningResources() {
        return new ArrayList<>(learningResources.values());
    }
    
    @Override
    public List<LearningResource> getLearningResourcesByOwner(String ownerId) {
        return learningResources.values().stream()
            .filter(r -> ownerId.equals(r.getOwnerId()))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<LearningResource> getLearningResourcesByType(LearningResourceType type) {
        return learningResources.values().stream()
            .filter(r -> type.equals(r.getType()))
            .collect(Collectors.toList());
    }
    
    @Override
    public CourseResult linkResourceToElement(String courseId, String elementId, 
                                              String resourceId, String userId) {
        System.out.println("\n[LINK-RESOURCE] Course: " + courseId + ", Element: " + elementId + ", Resource: " + resourceId);
        
        if (!hasPermission(courseId, userId, CourseRole.COACH)) {
            return new CourseResult(false, "Insufficient permissions", "UNAUTHORIZED");
        }
        
        CourseElement element = courseElements.get(makeElementKey(courseId, elementId));
        if (element == null) {
            return new CourseResult(false, "Element not found", "ELEMENT_NOT_FOUND");
        }
        
        LearningResource resource = learningResources.get(resourceId);
        if (resource == null) {
            return new CourseResult(false, "Learning resource not found", "RESOURCE_NOT_FOUND");
        }
        
        element.setLinkedResourceId(resourceId);
        
        System.out.println("✓ Resource linked to element");
        return new CourseResult(true, "Learning resource linked successfully", element);
    }
    
    @Override
    public CourseResult unlinkResourceFromElement(String courseId, String elementId, String userId) {
        System.out.println("\n[UNLINK-RESOURCE] Course: " + courseId + ", Element: " + elementId);
        
        if (!hasPermission(courseId, userId, CourseRole.COACH)) {
            return new CourseResult(false, "Insufficient permissions", "UNAUTHORIZED");
        }
        
        CourseElement element = courseElements.get(makeElementKey(courseId, elementId));
        if (element == null) {
            return new CourseResult(false, "Element not found", "ELEMENT_NOT_FOUND");
        }
        
        element.setLinkedResourceId(null);
        
        System.out.println("✓ Resource unlinked from element");
        return new CourseResult(true, "Learning resource unlinked successfully", element);
    }
    
    @Override
    public CourseResult deleteLearningResource(String resourceId, String userId) {
        System.out.println("\n[DELETE-RESOURCE] Resource: " + resourceId);
        
        LearningResource resource = learningResources.get(resourceId);
        if (resource == null) {
            return new CourseResult(false, "Learning resource not found", "RESOURCE_NOT_FOUND");
        }
        
        // Check if owner
        if (!userId.equals(resource.getOwnerId())) {
            return new CourseResult(false, "Only the owner can delete this resource", "UNAUTHORIZED");
        }
        
        // Unlink from any elements using this resource
        for (CourseElement element : courseElements.values()) {
            if (resourceId.equals(element.getLinkedResourceId())) {
                element.setLinkedResourceId(null);
            }
        }
        
        learningResources.remove(resourceId);
        
        System.out.println("✓ Learning resource deleted");
        return new CourseResult(true, "Learning resource deleted successfully");
    }
    
    @Override
    public CourseResult updateLearningResource(String resourceId, String title, 
                                               String description, String userId) {
        System.out.println("\n[UPDATE-RESOURCE] Resource: " + resourceId);
        
        LearningResource resource = learningResources.get(resourceId);
        if (resource == null) {
            return new CourseResult(false, "Learning resource not found", "RESOURCE_NOT_FOUND");
        }
        
        if (!userId.equals(resource.getOwnerId())) {
            return new CourseResult(false, "Only the owner can update this resource", "UNAUTHORIZED");
        }
        
        if (title != null) resource.setTitle(title);
        if (description != null) resource.setDescription(description);
        resource.setLastModified(new Date());
        
        System.out.println("✓ Learning resource updated");
        return new CourseResult(true, "Learning resource updated successfully", resource);
    }
    
    // ========== UTILITY METHODS ==========
    
    @Override
    public boolean hasPermission(String courseId, String userId, CourseRole requiredRole) {
        String memberKey = makeMemberKey(courseId, userId);
        CourseMember member = courseMembers.get(memberKey);
        
        if (member == null) return false;
        
        // Role hierarchy: OWNER > COACH > GROUP_COACH > PARTICIPANT > GUEST
        int userLevel = getRoleLevel(member.getRole());
        int requiredLevel = getRoleLevel(requiredRole);
        
        return userLevel >= requiredLevel;
    }
    
    private int getRoleLevel(CourseRole role) {
        switch (role) {
            case OWNER: return 5;
            case COACH: return 4;
            case GROUP_COACH: return 3;
            case PARTICIPANT: return 2;
            case GUEST: return 1;
            default: return 0;
        }
    }
    
    @Override
    public CourseResult validateCourseStructure(String courseId) {
        System.out.println("\n[VALIDATE-STRUCTURE] Course: " + courseId);
        
        Course course = courses.get(courseId);
        if (course == null) {
            return new CourseResult(false, "Course not found", "COURSE_NOT_FOUND");
        }
        
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        // Check root element exists
        if (course.getRootElementId() == null) {
            errors.add("Course has no root element");
        }
        
        // Check for orphan elements
        List<CourseElement> elements = getCourseStructure(courseId);
        for (CourseElement element : elements) {
            if (element.getParentId() != null && !element.getElementId().equals(course.getRootElementId())) {
                CourseElement parent = getCourseElement(courseId, element.getParentId());
                if (parent == null) {
                    warnings.add("Element " + element.getElementId() + " has missing parent");
                }
            }
        }
        
        // Check for empty structure
        if (elements.size() <= 1) {
            warnings.add("Course has no content elements");
        }
        
        StringBuilder message = new StringBuilder();
        message.append("Validation complete. ");
        if (errors.isEmpty() && warnings.isEmpty()) {
            message.append("No issues found.");
        } else {
            if (!errors.isEmpty()) {
                message.append("Errors: ").append(errors.size()).append(". ");
            }
            if (!warnings.isEmpty()) {
                message.append("Warnings: ").append(warnings.size()).append(".");
            }
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("errors", errors);
        data.put("warnings", warnings);
        
        System.out.println("✓ Validation: " + errors.size() + " errors, " + warnings.size() + " warnings");
        return new CourseResult(errors.isEmpty(), message.toString(), data);
    }
    
    @Override
    public List<Course> searchCourses(String keyword) {
        String term = keyword.toLowerCase();
        return courses.values().stream()
            .filter(c -> c.getCourseName().toLowerCase().contains(term) ||
                        (c.getCourseCode() != null && c.getCourseCode().toLowerCase().contains(term)) ||
                        (c.getDescription() != null && c.getDescription().toLowerCase().contains(term)))
            .collect(Collectors.toList());
    }
    
    // Helper methods
    private String makeMemberKey(String courseId, String userId) {
        return courseId + "_" + userId;
    }
    
    private String makeElementKey(String courseId, String elementId) {
        return courseId + "_" + elementId;
    }
}
