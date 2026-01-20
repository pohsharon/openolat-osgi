package my.um.cbse.enrollment.command;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.apache.karaf.shell.api.action.Action;
import my.um.cbse.api.model.Student;
import my.um.cbse.api.service.EnrollmentService;
import java.util.List;

/**
 * Karaf Shell Command: findStudent
 * Search for students in a course
 * 
 * Usage: cbse:findStudent <courseId> [searchTerm]
 */
@Component(
    service = Object.class,
    immediate = true,
    property = {
        "osgi.command.scope=cbse",
        "osgi.command.function=findStudent"
    }
)
public class FindStudentCommand implements Action {

    private String courseId;
    private String searchTerm;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private EnrollmentService enrollmentService;

    public void findStudent(String courseId, String searchTerm) throws Exception {
        this.courseId = courseId;
        this.searchTerm = searchTerm;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        if (enrollmentService == null) {
            System.out.println("ERROR: EnrollmentService is not available");
            return null;
        }

        if (courseId == null) {
            System.out.println("Usage: cbse:findStudent <courseId> [searchTerm]");
            return null;
        }

        List<Student> students;
        if (searchTerm != null && !searchTerm.isEmpty()) {
            students = enrollmentService.searchEnrolledStudents(courseId, searchTerm);
            System.out.println("\nSearch Results for '" + searchTerm + "' in course " + courseId + ":");
        } else {
            students = enrollmentService.getEnrolledStudents(courseId);
            System.out.println("\nAll enrolled students in course " + courseId + ":");
        }

        if (students == null || students.isEmpty()) {
            System.out.println("  (No students found)");
            return students;
        }

        System.out.println(String.format("%-15s %-20s %-20s", "Student ID", "Name", "Email"));
        System.out.println(repeatString("\u2500", 55));
        for (Student student : students) {
            System.out.println(String.format("%-15s %-20s %-20s", 
                student.getStudentId(), 
                student.getName(), 
                student.getEmail()));
        }
        System.out.println("\nTotal: " + students.size() + " student(s)");

        return students;
    }

    private String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}
