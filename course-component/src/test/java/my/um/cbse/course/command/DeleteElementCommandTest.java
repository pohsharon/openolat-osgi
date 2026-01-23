package my.um.cbse.course.command;

import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class DeleteElementCommandTest {
    private DeleteElementCommand command;
    private CourseService courseService;

    @Before
    public void setUp() {
        courseService = Mockito.mock(CourseService.class);
        command = new DeleteElementCommand();
        try {
            java.lang.reflect.Field field = DeleteElementCommand.class.getDeclaredField("courseService");
            field.setAccessible(true);
            field.set(command, courseService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExecuteDeletesElement() throws Exception {
        CourseResult result = new CourseResult(true, "Element deleted successfully");
        Mockito.when(courseService.deleteCourseElement(
            Mockito.eq("CS101"),
            Mockito.eq("ELEM001"),
            Mockito.eq("OWNER001")
        )).thenReturn(result);

        setField(command, "courseId", "CS101");
        setField(command, "elementId", "ELEM001");
        setField(command, "userId", "OWNER001");

        Object cmdResult = command.execute();
        assertNotNull(cmdResult);
        Mockito.verify(courseService).deleteCourseElement("CS101", "ELEM001", "OWNER001");
    }

    @Test
    public void testExecuteCannotDeleteRoot() throws Exception {
        CourseResult result = new CourseResult(false, "Cannot delete root element", "CANNOT_DELETE_ROOT");
        Mockito.when(courseService.deleteCourseElement(
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.anyString()
        )).thenReturn(result);

        setField(command, "courseId", "CS101");
        setField(command, "elementId", "ROOT_CS101");
        setField(command, "userId", "OWNER001");

        command.execute();
        Mockito.verify(courseService).deleteCourseElement(
            Mockito.anyString(),
            Mockito.anyString(),
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
