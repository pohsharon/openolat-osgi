package my.um.cbse.course.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.model.CourseRole;
import my.um.cbse.api.service.CourseService;

/**
 * Karaf Shell Command: course:update-role
 * UC3 - Update a member's role
 * 
 * Usage: course:update-role <courseId> <userId> <newRole> <updatedBy>
 */
@Command(scope = "course", name = "update-role", description = "Update a member's role")
@Component(
    service = UpdateMemberRoleCommand.class,
    property = {
        "osgi.command.scope=course",
        "osgi.command.function=update-role"
    }
)
public class UpdateMemberRoleCommand implements Action {

    @Argument(index = 0, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Argument(index = 1, name = "userId", description = "The user ID to update", required = true)
    private String userId;

    @Argument(index = 2, name = "newRole", description = "New role: OWNER, COACH, PARTICIPANT, GROUP_COACH, GUEST", required = true)
    private String newRole;

    @Argument(index = 3, name = "updatedBy", description = "The user ID making the update", required = true)
    private String updatedBy;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private CourseService courseService;

    @Override
    public Object execute() throws Exception {
        if (courseService == null) {
            System.out.println("ERROR: CourseService is not available");
            return null;
        }

        CourseRole courseRole;
        try {
            courseRole = CourseRole.valueOf(newRole.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Invalid role. Use OWNER, COACH, PARTICIPANT, GROUP_COACH, or GUEST");
            return null;
        }

        CourseResult result = courseService.updateMemberRole(courseId, userId, courseRole, updatedBy);
        
        if (result.isSuccess()) {
            System.out.println("✓ SUCCESS: " + result.getMessage());
            if (result.getData() != null) {
                System.out.println("  Updated Member: " + result.getData());
            }
        } else {
            System.out.println("✗ FAILED: " + result.getMessage());
        }
        
        return result;
    }
}
