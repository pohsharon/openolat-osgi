package my.um.cbse.api.model;

/**
 * Result object for course operations
 */
public class CourseResult {
    private boolean success;
    private String message;
    private String errorCode;
    private Object data;
    
    public CourseResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public CourseResult(boolean success, String message, String errorCode) {
        this.success = success;
        this.message = message;
        this.errorCode = errorCode;
    }
    
    public CourseResult(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
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
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
    
    @Override
    public String toString() {
        return "CourseResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                (errorCode != null ? ", errorCode='" + errorCode + '\'' : "") +
                '}';
    }
}
