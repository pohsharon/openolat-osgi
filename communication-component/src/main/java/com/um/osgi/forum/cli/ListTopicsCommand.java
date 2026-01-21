package com.um.osgi.forum.cli;

import com.um.osgi.forum.api.IForumService;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.Action;

import java.util.List;

@Command(scope = "cbse", name = "listTopics", description = "List topics for a course")
@Service
public class ListTopicsCommand implements Action {

    @Reference
    private IForumService forumService;

    @Argument(index = 0, name = "courseId", description = "Course id", required = true, multiValued = false)
    String courseId;

    @Override
    public Object execute() throws Exception {
        List<String> topics = forumService.getTopicsByCourse(courseId);
        if (topics.isEmpty()) {
            System.out.println("[ListTopicsCommand] No topics found for course: " + courseId);
        } else {
            System.out.println("[ListTopicsCommand] Topics for course " + courseId + ":");
            for (String t : topics) {
                System.out.println("  " + t);
            }
        }
        return null;
    }
}
