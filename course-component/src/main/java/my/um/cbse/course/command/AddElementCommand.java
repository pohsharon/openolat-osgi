package my.um.cbse.course.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.CourseElementType;
import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;

/**
 * Karaf Shell Command: course:add-element
 * UC4 - Add a course element
 * 
 * Usage: course:add-element <courseId> <title> <type> <userId> [--parent <parentId>]
 */
@Command(scope = "course", name = "add-element", description = "Add a course element")
@Component(
    service = AddElementCommand.class,
    property = {
        "osgi.command.scope=course",
        "osgi.command.function=add-element"
    }
)
public class AddElementCommand implements Action {

    @Argument(index = 0, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Argument(index = 1, name = "title", description = "Element title", required = true)
    private String title;

    @Argument(index = 2, name = "type", description = "Element type: STRUCTURE, FOLDER, TASK, TEST, WIKI, BLOG, VIDEO, FORUM, etc.", required = true)
    private String type;

    @Argument(index = 3, name = "userId", description = "The user ID adding the element", required = true)
    private String userId;

    @Option(name = "--parent", aliases = {"-p"}, description = "Parent element ID (optional, defaults to root)", required = false)
    private String parentId;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private CourseService courseService;

    @Override
    public Object execute() throws Exception {
        if (courseService == null) {
            System.out.println("ERROR: CourseService is not available");
            return null;
        }

        CourseElementType elementType;
        try {
            elementType = CourseElementType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Invalid element type. Valid types:");
            System.out.println("  STRUCTURE, FOLDER, SINGLE_PAGE, DOCUMENT, TASK, TEST,");
            System.out.println("  WIKI, BLOG, PODCAST, VIDEO, FORUM, FORM, ASSESSMENT,");
            System.out.println("  SCORM, EXTERNAL_PAGE, CALENDAR, NOTIFICATIONS, PARTICIPANT_LIST");
            return null;
        }

        CourseResult result = courseService.addCourseElement(courseId, parentId, title, elementType, userId);
        
        if (result.isSuccess()) {
            System.out.println("✓ SUCCESS: " + result.getMessage());
            if (result.getData() != null) {
                System.out.println("  Element: " + result.getData());
            }
        } else {
            System.out.println("✗ FAILED: " + result.getMessage());
        }
        
        return result;
    }
}
