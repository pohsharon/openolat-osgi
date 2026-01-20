package my.um.cbse.assessment.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.Submission;
import my.um.cbse.api.service.AssessmentService;

import java.util.List;

@Command(
    scope = "cbse",
    name = "list-submissions",
    description = "List all submissions for an assessment"
)
@Component(
    service = Action.class,
    immediate = true,
    property = {
        "osgi.command.scope=cbse",
        "osgi.command.function=listSubmissions"
    }
)
public class ListSubmissionsCommand implements Action {

    @Argument(index = 0, name = "assessmentId", description = "The assessment ID", required = true)
    private String assessmentId;

    @Reference
    private AssessmentService assessmentService;

    // ✅ Gogo entry point
    // Usage: cbse:listSubmissions <assessmentId>
    public void listSubmissions(String assessmentId) throws Exception {
        this.assessmentId = assessmentId;
        execute();
    }

    @Override
    public Object execute() throws Exception {

        List<Submission> submissions =
                assessmentService.listSubmissionsByAssessment(assessmentId);

        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║  SUBMISSIONS FOR ASSESSMENT: " + assessmentId);
        System.out.println("╚══════════════════════════════════════════════════════════════╝");

        if (submissions == null || submissions.isEmpty()) {
            System.out.println("(No submissions found)");
            return null;
        }

        System.out.printf("%-36s │ %-15s │ %-20s%n",
                "Submission ID", "Student ID", "Submitted At");
        System.out.println(repeatString("─", 80));

        for (Submission s : submissions) {
            String submittedAt =
                    s.getSubmittedAt() != null ? s.getSubmittedAt().toString() : "-";

            System.out.printf("%-36s │ %-15s │ %-20s%n",
                    s.getId(), s.getStudentId(), submittedAt);
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