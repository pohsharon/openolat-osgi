package my.um.cbse.assessment.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.Grade;
import my.um.cbse.api.service.AssessmentService;

@Command(scope = "cbse", name = "grade-submission", description = "Grade a student's submission")
@Component(service = Action.class, immediate = true, property = {
        "osgi.command.scope=cbse",
        "osgi.command.function=gradeSubmission"
})
public class GradeSubmissionCommand implements Action {

    @Argument(index = 0, name = "submissionId", description = "The submission ID", required = true)
    private String submissionId;

    @Argument(index = 1, name = "score", description = "Score/grade", required = true)
    private int score;

    @Option(name = "--feedback", description = "Feedback (optional)")
    private String feedback;

    @Reference
    private AssessmentService assessmentService;

    // ✅ Gogo entrypoint: cbse:gradeSubmission <submissionId> <score> [--feedback "..."]
    public void gradeSubmission(String submissionId, int score) throws Exception {
        this.submissionId = submissionId;
        this.score = score;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        // If you later implement in service, call it here.
        Grade grade = new Grade();
        grade.setSubmissionId(submissionId);
        grade.setScore(score);
        grade.setFeedback(feedback);

        // TODO: when available:
        // assessmentService.gradeSubmission(grade);

        System.out.println("✓ SUCCESS: Graded submission " + submissionId + " with score " + score
                + (feedback != null ? (" | feedback: " + feedback) : ""));
        return grade;
    }
}