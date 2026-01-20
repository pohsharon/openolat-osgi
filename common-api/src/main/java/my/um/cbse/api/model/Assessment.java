package my.um.cbse.api.model;

import java.util.Date;
import java.util.List;

public class Assessment {
    private String id;
    private String courseId;
    private String title;
    private String description;
    private String layout;
    private String learningPathOptions;
    private Rubric rubric;
    private int maxPoints;
    private boolean gradingEnabled;
    private boolean passFailVisible;
    private boolean feedbackCommentsEnabled;
    private boolean feedbackDocumentsEnabled;
    private boolean statusSettingsEnabled;
    private Date deadline;
    private List<String> files;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLayout() { return layout; }
    public void setLayout(String layout) { this.layout = layout; }

    public String getLearningPathOptions() { return learningPathOptions; }
    public void setLearningPathOptions(String learningPathOptions) { this.learningPathOptions = learningPathOptions; }

    public Rubric getRubric() { return rubric; }
    public void setRubric(Rubric rubric) { this.rubric = rubric; }

    public int getMaxPoints() { return maxPoints; }
    public void setMaxPoints(int maxPoints) { this.maxPoints = maxPoints; }

    public boolean isGradingEnabled() { return gradingEnabled; }
    public void setGradingEnabled(boolean gradingEnabled) { this.gradingEnabled = gradingEnabled; }

    public boolean isPassFailVisible() { return passFailVisible; }
    public void setPassFailVisible(boolean passFailVisible) { this.passFailVisible = passFailVisible; }

    public boolean isFeedbackCommentsEnabled() { return feedbackCommentsEnabled; }
    public void setFeedbackCommentsEnabled(boolean feedbackCommentsEnabled) { this.feedbackCommentsEnabled = feedbackCommentsEnabled; }

    public boolean isFeedbackDocumentsEnabled() { return feedbackDocumentsEnabled; }
    public void setFeedbackDocumentsEnabled(boolean feedbackDocumentsEnabled) { this.feedbackDocumentsEnabled = feedbackDocumentsEnabled; }

    public boolean isStatusSettingsEnabled() { return statusSettingsEnabled; }
    public void setStatusSettingsEnabled(boolean statusSettingsEnabled) { this.statusSettingsEnabled = statusSettingsEnabled; }

    public Date getDeadline() { return deadline; }
    public void setDeadline(Date deadline) { this.deadline = deadline; }

    public List<String> getFiles() { return files; }
    public void setFiles(List<String> files) { this.files = files; }
}
