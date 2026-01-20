package my.um.cbse.api.service;

import java.util.List;

import my.um.cbse.api.model.Course;
import my.um.cbse.api.model.CourseDesign;
import my.um.cbse.api.model.CourseElement;
import my.um.cbse.api.model.CourseElementType;
import my.um.cbse.api.model.CourseMember;
import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.model.CourseRole;
import my.um.cbse.api.model.CourseStatus;
import my.um.cbse.api.model.LearningResource;
import my.um.cbse.api.model.LearningResourceType;

/**
 * Course Management Service Interface
 * Defines the contract for course management operations
 * 
 * Covers Use Cases:
 * - UC1: Create Course
 * - UC2: Manage Course Details and Settings
 * - UC3: Assign/Remove Course Staff
 * - UC4: Design Course Structure
 * - UC5: Manage Course Learning Materials
 */
public interface CourseService {
    
    // ========== UC1: CREATE COURSE ==========
    
    /**
     * Create a new course learning resource
     * @param title the course title
     * @param design the course design type (LEARNING_PATH or CONVENTIONAL)
     * @param ownerId the user ID of the course owner/coach
     * @return CourseResult with the created course
     */
    CourseResult createCourse(String title, CourseDesign design, String ownerId);
    
    /**
     * Create a course with full details
     * @param title the course title
     * @param description the course description
     * @param design the course design type
     * @param semester the semester/execution period
     * @param ownerId the user ID of the course owner
     * @return CourseResult with the created course
     */
    CourseResult createCourse(String title, String description, CourseDesign design, 
                              String semester, String ownerId);
    
    /**
     * Copy an existing course
     * @param sourceCourseId the ID of the course to copy
     * @param newTitle the title for the new course
     * @param ownerId the user ID of the new course owner
     * @return CourseResult with the copied course
     */
    CourseResult copyCourse(String sourceCourseId, String newTitle, String ownerId);
    
    /**
     * Delete a course
     * @param courseId the course ID
     * @param userId the user requesting deletion
     * @return CourseResult indicating success or failure
     */
    CourseResult deleteCourse(String courseId, String userId);
    
    // ========== UC2: MANAGE COURSE DETAILS AND SETTINGS ==========
    
    /**
     * Get course by ID
     * @param courseId the course ID
     * @return the Course object or null if not found
     */
    Course getCourse(String courseId);
    
    /**
     * Get all courses
     * @return list of all courses
     */
    List<Course> getAllCourses();
    
    /**
     * Get courses by owner
     * @param ownerId the owner's user ID
     * @return list of courses owned by the user
     */
    List<Course> getCoursesByOwner(String ownerId);
    
    /**
     * Update course basic info (title, description, code)
     * @param courseId the course ID
     * @param title new title (null to keep existing)
     * @param description new description (null to keep existing)
     * @param courseCode new course code (null to keep existing)
     * @param userId the user making the update
     * @return CourseResult indicating success or failure
     */
    CourseResult updateCourseInfo(String courseId, String title, String description, 
                                  String courseCode, String userId);
    
    /**
     * Update course metadata (semester, credits, license, institution)
     * @param courseId the course ID
     * @param semester the semester
     * @param credits the credits
     * @param license the license
     * @param institution the institution
     * @param userId the user making the update
     * @return CourseResult indicating success or failure
     */
    CourseResult updateCourseMetadata(String courseId, String semester, Integer credits, 
                                      String license, String institution, String userId);
    
    /**
     * Update course execution settings (lecture & absence management)
     * @param courseId the course ID
     * @param lectureManagementEnabled enable lecture management
     * @param absenceManagementEnabled enable absence management
     * @param userId the user making the update
     * @return CourseResult indicating success or failure
     */
    CourseResult updateExecutionSettings(String courseId, boolean lectureManagementEnabled, 
                                         boolean absenceManagementEnabled, String userId);
    
    /**
     * Update course publication status
     * @param courseId the course ID
     * @param status the new status
     * @param userId the user making the update
     * @return CourseResult indicating success or failure
     */
    CourseResult updateCourseStatus(String courseId, CourseStatus status, String userId);
    
    /**
     * Update course access configuration (sharing settings)
     * @param courseId the course ID
     * @param accessConfig the access configuration
     * @param selfEnrollmentAllowed whether self-enrollment is allowed
     * @param userId the user making the update
     * @return CourseResult indicating success or failure
     */
    CourseResult updateAccessConfiguration(String courseId, String accessConfig, 
                                           boolean selfEnrollmentAllowed, String userId);
    
    // ========== UC3: ASSIGN/REMOVE COURSE STAFF ==========
    
    /**
     * Add a member to the course
     * @param courseId the course ID
     * @param userId the user ID to add
     * @param userName the user's name
     * @param email the user's email
     * @param role the role to assign
     * @param addedBy the user adding the member
     * @return CourseResult indicating success or failure
     */
    CourseResult addCourseMember(String courseId, String userId, String userName, 
                                 String email, CourseRole role, String addedBy);
    
    /**
     * Remove a member from the course
     * @param courseId the course ID
     * @param userId the user ID to remove
     * @param removedBy the user removing the member
     * @return CourseResult indicating success or failure
     */
    CourseResult removeCourseMember(String courseId, String userId, String removedBy);
    
    /**
     * Update a member's role
     * @param courseId the course ID
     * @param userId the user ID
     * @param newRole the new role
     * @param updatedBy the user making the update
     * @return CourseResult indicating success or failure
     */
    CourseResult updateMemberRole(String courseId, String userId, CourseRole newRole, String updatedBy);
    
    /**
     * Get all members of a course
     * @param courseId the course ID
     * @return list of course members
     */
    List<CourseMember> getCourseMembers(String courseId);
    
    /**
     * Get members by role
     * @param courseId the course ID
     * @param role the role to filter by
     * @return list of course members with the specified role
     */
    List<CourseMember> getMembersByRole(String courseId, CourseRole role);
    
    /**
     * Search members by name or email
     * @param courseId the course ID
     * @param searchTerm the search term
     * @return list of matching members
     */
    List<CourseMember> searchMembers(String courseId, String searchTerm);
    
    /**
     * Grant specific rights to a member
     * @param courseId the course ID
     * @param userId the user ID
     * @param rights list of rights to grant
     * @param grantedBy the user granting the rights
     * @return CourseResult indicating success or failure
     */
    CourseResult grantMemberRights(String courseId, String userId, List<String> rights, String grantedBy);
    
    /**
     * Revoke specific rights from a member
     * @param courseId the course ID
     * @param userId the user ID
     * @param rights list of rights to revoke
     * @param revokedBy the user revoking the rights
     * @return CourseResult indicating success or failure
     */
    CourseResult revokeMemberRights(String courseId, String userId, List<String> rights, String revokedBy);
    
    // ========== UC4: DESIGN COURSE STRUCTURE ==========
    
    /**
     * Get course element tree (structure)
     * @param courseId the course ID
     * @return list of course elements (tree structure)
     */
    List<CourseElement> getCourseStructure(String courseId);
    
    /**
     * Get a specific course element
     * @param courseId the course ID
     * @param elementId the element ID
     * @return the CourseElement or null if not found
     */
    CourseElement getCourseElement(String courseId, String elementId);
    
    /**
     * Add a new course element
     * @param courseId the course ID
     * @param parentElementId the parent element ID (null for root level)
     * @param title the element title
     * @param type the element type
     * @param userId the user adding the element
     * @return CourseResult with the created element
     */
    CourseResult addCourseElement(String courseId, String parentElementId, String title, 
                                  CourseElementType type, String userId);
    
    /**
     * Update a course element
     * @param courseId the course ID
     * @param elementId the element ID
     * @param title new title (null to keep existing)
     * @param description new description (null to keep existing)
     * @param visible visibility flag
     * @param userId the user making the update
     * @return CourseResult indicating success or failure
     */
    CourseResult updateCourseElement(String courseId, String elementId, String title, 
                                     String description, Boolean visible, String userId);
    
    /**
     * Delete a course element
     * @param courseId the course ID
     * @param elementId the element ID to delete
     * @param userId the user deleting the element
     * @return CourseResult indicating success or failure
     */
    CourseResult deleteCourseElement(String courseId, String elementId, String userId);
    
    /**
     * Move/reorder a course element
     * @param courseId the course ID
     * @param elementId the element ID to move
     * @param newParentId the new parent element ID
     * @param newOrder the new order position
     * @param userId the user making the change
     * @return CourseResult indicating success or failure
     */
    CourseResult moveCourseElement(String courseId, String elementId, String newParentId, 
                                   int newOrder, String userId);
    
    /**
     * Set access condition for an element
     * @param courseId the course ID
     * @param elementId the element ID
     * @param accessCondition the access condition expression
     * @param userId the user making the change
     * @return CourseResult indicating success or failure
     */
    CourseResult setElementAccessCondition(String courseId, String elementId, 
                                           String accessCondition, String userId);
    
    // ========== UC5: MANAGE COURSE LEARNING MATERIALS ==========
    
    /**
     * Create a new learning resource
     * @param title the resource title
     * @param description the resource description
     * @param type the resource type
     * @param ownerId the owner's user ID
     * @return CourseResult with the created resource
     */
    CourseResult createLearningResource(String title, String description, 
                                        LearningResourceType type, String ownerId);
    
    /**
     * Get a learning resource by ID
     * @param resourceId the resource ID
     * @return the LearningResource or null if not found
     */
    LearningResource getLearningResource(String resourceId);
    
    /**
     * Get all learning resources
     * @return list of all learning resources
     */
    List<LearningResource> getAllLearningResources();
    
    /**
     * Get learning resources by owner
     * @param ownerId the owner's user ID
     * @return list of learning resources owned by the user
     */
    List<LearningResource> getLearningResourcesByOwner(String ownerId);
    
    /**
     * Get learning resources by type
     * @param type the resource type
     * @return list of learning resources of the specified type
     */
    List<LearningResource> getLearningResourcesByType(LearningResourceType type);
    
    /**
     * Link a learning resource to a course element
     * @param courseId the course ID
     * @param elementId the course element ID
     * @param resourceId the learning resource ID
     * @param userId the user making the link
     * @return CourseResult indicating success or failure
     */
    CourseResult linkResourceToElement(String courseId, String elementId, 
                                       String resourceId, String userId);
    
    /**
     * Unlink a learning resource from a course element
     * @param courseId the course ID
     * @param elementId the course element ID
     * @param userId the user unlinking the resource
     * @return CourseResult indicating success or failure
     */
    CourseResult unlinkResourceFromElement(String courseId, String elementId, String userId);
    
    /**
     * Delete a learning resource
     * @param resourceId the resource ID
     * @param userId the user deleting the resource
     * @return CourseResult indicating success or failure
     */
    CourseResult deleteLearningResource(String resourceId, String userId);
    
    /**
     * Update a learning resource
     * @param resourceId the resource ID
     * @param title new title (null to keep existing)
     * @param description new description (null to keep existing)
     * @param userId the user making the update
     * @return CourseResult indicating success or failure
     */
    CourseResult updateLearningResource(String resourceId, String title, 
                                        String description, String userId);
    
    // ========== UTILITY METHODS ==========
    
    /**
     * Check if a user has permission for a course operation
     * @param courseId the course ID
     * @param userId the user ID
     * @param requiredRole minimum required role
     * @return true if user has permission
     */
    boolean hasPermission(String courseId, String userId, CourseRole requiredRole);
    
    /**
     * Validate course structure (check for errors)
     * @param courseId the course ID
     * @return CourseResult with validation results
     */
    CourseResult validateCourseStructure(String courseId);
    
    /**
     * Search courses by keyword
     * @param keyword the search keyword
     * @return list of matching courses
     */
    List<Course> searchCourses(String keyword);
}
