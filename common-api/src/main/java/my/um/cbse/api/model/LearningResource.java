package my.um.cbse.api.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Learning Resource entity representing reusable learning content
 */
public class LearningResource {
    private String resourceId;
    private String title;
    private String description;
    private LearningResourceType type;
    private String ownerId;
    private Date createdDate;
    private Date lastModified;
    private String version;
    private Map<String, String> metadata;
    private String filePath;
    private boolean shared;
    
    public LearningResource() {
        this.createdDate = new Date();
        this.lastModified = new Date();
        this.metadata = new HashMap<>();
        this.version = "1.0";
        this.shared = false;
    }
    
    public LearningResource(String resourceId, String title, LearningResourceType type, String ownerId) {
        this();
        this.resourceId = resourceId;
        this.title = title;
        this.type = type;
        this.ownerId = ownerId;
    }
    
    public String getResourceId() {
        return resourceId;
    }
    
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LearningResourceType getType() {
        return type;
    }
    
    public void setType(LearningResourceType type) {
        this.type = type;
    }
    
    public String getOwnerId() {
        return ownerId;
    }
    
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    
    public Date getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    public Date getLastModified() {
        return lastModified;
    }
    
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public Map<String, String> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public boolean isShared() {
        return shared;
    }
    
    public void setShared(boolean shared) {
        this.shared = shared;
    }
    
    @Override
    public String toString() {
        return "LearningResource{" +
                "resourceId='" + resourceId + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", ownerId='" + ownerId + '\'' +
                '}';
    }
}
