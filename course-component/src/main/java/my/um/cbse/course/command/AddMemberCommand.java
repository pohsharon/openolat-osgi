package my.um.cbse.course.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.model.CourseRole;
import my.um.cbse.api.service.CourseService;

/**
 * Karaf Shell Command: course:add-member
 * UC3 - Add a member to a course
 * 
 * Usage: course:add-member <courseId> <userId> <userName> <role> <addedBy> [--email <email>]
 */
@Command(scope = "course", name = "add-member", description = "Add a member to a course")
@Component(
    service = AddMemberCommand.class,
    property = {
        "osgi.command.scope=course",
        "osgi.command.function=add-member"
    }
)
public class AddMemberCommand implements Action {

    @Argument(index = 0, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Argument(index = 1, name = "userId", description = "The user ID to add", required = true)
    private String userId;

    @Argument(index = 2, name = "userName", description = "The user's name", required = true)
    private String userName;

    @Argument(index = 3, name = "role", description = "Role: OWNER, COACH, PARTICIPANT, GROUP_COACH, GUEST", required = true)
    private String role;

    @Argument(index = 4, name = "addedBy", description = "The user ID adding this member", required = true)
    private String addedBy;

    @Option(name = "--email", aliases = {"-e"}, description = "User's email address", required = false)
    private String email;

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
            courseRole = CourseRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Invalid role. Use OWNER, COACH, PARTICIPANT, GROUP_COACH, or GUEST");
            return null;
        }

        CourseResult result = courseService.addCourseMember(courseId, userId, userName, email, courseRole, addedBy);
        
        if (result.isSuccess()) {
            System.out.println("✓ SUCCESS: " + result.getMessage());
            if (result.getData() != null) {
                System.out.println("  Member: " + result.getData());
            }
        } else {
            System.out.println("✗ FAILED: " + result.getMessage());
        }
        
        return result;
    }
}
