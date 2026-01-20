package my.um.cbse.enrollment.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.StudentGroup;
import my.um.cbse.api.service.EnrollmentService;
import java.util.List;

/**
 * Karaf Shell Command: viewGroups
 * View all groups in a course
 */
@Command(scope = "cbse", name = "viewGroups", description = "View all groups in a course")
@Component(service = org.apache.karaf.shell.api.action.Action.class, immediate = true, property = {
    "osgi.command.scope=cbse",
    "osgi.command.function=viewGroups"
})
public class ViewGroupsCommand implements Action {

    @Argument(index = 0, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private EnrollmentService enrollmentService;

    public void viewGroups(String courseId) throws Exception {
        this.courseId = courseId;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        if (enrollmentService == null) {
            System.out.println("ERROR: EnrollmentService is not available");
            return null;
        }

        List<StudentGroup> groups = enrollmentService.getCourseGroups(courseId);
        System.out.println("\nGroups in " + courseId + ":");
        if (groups != null) {
            for (StudentGroup g : groups) {
                System.out.println("  - " + g.getGroupName() + " (Max: " + g.getMaxSize() + ")");
            }
        }
        return groups;
    }
}
