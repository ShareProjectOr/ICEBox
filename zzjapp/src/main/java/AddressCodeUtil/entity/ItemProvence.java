package AddressCodeUtil.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WH on 2017/7/22.
 */
//省份
public class ItemProvence extends ItemBase implements Serializable {
//    public String name;
//    private String id;//区号
//    private String parentId;//父类区号
    public List<ItemCity> cites;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getParentId() {
//        return parentId;
//    }
//
//    public void setParentId(String parentId) {
//        this.parentId = parentId;
//    }

    public List<ItemCity> getCites() {
        return cites;
    }

    public void setCites(List<ItemCity> cites) {
        this.cites = cites;
    }
}
