package my.um.cbse.assessment.command;

import my.um.cbse.api.model.Submission;
import my.um.cbse.api.service.AssessmentService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class SubmitAssignmentCommandTest {
    private SubmitAssignmentCommand command;
    private AssessmentService assessmentService;

    @Before
    public void setUp() {
        assessmentService = Mockito.mock(AssessmentService.class);
        command = new SubmitAssignmentCommand();
        try {
            java.lang.reflect.Field field = SubmitAssignmentCommand.class.getDeclaredField("assessmentService");
            field.setAccessible(true);
            field.set(command, assessmentService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExecute() throws Exception {
        setField(command, "assessmentId", "A1");
        setField(command, "studentId", "S1");
        setField(command, "submissionContent", "My submission");
        // Mock the service to return a Submission object
        Submission mockSubmission = new Submission();
        mockSubmission.setAssessmentId("A1");
        mockSubmission.setStudentId("S1");
        mockSubmission.setTextEntry("My submission");
        Mockito.when(assessmentService.submitAssignment(Mockito.any(Submission.class))).thenReturn(mockSubmission);
        Object result = command.execute();
        assertTrue(result instanceof Submission);
        Submission submission = (Submission) result;
        assertEquals("A1", submission.getAssessmentId());
        assertEquals("S1", submission.getStudentId());
        assertEquals("My submission", submission.getTextEntry());
    }

    // Helper to set private fields via reflection
    private void setField(Object obj, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
