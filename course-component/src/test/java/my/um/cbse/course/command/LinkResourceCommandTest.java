package my.um.cbse.course.command;

import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class LinkResourceCommandTest {
    private LinkResourceCommand command;
    private CourseService courseService;

    @Before
    public void setUp() {
        courseService = Mockito.mock(CourseService.class);
        command = new LinkResourceCommand();
        try {
            java.lang.reflect.Field field = LinkResourceCommand.class.getDeclaredField("courseService");
            field.setAccessible(true);
            field.set(command, courseService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExecuteLinksResource() throws Exception {
        CourseResult result = new CourseResult(true, "Resource linked successfully");
        Mockito.when(courseService.linkResourceToElement(
            Mockito.eq("CS101"),
            Mockito.eq("ELEM001"),
            Mockito.eq("RES001"),
            Mockito.eq("OWNER001")
        )).thenReturn(result);

        setField(command, "courseId", "CS101");
        setField(command, "elementId", "ELEM001");
        setField(command, "resourceId", "RES001");
        setField(command, "userId", "OWNER001");

        Object cmdResult = command.execute();
        assertNotNull(cmdResult);
        Mockito.verify(courseService).linkResourceToElement(
            "CS101", "ELEM001", "RES001", "OWNER001"
        );
    }

    @Test
    public void testExecuteWithNonExistentResource() throws Exception {
        CourseResult result = new CourseResult(false, "Resource not found", "RESOURCE_NOT_FOUND");
        Mockito.when(courseService.linkResourceToElement(
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.eq("INVALID"),
            Mockito.anyString()
        )).thenReturn(result);

        setField(command, "courseId", "CS101");
        setField(command, "elementId", "ELEM001");
        setField(command, "resourceId", "INVALID");
        setField(command, "userId", "OWNER001");

        command.execute();
        Mockito.verify(courseService).linkResourceToElement(
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.eq("INVALID"),
            Mockito.anyString()
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
