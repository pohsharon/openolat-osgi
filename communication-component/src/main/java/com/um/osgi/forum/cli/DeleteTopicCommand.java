package com.um.osgi.forum.cli;

import com.um.osgi.forum.api.IForumService;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.Action;

@Command(scope = "cbse", name = "deleteTopic", description = "Delete a topic")
@Service
public class DeleteTopicCommand implements Action {

    @Reference
    private IForumService forumService;

    @Argument(index = 0, name = "topicId", description = "Topic id", required = true, multiValued = false)
    String topicId;

    @Override
    public Object execute() throws Exception {
        forumService.deleteTopic(topicId);
        System.out.println("[DeleteTopicCommand] Deleted topic: " + topicId);
        return null;
    }
}
