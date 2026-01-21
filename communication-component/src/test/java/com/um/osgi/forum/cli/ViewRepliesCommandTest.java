package com.um.osgi.forum.cli;

import com.um.osgi.forum.api.IForumService;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ViewRepliesCommandTest {

    @Test
    public void testViewReplies() throws Exception {
        IForumService mockService = Mockito.mock(IForumService.class);
        Mockito.when(mockService.getRepliesByTopic("topic_1"))
               .thenReturn(Arrays.asList("Alice: Great explanation!", "Bob: Thanks!"));
        
        ViewRepliesCommand command = new ViewRepliesCommand();
        Field serviceField = ViewRepliesCommand.class.getDeclaredField("forumService");
        serviceField.setAccessible(true);
        serviceField.set(command, mockService);
        
        command.topicId = "topic_1";
        
        Object result = command.execute();
        assertNull(result);
        
        Mockito.verify(mockService).getRepliesByTopic("topic_1");
    }
}
