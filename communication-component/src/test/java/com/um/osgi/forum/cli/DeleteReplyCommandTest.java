package com.um.osgi.forum.cli;

import com.um.osgi.forum.api.IForumService;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class DeleteReplyCommandTest {

    @Test
    public void testDeleteReply() throws Exception {
        IForumService mockService = Mockito.mock(IForumService.class);
        
        DeleteReplyCommand command = new DeleteReplyCommand();
        Field serviceField = DeleteReplyCommand.class.getDeclaredField("forumService");
        serviceField.setAccessible(true);
        serviceField.set(command, mockService);
        
        command.topicId = "topic_1";
        command.replyIndex = 0;
        
        Object result = command.execute();
        assertNull(result);
        
        Mockito.verify(mockService).deleteReply("topic_1", 0);
    }
}
