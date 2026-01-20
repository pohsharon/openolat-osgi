package my.um.cbse.assessment.command;

import my.um.cbse.api.model.Submission;
import my.um.cbse.api.service.AssessmentService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ListSubmissionsCommandTest {
    private ListSubmissionsCommand command;
    private AssessmentService assessmentService;

    @Before
    public void setUp() {
        assessmentService = Mockito.mock(AssessmentService.class);
        command = new ListSubmissionsCommand();
        try {
            java.lang.reflect.Field field = ListSubmissionsCommand.class.getDeclaredField("assessmentService");
            field.setAccessible(true);
            field.set(command, assessmentService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExecuteWithNoSubmissions() throws Exception {
        Mockito.when(assessmentService.listSubmissionsByAssessment(Mockito.anyString())).thenReturn(Collections.emptyList());
        setField(command, "assessmentId", "A1");
        Object result = command.execute();
        assertNull(result);
    }

    @Test
    public void testExecuteWithSubmissions() throws Exception {
        Submission s = new Submission();
        try {
            java.lang.reflect.Method setId = Submission.class.getMethod("setId", String.class);
            setId.invoke(s, "S1");
        } catch (NoSuchMethodException ignore) {}
        List<Submission> list = Collections.singletonList(s);
        Mockito.when(assessmentService.listSubmissionsByAssessment(Mockito.anyString())).thenReturn(list);
        setField(command, "assessmentId", "A1");
        Object result = command.execute();
        assertNull(result);
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
