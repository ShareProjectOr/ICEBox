package otherutils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Administrator on 2017/7/28.
 */

public class Json2Map {
    public static HashMap<String, String> toHashMap(JSONObject object) {
        HashMap<String, String> data = new HashMap<String, String>();
        // 将json字符串转换成jsonObject
        //JSONObject jsonObject = JSONObject.fromObject(object);
        Iterator it = object.keys();
        // 遍历jsonObject数据，添加到Map对象
        while (it.hasNext()) {

            try {
                String key = String.valueOf(it.next());
                String value = (String) object.getString(key);
                data.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return data;
    }
}
