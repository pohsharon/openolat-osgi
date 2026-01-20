package my.um.cbse.course.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;

/**
 * Karaf Shell Command: course:copy
 * UC1 - Copy an existing course
 * 
 * Usage: course:copy <sourceCourseId> <ownerId> [--title <newTitle>]
 */
@Command(scope = "course", name = "copy", description = "Copy an existing course")
@Component(
    service = CopyCourseCommand.class,
    property = {
        "osgi.command.scope=course",
        "osgi.command.function=copy"
    }
)
public class CopyCourseCommand implements Action {

    @Argument(index = 0, name = "sourceCourseId", description = "The source course ID to copy", required = true)
    private String sourceCourseId;

    @Argument(index = 1, name = "ownerId", description = "The owner user ID for the new course", required = true)
    private String ownerId;

    @Option(name = "--title", aliases = {"-t"}, description = "New course title (optional)", required = false)
    private String newTitle;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private CourseService courseService;

    @Override
    public Object execute() throws Exception {
        if (courseService == null) {
            System.out.println("ERROR: CourseService is not available");
            return null;
        }

        CourseResult result = courseService.copyCourse(sourceCourseId, newTitle, ownerId);
        
        if (result.isSuccess()) {
            System.out.println("✓ SUCCESS: " + result.getMessage());
            if (result.getData() != null) {
                System.out.println("  Copied Course: " + result.getData());
            }
        } else {
            System.out.println("✗ FAILED: " + result.getMessage());
        }
        
        return result;
    }
}
