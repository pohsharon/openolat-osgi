package my.um.cbse.assessment;

import my.um.cbse.api.model.*;
import my.um.cbse.api.service.AssessmentService;
import org.osgi.service.component.annotations.Component;
import java.util.*;

/**
 * In-memory implementation of AssessmentService for demonstration/testing.
 * Replace with persistent storage in production.
 */
@Component(service = AssessmentService.class)
public class AssessmentServiceImpl implements AssessmentService {
    private final Map<String, Assessment> assessments = new HashMap<>();
    private final Map<String, Submission> submissions = new HashMap<>();
    private final Map<String, List<Submission>> assessmentSubmissions = new HashMap<>();
    private final Map<String, Grade> grades = new HashMap<>();

    // UC6: Create and configure an assessment element for a course
    @Override
    public Assessment createAssessment(Assessment assessment) {
        String id = UUID.randomUUID().toString();
        assessment.setId(id);
        assessments.put(id, assessment);
        return assessment;
    }

    // UC6: Update/configure an existing assessment
    @Override
    public boolean updateAssessment(String id, Assessment updated) {
        if (assessments.containsKey(id)) {
            updated.setId(id);
            assessments.put(id, updated);
            return true;
        }
        return false;
    }

    // UC6, UC-View: Get an assessment by ID
    @Override
    public Assessment getAssessment(String id) {
        return assessments.get(id);
    }

    // UC6: Delete an assessment
    @Override
    public boolean deleteAssessment(String id) {
        return assessments.remove(id) != null;
    }

    // UC6: List all assessments for a course
    @Override
    public List<Assessment> listAssessmentsByCourse(String courseId) {
        List<Assessment> result = new ArrayList<>();
        for (Assessment a : assessments.values()) {
            if (a.getCourseId().equals(courseId)) {
                result.add(a);
            }
        }
        return result;
    }

    // UC7: List all submissions for an assessment
    @Override
    public List<Submission> listSubmissionsByAssessment(String assessmentId) {
        return assessmentSubmissions.getOrDefault(assessmentId, new ArrayList<>());
    }

    // UC7: Get a specific submission by ID
    @Override
    public Submission getSubmission(String submissionId) {
        return submissions.get(submissionId);
    }

    // UC7: Download a submission file (returns dummy byte[])
    @Override
    public byte[] downloadSubmissionFile(String submissionId) {
        // In a real system, fetch file from storage
        Submission s = submissions.get(submissionId);
        if (s != null && s.getFilePath() != null) {
            return ("Dummy file content for " + s.getFilePath()).getBytes();
        }
        return null;
    }

    // UC7: Download all submissions as a bundle (returns dummy byte[])
    @Override
    public byte[] downloadAllSubmissions(String assessmentId) {
        // In a real system, zip all files and return
        List<Submission> list = assessmentSubmissions.getOrDefault(assessmentId, new ArrayList<>());
        return ("Dummy zip for submissions: " + list.size()).getBytes();
    }

    // UC8: Grade a submission and save the grade
    @Override
    public boolean gradeSubmission(Grade grade) {
        if (grade == null || grade.getSubmissionId() == null) return false;
        grades.put(grade.getSubmissionId(), grade);
        Submission s = submissions.get(grade.getSubmissionId());
        if (s != null) {
            s.setStatus(SubmissionStatus.GRADED);
        }
        return true;
    }

    // UC8: Provide feedback for a submission
    @Override
    public boolean provideFeedback(String submissionId, String feedback) {
        Grade g = grades.get(submissionId);
        if (g != null) {
            g.setFeedback(feedback);
            return true;
        }
        return false;
    }

    // UC-Submit: Submit an assignment (file, text, or link)
    @Override
    public Submission submitAssignment(Submission submission) {
        String id = UUID.randomUUID().toString();
        submission.setId(id);
        submission.setSubmittedAt(new Date());
        submission.setStatus(SubmissionStatus.SUBMITTED);
        submissions.put(id, submission);
        assessmentSubmissions.computeIfAbsent(submission.getAssessmentId(), k -> new ArrayList<>()).add(submission);
        return submission;
    }

    // UC-Submit: Resubmit an assignment (update submission)
    @Override
    public Submission resubmitAssignment(String submissionId, Submission updatedSubmission) {
        Submission existing = submissions.get(submissionId);
        if (existing != null) {
            updatedSubmission.setId(submissionId);
            updatedSubmission.setSubmittedAt(new Date());
            updatedSubmission.setStatus(SubmissionStatus.RESUBMITTED);
            submissions.put(submissionId, updatedSubmission);
            // Replace in assessmentSubmissions list
            List<Submission> list = assessmentSubmissions.get(updatedSubmission.getAssessmentId());
            if (list != null) {
                list.removeIf(s -> s.getId().equals(submissionId));
                list.add(updatedSubmission);
            }
            return updatedSubmission;
        }
        return null;
    }

    // UC-Submit: Mark a submission as late
    @Override
    public boolean markSubmissionLate(String submissionId) {
        Submission s = submissions.get(submissionId);
        if (s != null) {
            s.setLateSubmission(true);
            s.setStatus(SubmissionStatus.LATE);
            return true;
        }
        return false;
    }

    // UC-View: List assignments for a student (optional, for student dashboard)
    @Override
    public List<Assessment> listAssignmentsForStudent(String studentId) {
        // In a real system, filter by student enrollment
        return new ArrayList<>(assessments.values());
    }
}
