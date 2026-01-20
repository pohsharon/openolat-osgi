package my.um.cbse.assessment.command;

import my.um.cbse.api.model.Assessment;
import my.um.cbse.api.service.AssessmentService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ListAssessmentsCommandTest {
    private ListAssessmentsCommand command;
    private AssessmentService assessmentService;

    @Before
    public void setUp() {
        assessmentService = Mockito.mock(AssessmentService.class);
        command = new ListAssessmentsCommand();
        try {
            java.lang.reflect.Field field = ListAssessmentsCommand.class.getDeclaredField("assessmentService");
            field.setAccessible(true);
            field.set(command, assessmentService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExecuteWithNoAssessments() throws Exception {
        Mockito.when(assessmentService.listAssessmentsByCourse(Mockito.anyString())).thenReturn(Collections.emptyList());
        setField(command, "courseId", "COURSE1");
        Object result = command.execute();
        assertNull(result);
    }

    @Test
    public void testExecuteWithAssessments() throws Exception {
        Assessment a = new Assessment();
        try {
            java.lang.reflect.Method setId = Assessment.class.getMethod("setId", String.class);
            setId.invoke(a, "A1");
        } catch (NoSuchMethodException ignore) {}
        try {
            java.lang.reflect.Method setTitle = Assessment.class.getMethod("setTitle", String.class);
            setTitle.invoke(a, "Title");
        } catch (NoSuchMethodException ignore) {}
        try {
            java.lang.reflect.Method setDescription = Assessment.class.getMethod("setDescription", String.class);
            setDescription.invoke(a, "Desc");
        } catch (NoSuchMethodException ignore) {}
        List<Assessment> list = Collections.singletonList(a);
        Mockito.when(assessmentService.listAssessmentsByCourse(Mockito.anyString())).thenReturn(list);
        setField(command, "courseId", "COURSE1");
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
