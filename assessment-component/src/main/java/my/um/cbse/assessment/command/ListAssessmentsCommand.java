package my.um.cbse.assessment.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.Assessment;
import my.um.cbse.api.service.AssessmentService;

import java.util.List;

@Command(scope = "cbse", name = "list-assessments", description = "List all assessments for a course")
@Component(service = Action.class, immediate = true, property = {
        "osgi.command.scope=cbse",
        "osgi.command.function=listAssessments"
})
public class ListAssessmentsCommand implements Action {

    @Argument(index = 0, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Reference
    private AssessmentService assessmentService;

    // ✅ This is what makes cbse:listAssessments work
    public void listAssessments(String courseId) throws Exception {
        this.courseId = courseId;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        List<Assessment> assessments = assessmentService.listAssessmentsByCourse(courseId);

        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println(String.format("║ %-110s ║", "ASSESSMENTS FOR COURSE: " + courseId));
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");

        if (assessments == null || assessments.isEmpty()) {
            System.out.println("(No assessments found)");
            return null;
        }

        // Table header
        System.out.printf("%-38s │ %-30s │ %-40s%n",
                "Assessment ID", "Title", "Description");
        System.out.println(repeatString("─", 115));

        // Table rows
        for (Assessment a : assessments) {
            System.out.printf("%-38s │ %-30s │ %-40s%n",
                    a.getId(), a.getTitle(), a.getDescription());
        }

        return null;
    }

     private String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}