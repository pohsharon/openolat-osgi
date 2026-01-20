package my.um.cbse.course.command;

import java.util.List;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.CourseMember;
import my.um.cbse.api.model.CourseRole;
import my.um.cbse.api.service.CourseService;

/**
 * Karaf Shell Command: course:members
 * UC3 - List course members
 * 
 * Usage: course:members <courseId> [--role <role>] [--search <term>]
 */
@Command(scope = "course", name = "members", description = "List course members")
@Component(
    service = ListMembersCommand.class,
    property = {
        "osgi.command.scope=course",
        "osgi.command.function=members"
    }
)
public class ListMembersCommand implements Action {

    @Argument(index = 0, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Option(name = "--role", aliases = {"-r"}, description = "Filter by role: OWNER, COACH, PARTICIPANT, GROUP_COACH, GUEST", required = false)
    private String role;

    @Option(name = "--search", aliases = {"-s"}, description = "Search by name or email", required = false)
    private String searchTerm;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private CourseService courseService;

    @Override
    public Object execute() throws Exception {
        if (courseService == null) {
            System.out.println("ERROR: CourseService is not available");
            return null;
        }

        List<CourseMember> members;
        
        if (searchTerm != null && !searchTerm.isEmpty()) {
            members = courseService.searchMembers(courseId, searchTerm);
            System.out.println("\n=== MEMBERS MATCHING: '" + searchTerm + "' ===");
        } else if (role != null && !role.isEmpty()) {
            try {
                CourseRole courseRole = CourseRole.valueOf(role.toUpperCase());
                members = courseService.getMembersByRole(courseId, courseRole);
                System.out.println("\n=== MEMBERS WITH ROLE: " + courseRole + " ===");
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: Invalid role. Use OWNER, COACH, PARTICIPANT, GROUP_COACH, or GUEST");
                return null;
            }
        } else {
            members = courseService.getCourseMembers(courseId);
            System.out.println("\n=== COURSE MEMBERS: " + courseId + " ===");
        }
        
        if (members.isEmpty()) {
            System.out.println("No members found.");
        } else {
            System.out.println(String.format("%-12s %-25s %-30s %-12s %-8s", 
                "USER ID", "NAME", "EMAIL", "ROLE", "ACTIVE"));
            System.out.println(repeatString("-", 90));
            
            for (CourseMember member : members) {
                System.out.println(String.format("%-12s %-25s %-30s %-12s %-8s",
                    member.getUserId(),
                    truncate(member.getUserName(), 23),
                    truncate(member.getEmail() != null ? member.getEmail() : "N/A", 28),
                    member.getRole(),
                    member.isActive() ? "Yes" : "No"));
            }
            System.out.println("\nTotal: " + members.size() + " member(s)");
        }
        
        return members;
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
