package my.um.cbse.course.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;

/**
 * Karaf Shell Command: course:update_metadata
 * UC2 - Update course metadata
 * 
 * Usage: course:update_metadata <courseId> <semester> <credits> <license> <institution> <userId>
 */
@Command(scope = "course", name = "update-metadata", description = "Update course metadata")
@Component(
    service = Action.class,
    property = {
        "osgi.command.scope=course",
        "osgi.command.function=update_metadata"
    }
)
public class UpdateCourseMetadataCommand implements Action {

    @Argument(index = 0, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Argument(index = 1, name = "semester", description = "Semester (e.g., 2024-1, 2024-2)", required = true)
    private String semester;

    @Argument(index = 2, name = "credits", description = "Number of credits", required = true)
    private String creditsStr;

    @Argument(index = 3, name = "license", description = "License type", required = true)
    private String license;

    @Argument(index = 4, name = "institution", description = "Institution name", required = true)
    private String institution;

    @Argument(index = 5, name = "userId", description = "User ID making the update", required = true)
    private String userId;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private CourseService courseService;

    public void update_metadata(String courseId, String semester, String creditsStr, String license, String institution, String userId) throws Exception {
        this.courseId = courseId;
        this.semester = semester;
        this.creditsStr = creditsStr;
        this.license = license;
        this.institution = institution;
        this.userId = userId;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        if (courseService == null) {
            System.out.println("ERROR: CourseService is not available");
            return null;
        }

        Integer credits;
        try {
            credits = Integer.parseInt(creditsStr);
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Credits must be a valid number");
            return null;
        }

        System.out.println("\n[UPDATE-METADATA] Course: " + courseId);
        System.out.println("  Semester: " + semester);
        System.out.println("  Credits: " + credits);
        System.out.println("  License: " + license);
        System.out.println("  Institution: " + institution);
        
        CourseResult result = courseService.updateCourseMetadata(courseId, semester, credits, license, institution, userId);
        
        if (result.isSuccess()) {
            System.out.println("✓ SUCCESS: " + result.getMessage());
        } else {
            System.out.println("✗ FAILED: " + result.getMessage());
        }
        
        return result;
    }
}
