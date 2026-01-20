package my.um.cbse.assessment.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.Assessment;
import my.um.cbse.api.service.AssessmentService;

@Command(scope = "cbse", name = "view-assignment", description = "View assignment details for students")
@Component(service = Action.class, immediate = true, property = {
        "osgi.command.scope=cbse",
        "osgi.command.function=viewAssignment"
})
public class ViewAssignmentCommand implements Action {

    @Argument(index = 0, name = "assessmentId", description = "The assessment ID", required = true)
    private String assessmentId;

    @Reference
    private AssessmentService assessmentService;

    // ✅ Public method for Gogo shell invocation: cbse:viewAssignment <assessmentId>
    public void viewAssignment(String assessmentId) throws Exception {
        this.assessmentId = assessmentId;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        Assessment a = assessmentService.getAssessment(assessmentId);

        if (a == null) {
            System.out.println("Assignment not found: " + assessmentId);
            return null;
        }

        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println(String.format("║ %-110s ║", "ASSIGNMENT DETAILS"));
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.printf("%-20s │ %-90s%n", "Field", "Value");
        System.out.println(repeatString("─", 115));
        System.out.printf("%-20s │ %-90s%n", "ID", a.getId());
        System.out.printf("%-20s │ %-90s%n", "Course ID", a.getCourseId());
        System.out.printf("%-20s │ %-90s%n", "Title", a.getTitle());
        System.out.printf("%-20s │ %-90s%n", "Description", a.getDescription());
        System.out.printf("%-20s │ %-90s%n", "Max Points", a.getMaxPoints());
        System.out.printf("%-20s │ %-90s%n", "Deadline", String.valueOf(a.getDeadline()));
        System.out.println(repeatString("─", 115));
        System.out.println();

        return a;
    }

    private String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}