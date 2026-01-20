package my.um.cbse.enrollment.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.Enrollment;
import my.um.cbse.api.service.EnrollmentService;
import java.util.List;

/**
 * Karaf Shell Command: viewCourseEnrollments
 * View all enrollment records for a course
 */
@Command(scope = "cbse", name = "viewCourseEnrollments", description = "View all enrollments in a course")
@Component(service = org.apache.karaf.shell.api.action.Action.class, immediate = true, property = {
    "osgi.command.scope=cbse",
    "osgi.command.function=viewCourseEnrollments"
})
public class ViewCourseEnrollmentsCommand implements Action {

    @Argument(index = 0, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private EnrollmentService enrollmentService;

    public void viewCourseEnrollments(String courseId) throws Exception {
        this.courseId = courseId;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        if (enrollmentService == null) {
            System.out.println("ERROR: EnrollmentService is not available");
            return null;
        }

        List<Enrollment> enrollments = enrollmentService.getAllEnrollments(courseId);
        System.out.println("\nAll Enrollments in " + courseId + ":");
        System.out.println("Total: " + (enrollments != null ? enrollments.size() : 0));
        if (enrollments != null) {
            for (Enrollment e : enrollments) {
                System.out.println("  " + e.toString());
            }
        }
        return enrollments;
    }
}
