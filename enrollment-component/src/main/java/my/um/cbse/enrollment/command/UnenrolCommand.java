package my.um.cbse.enrollment.command;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.apache.karaf.shell.api.action.Action;
import my.um.cbse.api.model.EnrollmentResult;
import my.um.cbse.api.service.EnrollmentService;

/**
 * Karaf Shell Command: unenrol
 * Allows students to self-unenroll from a course
 * 
 * Usage: cbse:unenrol <studentId> <courseId>
 */
@Component(
    service = Object.class,
    immediate = true,
    property = {
        "osgi.command.scope=cbse",
        "osgi.command.function=unenrol"
    }
)
public class UnenrolCommand implements Action {

    private String studentId;
    private String courseId;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private EnrollmentService enrollmentService;

    public void unenrol(String studentId, String courseId) throws Exception {
        this.studentId = studentId;
        this.courseId = courseId;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        if (enrollmentService == null) {
            System.out.println("ERROR: EnrollmentService is not available");
            return null;
        }

        if (studentId == null || courseId == null) {
            System.out.println("Usage: cbse:unenrol <studentId> <courseId>");
            return null;
        }

        EnrollmentResult result = enrollmentService.selfUnenroll(studentId, courseId);
        
        if (result.isSuccess()) {
            System.out.println("✓ SUCCESS: " + result.getMessage());
        } else {
            System.out.println("✗ FAILED: " + result.getMessage());
        }
        
        return result;
    }
}
