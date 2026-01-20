package com.um.osgi.forum.api;

import java.util.List;

/**
 * Forum service API for creating topics, listing topics and handling replies.
 */
public interface IForumService {

    void createTopic(String courseId, String title, String author);

    List<String> getTopicsByCourse(String courseId);

    void addReply(String topicId, String content, String author);

    void deleteTopic(String topicId);
}
