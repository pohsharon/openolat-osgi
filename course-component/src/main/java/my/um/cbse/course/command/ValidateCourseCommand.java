package my.um.cbse.course.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;

/**
 * Karaf Shell Command: course:validate
 * UC4 - Validate course structure
 * 
 * Usage: course:validate <courseId>
 */
@Command(scope = "course", name = "validate", description = "Validate course structure")
@Component(
    service = ValidateCourseCommand.class,
    property = {
        "osgi.command.scope=course",
        "osgi.command.function=validate"
    }
)
public class ValidateCourseCommand implements Action {

    @Argument(index = 0, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private CourseService courseService;

    @Override
    public Object execute() throws Exception {
        if (courseService == null) {
            System.out.println("ERROR: CourseService is not available");
            return null;
        }

        CourseResult result = courseService.validateCourseStructure(courseId);
        
        System.out.println("\n=== COURSE VALIDATION RESULTS ===\n");
        
        if (result.isSuccess()) {
            System.out.println("✓ " + result.getMessage());
        } else {
            System.out.println("✗ " + result.getMessage());
        }
        
        if (result.getData() != null && result.getData() instanceof java.util.Map) {
            @SuppressWarnings("unchecked")
            java.util.Map<String, java.util.List<String>> data = 
                (java.util.Map<String, java.util.List<String>>) result.getData();
            
            java.util.List<String> errors = data.get("errors");
            java.util.List<String> warnings = data.get("warnings");
            
            if (errors != null && !errors.isEmpty()) {
                System.out.println("\n  ERRORS:");
                for (String error : errors) {
                    System.out.println("    ✗ " + error);
                }
            }
            
            if (warnings != null && !warnings.isEmpty()) {
                System.out.println("\n  WARNINGS:");
                for (String warning : warnings) {
                    System.out.println("    ⚠ " + warning);
                }
            }
        }
        
        return result;
    }
}
