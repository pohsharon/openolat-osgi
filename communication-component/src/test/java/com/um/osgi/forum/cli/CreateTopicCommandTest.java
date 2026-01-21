package com.um.osgi.forum.cli;

import com.um.osgi.forum.api.IForumService;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class CreateTopicCommandTest {

    @Test
    public void testCreateWithValidInputs() throws Exception {
        IForumService mockService = Mockito.mock(IForumService.class);
        
        CreateTopicCommand command = new CreateTopicCommand();
        Field serviceField = CreateTopicCommand.class.getDeclaredField("forumService");
        serviceField.setAccessible(true);
        serviceField.set(command, mockService);
        
        command.courseId = "COURSE123";
        command.title = "Test Topic";
        command.author = "Eugene";
        
        Object result = command.execute();
        assertNull(result);
        
        Mockito.verify(mockService).createTopic("COURSE123", "Test Topic", "Eugene");
    }

    @Test
    public void testCreateWithNullService() throws Exception {
        CreateTopicCommand command = new CreateTopicCommand();
        command.courseId = "COURSE123";
        command.title = "Test Topic";
        command.author = "Eugene";
        
        try {
            command.execute();
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}
