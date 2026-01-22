package my.um.cbse.course.command;

import my.um.cbse.api.model.CourseElement;
import my.um.cbse.api.model.CourseElementType;
import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class AddElementCommandTest {
    private AddElementCommand command;
    private CourseService courseService;

    @Before
    public void setUp() {
        courseService = Mockito.mock(CourseService.class);
        command = new AddElementCommand();
        try {
            java.lang.reflect.Field field = AddElementCommand.class.getDeclaredField("courseService");
            field.setAccessible(true);
            field.set(command, courseService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExecuteAddsElement() throws Exception {
        CourseElement element = new CourseElement("ELEM001", "Week 1 - Introduction", CourseElementType.STRUCTURE);
        CourseResult result = new CourseResult(true, "Course element added with ID: ELEM001", element);
        
        Mockito.when(courseService.addCourseElement(
            Mockito.eq("CS101"),
            Mockito.isNull(),
            Mockito.eq("Week 1 - Introduction"),
            Mockito.any(CourseElementType.class),
            Mockito.eq("OWNER001")
        )).thenReturn(result);

        setField(command, "courseId", "CS101");
        setField(command, "title", "Week 1 - Introduction");
        setField(command, "type", "STRUCTURE");
        setField(command, "userId", "OWNER001");

        Object cmdResult = command.execute();
        assertNotNull(cmdResult);
        Mockito.verify(courseService).addCourseElement(
            Mockito.eq("CS101"),
            Mockito.isNull(),
            Mockito.eq("Week 1 - Introduction"),
            Mockito.any(CourseElementType.class),
            Mockito.eq("OWNER001")
        );
    }

    @Test
    public void testExecuteWithInvalidType() throws Exception {
        setField(command, "courseId", "CS101");
        setField(command, "title", "Test Element");
        setField(command, "type", "INVALID_TYPE");
        setField(command, "userId", "OWNER001");

        Object result = command.execute();
        assertNull(result);
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
