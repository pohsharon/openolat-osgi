package my.um.cbse.course.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.model.CourseStatus;
import my.um.cbse.api.service.CourseService;

/**
 * Karaf Shell Command: course:publish
 * UC2 - Update course publication status
 * 
 * Usage: course:publish <courseId> <status> <userId>
 *        status: DRAFT, PUBLISHED, CLOSED, ARCHIVED
 */
@Command(scope = "course", name = "publish", description = "Update course publication status")
@Component(
    service = PublishCourseCommand.class,
    property = {
        "osgi.command.scope=course",
        "osgi.command.function=publish"
    }
)
public class PublishCourseCommand implements Action {

    @Argument(index = 0, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Argument(index = 1, name = "status", description = "New status: DRAFT, PUBLISHED, CLOSED, ARCHIVED", required = true)
    private String status;

    @Argument(index = 2, name = "userId", description = "The user ID making the change", required = true)
    private String userId;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private CourseService courseService;

    @Override
    public Object execute() throws Exception {
        if (courseService == null) {
            System.out.println("ERROR: CourseService is not available");
            return null;
        }

        CourseStatus courseStatus;
        try {
            courseStatus = CourseStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Invalid status. Use DRAFT, PUBLISHED, CLOSED, or ARCHIVED");
            return null;
        }

        CourseResult result = courseService.updateCourseStatus(courseId, courseStatus, userId);
        
        if (result.isSuccess()) {
            System.out.println("✓ SUCCESS: " + result.getMessage());
        } else {
            System.out.println("✗ FAILED: " + result.getMessage());
        }
        
        return result;
    }
}
