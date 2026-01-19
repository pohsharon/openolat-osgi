package my.um.cbse.enrollment.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.service.EnrollmentService;

/**
 * Karaf Shell Command: updateParticipation
 * Update participation score for a student in a course
 */
@Command(scope = "cbse", name = "updateParticipation", description = "Update participation score for a student in a course")
@Component(
    service = UpdateParticipationCommand.class,
    property = {
        "osgi.command.scope=cbse",
        "osgi.command.function=updateParticipation"
    }
)
public class UpdateParticipationCommand implements Action {

    @Argument(index = 0, name = "studentId", description = "The student ID", required = true)
    private String studentId;

    @Argument(index = 1, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Argument(index = 2, name = "score", description = "The participation score", required = true)
    private String scoreArg;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private EnrollmentService enrollmentService;

    @Override
    public Object execute() throws Exception {
        if (enrollmentService == null) {
            System.out.println("ERROR: EnrollmentService is not available");
            return null;
        }

        try {
            int score = Integer.parseInt(scoreArg);
            
            boolean success = enrollmentService.updateParticipationScore(studentId, courseId, score);
            
            if (success) {
                System.out.println("✓ Updated participation score for " + studentId);
                System.out.println("  New Score: " + score);
                
                int currentScore = enrollmentService.getParticipationScore(studentId, courseId);
                System.out.println("  Current Total: " + currentScore);
            } else {
                System.out.println("✗ Failed to update participation score");
            }
            
            return success;
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Score must be a valid integer");
            return false;
        }
    }
}
