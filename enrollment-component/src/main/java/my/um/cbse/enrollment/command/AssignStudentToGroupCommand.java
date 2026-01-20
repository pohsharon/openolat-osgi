package my.um.cbse.enrollment.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.service.EnrollmentService;

/**
 * Karaf Shell Command: assignStudentToGroup
 * Assign a student to a group in a course
 */
@Command(scope = "cbse", name = "assignStudentToGroup", description = "Assign a student to a group")
@Component(service = org.apache.karaf.shell.api.action.Action.class, immediate = true, property = {
    "osgi.command.scope=cbse",
    "osgi.command.function=assignStudentToGroup"
})
public class AssignStudentToGroupCommand implements Action {

    @Argument(index = 0, name = "studentId", description = "The student ID", required = true)
    private String studentId;

    @Argument(index = 1, name = "groupId", description = "The group ID", required = true)
    private String groupId;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private EnrollmentService enrollmentService;

    public void assignStudentToGroup(String studentId, String groupId) throws Exception {
        this.studentId = studentId;
        this.groupId = groupId;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        if (enrollmentService == null) {
            System.out.println("ERROR: EnrollmentService is not available");
            return null;
        }

        boolean assigned = enrollmentService.assignStudentToGroup(studentId, groupId);
        if (assigned) {
            System.out.println("✓ SUCCESS: Student " + studentId + " assigned to group " + groupId);
            return Boolean.TRUE;
        } else {
            System.out.println("✗ FAILED: Could not assign student");
            return Boolean.FALSE;
        }
    }
}
