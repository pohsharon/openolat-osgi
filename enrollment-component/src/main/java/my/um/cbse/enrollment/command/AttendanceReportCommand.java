package my.um.cbse.enrollment.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.service.EnrollmentService;
import java.util.List;
import my.um.cbse.api.model.AttendanceRecord;

/**
 * Karaf Shell Command: attendanceReport
 * Display attendance report for a student in a course
 */
@Command(scope = "cbse", name = "attendanceReport", description = "Display attendance report for a student in a course")
@Component(
    service = AttendanceReportCommand.class,
    property = {
        "osgi.command.scope=cbse",
        "osgi.command.function=attendanceReport"
    }
)
public class AttendanceReportCommand implements Action {

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

        List<AttendanceRecord> records = enrollmentService.getStudentAttendance(studentId, courseId);
        double attendanceRate = enrollmentService.getAttendanceRate(studentId, courseId);
        
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║     ATTENDANCE REPORT - " + studentId + " in " + courseId + "");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println(String.format("Attendance Rate: %.1f%%", attendanceRate));
        System.out.println(repeatString("─", 55));

        if (records == null || records.isEmpty()) {
            System.out.println("  (No attendance records found)");
            return records;
        }

        System.out.println(String.format("%-15s %-15s", "Date", "Status"));
        System.out.println(repeatString("─", 30));
        
        int present = 0, absent = 0;
        for (AttendanceRecord record : records) {
            String status = record.isPresent() ? "PRESENT" : "ABSENT";
            if (record.isPresent()) present++;
            else absent++;
            System.out.println(String.format("%-15s %-15s", record.getSessionDate(), status));
        }
        
        System.out.println(repeatString("─", 30));
        System.out.println(String.format("Present: %d  |  Absent: %d  |  Total: %d", present, absent, records.size()));
        System.out.println();

        return records;
    }

    private String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}
