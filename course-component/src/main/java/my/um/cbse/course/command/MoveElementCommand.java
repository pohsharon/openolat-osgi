package my.um.cbse.course.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;

/**
 * Karaf Shell Command: course:move-element
 * UC4 - Move/reorder a course element
 * 
 * Usage: course:move-element <courseId> <elementId> <newParentId> <newOrder> <userId>
 */
@Command(scope = "course", name = "move-element", description = "Move/reorder a course element")
@Component(
    service = Action.class,
    property = {
        "osgi.command.scope=course",
        "osgi.command.function=move_element"
    }
)
public class MoveElementCommand implements Action {

    @Argument(index = 0, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Argument(index = 1, name = "elementId", description = "The element ID to move", required = true)
    private String elementId;

    @Argument(index = 2, name = "newParentId", description = "The new parent element ID", required = true)
    private String newParentId;

    @Argument(index = 3, name = "newOrder", description = "The new order position", required = true)
    private int newOrder;

    @Argument(index = 4, name = "userId", description = "The user ID making the change", required = true)
    private String userId;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private CourseService courseService;

    public void move_element(String courseId, String elementId, String newParentId, int newOrder, String userId) throws Exception {
        this.courseId = courseId;
        this.elementId = elementId;
        this.newParentId = newParentId;
        this.newOrder = newOrder;
        this.userId = userId;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        if (courseService == null) {
            System.out.println("ERROR: CourseService is not available");
            return null;
        }

        CourseResult result = courseService.moveCourseElement(courseId, elementId, newParentId, newOrder, userId);
        
        if (result.isSuccess()) {
            System.out.println("✓ SUCCESS: " + result.getMessage());
            if (result.getData() != null) {
                System.out.println("  Moved Element: " + result.getData());
            }
        } else {
            System.out.println("✗ FAILED: " + result.getMessage());
        }
        
        return result;
    }
}
