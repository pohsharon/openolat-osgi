package my.um.cbse.course.command;

import java.util.List;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.Course;
import my.um.cbse.api.service.CourseService;

/**
 * Karaf Shell Command: course:list
 * UC2 - List all courses or filter by owner
 * 
 * Usage: course:list [--owner <ownerId>] [--search <keyword>]
 */
@Command(scope = "course", name = "list", description = "List all courses")
@Component(
    service = Action.class,
    property = {
        "osgi.command.scope=course",
        "osgi.command.function=list"
    }
)
public class ListCoursesCommand implements Action {

    @Option(name = "--owner", aliases = {"-o"}, description = "Filter by owner ID", required = false)
    private String ownerId;

    @Option(name = "--search", aliases = {"-s"}, description = "Search by keyword", required = false)
    private String keyword;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private CourseService courseService;

    public void list() throws Exception {
        execute();
    }

    @Override
    public Object execute() throws Exception {
        if (courseService == null) {
            System.out.println("ERROR: CourseService is not available");
            return null;
        }

        List<Course> courses;
        
        if (keyword != null && !keyword.isEmpty()) {
            courses = courseService.searchCourses(keyword);
            System.out.println("\n=== COURSES MATCHING: '" + keyword + "' ===");
        } else if (ownerId != null && !ownerId.isEmpty()) {
            courses = courseService.getCoursesByOwner(ownerId);
            System.out.println("\n=== COURSES OWNED BY: " + ownerId + " ===");
        } else {
            courses = courseService.getAllCourses();
            System.out.println("\n=== ALL COURSES ===");
        }
        
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            System.out.println(String.format("%-10s %-30s %-12s %-15s %-10s", 
                "ID", "NAME", "STATUS", "DESIGN", "OWNER"));
            System.out.println(repeatString("-", 80));
            
            for (Course course : courses) {
                System.out.println(String.format("%-10s %-30s %-12s %-15s %-10s",
                    course.getCourseId(),
                    truncate(course.getCourseName(), 28),
                    course.getStatus(),
                    course.getDesign(),
                    course.getOwnerId()));
            }
            System.out.println("\nTotal: " + courses.size() + " course(s)");
        }
        
        return courses;
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
