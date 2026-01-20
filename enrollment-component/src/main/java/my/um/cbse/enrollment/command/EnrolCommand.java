package my.um.cbse.enrollment.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.EnrollmentResult;
import my.um.cbse.api.service.EnrollmentService;

/**
 * Karaf Shell Command: enrol
 * Allows students to self-enroll in a course
 * 
 * Usage: cbse:enrol <studentId> <courseId>
 */
@Command(scope = "cbse", name = "enrol", description = "Enrol a student in a course")
@Component(
    service = EnrolCommand.class,
    property = {
        "osgi.command.scope=cbse",
        "osgi.command.function=enrol"
    }
)
public class EnrolCommand implements Action {

    @Argument(index = 0, name = "studentId", description = "The student ID", required = true)
    private String studentId;

    @Argument(index = 1, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private EnrollmentService enrollmentService;

    public void enrol(String studentId, String courseId) throws Exception {
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
            System.out.println("Usage: cbse:enrol <studentId> <courseId>");
            return null;
        }

        EnrollmentResult result = enrollmentService.selfEnroll(studentId, courseId);
        
        if (result.isSuccess()) {
            System.out.println("✓ SUCCESS: " + result.getMessage());
        } else {
            System.out.println("✗ FAILED: " + result.getMessage());
        }
        
        return result;
    }
}
