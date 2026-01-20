package my.um.cbse.enrollment.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.service.EnrollmentService;

/**
 * Karaf Shell Command: availableSpots
 * Check available enrollment spots in a course
 */
@Command(scope = "cbse", name = "availableSpots", description = "Check available enrollment spots")
@Component(service = org.apache.karaf.shell.api.action.Action.class, immediate = true, property = {
    "osgi.command.scope=cbse",
    "osgi.command.function=availableSpots"
})
public class AvailableSpotsCommand implements Action {

    @Argument(index = 0, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private EnrollmentService enrollmentService;

    public void availableSpots(String courseId) throws Exception {
        this.courseId = courseId;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        if (enrollmentService == null) {
            System.out.println("ERROR: EnrollmentService is not available");
            return null;
        }

        int available = enrollmentService.getAvailableSpots(courseId);
        System.out.println("Course " + courseId + " - Available spots: " + available + "/30");
        return available;
    }
}
