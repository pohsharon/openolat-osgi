package my.um.cbse.api.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Course Element entity representing a node in the course structure tree
 */
public class CourseElement {
    private String elementId;
    private String parentId;
    private String title;
    private String description;
    private CourseElementType type;
    private int order;
    private boolean visible;
    private String accessCondition;
    private String linkedResourceId;
    private Map<String, String> configuration;
    private List<String> childElementIds;
    
    public CourseElement() {
        this.visible = true;
        this.configuration = new HashMap<>();
        this.childElementIds = new ArrayList<>();
    }
    
    public CourseElement(String elementId, String title, CourseElementType type) {
        this();
        this.elementId = elementId;
        this.title = title;
        this.type = type;
    }
    
    public String getElementId() {
        return elementId;
    }
    
    public void setElementId(String elementId) {
        this.elementId = elementId;
    }
    
    public String getParentId() {
        return parentId;
    }
    
    public void setParentId(String parentId) {
        this.parentId = parentId;
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
    
    public CourseElementType getType() {
        return type;
    }
    
    public void setType(CourseElementType type) {
        this.type = type;
    }
    
    public int getOrder() {
        return order;
    }
    
    public void setOrder(int order) {
        this.order = order;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public String getAccessCondition() {
        return accessCondition;
    }
    
    public void setAccessCondition(String accessCondition) {
        this.accessCondition = accessCondition;
    }
    
    public String getLinkedResourceId() {
        return linkedResourceId;
    }
    
    public void setLinkedResourceId(String linkedResourceId) {
        this.linkedResourceId = linkedResourceId;
    }
    
    public Map<String, String> getConfiguration() {
        return configuration;
    }
    
    public void setConfiguration(Map<String, String> configuration) {
        this.configuration = configuration;
    }
    
    public List<String> getChildElementIds() {
        return childElementIds;
    }
    
    public void setChildElementIds(List<String> childElementIds) {
        this.childElementIds = childElementIds;
    }
    
    @Override
    public String toString() {
        return "CourseElement{" +
                "elementId='" + elementId + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", visible=" + visible +
                ", children=" + childElementIds.size() +
                '}';
    }
}
