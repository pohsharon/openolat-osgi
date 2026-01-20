package my.um.cbse.enrollment.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import my.um.cbse.api.service.EnrollmentService;

@Command(scope = "cbse", name = "participationScore", description = "View or update participation score")
@Component(service = org.apache.karaf.shell.api.action.Action.class, immediate = true, property = {
    "osgi.command.scope=cbse",
    "osgi.command.function=participationScore"
})
public class ParticipationScoreCommand implements Action {

    @Argument(index = 0, name = "studentId", required = true, description = "Student ID")
    private String studentId;

    @Argument(index = 1, name = "courseId", required = true, description = "Course ID")
    private String courseId;

    @Argument(index = 2, name = "score", required = false, description = "Participation score (optional)")
    private String scoreArg;

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private EnrollmentService enrollmentService;

    public void participationScore(String studentId, String courseId, String scoreArg) throws Exception {
        this.studentId = studentId;
        this.courseId = courseId;
        this.scoreArg = scoreArg;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        if (enrollmentService == null) {
            System.out.println("ERROR: EnrollmentService is not available");
            return null;
        }

        try {
            if (scoreArg != null && !scoreArg.isEmpty()) {
                Integer score = Integer.parseInt(scoreArg);
                enrollmentService.updateParticipationScore(studentId, courseId, score);
                System.out.println("Participation score for " + studentId + " in " + courseId + " updated to: " + score);
                return score;
            } else {
                Integer currentScore = enrollmentService.getParticipationScore(studentId, courseId);
                System.out.println("Current participation score for " + studentId + " in " + courseId + ": " + (currentScore != null ? currentScore : "N/A"));
                return currentScore;
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            return null;
        }
    }
}