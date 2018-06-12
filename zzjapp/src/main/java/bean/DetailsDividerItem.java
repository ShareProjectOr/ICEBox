package bean;

/**
 * Created by Administrator on 2017/8/28.
 */

public class DetailsDividerItem {
    String title;
    String content;

    public DetailsDividerItem(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
