package my.um.cbse.course.command;

import java.util.List;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.LearningResource;
import my.um.cbse.api.model.LearningResourceType;
import my.um.cbse.api.service.CourseService;

/**
 * Karaf Shell Command: course:resources
 * UC5 - List learning resources
 * 
 * Usage: course:resources [--owner <ownerId>] [--type <type>]
 */
@Command(scope = "course", name = "resources", description = "List learning resources")
@Component(
    service = Action.class,
    property = {
        "osgi.command.scope=course",
        "osgi.command.function=resources"
    }
)
public class ListResourcesCommand implements Action {

    @Option(name = "--owner", aliases = {"-o"}, description = "Filter by owner ID", required = false)
    private String ownerId;

    @Option(name = "--type", aliases = {"-t"}, description = "Filter by type: TEST, WIKI, BLOG, etc.", required = false)
    private String type;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private CourseService courseService;

    public void resources() throws Exception {
        execute();
    }

    @Override
    public Object execute() throws Exception {
        if (courseService == null) {
            System.out.println("ERROR: CourseService is not available");
            return null;
        }

        List<LearningResource> resources;
        
        if (type != null && !type.isEmpty()) {
            try {
                LearningResourceType resourceType = LearningResourceType.valueOf(type.toUpperCase());
                resources = courseService.getLearningResourcesByType(resourceType);
                System.out.println("\n=== LEARNING RESOURCES OF TYPE: " + resourceType + " ===");
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: Invalid resource type.");
                return null;
            }
        } else if (ownerId != null && !ownerId.isEmpty()) {
            resources = courseService.getLearningResourcesByOwner(ownerId);
            System.out.println("\n=== LEARNING RESOURCES BY OWNER: " + ownerId + " ===");
        } else {
            resources = courseService.getAllLearningResources();
            System.out.println("\n=== ALL LEARNING RESOURCES ===");
        }
        
        if (resources.isEmpty()) {
            System.out.println("No resources found.");
        } else {
            System.out.println(String.format("%-10s %-30s %-15s %-10s %-8s", 
                "ID", "TITLE", "TYPE", "OWNER", "SHARED"));
            System.out.println(repeatString("-", 75));
            
            for (LearningResource resource : resources) {
                System.out.println(String.format("%-10s %-30s %-15s %-10s %-8s",
                    resource.getResourceId(),
                    truncate(resource.getTitle(), 28),
                    resource.getType(),
                    resource.getOwnerId(),
                    resource.isShared() ? "Yes" : "No"));
            }
            System.out.println("\nTotal: " + resources.size() + " resource(s)");
        }
        
        return resources;
    }
    
    private String truncate(String str, int maxLen) {
        if (str == null) return "";
        return str.length() <= maxLen ? str : str.substring(0, maxLen - 2) + "..";
    }
    
    private String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}
