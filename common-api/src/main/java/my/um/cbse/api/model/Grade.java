package my.um.cbse.api.model;

public class Grade {
    private String submissionId;
    private String graderId;
    private int score;
    private String feedback;
    private boolean passed;

    public String getSubmissionId() { return submissionId; }
    public void setSubmissionId(String submissionId) { this.submissionId = submissionId; }

    public String getGraderId() { return graderId; }
    public void setGraderId(String graderId) { this.graderId = graderId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }
}
