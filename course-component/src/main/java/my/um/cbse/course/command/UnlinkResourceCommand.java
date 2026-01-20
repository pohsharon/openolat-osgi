package my.um.cbse.course.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;

/**
 * Karaf Shell Command: course:unlink-resource
 * UC5 - Unlink a learning resource from a course element
 * 
 * Usage: course:unlink-resource <courseId> <elementId> <userId>
 */
@Command(scope = "course", name = "unlink-resource", description = "Unlink a learning resource from a course element")
@Component(
    service = Action.class,
    property = {
        "osgi.command.scope=course",
        "osgi.command.function=unlink_resource"
    }
)
public class UnlinkResourceCommand implements Action {

    @Argument(index = 0, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Argument(index = 1, name = "elementId", description = "The course element ID", required = true)
    private String elementId;

    @Argument(index = 2, name = "userId", description = "The user ID unlinking the resource", required = true)
    private String userId;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private CourseService courseService;

    public void unlink_resource(String courseId, String elementId, String userId) throws Exception {
        this.courseId = courseId;
        this.elementId = elementId;
        this.userId = userId;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        if (courseService == null) {
            System.out.println("ERROR: CourseService is not available");
            return null;
        }

        CourseResult result = courseService.unlinkResourceFromElement(courseId, elementId, userId);
        
        if (result.isSuccess()) {
            System.out.println("✓ SUCCESS: " + result.getMessage());
        } else {
            System.out.println("✗ FAILED: " + result.getMessage());
        }
        
        return result;
    }
}
