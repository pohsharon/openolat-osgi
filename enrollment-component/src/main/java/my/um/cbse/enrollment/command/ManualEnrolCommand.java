package my.um.cbse.enrollment.command;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.EnrollmentResult;
import my.um.cbse.api.model.UserRole;
import my.um.cbse.api.service.EnrollmentService;

/**
 * Karaf Shell Command: manualEnrol
 * Manually enrol a student in a course (Lecturer/Admin only)
 * 
 * Usage: cbse:manualEnrol <studentId> <courseId> <enrolledBy> <role>
 */
@Component(
    service = Object.class,
    immediate = true,
    property = {
        "osgi.command.scope=cbse",
        "osgi.command.function=manualEnrol"
    }
)
public class ManualEnrolCommand implements Action {

    private String studentId;
    private String courseId;
    private String enrolledBy;
    private String roleArg;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private EnrollmentService enrollmentService;

    public ManualEnrolCommand manualEnrol(String studentId, String courseId, String enrolledBy, String role) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrolledBy = enrolledBy;
        this.roleArg = role;
        return this;
    }

    @Override
    public Object execute() throws Exception {
        if (enrollmentService == null) {
            System.out.println("ERROR: EnrollmentService is not available");
            return null;
        }

        if (studentId == null || courseId == null || enrolledBy == null || roleArg == null) {
            System.out.println("Usage: cbse:manualEnrol <studentId> <courseId> <enrolledBy> <role>");
            return null;
        }

        try {
            UserRole role = UserRole.valueOf(roleArg.toUpperCase());
            
            EnrollmentResult result = enrollmentService.manualEnroll(studentId, courseId, enrolledBy, role);
            
            if (result.isSuccess()) {
                System.out.println("✓ SUCCESS: " + result.getMessage());
                System.out.println("  Enrolled by: " + enrolledBy + " (role: " + role + ")");
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
