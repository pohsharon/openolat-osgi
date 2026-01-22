package my.um.cbse.course.command;

import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class RemoveMemberCommandTest {
    private RemoveMemberCommand command;
    private CourseService courseService;

    @Before
    public void setUp() {
        courseService = Mockito.mock(CourseService.class);
        command = new RemoveMemberCommand();
        try {
            java.lang.reflect.Field field = RemoveMemberCommand.class.getDeclaredField("courseService");
            field.setAccessible(true);
            field.set(command, courseService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExecuteRemovesMember() throws Exception {
        CourseResult result = new CourseResult(true, "Member removed successfully");
        Mockito.when(courseService.removeCourseMember(
            Mockito.eq("CS101"),
            Mockito.eq("USER002"),
            Mockito.eq("OWNER001")
        )).thenReturn(result);

        setField(command, "courseId", "CS101");
        setField(command, "userId", "USER002");
        setField(command, "removedBy", "OWNER001");

        Object cmdResult = command.execute();
        assertNotNull(cmdResult);
        Mockito.verify(courseService).removeCourseMember("CS101", "USER002", "OWNER001");
    }

    @Test
    public void testExecuteCannotRemoveOwner() throws Exception {
        CourseResult result = new CourseResult(false, "Cannot remove the course owner", "CANNOT_REMOVE_OWNER");
        Mockito.when(courseService.removeCourseMember(
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.anyString()
        )).thenReturn(result);

        setField(command, "courseId", "CS101");
        setField(command, "userId", "OWNER001");
        setField(command, "removedBy", "OWNER001");

        command.execute();
        Mockito.verify(courseService).removeCourseMember(
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
