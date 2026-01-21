package com.um.osgi.forum.cli;

import com.um.osgi.forum.api.IForumService;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.Action;

@Command(scope = "cbse", name = "lockTopic", description = "Lock or unlock a discussion topic")
@Service
public class LockTopicCommand implements Action {

    @Reference
    private IForumService forumService;

    @Argument(index = 0, name = "topicId", description = "Topic id", required = true, multiValued = false)
    String topicId;

    @Argument(index = 1, name = "lock", description = "true to lock, false to unlock", required = true, multiValued = false)
    boolean lock;

    @Override
    public Object execute() throws Exception {
        forumService.lockTopic(topicId, lock);
        System.out.println("[LockTopicCommand] Topic " + topicId + " is now " + (lock ? "locked" : "unlocked"));
        return null;
    }
}
