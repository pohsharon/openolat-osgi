package my.um.cbse.course.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;

/**
 * Karaf Shell Command: course:update
 * UC2 - Update course info and metadata
 * 
 * Usage: course:update <courseId> <userId> [--title <title>] [--description <desc>] [--code <code>] 
 *        [--semester <sem>] [--credits <credits>] [--institution <inst>]
 */
@Command(scope = "course", name = "update", description = "Update course information")
@Component(
    service = UpdateCourseCommand.class,
    property = {
        "osgi.command.scope=course",
        "osgi.command.function=update"
    }
)
public class UpdateCourseCommand implements Action {

    @Argument(index = 0, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Argument(index = 1, name = "userId", description = "The user ID making the update", required = true)
    private String userId;

    @Option(name = "--title", aliases = {"-t"}, description = "New course title", required = false)
    private String title;

    @Option(name = "--description", aliases = {"-d"}, description = "New course description", required = false)
    private String description;

    @Option(name = "--code", aliases = {"-c"}, description = "New course code", required = false)
    private String courseCode;

    @Option(name = "--semester", aliases = {"-s"}, description = "New semester", required = false)
    private String semester;

    @Option(name = "--credits", description = "New credits value", required = false)
    private Integer credits;

    @Option(name = "--institution", aliases = {"-i"}, description = "New institution", required = false)
    private String institution;

    @Option(name = "--license", aliases = {"-l"}, description = "New license", required = false)
    private String license;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private CourseService courseService;

    @Override
    public Object execute() throws Exception {
        if (courseService == null) {
            System.out.println("ERROR: CourseService is not available");
            return null;
        }

        boolean updated = false;
        CourseResult result = null;

        // Update basic info if provided
        if (title != null || description != null || courseCode != null) {
            result = courseService.updateCourseInfo(courseId, title, description, courseCode, userId);
            if (result.isSuccess()) {
                updated = true;
                System.out.println("✓ Course info updated");
            } else {
                System.out.println("✗ Failed to update course info: " + result.getMessage());
                return result;
            }
        }

        // Update metadata if provided
        if (semester != null || credits != null || license != null || institution != null) {
            result = courseService.updateCourseMetadata(courseId, semester, credits, license, institution, userId);
            if (result.isSuccess()) {
                updated = true;
                System.out.println("✓ Course metadata updated");
            } else {
                System.out.println("✗ Failed to update metadata: " + result.getMessage());
                return result;
            }
        }

        if (!updated) {
            System.out.println("No updates specified. Use options like --title, --description, etc.");
        } else {
            System.out.println("\n✓ SUCCESS: Course updated successfully");
        }
        
        return result;
    }
}
