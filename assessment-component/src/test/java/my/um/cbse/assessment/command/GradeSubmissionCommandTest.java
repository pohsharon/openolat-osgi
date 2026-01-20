package my.um.cbse.assessment.command;

import my.um.cbse.api.model.Grade;
import my.um.cbse.api.service.AssessmentService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class GradeSubmissionCommandTest {
    private GradeSubmissionCommand command;
    private AssessmentService assessmentService;

    @Before
    public void setUp() {
        assessmentService = Mockito.mock(AssessmentService.class);
        command = new GradeSubmissionCommand();
        try {
            java.lang.reflect.Field field = GradeSubmissionCommand.class.getDeclaredField("assessmentService");
            field.setAccessible(true);
            field.set(command, assessmentService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExecute() throws Exception {
        Mockito.when(assessmentService.gradeSubmission(Mockito.any())).thenReturn(true);
        setField(command, "submissionId", "S1");
        setField(command, "score", 95);
        setField(command, "feedback", "Good job");
        Object result = command.execute();
        assertTrue(result == null || Boolean.TRUE.equals(result)); // Accept null or true as success
        Mockito.verify(assessmentService).gradeSubmission(Mockito.any());
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
