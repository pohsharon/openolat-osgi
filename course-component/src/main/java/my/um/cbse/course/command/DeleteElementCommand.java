package my.um.cbse.course.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;

/**
 * Karaf Shell Command: course:delete-element
 * UC4 - Delete a course element
 * 
 * Usage: course:delete-element <courseId> <elementId> <userId>
 */
@Command(scope = "course", name = "delete-element", description = "Delete a course element")
@Component(
    service = DeleteElementCommand.class,
    property = {
        "osgi.command.scope=course",
        "osgi.command.function=delete-element"
    }
)
public class DeleteElementCommand implements Action {

    @Argument(index = 0, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Argument(index = 1, name = "elementId", description = "The element ID to delete", required = true)
    private String elementId;

    @Argument(index = 2, name = "userId", description = "The user ID deleting the element", required = true)
    private String userId;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private CourseService courseService;

    @Override
    public Object execute() throws Exception {
        if (courseService == null) {
            System.out.println("ERROR: CourseService is not available");
            return null;
        }

        CourseResult result = courseService.deleteCourseElement(courseId, elementId, userId);
        
        if (result.isSuccess()) {
            System.out.println("✓ SUCCESS: " + result.getMessage());
        } else {
            System.out.println("✗ FAILED: " + result.getMessage());
        }
        
        return result;
    }
}
