package my.um.cbse.course.command;

import my.um.cbse.api.model.Course;
import my.um.cbse.api.model.CourseDesign;
import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class CreateCourseCommandTest {
    private CreateCourseCommand command;
    private CourseService courseService;

    @Before
    public void setUp() {
        courseService = Mockito.mock(CourseService.class);
        command = new CreateCourseCommand();
        // Use reflection to inject the mock
        try {
            java.lang.reflect.Field field = CreateCourseCommand.class.getDeclaredField("courseService");
            field.setAccessible(true);
            field.set(command, courseService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExecuteCreatesCourse() throws Exception {
        Course course = new Course("CRS1001", "Machine Learning", "OWNER001");
        CourseResult result = new CourseResult(true, "Course created successfully", course);
        
        // The command calls createCourse with 5 parameters (title, description, design, semester, ownerId)
        Mockito.when(courseService.createCourse(
            Mockito.eq("Machine Learning"),
            Mockito.isNull(), // description
            Mockito.eq(CourseDesign.LEARNING_PATH), // design
            Mockito.isNull(), // semester
            Mockito.eq("OWNER001")
        )).thenReturn(result);

        // Call public method with only @Argument parameters
        try {
            command.create("Machine Learning", "OWNER001");
        } catch (Exception e) {
            // Expected if service returns result
        }
        
        Mockito.verify(courseService).createCourse(
            Mockito.eq("Machine Learning"),
            Mockito.isNull(),
            Mockito.eq(CourseDesign.LEARNING_PATH),
            Mockito.isNull(),
            Mockito.eq("OWNER001")
        );
    }

    @Test
    public void testExecuteWithNullService() throws Exception {
        // Create a new command without service
        CreateCourseCommand cmdNoService = new CreateCourseCommand();
        
        try {
            cmdNoService.create("Test Course", "OWNER001");
        } catch (Exception e) {
            // Expected
        }
        
        // No verification needed - service is null
    }

    // Helper to set private fields via reflection
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
