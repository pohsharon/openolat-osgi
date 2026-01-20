package my.um.cbse.course.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.model.Course;
import my.um.cbse.api.service.CourseService;

/**
 * Karaf Shell Command: course:info
 * UC2 - Display course details
 * 
 * Usage: course:info <courseId>
 */
@Command(scope = "course", name = "info", description = "Display course details")
@Component(
    service = Action.class,
    property = {
        "osgi.command.scope=course",
        "osgi.command.function=info"
    }
)
public class CourseInfoCommand implements Action {

    @Argument(index = 0, name = "courseId", description = "The course ID", required = true)
    private String courseId;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private CourseService courseService;

    public void info(String courseId) throws Exception {
        this.courseId = courseId;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        if (courseService == null) {
            System.out.println("ERROR: CourseService is not available");
            return null;
        }

        Course course = courseService.getCourse(courseId);
        
        if (course == null) {
            System.out.println("✗ Course not found: " + courseId);
            return null;
        }
        
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                    COURSE INFORMATION                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("  Course ID:        " + course.getCourseId());
        System.out.println("  Course Code:      " + (course.getCourseCode() != null ? course.getCourseCode() : "N/A"));
        System.out.println("  Title:            " + course.getCourseName());
        System.out.println("  Description:      " + (course.getDescription() != null ? course.getDescription() : "N/A"));
        System.out.println();
        System.out.println("  ─── Design & Status ───");
        System.out.println("  Design:           " + course.getDesign());
        System.out.println("  Status:           " + course.getStatus());
        System.out.println("  Self-Enrollment:  " + (course.isSelfEnrollmentAllowed() ? "Allowed" : "Not Allowed"));
        System.out.println();
        System.out.println("  ─── Metadata ───");
        System.out.println("  Owner:            " + course.getOwnerId());
        System.out.println("  Lecturer:         " + (course.getLecturerId() != null ? course.getLecturerId() : "N/A"));
        System.out.println("  Semester:         " + (course.getSemester() != null ? course.getSemester() : "N/A"));
        System.out.println("  Credits:          " + course.getCredits());
        System.out.println("  Institution:      " + (course.getInstitution() != null ? course.getInstitution() : "N/A"));
        System.out.println("  License:          " + (course.getLicense() != null ? course.getLicense() : "N/A"));
        System.out.println();
        System.out.println("  ─── Settings ───");
        System.out.println("  Max Students:     " + course.getMaxStudents());
        System.out.println("  Lecture Mgmt:     " + (course.isLectureManagementEnabled() ? "Enabled" : "Disabled"));
        System.out.println("  Absence Mgmt:     " + (course.isAbsenceManagementEnabled() ? "Enabled" : "Disabled"));
        System.out.println();
        System.out.println("  ─── Dates ───");
        System.out.println("  Created:          " + course.getCreatedDate());
        System.out.println("  Last Modified:    " + course.getLastModified());
        System.out.println("  Start Date:       " + (course.getStartDate() != null ? course.getStartDate() : "N/A"));
        System.out.println("  End Date:         " + (course.getEndDate() != null ? course.getEndDate() : "N/A"));
        System.out.println();
        
        if (!course.getPrerequisites().isEmpty()) {
            System.out.println("  ─── Prerequisites ───");
            for (String prereq : course.getPrerequisites()) {
                System.out.println("    • " + prereq);
            }
            System.out.println();
        }
        
        return course;
    }
}
