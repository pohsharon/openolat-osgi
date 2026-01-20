package my.um.cbse.enrollment.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import my.um.cbse.api.model.Course;
import my.um.cbse.api.service.EnrollmentService;
import java.util.List;

@Command(scope = "cbse", name = "enrollmentInfo", description = "Get all courses enrolled by student")
@Component(service = org.apache.karaf.shell.api.action.Action.class, immediate = true, property = {
    "osgi.command.scope=cbse",
    "osgi.command.function=enrollmentInfo"
})
public class EnrollmentInfoCommand implements Action {

    @Argument(index = 0, name = "studentId", required = true, description = "Student ID")
    private String studentId;

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private EnrollmentService enrollmentService;

    public void enrollmentInfo(String studentId) throws Exception {
        this.studentId = studentId;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        if (enrollmentService == null) {
            System.out.println("ERROR: EnrollmentService is not available");
            return null;
        }

        try {
            List<Course> courses = enrollmentService.getStudentCourses(studentId);
            
            if (courses == null || courses.isEmpty()) {
                System.out.println("No courses found for student: " + studentId);
                return null;
            }

            System.out.println("╔════════════════════════════════════════════════════════╗");
            System.out.println("║ Enrolled Courses for Student: " + studentId);
            System.out.println("╠════════════════════════════════════════════════════════╣");
            
            for (Course course : courses) {
                System.out.println("Course ID: " + course.getCourseId() + 
                                   " | Name: " + course.getCourseName() + 
                                   " | Lecturer: " + course.getLecturerId());
            }
            
            System.out.println("╚════════════════════════════════════════════════════════╝");
            return courses;
        } catch (Exception e) {
            System.out.println("ERROR retrieving courses: " + e.getMessage());
            return null;
        }
    }
}
