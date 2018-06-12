package entity;

import java.io.Serializable;

/**
 * Created by WH on 2017/9/4.
 */

public class Item  implements Serializable{
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
