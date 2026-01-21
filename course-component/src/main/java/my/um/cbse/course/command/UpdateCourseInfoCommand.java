package my.um.cbse.course.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;

/**
 * Karaf Shell Command: course:update_info
 * UC2 - Update course basic information
 * 
 * Usage: course:update_info <courseId> <title> <description> <courseCode> <userId>
 */
@Command(scope = "course", name = "update-info", description = "Update course basic information")
@Component(
    service = Action.class,
    property = {
        "osgi.command.scope=course",
        "osgi.command.function=update_info"
    }
)
public class UpdateCourseInfoCommand implements Action {

    @Argument(index = 0, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Argument(index = 1, name = "title", description = "New course title", required = true)
    private String title;

    @Argument(index = 2, name = "description", description = "New course description", required = true)
    private String description;

    @Argument(index = 3, name = "courseCode", description = "New course code", required = true)
    private String courseCode;

    @Argument(index = 4, name = "userId", description = "User ID making the update", required = true)
    private String userId;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private CourseService courseService;

    public void update_info(String courseId, String title, String description, String courseCode, String userId) throws Exception {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.courseCode = courseCode;
        this.userId = userId;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        if (courseService == null) {
            System.out.println("ERROR: CourseService is not available");
            return null;
        }

        System.out.println("\n[UPDATE-INFO] Course: " + courseId);
        System.out.println("  New Title: " + title);
        System.out.println("  New Description: " + description);
        System.out.println("  New Code: " + courseCode);
        
        CourseResult result = courseService.updateCourseInfo(courseId, title, description, courseCode, userId);
        
        if (result.isSuccess()) {
            System.out.println("✓ SUCCESS: " + result.getMessage());
        } else {
            System.out.println("✗ FAILED: " + result.getMessage());
        }
        
        return result;
    }
}
