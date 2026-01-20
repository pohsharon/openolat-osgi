package my.um.cbse.assessment.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.Assessment;
import my.um.cbse.api.service.AssessmentService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;

@Command(scope = "cbse", name = "create-assessment", description = "Create an assessment for a course")
@Component(service = Action.class, immediate = true, property = {
        "osgi.command.scope=cbse",
        "osgi.command.function=createAssessment"
})
public class CreateAssessmentCommand implements Action {

    @Argument(index = 0, name = "courseId", required = true)
    private String courseId;

    @Argument(index = 1, name = "title", required = true)
    private String title;

    @Argument(index = 2, name = "description", required = true)
    private String description;

    @Argument(index = 3, name = "maxPoints", required = true)
    private int maxPoints;

    // accept as String so Gogo won't fail
    @Argument(index = 4, name = "deadline", required = false, description = "Deadline (yyyy-MM-dd)")
    private String deadline;

    @Reference
    private AssessmentService assessmentService;

    // ✅ matches how you call it (deadline is String)
    public void createAssessment(String courseId, String title, String description, int maxPoints, String deadline) throws Exception {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.maxPoints = maxPoints;
        this.deadline = deadline;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        Date deadlineDate = null;

        if (deadline != null && !deadline.isEmpty()) {
            try {
                LocalDate ld = LocalDate.parse(deadline); // expects yyyy-MM-dd

                // Convert LocalDate -> Date at start of day (local timezone)
                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                cal.set(ld.getYear(), ld.getMonthValue() - 1, ld.getDayOfMonth(), 0, 0, 0);
                cal.set(Calendar.MILLISECOND, 0);
                deadlineDate = cal.getTime();

            } catch (DateTimeParseException e) {
                System.out.println("Invalid deadline format. Use yyyy-MM-dd (e.g., 2026-02-01).");
                return null;
            }
        }

        Assessment assessment = new Assessment();
        assessment.setCourseId(courseId);
        assessment.setTitle(title);
        assessment.setDescription(description);
        assessment.setMaxPoints(maxPoints);
        assessment.setDeadline(deadlineDate);

        Assessment created = assessmentService.createAssessment(assessment);
        System.out.println("✓ Created assessment with ID: " + created.getId());
        return null;
    }
}