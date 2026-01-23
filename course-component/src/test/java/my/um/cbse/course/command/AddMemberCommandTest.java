package my.um.cbse.course.command;

import my.um.cbse.api.model.CourseMember;
import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.model.CourseRole;
import my.um.cbse.api.service.CourseService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class AddMemberCommandTest {
    private AddMemberCommand command;
    private CourseService courseService;

    @Before
    public void setUp() {
        courseService = Mockito.mock(CourseService.class);
        command = new AddMemberCommand();
        try {
            java.lang.reflect.Field field = AddMemberCommand.class.getDeclaredField("courseService");
            field.setAccessible(true);
            field.set(command, courseService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExecuteAddsMember() throws Exception {
        CourseMember member = new CourseMember("CS101", "USER001", "John Doe", CourseRole.COACH);
        CourseResult result = new CourseResult(true, "Member added successfully", member);
        
        Mockito.when(courseService.addCourseMember(
            Mockito.eq("CS101"),
            Mockito.eq("USER001"),
            Mockito.eq("John Doe"),
            Mockito.isNull(),
            Mockito.any(CourseRole.class),
            Mockito.eq("OWNER001")
        )).thenReturn(result);

        setField(command, "courseId", "CS101");
        setField(command, "userId", "USER001");
        setField(command, "userName", "John Doe");
        setField(command, "role", "COACH");
        setField(command, "addedBy", "OWNER001");

        Object cmdResult = command.execute();
        assertNotNull(cmdResult);
        Mockito.verify(courseService).addCourseMember(
            Mockito.eq("CS101"),
            Mockito.eq("USER001"),
            Mockito.eq("John Doe"),
            Mockito.isNull(),
            Mockito.any(CourseRole.class),
            Mockito.eq("OWNER001")
        );
    }

    @Test
    public void testExecuteWithInvalidRole() throws Exception {
        setField(command, "courseId", "CS101");
        setField(command, "userId", "USER001");
        setField(command, "userName", "John Doe");
        setField(command, "role", "INVALID_ROLE");
        setField(command, "addedBy", "OWNER001");

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
