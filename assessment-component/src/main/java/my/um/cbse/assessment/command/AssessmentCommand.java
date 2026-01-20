package my.um.cbse.assessment.command;

public class AssessmentCommand {
    // TODO: Implement command logic for assessment
    public void execute() {
        System.out.println("Assessment command executed.");
    }

    public void runCommand() {
        try {
            execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
