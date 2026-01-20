package my.um.cbse.enrollment.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.service.EnrollmentService;

/**
 * Karaf Shell Command: markAttendance
 * Mark attendance for a student in a course session
 */
@Command(scope = "cbse", name = "markAttendance", description = "Mark attendance for a student in a course session")
@Component(
    service = MarkAttendanceCommand.class,
    property = {
        "osgi.command.scope=cbse",
        "osgi.command.function=markAttendance"
    }
)
public class MarkAttendanceCommand implements Action {

    @Argument(index = 0, name = "studentId", description = "The student ID", required = true)
    private String studentId;

    @Argument(index = 1, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Argument(index = 2, name = "sessionDate", description = "The session date (YYYY-MM-DD)", required = true)
    private String sessionDate;

    @Argument(index = 3, name = "present", description = "Attendance status (true/false or yes/no)", required = true)
    private String presentArg;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private EnrollmentService enrollmentService;

    public void markAttendance(String studentId, String courseId, String sessionDate, String presentArg) throws Exception {
        this.studentId = studentId;
        this.courseId = courseId;
        this.sessionDate = sessionDate;
        this.presentArg = presentArg;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        if (enrollmentService == null) {
            System.out.println("ERROR: EnrollmentService is not available");
            return null;
        }

        // Parse presence argument
        boolean present = parseBoolean(presentArg);
        
        Object attendance = enrollmentService.markAttendance(studentId, courseId, sessionDate, present);
        
        String status = present ? "PRESENT" : "ABSENT";
        System.out.println("✓ Marked " + studentId + " as " + status + " on " + sessionDate);
        System.out.println("  Attendance Rate: " + String.format("%.1f%%", 
            enrollmentService.getAttendanceRate(studentId, courseId)));

        return attendance;
    }

    private boolean parseBoolean(String value) {
        return value.equalsIgnoreCase("true") || 
               value.equalsIgnoreCase("yes") || 
               value.equalsIgnoreCase("1") ||
               value.equalsIgnoreCase("y");
    }
}
