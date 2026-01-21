package com.um.osgi.forum.cli;

import com.um.osgi.forum.api.IForumService;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class ListTopicsCommandTest {

    @Test
    public void testListTopicsWithResults() throws Exception {
        IForumService mockService = Mockito.mock(IForumService.class);
        Mockito.when(mockService.getTopicsByCourse("COURSE123"))
               .thenReturn(Arrays.asList("topic_1 | OSGi Basics | by Eugene"));
        
        ListTopicsCommand command = new ListTopicsCommand();
        Field serviceField = ListTopicsCommand.class.getDeclaredField("forumService");
        serviceField.setAccessible(true);
        serviceField.set(command, mockService);
        
        command.courseId = "COURSE123";
        
        Object result = command.execute();
        assertNull(result);
    }

    @Test
    public void testListTopicsEmpty() throws Exception {
        IForumService mockService = Mockito.mock(IForumService.class);
        Mockito.when(mockService.getTopicsByCourse("COURSE456"))
               .thenReturn(Collections.emptyList());
        
        ListTopicsCommand command = new ListTopicsCommand();
        Field serviceField = ListTopicsCommand.class.getDeclaredField("forumService");
        serviceField.setAccessible(true);
        serviceField.set(command, mockService);
        
        command.courseId = "COURSE456";
        
        Object result = command.execute();
        assertNull(result);
    }
}
