package my.um.cbse.course.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.CourseDesign;
import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;

/**
 * Karaf Shell Command: course:create
 * UC1 - Create a new course
 * 
 * Usage: course:create <title> <ownerId> [--design LEARNING_PATH|CONVENTIONAL] [--description <desc>] [--semester <sem>]
 */
@Command(scope = "course", name = "create", description = "Create a new course")
@Component(
    service = CreateCourseCommand.class,
    property = {
        "osgi.command.scope=course",
        "osgi.command.function=create"
    }
)
public class CreateCourseCommand implements Action {

    @Argument(index = 0, name = "title", description = "The course title", required = true)
    private String title;

    @Argument(index = 1, name = "ownerId", description = "The owner/coach user ID", required = true)
    private String ownerId;

    @Option(name = "--design", aliases = {"-d"}, description = "Course design: LEARNING_PATH or CONVENTIONAL", required = false)
    private String design = "LEARNING_PATH";

    @Option(name = "--description", aliases = {"-desc"}, description = "Course description", required = false)
    private String description;

    @Option(name = "--semester", aliases = {"-s"}, description = "Semester/execution period", required = false)
    private String semester;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private CourseService courseService;

    @Override
    public Object execute() throws Exception {
        if (courseService == null) {
            System.out.println("ERROR: CourseService is not available");
            return null;
        }

        CourseDesign courseDesign;
        try {
            courseDesign = CourseDesign.valueOf(design.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Invalid design type. Use LEARNING_PATH or CONVENTIONAL");
            return null;
        }

        CourseResult result = courseService.createCourse(title, description, courseDesign, semester, ownerId);
        
        if (result.isSuccess()) {
            System.out.println("✓ SUCCESS: " + result.getMessage());
            if (result.getData() != null) {
                System.out.println("  Course: " + result.getData());
            }
        } else {
            System.out.println("✗ FAILED: " + result.getMessage());
        }
        
        return result;
    }
}
