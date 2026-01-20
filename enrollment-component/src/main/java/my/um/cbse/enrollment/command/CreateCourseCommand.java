package my.um.cbse.enrollment.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import my.um.cbse.api.model.Course;
import my.um.cbse.api.service.EnrollmentService;

@Command(scope = "cbse", name = "createCourse", description = "Create a new course")
@Component(service = org.apache.karaf.shell.api.action.Action.class, immediate = true, property = {
    "osgi.command.scope=cbse",
    "osgi.command.function=createCourse"
})
public class CreateCourseCommand implements Action {

    @Argument(index = 0, name = "courseId", required = true, description = "Course ID")
    private String courseId;

    @Argument(index = 1, name = "courseName", required = true, description = "Course name")
    private String courseName;

    @Argument(index = 2, name = "lecturerId", required = true, description = "Lecturer ID")
    private String lecturerId;

    @Option(name = "--max-students", description = "Maximum students allowed")
    private Integer maxStudents;

    @Option(name = "--semester", description = "Semester (e.g., Spring2024)")
    private String semester;

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private EnrollmentService enrollmentService;

    // Public method for Gogo shell to invoke
    public void createCourse(String courseId, String courseName, String lecturerId) throws Exception {
        this.courseId = courseId;
        this.courseName = courseName;
        this.lecturerId = lecturerId;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        if (enrollmentService == null) {
            System.out.println("ERROR: EnrollmentService is not available");
            return null;
        }

        try {
            Course course = new Course(courseId, courseName, lecturerId);
            
            if (maxStudents != null) {
                course.setMaxStudents(maxStudents);
            }
            if (semester != null) {
                course.setSemester(semester);
            }

            System.out.println("╔════════════════════════════════════════════════════════╗");
            System.out.println("║ Course Created Successfully");
            System.out.println("╠════════════════════════════════════════════════════════╣");
            System.out.println("Course ID:      " + course.getCourseId());
            System.out.println("Course Name:    " + course.getCourseName());
            System.out.println("Lecturer ID:    " + course.getLecturerId());
            if (maxStudents != null) {
                System.out.println("Max Students:   " + maxStudents);
            }
            if (semester != null) {
                System.out.println("Semester:       " + semester);
            }
            System.out.println("╚════════════════════════════════════════════════════════╝");
            
            return course;
        } catch (Exception e) {
            System.out.println("ERROR creating course: " + e.getMessage());
            return null;
        }
    }
}
