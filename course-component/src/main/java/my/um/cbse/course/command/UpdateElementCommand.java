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
 * Karaf Shell Command: course:update-element
 * UC4 - Update a course element
 * 
 * Usage: course:update-element <courseId> <elementId> <userId> [--title <title>] [--description <desc>] [--visible true|false]
 */
@Command(scope = "course", name = "update-element", description = "Update a course element")
@Component(
    service = UpdateElementCommand.class,
    property = {
        "osgi.command.scope=course",
        "osgi.command.function=update-element"
    }
)
public class UpdateElementCommand implements Action {

    @Argument(index = 0, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Argument(index = 1, name = "elementId", description = "The element ID to update", required = true)
    private String elementId;

    @Argument(index = 2, name = "userId", description = "The user ID making the update", required = true)
    private String userId;

    @Option(name = "--title", aliases = {"-t"}, description = "New element title", required = false)
    private String title;

    @Option(name = "--description", aliases = {"-d"}, description = "New element description", required = false)
    private String description;

    @Option(name = "--visible", aliases = {"-v"}, description = "Visibility: true or false", required = false)
    private String visible;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private CourseService courseService;

    @Override
    public Object execute() throws Exception {
        if (courseService == null) {
            System.out.println("ERROR: CourseService is not available");
            return null;
        }

        Boolean visibleFlag = null;
        if (visible != null) {
            visibleFlag = Boolean.parseBoolean(visible);
        }

        if (title == null && description == null && visible == null) {
            System.out.println("No updates specified. Use --title, --description, or --visible options.");
            return null;
        }

        CourseResult result = courseService.updateCourseElement(courseId, elementId, title, description, visibleFlag, userId);
        
        if (result.isSuccess()) {
            System.out.println("✓ SUCCESS: " + result.getMessage());
            if (result.getData() != null) {
                System.out.println("  Updated Element: " + result.getData());
            }
        } else {
            System.out.println("✗ FAILED: " + result.getMessage());
        }
        
        return result;
    }
}
