package com.um.osgi.forum.impl;

import com.um.osgi.forum.api.IForumService;
import org.osgi.service.component.annotations.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Bare-bones in-memory forum service implementation using OSGi Declarative Services (DS).
 */
@Component(service = IForumService.class, immediate = true)
public class ForumServiceImpl implements IForumService {

    // courseId -> list of topic display strings (topicId | title | author)
    private final Map<String, List<String>> topicsByCourse = new ConcurrentHashMap<>();

    // topicId -> list of replies (author: content)
    private final Map<String, List<String>> repliesByTopic = new ConcurrentHashMap<>();

    // topicId -> courseId (reverse mapping for delete)
    private final Map<String, String> topicToCourse = new ConcurrentHashMap<>();

    private final AtomicInteger topicCounter = new AtomicInteger(0);

    @Override
    public void createTopic(String courseId, String title, String author) {
        String topicId = "topic_" + topicCounter.incrementAndGet();
        String display = topicId + " | " + title + " | by " + author;

        topicsByCourse.computeIfAbsent(courseId, k -> Collections.synchronizedList(new ArrayList<>()))
                .add(display);
        repliesByTopic.putIfAbsent(topicId, Collections.synchronizedList(new ArrayList<>()));
        topicToCourse.put(topicId, courseId);

        System.out.println("[ForumService] Created topic: " + display + " in course: " + courseId);
    }

    @Override
    public List<String> getTopicsByCourse(String courseId) {
        List<String> topics = topicsByCourse.get(courseId);
        if (topics == null) return Collections.emptyList();
        synchronized (topics) {
            return new ArrayList<>(topics);
        }
    }

    @Override
    public void addReply(String topicId, String content, String author) {
        List<String> replies = repliesByTopic.get(topicId);
        if (replies == null) {
            System.out.println("[ForumService] No such topic: " + topicId + " — cannot add reply");
            return;
        }
        String entry = author + ": " + content;
        replies.add(entry);
        System.out.println("[ForumService] Added reply to " + topicId + ": " + entry);
    }

    @Override
    public void deleteTopic(String topicId) {
        String courseId = topicToCourse.remove(topicId);
        if (courseId != null) {
            List<String> topics = topicsByCourse.get(courseId);
            if (topics != null) {
                synchronized (topics) {
                    topics.removeIf(t -> t.startsWith(topicId + " ") || t.startsWith(topicId + "|") || t.startsWith(topicId + " |"));
                }
            }
        }
        repliesByTopic.remove(topicId);
        System.out.println("[ForumService] Deleted topic: " + topicId);
    }
}
