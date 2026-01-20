package com.um.osgi.forum.cli;

import com.um.osgi.forum.api.IForumService;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.Action;

@Command(scope = "forum", name = "reply", description = "Add a reply to a topic")
@Service
public class ReplyCommand implements Action {

    @Reference
    private IForumService forumService;

    @Argument(index = 0, name = "topicId", description = "Topic id", required = true, multiValued = false)
    String topicId;

    @Argument(index = 1, name = "message", description = "Reply message", required = true, multiValued = false)
    String message;

    @Argument(index = 2, name = "author", description = "Author name", required = true, multiValued = false)
    String author;

    @Override
    public Object execute() throws Exception {
        forumService.addReply(topicId, message, author);
        System.out.println("[ReplyCommand] Added reply to " + topicId + " by " + author);
        return null;
    }
}
