package my.um.cbse.enrollment.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.Enrollment;
import my.um.cbse.api.service.EnrollmentService;

/**
 * Karaf Shell Command: enrollmentInfo
 * Get detailed enrollment information for a student in a course
 */
@Command(scope = "cbse", name = "enrollmentInfo", description = "Get enrollment information for a student in a course")
@Component(
    service = EnrollmentInfoCommand.class,
    property = {
        "osgi.command.scope=cbse",
        "osgi.command.function=enrollmentInfo"
    }
)
public class EnrollmentInfoCommand implements Action {

    @Argument(index = 0, name = "studentId", description = "The student ID", required = true)
    private String studentId;

    @Argument(index = 1, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private EnrollmentService enrollmentService;

    @Override
    public Object execute() throws Exception {
        if (enrollmentService == null) {
            System.out.println("ERROR: EnrollmentService is not available");
            return null;
        }

        Enrollment enrollment = enrollmentService.getEnrollment(studentId, courseId);
        
        if (enrollment == null) {
            System.out.println("✗ No enrollment found for student " + studentId + " in course " + courseId);
            return null;
        }

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║            ENROLLMENT INFORMATION                       ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println("Student ID:           " + enrollment.getStudentId());
        System.out.println("Course ID:            " + enrollment.getCourseId());
        System.out.println("Status:               " + enrollment.getStatus());
        System.out.println("Enrollment Date:      " + enrollment.getEnrollmentDate());
        System.out.println("Participation Score:  " + enrollment.getParticipationScore());
        
        double attendanceRate = enrollmentService.getAttendanceRate(studentId, courseId);
        System.out.println("Attendance Rate:      " + String.format("%.1f%%", attendanceRate));
        System.out.println();

        return enrollment;
    }
}
