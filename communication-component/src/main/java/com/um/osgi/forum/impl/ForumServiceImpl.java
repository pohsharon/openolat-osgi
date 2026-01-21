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

    // topicId -> locked status
    private final Map<String, Boolean> lockedTopics = new ConcurrentHashMap<>();

    // topicId -> topic title (for editing)
    private final Map<String, String> topicTitles = new ConcurrentHashMap<>();

    private final AtomicInteger topicCounter = new AtomicInteger(0);

    @Override
    public void createTopic(String courseId, String title, String author) {
        String topicId = "topic_" + topicCounter.incrementAndGet();
        String display = topicId + " | " + title + " | by " + author;

        topicsByCourse.computeIfAbsent(courseId, k -> Collections.synchronizedList(new ArrayList<>()))
                .add(display);
        repliesByTopic.putIfAbsent(topicId, Collections.synchronizedList(new ArrayList<>()));
        topicToCourse.put(topicId, courseId);
        topicTitles.put(topicId, title);
        lockedTopics.put(topicId, false);

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
        // Check if topic is locked
        if (Boolean.TRUE.equals(lockedTopics.get(topicId))) {
            System.out.println("[ForumService] Topic " + topicId + " is locked — cannot add reply");
            return;
        }

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
        lockedTopics.remove(topicId);
        topicTitles.remove(topicId);
        System.out.println("[ForumService] Deleted topic: " + topicId);
    }

    @Override
    public List<String> getRepliesByTopic(String topicId) {
        List<String> replies = repliesByTopic.get(topicId);
        if (replies == null) return Collections.emptyList();
        synchronized (replies) {
            return new ArrayList<>(replies);
        }
    }

    @Override
    public void lockTopic(String topicId, boolean lock) {
        if (!topicToCourse.containsKey(topicId)) {
            System.out.println("[ForumService] No such topic: " + topicId);
            return;
        }
        lockedTopics.put(topicId, lock);
        System.out.println("[ForumService] Topic " + topicId + " is now " + (lock ? "locked" : "unlocked"));
    }

    @Override
    public void editTopic(String topicId, String newTitle) {
        String courseId = topicToCourse.get(topicId);
        if (courseId == null) {
            System.out.println("[ForumService] No such topic: " + topicId);
            return;
        }

        // Get the author from existing topic
        List<String> topics = topicsByCourse.get(courseId);
        if (topics == null) return;

        String author = "Unknown";
        synchronized (topics) {
            for (String topic : topics) {
                if (topic.startsWith(topicId + " |")) {
                    // Extract author from "topic_1 | Old Title | by Author"
                    String[] parts = topic.split(" \\| ");
                    if (parts.length >= 3) {
                        author = parts[2].replace("by ", "");
                    }
                    // Remove old entry
                    topics.remove(topic);
                    break;
                }
            }
            // Add updated entry
            String newDisplay = topicId + " | " + newTitle + " | by " + author;
            topics.add(newDisplay);
        }

        topicTitles.put(topicId, newTitle);
        System.out.println("[ForumService] Updated topic " + topicId + " to: " + newTitle);
    }

    @Override
    public void deleteReply(String topicId, int replyIndex) {
        List<String> replies = repliesByTopic.get(topicId);
        if (replies == null) {
            System.out.println("[ForumService] No such topic: " + topicId);
            return;
        }

        synchronized (replies) {
            if (replyIndex < 0 || replyIndex >= replies.size()) {
                System.out.println("[ForumService] Invalid reply index: " + replyIndex);
                return;
            }
            String removed = replies.remove(replyIndex);
            System.out.println("[ForumService] Deleted reply from " + topicId + ": " + removed);
        }
    }
}
