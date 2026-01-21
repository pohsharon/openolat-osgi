package com.um.osgi.forum.cli;

import com.um.osgi.forum.api.IForumService;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.Action;

@Command(scope = "cbse", name = "editTopic", description = "Edit a topic title")
@Service
public class EditTopicCommand implements Action {

    @Reference
    private IForumService forumService;

    @Argument(index = 0, name = "topicId", description = "Topic id", required = true, multiValued = false)
    String topicId;

    @Argument(index = 1, name = "newTitle", description = "New topic title", required = true, multiValued = false)
    String newTitle;

    @Override
    public Object execute() throws Exception {
        forumService.editTopic(topicId, newTitle);
        System.out.println("[EditTopicCommand] Updated topic " + topicId);
        return null;
    }
}
