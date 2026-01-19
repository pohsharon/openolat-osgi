package my.um.cbse.enrollment.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import my.um.cbse.api.service.EnrollmentService;

/**
 * Karaf Shell Command: registerStudent
 * Register a new student
 */
@Command(scope = "cbse", name = "registerStudent", description = "Register a new student")
@Component(
    service = RegisterStudentCommand.class,
    property = {
        "osgi.command.scope=cbse",
        "osgi.command.function=registerStudent"
    }
)
public class RegisterStudentCommand implements Action {

    @Argument(index = 0, name = "studentId", description = "The student ID", required = true)
    private String studentId;

    @Argument(index = 1, name = "name", description = "The student's full name", required = true)
    private String name;

    @Argument(index = 2, name = "email", description = "The student's email", required = true)
    private String email;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private EnrollmentService enrollmentService;

    // Public method for Gogo shell to invoke
    public void registerStudent(String studentId, String name, String email) throws Exception {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        execute();
    }

    @Override
    public Object execute() throws Exception {
        if (enrollmentService == null) {
            System.out.println("ERROR: EnrollmentService is not available");
            return null;
        }

        // Create a student object
        my.um.cbse.api.model.Student student = new my.um.cbse.api.model.Student(studentId, name, email);

        boolean success = enrollmentService.registerStudent(student);
        
        if (success) {
            System.out.println("✓ Student registered successfully");
            System.out.println("  ID: " + studentId);
            System.out.println("  Name: " + name);
            System.out.println("  Email: " + email);
        } else {
            System.out.println("✗ Failed to register student (may already exist)");
        }

        return success;
    }
}
