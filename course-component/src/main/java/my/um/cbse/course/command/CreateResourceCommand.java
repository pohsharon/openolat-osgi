package my.um.cbse.course.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.model.LearningResourceType;
import my.um.cbse.api.service.CourseService;

/**
 * Karaf Shell Command: course:create-resource
 * UC5 - Create a learning resource
 * 
 * Usage: course:create-resource <title> <type> <ownerId> [--description <desc>]
 */
@Command(scope = "course", name = "create-resource", description = "Create a learning resource")
@Component(
    service = CreateResourceCommand.class,
    property = {
        "osgi.command.scope=course",
        "osgi.command.function=create-resource"
    }
)
public class CreateResourceCommand implements Action {

    @Argument(index = 0, name = "title", description = "Resource title", required = true)
    private String title;

    @Argument(index = 1, name = "type", description = "Resource type: TEST, WIKI, BLOG, PODCAST, DOCUMENT, VIDEO, FORM, SCORM, CP, GLOSSARY, RESOURCE_FOLDER", required = true)
    private String type;

    @Argument(index = 2, name = "ownerId", description = "The owner user ID", required = true)
    private String ownerId;

    @Option(name = "--description", aliases = {"-d"}, description = "Resource description", required = false)
    private String description;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private CourseService courseService;

    @Override
    public Object execute() throws Exception {
        if (courseService == null) {
            System.out.println("ERROR: CourseService is not available");
            return null;
        }

        LearningResourceType resourceType;
        try {
            resourceType = LearningResourceType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Invalid resource type. Valid types:");
            System.out.println("  TEST, WIKI, BLOG, PODCAST, DOCUMENT, VIDEO,");
            System.out.println("  FORM, SCORM, CP, GLOSSARY, RESOURCE_FOLDER");
            return null;
        }

        CourseResult result = courseService.createLearningResource(title, description, resourceType, ownerId);
        
        if (result.isSuccess()) {
            System.out.println("✓ SUCCESS: " + result.getMessage());
            if (result.getData() != null) {
                System.out.println("  Resource: " + result.getData());
            }
        } else {
            System.out.println("✗ FAILED: " + result.getMessage());
        }
        
        return result;
    }
}
