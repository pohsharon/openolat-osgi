package my.um.cbse.course.command;

import my.um.cbse.api.model.Course;
import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class UpdateCourseInfoCommandTest {
    private UpdateCourseInfoCommand command;
    private CourseService courseService;

    @Before
    public void setUp() {
        courseService = Mockito.mock(CourseService.class);
        command = new UpdateCourseInfoCommand();
        try {
            java.lang.reflect.Field field = UpdateCourseInfoCommand.class.getDeclaredField("courseService");
            field.setAccessible(true);
            field.set(command, courseService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExecuteUpdatesCourseInfo() throws Exception {
        Course updatedCourse = new Course("CS101", "Database Systems Advanced", "COACH001");
        CourseResult result = new CourseResult(true, "Course info updated successfully", updatedCourse);
        
        Mockito.when(courseService.updateCourseInfo(
            Mockito.eq("CS101"),
            Mockito.eq("Database Systems Advanced"),
            Mockito.eq("Updated description"),
            Mockito.eq("CS101-V2"),
            Mockito.eq("COACH001")
        )).thenReturn(result);

        setField(command, "courseId", "CS101");
        setField(command, "title", "Database Systems Advanced");
        setField(command, "description", "Updated description");
        setField(command, "courseCode", "CS101-V2");
        setField(command, "userId", "COACH001");

        Object cmdResult = command.execute();
        assertNotNull(cmdResult);
        Mockito.verify(courseService).updateCourseInfo(
            "CS101",
            "Database Systems Advanced",
            "Updated description",
            "CS101-V2",
            "COACH001"
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
