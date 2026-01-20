package my.um.cbse.api.model;

/**
 * Enumeration representing roles within a course
 */
public enum CourseRole {
    OWNER,           // Course owner with full permissions
    COACH,           // Course coach/instructor
    PARTICIPANT,     // Student/participant
    GROUP_COACH,     // Group-specific coach
    GUEST            // Guest access (limited)
}
