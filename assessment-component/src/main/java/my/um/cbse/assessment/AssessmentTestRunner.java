package my.um.cbse.assessment;
import my.um.cbse.api.model.Assessment;


public class AssessmentTestRunner {
    public static void main(String[] args) {
        AssessmentServiceImpl service = new AssessmentServiceImpl();

        // Create a sample assessment
        Assessment assessment = new Assessment();
        assessment.setCourseId("COURSE123");
        assessment.setTitle("Presentation Assessment");
        assessment.setDescription("Assess student presentations");
        assessment.setMaxPoints(100);
        // Set other fields as needed

        Assessment created = service.createAssessment(assessment);
        System.out.println("Created assessment with ID: " + created.getId());

        // List all assessments for the course
        System.out.println("Assessments for course COURSE123:");
        for (Assessment a : service.listAssessmentsByCourse("COURSE123")) {
            System.out.println("- " + a.getTitle() + ": " + a.getDescription());
        }
    }
}
