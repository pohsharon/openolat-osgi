package my.um.cbse.api.service;

import my.um.cbse.api.model.*;
import java.util.List;

/**
 * AssessmentService - Interface for managing assessments, submissions, and grading.
 * Covers use cases: Manage Assessments, Review Submission, Grade Submission, View Assignment, Submit Assignment.
 */
public interface AssessmentService {
    /**
     * UC6: Create and configure an assessment element for a course.
     */
    Assessment createAssessment(Assessment assessment);

    /**
     * UC6: Update/configure an existing assessment.
     */
    boolean updateAssessment(String id, Assessment updated);

    /**
     * UC6, UC-View: Get an assessment by ID.
     */
    Assessment getAssessment(String id);

    /**
     * UC6: Delete an assessment.
     */
    boolean deleteAssessment(String id);

    /**
     * UC6: List all assessments for a course.
     */
    List<Assessment> listAssessmentsByCourse(String courseId);

    /**
     * UC7: List all submissions for an assessment.
     */
    List<Submission> listSubmissionsByAssessment(String assessmentId);

    /**
     * UC7: Get a specific submission by ID.
     */
    Submission getSubmission(String submissionId);

    /**
     * UC7: Download a submission file (returns file path or byte[]).
     */
    byte[] downloadSubmissionFile(String submissionId);

    /**
     * UC7: Download all submissions as a bundle (returns file path or byte[]).
     */
    byte[] downloadAllSubmissions(String assessmentId);

    /**
     * UC8: Grade a submission and save the grade.
     */
    boolean gradeSubmission(Grade grade);

    /**
     * UC8: Provide feedback for a submission.
     */
    boolean provideFeedback(String submissionId, String feedback);

    /**
     * UC-Submit: Submit an assignment (file, text, or link).
     */
    Submission submitAssignment(Submission submission);

    /**
     * UC-Submit: Resubmit an assignment (update submission).
     */
    Submission resubmitAssignment(String submissionId, Submission updatedSubmission);

    /**
     * UC-Submit: Mark a submission as late.
     */
    boolean markSubmissionLate(String submissionId);

    /**
     * UC-View: List assignments for a student (optional, for student dashboard).
     */
    List<Assessment> listAssignmentsForStudent(String studentId);
}
