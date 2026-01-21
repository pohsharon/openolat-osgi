package com.um.osgi.forum.cli;

import com.um.osgi.forum.api.IForumService;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class EditTopicCommandTest {

    @Test
    public void testEditTopic() throws Exception {
        IForumService mockService = Mockito.mock(IForumService.class);
        
        EditTopicCommand command = new EditTopicCommand();
        Field serviceField = EditTopicCommand.class.getDeclaredField("forumService");
        serviceField.setAccessible(true);
        serviceField.set(command, mockService);
        
        command.topicId = "topic_1";
        command.newTitle = "Updated Title";
        
        Object result = command.execute();
        assertNull(result);
        
        Mockito.verify(mockService).editTopic("topic_1", "Updated Title");
    }
}
