package my.um.cbse.course.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;

/**
 * Karaf Shell Command: course:link-resource
 * UC5 - Link a learning resource to a course element
 * 
 * Usage: course:link-resource <courseId> <elementId> <resourceId> <userId>
 */
@Command(scope = "course", name = "link-resource", description = "Link a learning resource to a course element")
@Component(
    service = Action.class,
    property = {
        "osgi.command.scope=course",
        "osgi.command.function=link_resource"
    }
)
public class LinkResourceCommand implements Action {

    @Argument(index = 0, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Argument(index = 1, name = "elementId", description = "The course element ID", required = true)
    private String elementId;

    @Argument(index = 2, name = "resourceId", description = "The learning resource ID", required = true)
    private String resourceId;

    @Argument(index = 3, name = "userId", description = "The user ID making the link", required = true)
    private String userId;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private CourseService courseService;

    public void link_resource(String courseId, String elementId, String resourceId, String userId) throws Exception {
        this.courseId = courseId;
        this.elementId = elementId;
        this.resourceId = resourceId;
        this.userId = userId;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        if (courseService == null) {
            System.out.println("ERROR: CourseService is not available");
            return null;
        }

        CourseResult result = courseService.linkResourceToElement(courseId, elementId, resourceId, userId);
        
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
