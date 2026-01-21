package com.um.osgi.forum.cli;

import com.um.osgi.forum.api.IForumService;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class LockTopicCommandTest {

    @Test
    public void testLockTopic() throws Exception {
        IForumService mockService = Mockito.mock(IForumService.class);
        
        LockTopicCommand command = new LockTopicCommand();
        Field serviceField = LockTopicCommand.class.getDeclaredField("forumService");
        serviceField.setAccessible(true);
        serviceField.set(command, mockService);
        
        command.topicId = "topic_1";
        command.lock = true;
        
        Object result = command.execute();
        assertNull(result);
        
        Mockito.verify(mockService).lockTopic("topic_1", true);
    }

    @Test
    public void testUnlockTopic() throws Exception {
        IForumService mockService = Mockito.mock(IForumService.class);
        
        LockTopicCommand command = new LockTopicCommand();
        Field serviceField = LockTopicCommand.class.getDeclaredField("forumService");
        serviceField.setAccessible(true);
        serviceField.set(command, mockService);
        
        command.topicId = "topic_1";
        command.lock = false;
        
        Object result = command.execute();
        assertNull(result);
        
        Mockito.verify(mockService).lockTopic("topic_1", false);
    }
}
