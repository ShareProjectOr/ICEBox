package example.jni.com.coffeeseller.httputils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * Created by WH on 2017/12/6.
 */

public class JsonUtil {
    /*
    * 将json转换为类对象
    * @params json
    *  @params itemObject
    * */
    public static <T> T jsonToJavaBean(String json, T itemObject) {
        Gson gson = new Gson();
        itemObject = (T) gson.fromJson(json, itemObject.getClass());
        return itemObject;
    }

    public static <T> List<T> jsonToJaveBeanList(String json, T itemObject) {
        Gson gson = new Gson();
        List<T> itemObjects = gson.fromJson(json, new TypeToken<List<T>>() {
        }.getType());
        return itemObjects;
    }

    /*
    * map to json
    * @params map
    * */
    public static String mapToJson(Map<String, Object> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        return json;
    }

    /*
    *list to json
    * @param list
    * */
    public static String listToJson(List<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
