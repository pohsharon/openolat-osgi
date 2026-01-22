package my.um.cbse.course.command;

import my.um.cbse.api.model.CourseMember;
import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.model.CourseRole;
import my.um.cbse.api.service.CourseService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class UpdateMemberRoleCommandTest {
    private UpdateMemberRoleCommand command;
    private CourseService courseService;

    @Before
    public void setUp() {
        courseService = Mockito.mock(CourseService.class);
        command = new UpdateMemberRoleCommand();
        try {
            java.lang.reflect.Field field = UpdateMemberRoleCommand.class.getDeclaredField("courseService");
            field.setAccessible(true);
            field.set(command, courseService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExecuteUpdatesRole() throws Exception {
        CourseMember member = new CourseMember("CS101", "USER001", "John Doe", CourseRole.OWNER);
        CourseResult result = new CourseResult(true, "Member role updated successfully", member);
        
        Mockito.when(courseService.updateMemberRole(
            Mockito.eq("CS101"),
            Mockito.eq("USER001"),
            Mockito.any(CourseRole.class),
            Mockito.eq("OWNER001")
        )).thenReturn(result);

        setField(command, "courseId", "CS101");
        setField(command, "userId", "USER001");
        setField(command, "newRole", "OWNER");
        setField(command, "updatedBy", "OWNER001");

        Object cmdResult = command.execute();
        assertNotNull(cmdResult);
        Mockito.verify(courseService).updateMemberRole(
            Mockito.eq("CS101"),
            Mockito.eq("USER001"),
            Mockito.any(CourseRole.class),
            Mockito.eq("OWNER001")
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
