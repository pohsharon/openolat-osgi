package com.um.osgi.forum.cli;

import com.um.osgi.forum.api.IForumService;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class ReplyCommandTest {

    @Test
    public void testReplyToTopic() throws Exception {
        IForumService mockService = Mockito.mock(IForumService.class);
        
        ReplyCommand command = new ReplyCommand();
        Field serviceField = ReplyCommand.class.getDeclaredField("forumService");
        serviceField.setAccessible(true);
        serviceField.set(command, mockService);
        
        command.topicId = "topic_1";
        command.message = "Great discussion!";
        command.author = "Sharon";
        
        Object result = command.execute();
        assertNull(result);
        
        Mockito.verify(mockService).addReply("topic_1", "Great discussion!", "Sharon");
    }
}
