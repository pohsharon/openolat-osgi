package my.um.cbse.course.command;

import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class DeleteCourseCommandTest {
    private DeleteCourseCommand command;
    private CourseService courseService;

    @Before
    public void setUp() {
        courseService = Mockito.mock(CourseService.class);
        command = new DeleteCourseCommand();
        try {
            java.lang.reflect.Field field = DeleteCourseCommand.class.getDeclaredField("courseService");
            field.setAccessible(true);
            field.set(command, courseService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExecuteDeletesCourse() throws Exception {
        CourseResult result = new CourseResult(true, "Course deleted successfully");
        Mockito.when(courseService.deleteCourse(
            Mockito.eq("CS103"),
            Mockito.eq("OWNER001")
        )).thenReturn(result);

        setField(command, "courseId", "CS103");
        setField(command, "userId", "OWNER001");

        Object cmdResult = command.execute();
        assertNotNull(cmdResult);
        Mockito.verify(courseService).deleteCourse("CS103", "OWNER001");
    }

    @Test
    public void testExecuteUnauthorizedDeletion() throws Exception {
        CourseResult result = new CourseResult(false, "Only course owner can delete the course", "UNAUTHORIZED");
        Mockito.when(courseService.deleteCourse(
            Mockito.eq("CS101"),
            Mockito.eq("USER001")
        )).thenReturn(result);

        setField(command, "courseId", "CS101");
        setField(command, "userId", "USER001");

        command.execute();
        Mockito.verify(courseService).deleteCourse("CS101", "USER001");
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
