package my.um.cbse.enrollment.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.EnrollmentResult;
import my.um.cbse.api.model.UserRole;
import my.um.cbse.api.service.EnrollmentService;

/**
 * Karaf Shell Command: manualUnenrol
 * Manually unenrol a student from a course (Lecturer/Admin only)
 */
@Command(scope = "cbse", name = "manualUnenrol", description = "Manually unenrol a student from a course (Lecturer/Admin)")
@Component(
    service = ManualUnenrolCommand.class,
    property = {
        "osgi.command.scope=cbse",
        "osgi.command.function=manualUnenrol"
    }
)
public class ManualUnenrolCommand implements Action {

    @Argument(index = 0, name = "studentId", description = "The student ID", required = true)
    private String studentId;

    @Argument(index = 1, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Argument(index = 2, name = "unenrolledBy", description = "The lecturer/admin ID", required = true)
    private String unenrolledBy;

    @Argument(index = 3, name = "role", description = "Role of person unenrolling (LECTURER or ADMIN)", required = true)
    private String roleArg;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private EnrollmentService enrollmentService;

    public void manualUnenrol(String studentId, String courseId, String unenrolledBy, String roleArg) throws Exception {
        this.studentId = studentId;
        this.courseId = courseId;
        this.unenrolledBy = unenrolledBy;
        this.roleArg = roleArg;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        if (enrollmentService == null) {
            System.out.println("ERROR: EnrollmentService is not available");
            return null;
        }

        try {
            UserRole role = UserRole.valueOf(roleArg.toUpperCase());
            
            EnrollmentResult result = enrollmentService.manualUnenroll(studentId, courseId, unenrolledBy, role);
            
            if (result.isSuccess()) {
                System.out.println("✓ SUCCESS: " + result.getMessage());
                System.out.println("  Unenrolled by: " + unenrolledBy + " (role: " + role + ")");
            } else {
                System.out.println("✗ FAILED: " + result.getMessage());
            }
            
            return result;
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Invalid role. Use LECTURER or ADMIN");
            return null;
        }
    }
}
