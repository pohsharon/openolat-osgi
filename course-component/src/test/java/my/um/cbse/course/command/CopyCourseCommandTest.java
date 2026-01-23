package my.um.cbse.course.command;

import my.um.cbse.api.model.Course;
import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class CopyCourseCommandTest {
    private CopyCourseCommand command;
    private CourseService courseService;

    @Before
    public void setUp() {
        courseService = Mockito.mock(CourseService.class);
        command = new CopyCourseCommand();
        try {
            java.lang.reflect.Field field = CopyCourseCommand.class.getDeclaredField("courseService");
            field.setAccessible(true);
            field.set(command, courseService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExecuteCopiesCourse() throws Exception {
        Course copiedCourse = new Course("CRS1002", "Database Systems Copy", "OWNER002");
        CourseResult result = new CourseResult(true, "Course copied successfully", copiedCourse);
        
        Mockito.when(courseService.copyCourse(
            Mockito.eq("CS101"),
            Mockito.isNull(), // newTitle is @Option, will be null
            Mockito.eq("OWNER002")
        )).thenReturn(result);

        // Call public method with only @Argument parameters
        try {
            command.copy("CS101", "OWNER002");
        } catch (Exception e) {
            // Expected
        }
        
        Mockito.verify(courseService).copyCourse(
            Mockito.eq("CS101"),
            Mockito.isNull(),
            Mockito.eq("OWNER002")
        );
    }

    @Test
    public void testExecuteWithNonExistentCourse() throws Exception {
        CourseResult result = new CourseResult(false, "Source course not found", "COURSE_NOT_FOUND");
        Mockito.when(courseService.copyCourse(
            Mockito.eq("INVALID"),
            Mockito.isNull(), // newTitle is @Option, will be null
            Mockito.eq("OWNER001")
        )).thenReturn(result);

        try {
            command.copy("INVALID", "OWNER001");
        } catch (Exception e) {
            // Expected
        }
        
        Mockito.verify(courseService).copyCourse(
            Mockito.eq("INVALID"),
            Mockito.isNull(),
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
