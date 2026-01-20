package my.um.cbse.enrollment.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.StudentGroup;
import my.um.cbse.api.service.EnrollmentService;

/**
 * Karaf Shell Command: createGroup
 * Create a student group/team for a course project
 */
@Command(scope = "cbse", name = "createGroup", description = "Create a new group in a course")
@Component(service = org.apache.karaf.shell.api.action.Action.class, immediate = true, property = {
    "osgi.command.scope=cbse",
    "osgi.command.function=createGroup"
})
public class CreateGroupCommand implements Action {

    @Argument(index = 0, name = "groupId", description = "The group ID", required = true)
    private String groupId;

    @Argument(index = 1, name = "groupName", description = "The group name", required = true)
    private String groupName;

    @Argument(index = 2, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Argument(index = 3, name = "maxSize", description = "Maximum group size", required = true)
    private int maxSize;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private EnrollmentService enrollmentService;

    public void createGroup(String groupId, String groupName, String courseId, int maxSize) throws Exception {
        this.groupId = groupId;
        this.groupName = groupName;
        this.courseId = courseId;
        this.maxSize = maxSize;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        if (enrollmentService == null) {
            System.out.println("ERROR: EnrollmentService is not available");
            return null;
        }

        StudentGroup group = enrollmentService.createGroup(groupId, groupName, courseId, maxSize);
        System.out.println("✓ Group created: " + group.getGroupName() + " (Max: " + group.getMaxSize() + ")");
        return group;
    }
}
