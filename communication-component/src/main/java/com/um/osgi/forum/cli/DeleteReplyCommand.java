package com.um.osgi.forum.cli;

import com.um.osgi.forum.api.IForumService;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.Action;

@Command(scope = "cbse", name = "deleteReply", description = "Delete a specific reply from a topic")
@Service
public class DeleteReplyCommand implements Action {

    @Reference
    private IForumService forumService;

    @Argument(index = 0, name = "topicId", description = "Topic id", required = true, multiValued = false)
    String topicId;

    @Argument(index = 1, name = "replyIndex", description = "Reply index (0-based)", required = true, multiValued = false)
    int replyIndex;

    @Override
    public Object execute() throws Exception {
        forumService.deleteReply(topicId, replyIndex);
        System.out.println("[DeleteReplyCommand] Deleted reply " + replyIndex + " from topic " + topicId);
        return null;
    }
}
