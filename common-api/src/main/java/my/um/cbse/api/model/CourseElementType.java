package my.um.cbse.api.model;

/**
 * Enumeration representing types of course elements
 */
public enum CourseElementType {
    STRUCTURE,       // Container/section element
    FOLDER,          // File folder for documents
    SINGLE_PAGE,     // HTML page
    DOCUMENT,        // Document viewer
    TASK,            // Task/assignment element
    TEST,            // Quiz/test element
    WIKI,            // Collaborative wiki
    BLOG,            // Blog element
    PODCAST,         // Podcast/audio content
    VIDEO,           // Video content
    FORUM,           // Discussion forum
    FORM,            // Survey/form element
    ASSESSMENT,      // Assessment element
    SCORM,           // SCORM learning module
    EXTERNAL_PAGE,   // External URL/page
    CALENDAR,        // Calendar element
    NOTIFICATIONS,   // Notifications element
    PARTICIPANT_LIST // Participant list element
}
