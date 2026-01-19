package my.um.cbse.enrollment.command;

/**
 * Simple Action interface for Karaf commands
 * Implementing classes can be discovered and registered by Karaf's SCR handler
 */
public interface Action {
    Object execute() throws Exception;
}
