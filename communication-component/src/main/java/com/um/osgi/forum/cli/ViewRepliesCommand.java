package com.um.osgi.forum.cli;

import com.um.osgi.forum.api.IForumService;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.Action;

import java.util.List;

@Command(scope = "cbse", name = "viewReplies", description = "View replies for a topic")
@Service
public class ViewRepliesCommand implements Action {

    @Reference
    private IForumService forumService;

    @Argument(index = 0, name = "topicId", description = "Topic id", required = true, multiValued = false)
    String topicId;

    @Override
    public Object execute() throws Exception {
        List<String> replies = forumService.getRepliesByTopic(topicId);
        if (replies.isEmpty()) {
            System.out.println("[ViewRepliesCommand] No replies found for topic: " + topicId);
        } else {
            System.out.println("[ViewRepliesCommand] Replies for topic " + topicId + ":");
            for (String reply : replies) {
                System.out.println("  " + reply);
            }
        }
        return null;
    }
}
