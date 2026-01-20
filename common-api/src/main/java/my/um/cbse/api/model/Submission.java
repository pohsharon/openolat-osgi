package my.um.cbse.api.model;

import java.util.Date;

public class Submission {
    private String id;
    private String assessmentId;
    private String studentId;
    private Date submittedAt;
    private String filePath;
    private String textEntry;
    private String link;
    private boolean lateSubmission;
    private SubmissionStatus status;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAssessmentId() { return assessmentId; }
    public void setAssessmentId(String assessmentId) { this.assessmentId = assessmentId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public Date getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Date submittedAt) { this.submittedAt = submittedAt; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getTextEntry() { return textEntry; }
    public void setTextEntry(String textEntry) { this.textEntry = textEntry; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public boolean isLateSubmission() { return lateSubmission; }
    public void setLateSubmission(boolean lateSubmission) { this.lateSubmission = lateSubmission; }

    public SubmissionStatus getStatus() { return status; }
    public void setStatus(SubmissionStatus status) { this.status = status; }
}
