package entity;

/**
 * Created by WH on 2017/7/24.
 */

public class ItemBase {
    public String name;
    private String id;//区号
    private String parentId;//父类区号

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
