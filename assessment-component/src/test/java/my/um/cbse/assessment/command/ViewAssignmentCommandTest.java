package my.um.cbse.assessment.command;

import my.um.cbse.api.model.Assessment;
import my.um.cbse.api.service.AssessmentService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class ViewAssignmentCommandTest {
    private ViewAssignmentCommand command;
    private AssessmentService assessmentService;

    @Before
    public void setUp() {
        assessmentService = Mockito.mock(AssessmentService.class);
        command = new ViewAssignmentCommand();
        try {
            java.lang.reflect.Field field = ViewAssignmentCommand.class.getDeclaredField("assessmentService");
            field.setAccessible(true);
            field.set(command, assessmentService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExecuteWithNullAssessment() throws Exception {
        Mockito.when(assessmentService.getAssessment(Mockito.anyString())).thenReturn(null);
        setField(command, "assessmentId", "A1");
        Object result = command.execute();
        assertNull(result);
    }

    @Test
    public void testExecuteWithAssessment() throws Exception {
        Assessment a = new Assessment();
        try {
            java.lang.reflect.Method setId = Assessment.class.getMethod("setId", String.class);
            setId.invoke(a, "A1");
        } catch (NoSuchMethodException ignore) {}
        Mockito.when(assessmentService.getAssessment(Mockito.anyString())).thenReturn(a);
        setField(command, "assessmentId", "A1");
        Object result = command.execute();
        assertEquals(a, result);
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
