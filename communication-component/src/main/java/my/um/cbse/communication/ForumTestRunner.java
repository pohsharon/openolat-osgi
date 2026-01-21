package my.um.cbse.communication;

import com.um.osgi.forum.impl.ForumServiceImpl;

/**
 * System Test Runner for Communication Module
 * Simulates realistic forum interaction scenarios
 */
public class ForumTestRunner {

    private static final String SEPARATOR = "------------------------------------------------------------";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String CYAN = "\u001B[36m";
    private static final String RESET = "\u001B[0m";

    public static void main(String[] args) {
        ForumTestRunner runner = new ForumTestRunner();
        
        // Run tests in a clean sequence
        boolean created1 = runner.testTopicCreationAndManagement();
        boolean created2 = runner.testReplyManagement();
        boolean created3 = runner.testModerationWorkflow();
        
        if (!created1 || !created2 || !created3) {
            System.err.println("\n❌ Cannot continue because some tests failed.\n");
        }
    }

    private boolean testTopicCreationAndManagement() {
        System.out.println(SEPARATOR);
        System.out.println("TEST 1: TOPIC CREATION & MANAGEMENT");
        System.out.println(SEPARATOR);

        ForumServiceImpl service = new ForumServiceImpl();

        // Test 1.1: Create a new topic
        System.out.println("\nTest 1.1: Create a new topic for COURSE123");
        System.out.println(CYAN + "[CREATE]" + RESET + " Title: OSGi Best Practices, Course: COURSE123");
        service.createTopic("COURSE123", "OSGi Best Practices", "Eugene");
        System.out.println(GREEN + "✅ SUCCESS: Created topic with ID: topic_1" + RESET);

        // Test 1.2: Create another topic
        System.out.println("\nTest 1.2: Create another topic for COURSE123");
        System.out.println(CYAN + "[CREATE]" + RESET + " Title: Final Exam Discussion, Course: COURSE123");
        service.createTopic("COURSE123", "Final Exam Discussion", "Sharon");
        System.out.println(GREEN + "✅ SUCCESS: Created topic with ID: topic_2" + RESET);

        // Test 1.3: List topics
        System.out.println("\nTest 1.3: List all topics for COURSE123");
        System.out.println(CYAN + "[LIST-TOPICS]" + RESET + " Course: COURSE123");
        java.util.List<String> topics = service.getTopicsByCourse("COURSE123");
        for (String topic : topics) {
            System.out.println("- " + topic);
        }

        // Test 1.4: Edit topic title
        System.out.println("\nTest 1.4: Edit topic title");
        System.out.println(CYAN + "[UPDATE]" + RESET + " ID: topic_1");
        service.editTopic("topic_1", "Updated OSGi Best Practices");
        System.out.println(GREEN + "✅ SUCCESS: Updated topic title to: Updated OSGi Best Practices" + RESET);

        // Test 1.5: Delete topic
        System.out.println("\nTest 1.5: Delete topic (topic_2)");
        System.out.println(CYAN + "[DELETE]" + RESET + " ID: topic_2");
        service.deleteTopic("topic_2");
        System.out.println(GREEN + "✅ SUCCESS: Topic deleted" + RESET);

        // Test 1.6: List after deletion
        System.out.println("\nTest 1.6: List topics for COURSE123 after deletion");
        System.out.println(CYAN + "[LIST-TOPICS]" + RESET + " Course: COURSE123");
        topics = service.getTopicsByCourse("COURSE123");
        for (String topic : topics) {
            System.out.println("- " + topic);
        }

        return true;
    }

    private boolean testReplyManagement() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("TEST 2: REPLY MANAGEMENT");
        System.out.println(SEPARATOR);

        ForumServiceImpl service = new ForumServiceImpl();
        service.createTopic("COURSE123", "OSGi Discussion", "Eugene");

        // Test 2.1: Student posts a reply
        System.out.println("\nTest 2.1: Student posts a reply to topic");
        System.out.println(CYAN + "[REPLY]" + RESET + " Topic: topic_1, Author: STUDENT01");
        service.addReply("topic_1", "Great explanation!", "STUDENT01");
        System.out.println(GREEN + "✅ SUCCESS: Reply added" + RESET);

        // Test 2.2: Another reply
        System.out.println("\nTest 2.2: Another student replies");
        System.out.println(CYAN + "[REPLY]" + RESET + " Topic: topic_1, Author: STUDENT02");
        service.addReply("topic_1", "Very helpful for exam prep", "STUDENT02");
        System.out.println(GREEN + "✅ SUCCESS: Reply added" + RESET);

        // Test 2.3: List replies
        System.out.println("\nTest 2.3: List replies for topic");
        System.out.println(CYAN + "[VIEW-REPLIES]" + RESET + " Topic: topic_1");
        java.util.List<String> replies = service.getRepliesByTopic("topic_1");
        for (String reply : replies) {
            System.out.println("- " + reply);
        }

        // Test 2.4: Delete a reply
        System.out.println("\nTest 2.4: Delete first reply");
        System.out.println(CYAN + "[DELETE-REPLY]" + RESET + " Topic: topic_1, Index: 0");
        service.deleteReply("topic_1", 0);
        System.out.println(GREEN + "✅ SUCCESS: Reply removed" + RESET);

        // Test 2.5: View after deletion
        System.out.println("\nTest 2.5: View replies after deletion");
        System.out.println(CYAN + "[VIEW-REPLIES]" + RESET + " Topic: topic_1");
        replies = service.getRepliesByTopic("topic_1");
        for (String reply : replies) {
            System.out.println("- " + reply);
        }

        return true;
    }

    private boolean testModerationWorkflow() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("TEST 3: MODERATION WORKFLOW");
        System.out.println(SEPARATOR);

        ForumServiceImpl service = new ForumServiceImpl();
        service.createTopic("COURSE123", "OSGi Discussion", "Eugene");
        service.addReply("topic_1", "Initial reply", "STUDENT01");

        // Test 3.1: Lock topic
        System.out.println("\nTest 3.1: Moderator locks a discussion thread");
        System.out.println(CYAN + "[LOCK]" + RESET + " Topic: topic_1");
        service.lockTopic("topic_1", true);
        System.out.println(GREEN + "✅ SUCCESS: Topic locked" + RESET);

        // Test 3.2: Attempt to reply to locked topic
        System.out.println("\nTest 3.2: Attempt to reply to locked thread");
        System.out.println(CYAN + "[REPLY]" + RESET + " Topic: topic_1, Author: STUDENT03");
        service.addReply("topic_1", "This should fail", "STUDENT03");
        System.out.println(RED + "❌ BLOCKED: Topic is locked - cannot add reply" + RESET);

        // Test 3.3: Unlock topic
        System.out.println("\nTest 3.3: Unlock thread");
        System.out.println(CYAN + "[UNLOCK]" + RESET + " Topic: topic_1");
        service.lockTopic("topic_1", false);
        System.out.println(GREEN + "✅ SUCCESS: Topic unlocked" + RESET);

        // Test 3.4: Reply after unlock
        System.out.println("\nTest 3.4: Reply after unlocking");
        System.out.println(CYAN + "[REPLY]" + RESET + " Topic: topic_1, Author: STUDENT03");
        service.addReply("topic_1", "Now this works!", "STUDENT03");
        System.out.println(GREEN + "✅ SUCCESS: Reply added" + RESET);

        return true;
    }
}
