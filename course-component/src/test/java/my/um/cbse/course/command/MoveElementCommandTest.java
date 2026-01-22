package my.um.cbse.course.command;

import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class MoveElementCommandTest {
    private MoveElementCommand command;
    private CourseService courseService;

    @Before
    public void setUp() {
        courseService = Mockito.mock(CourseService.class);
        command = new MoveElementCommand();
        try {
            java.lang.reflect.Field field = MoveElementCommand.class.getDeclaredField("courseService");
            field.setAccessible(true);
            field.set(command, courseService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExecuteMovesElement() throws Exception {
        CourseResult result = new CourseResult(true, "Element moved successfully");
        Mockito.when(courseService.moveCourseElement(
            Mockito.eq("CS101"),
            Mockito.eq("ELEM001"),
            Mockito.eq("ROOT"),
            Mockito.eq(0),
            Mockito.eq("OWNER001")
        )).thenReturn(result);

        setField(command, "courseId", "CS101");
        setField(command, "elementId", "ELEM001");
        setField(command, "newParentId", "ROOT");
        setField(command, "newOrder", 0);
        setField(command, "userId", "OWNER001");

        Object cmdResult = command.execute();
        assertNotNull(cmdResult);
        Mockito.verify(courseService).moveCourseElement(
            "CS101", "ELEM001", "ROOT", 0, "OWNER001"
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
