package my.um.cbse.course.command;

import my.um.cbse.api.model.Course;
import my.um.cbse.api.model.CourseResult;
import my.um.cbse.api.service.CourseService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class UpdateCourseMetadataCommandTest {
    private UpdateCourseMetadataCommand command;
    private CourseService courseService;

    @Before
    public void setUp() {
        courseService = Mockito.mock(CourseService.class);
        command = new UpdateCourseMetadataCommand();
        try {
            java.lang.reflect.Field field = UpdateCourseMetadataCommand.class.getDeclaredField("courseService");
            field.setAccessible(true);
            field.set(command, courseService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExecuteUpdatesMetadata() throws Exception {
        Course updatedCourse = new Course("CS101", "Database Systems", "COACH001");
        CourseResult result = new CourseResult(true, "Course metadata updated successfully", updatedCourse);
        
        Mockito.when(courseService.updateCourseMetadata(
            Mockito.eq("CS101"),
            Mockito.eq("2024-2"),
            Mockito.eq(4),
            Mockito.eq("MIT-License"),
            Mockito.eq("University of Computing"),
            Mockito.eq("COACH001")
        )).thenReturn(result);

        setField(command, "courseId", "CS101");
        setField(command, "semester", "2024-2");
        setField(command, "creditsStr", "4");
        setField(command, "license", "MIT-License");
        setField(command, "institution", "University of Computing");
        setField(command, "userId", "COACH001");

        Object cmdResult = command.execute();
        assertNotNull(cmdResult);
        Mockito.verify(courseService).updateCourseMetadata(
            "CS101", "2024-2", 4, "MIT-License", "University of Computing", "COACH001"
        );
    }

    @Test
    public void testExecuteWithInvalidCredits() throws Exception {
        setField(command, "courseId", "CS101");
        setField(command, "creditsStr", "INVALID");
        setField(command, "semester", "2024-2");
        setField(command, "license", "MIT");
        setField(command, "institution", "MIT");
        setField(command, "userId", "COACH001");

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
