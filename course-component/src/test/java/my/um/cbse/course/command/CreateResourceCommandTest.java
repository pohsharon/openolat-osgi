package my.um.cbse.course.command;

import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.model.LearningResource;
import my.um.cbse.api.model.LearningResourceType;
import my.um.cbse.api.service.CourseService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class CreateResourceCommandTest {
    private CreateResourceCommand command;
    private CourseService courseService;

    @Before
    public void setUp() {
        courseService = Mockito.mock(CourseService.class);
        command = new CreateResourceCommand();
        try {
            java.lang.reflect.Field field = CreateResourceCommand.class.getDeclaredField("courseService");
            field.setAccessible(true);
            field.set(command, courseService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExecuteCreatesResource() throws Exception {
        LearningResource resource = new LearningResource("RES001", "Lecture Video 1", LearningResourceType.VIDEO, "OWNER001");
        CourseResult result = new CourseResult(true, "Resource created with ID: RES001", resource);
        
        Mockito.when(courseService.createLearningResource(
            Mockito.eq("Lecture Video 1"),
            Mockito.isNull(),
            Mockito.eq(LearningResourceType.VIDEO),
            Mockito.eq("OWNER001")
        )).thenReturn(result);

        // Call public method with only @Argument parameters
        try {
            command.create_resource("Lecture Video 1", "VIDEO", "OWNER001");
        } catch (Exception e) {
            // Expected
        }
        
        Mockito.verify(courseService).createLearningResource(
            Mockito.eq("Lecture Video 1"),
            Mockito.isNull(),
            Mockito.eq(LearningResourceType.VIDEO),
            Mockito.eq("OWNER001")
        );
    }

    @Test
    public void testExecuteWithInvalidType() throws Exception {
        try {
            command.create_resource("Test Resource", "INVALID_TYPE", "OWNER001");
        } catch (Exception e) {
            // Expected
        }
        
        // Verify service was never called
        Mockito.verify(courseService, Mockito.never()).createLearningResource(
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.any(),
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
