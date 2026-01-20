package com.um.osgi.forum.cli;

import com.um.osgi.forum.api.IForumService;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.Action;

/**
 * Create command for forum topics
 */
@Command(scope = "forum", name = "create", description = "Create a topic")
@Service
public class CreateTopicCommand implements Action {

    @Reference
    private IForumService forumService;

    @Argument(index = 0, name = "courseId", description = "Course id", required = true, multiValued = false)
    String courseId;

    @Argument(index = 1, name = "title", description = "Topic title", required = true, multiValued = false)
    String title;

    @Argument(index = 2, name = "author", description = "Author name", required = true, multiValued = false)
    String author;

    @Override
    public Object execute() throws Exception {
        forumService.createTopic(courseId, title, author);
        System.out.println("[CreateTopicCommand] Created topic in course: " + courseId);
        return null;
    }
}
