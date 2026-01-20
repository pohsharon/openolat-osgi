package my.um.cbse.api.model;

/**
 * Enumeration representing the publication status of a course
 */
public enum CourseStatus {
    DRAFT,           // Course is being created/edited, not visible to students
    PUBLISHED,       // Course is published and available
    CLOSED,          // Course is closed, no new enrollments
    ARCHIVED         // Course is archived for historical reference
}
