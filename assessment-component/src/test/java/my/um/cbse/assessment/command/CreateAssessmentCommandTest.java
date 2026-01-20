package my.um.cbse.assessment.command;

import my.um.cbse.api.model.Assessment;
import my.um.cbse.api.service.AssessmentService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class CreateAssessmentCommandTest {
    private CreateAssessmentCommand command;
    private AssessmentService assessmentService;

    @Before
    public void setUp() {
        assessmentService = Mockito.mock(AssessmentService.class);
        command = new CreateAssessmentCommand();
        // Use reflection to inject the mock
        try {
            java.lang.reflect.Field field = CreateAssessmentCommand.class.getDeclaredField("assessmentService");
            field.setAccessible(true);
            field.set(command, assessmentService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExecuteCreatesAssessment() throws Exception {
        Assessment assessment = new Assessment();
        // If Assessment has setId, use it; otherwise skip
        try {
            java.lang.reflect.Method setId = Assessment.class.getMethod("setId", String.class);
            setId.invoke(assessment, "A1");
        } catch (NoSuchMethodException ignore) {}
        Mockito.when(assessmentService.createAssessment(Mockito.any())).thenReturn(assessment);

        setField(command, "courseId", "COURSE1");
        setField(command, "title", "Test Title");
        setField(command, "description", "Test Desc");
        setField(command, "maxPoints", 100);
        setField(command, "deadline", null);

        Object result = command.execute();
        assertNull(result);
        Mockito.verify(assessmentService).createAssessment(Mockito.any());
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