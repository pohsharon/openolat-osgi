package my.um.cbse.course.command;

import java.util.List;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.Course;
import my.um.cbse.api.model.CourseElement;
import my.um.cbse.api.service.CourseService;

/**
 * Karaf Shell Command: course:structure
 * UC4 - Display course structure (element tree)
 * 
 * Usage: course:structure <courseId>
 */
@Command(scope = "course", name = "structure", description = "Display course structure")
@Component(
    service = Action.class,
    property = {
        "osgi.command.scope=course",
        "osgi.command.function=structure"
    }
)
public class CourseStructureCommand implements Action {

    @Argument(index = 0, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private CourseService courseService;

    public void structure(String courseId) throws Exception {
        this.courseId = courseId;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        if (courseService == null) {
            System.out.println("ERROR: CourseService is not available");
            return null;
        }

        Course course = courseService.getCourse(courseId);
        if (course == null) {
            System.out.println("✗ Course not found: " + courseId);
            return null;
        }

        List<CourseElement> elements = courseService.getCourseStructure(courseId);
        
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                    COURSE STRUCTURE                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println("\nCourse: " + course.getCourseName() + " (" + courseId + ")");
        System.out.println();
        
        if (elements.isEmpty()) {
            System.out.println("No elements found.");
        } else {
            // Print tree structure
            String rootId = course.getRootElementId();
            printElementTree(courseId, rootId, elements, 0);
            
            System.out.println("\n" + repeatString("-", 60));
            System.out.println("Total: " + elements.size() + " element(s)");
        }
        
        return elements;
    }
    
    private void printElementTree(String courseId, String elementId, List<CourseElement> allElements, int depth) {
        CourseElement element = findElement(allElements, elementId);
        if (element == null) return;
        
        String indent = repeatString("  ", depth);
        String prefix = depth == 0 ? "📚 " : "├─ ";
        String visibility = element.isVisible() ? "" : " [HIDDEN]";
        String linked = element.getLinkedResourceId() != null ? " → " + element.getLinkedResourceId() : "";
        
        String icon = getElementIcon(element.getType().toString());
        
        System.out.println(indent + prefix + icon + " " + element.getTitle() + 
            " [" + element.getType() + "]" + visibility + linked);
        System.out.println(indent + "   ID: " + element.getElementId());
        
        // Print children
        for (String childId : element.getChildElementIds()) {
            printElementTree(courseId, childId, allElements, depth + 1);
        }
    }
    
    private CourseElement findElement(List<CourseElement> elements, String elementId) {
        return elements.stream()
            .filter(e -> elementId.equals(e.getElementId()))
            .findFirst()
            .orElse(null);
    }
    
    private String getElementIcon(String type) {
        switch (type) {
            case "STRUCTURE": return "📁";
            case "FOLDER": return "📂";
            case "SINGLE_PAGE": return "📄";
            case "DOCUMENT": return "📃";
            case "TASK": return "✅";
            case "TEST": return "📝";
            case "WIKI": return "📖";
            case "BLOG": return "📰";
            case "PODCAST": return "🎙";
            case "VIDEO": return "🎬";
            case "FORUM": return "💬";
            case "FORM": return "📋";
            case "ASSESSMENT": return "📊";
            default: return "📌";
        }
    }
    
    private String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}
