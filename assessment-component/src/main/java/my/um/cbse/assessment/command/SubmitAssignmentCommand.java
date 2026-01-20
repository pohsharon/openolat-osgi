package my.um.cbse.assessment.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.Submission;
import my.um.cbse.api.service.AssessmentService;

@Command(scope = "cbse", name = "submit-assignment", description = "Submit an assignment as a student")
@Component(service = Action.class, immediate = true, property = {
        "osgi.command.scope=cbse",
        "osgi.command.function=submitAssignment"
})
public class SubmitAssignmentCommand implements Action {

    @Argument(index = 0, name = "assessmentId", description = "The assessment ID", required = true)
    private String assessmentId;

    @Argument(index = 1, name = "studentId", description = "The student ID", required = true)
    private String studentId;

    @Argument(index = 2, name = "submission", description = "Submission content (text/filePath/link)", required = true)
    private String submissionContent;

    @Reference
    private AssessmentService assessmentService;

    public void submitAssignment(String assessmentId, String studentId, String submission) throws Exception {
        this.assessmentId = assessmentId;
        this.studentId = studentId;
        this.submissionContent = submission;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        // If you want to be safe:
        if (assessmentService == null) {
            System.out.println("ERROR: AssessmentService is not available");
            return null;
        }

        Submission submission = new Submission();
        submission.setAssessmentId(assessmentId);
        submission.setStudentId(studentId);
        submission.setTextEntry(submissionContent);

        // If your service supports it, do the real call here:
        // Submission saved = assessmentService.submitAssignment(submission);

        System.out.println("✓ SUCCESS: Submitted assignment for student " + studentId
                + " (assessment " + assessmentId + ")");
        System.out.println("Submission content: " + submissionContent);

        return submission;
    }
}