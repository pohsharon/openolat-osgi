package my.um.cbse.course.command;

import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class DeleteResourceCommandTest {
    private DeleteResourceCommand command;
    private CourseService courseService;

    @Before
    public void setUp() {
        courseService = Mockito.mock(CourseService.class);
        command = new DeleteResourceCommand();
        try {
            java.lang.reflect.Field field = DeleteResourceCommand.class.getDeclaredField("courseService");
            field.setAccessible(true);
            field.set(command, courseService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExecuteDeletesResource() throws Exception {
        CourseResult result = new CourseResult(true, "Resource deleted successfully");
        Mockito.when(courseService.deleteLearningResource(
            Mockito.eq("RES001"),
            Mockito.eq("OWNER001")
        )).thenReturn(result);

        setField(command, "resourceId", "RES001");
        setField(command, "userId", "OWNER001");

        Object cmdResult = command.execute();
        assertNotNull(cmdResult);
        Mockito.verify(courseService).deleteLearningResource("RES001", "OWNER001");
    }

    @Test
    public void testExecuteWithUnauthorizedUser() throws Exception {
        CourseResult result = new CourseResult(false, "Only owner can delete resource", "UNAUTHORIZED");
        Mockito.when(courseService.deleteLearningResource(
            Mockito.anyString(),
            Mockito.eq("USER001")
        )).thenReturn(result);

        setField(command, "resourceId", "RES001");
        setField(command, "userId", "USER001");

        command.execute();
        Mockito.verify(courseService).deleteLearningResource(
            Mockito.anyString(),
            Mockito.eq("USER001")
        );
    }

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
