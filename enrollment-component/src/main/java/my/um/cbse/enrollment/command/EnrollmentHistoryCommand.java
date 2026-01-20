package my.um.cbse.enrollment.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.service.EnrollmentService;
import java.util.List;

/**
 * Karaf Shell Command: enrollmentHistory
 * View enrollment history for a student in a course
 */
@Command(scope = "cbse", name = "enrollmentHistory", description = "Show enrollment history for a course or student")
@Component(service = org.apache.karaf.shell.api.action.Action.class, immediate = true, property = {
    "osgi.command.scope=cbse",
    "osgi.command.function=enrollmentHistory"
})
public class EnrollmentHistoryCommand implements Action {
    @Argument(index = 0, name = "studentId", description = "The student ID", required = true)
    private String studentId;

    @Argument(index = 1, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private EnrollmentService enrollmentService;

    public void enrollmentHistory(String studentId, String courseId) throws Exception {
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

        List<String> history = enrollmentService.getEnrollmentHistory(studentId, courseId);
        
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║     ENROLLMENT HISTORY - " + studentId + " in " + courseId + "");
        System.out.println("╚════════════════════════════════════════════════════════╝");

        if (history == null || history.isEmpty()) {
            System.out.println("  (No history records found)");
            return history;
        }

        for (int i = 0; i < history.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + history.get(i));
        }
        System.out.println();

        return history;
    }
}
