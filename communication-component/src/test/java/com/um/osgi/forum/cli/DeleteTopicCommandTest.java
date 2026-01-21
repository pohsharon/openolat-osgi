package com.um.osgi.forum.cli;

import com.um.osgi.forum.api.IForumService;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class DeleteTopicCommandTest {

    @Test
    public void testDeleteTopic() throws Exception {
        IForumService mockService = Mockito.mock(IForumService.class);
        
        DeleteTopicCommand command = new DeleteTopicCommand();
        Field serviceField = DeleteTopicCommand.class.getDeclaredField("forumService");
        serviceField.setAccessible(true);
        serviceField.set(command, mockService);
        
        command.topicId = "topic_1";
        
        Object result = command.execute();
        assertNull(result);
        
        Mockito.verify(mockService).deleteTopic("topic_1");
    }
}
