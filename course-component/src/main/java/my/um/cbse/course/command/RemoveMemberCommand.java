package my.um.cbse.course.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;

/**
 * Karaf Shell Command: course:remove-member
 * UC3 - Remove a member from a course
 * 
 * Usage: course:remove-member <courseId> <userId> <removedBy>
 */
@Command(scope = "course", name = "remove-member", description = "Remove a member from a course")
@Component(
    service = RemoveMemberCommand.class,
    property = {
        "osgi.command.scope=course",
        "osgi.command.function=remove-member"
    }
)
public class RemoveMemberCommand implements Action {

    @Argument(index = 0, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Argument(index = 1, name = "userId", description = "The user ID to remove", required = true)
    private String userId;

    @Argument(index = 2, name = "removedBy", description = "The user ID removing this member", required = true)
    private String removedBy;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private CourseService courseService;

    @Override
    public Object execute() throws Exception {
        if (courseService == null) {
            System.out.println("ERROR: CourseService is not available");
            return null;
        }

        CourseResult result = courseService.removeCourseMember(courseId, userId, removedBy);
        
        if (result.isSuccess()) {
            System.out.println("✓ SUCCESS: " + result.getMessage());
        } else {
            System.out.println("✗ FAILED: " + result.getMessage());
        }
        
        return result;
    }
}
