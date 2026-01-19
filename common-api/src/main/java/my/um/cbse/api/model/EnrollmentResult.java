package my.um.cbse.api.model;

/**
 * Result of an enrollment operation
 */
public class EnrollmentResult {
    private boolean success;
    private String message;
    private String errorCode;
    
    public EnrollmentResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public EnrollmentResult(boolean success, String message, String errorCode) {
        this.success = success;
        this.message = message;
        this.errorCode = errorCode;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    @Override
    public String toString() {
        return "EnrollmentResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                (errorCode != null ? ", errorCode='" + errorCode + '\'' : "") +
                '}';
    }
}
