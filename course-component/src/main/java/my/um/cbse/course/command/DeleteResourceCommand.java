package my.um.cbse.course.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;

/**
 * Karaf Shell Command: course:delete-resource
 * UC5 - Delete a learning resource
 * 
 * Usage: course:delete-resource <resourceId> <userId>
 */
@Command(scope = "course", name = "delete-resource", description = "Delete a learning resource")
@Component(
    service = DeleteResourceCommand.class,
    property = {
        "osgi.command.scope=course",
        "osgi.command.function=delete-resource"
    }
)
public class DeleteResourceCommand implements Action {

    @Argument(index = 0, name = "resourceId", description = "The resource ID to delete", required = true)
    private String resourceId;

    @Argument(index = 1, name = "userId", description = "The user ID deleting the resource", required = true)
    private String userId;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private CourseService courseService;

    @Override
    public Object execute() throws Exception {
        if (courseService == null) {
            System.out.println("ERROR: CourseService is not available");
            return null;
        }

        CourseResult result = courseService.deleteLearningResource(resourceId, userId);
        
        if (result.isSuccess()) {
            System.out.println("✓ SUCCESS: " + result.getMessage());
        } else {
            System.out.println("✗ FAILED: " + result.getMessage());
        }
        
        return result;
    }
}
