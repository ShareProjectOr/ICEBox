package example.jni.com.coffeeseller.contentprovider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cof.ac.inter.ContainerConfig;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.bean.Process;

/**
 * Created by Administrator on 2018/4/16.
 */

public class SingleMaterialLsit {
    private static SingleMaterialLsit mInstance;

    private List<Coffee> coffeeList;

    public SingleMaterialLsit() {
        coffeeList = new ArrayList<>();
    }

    public static synchronized SingleMaterialLsit getInstance() {
        if (mInstance == null) {
            mInstance = new SingleMaterialLsit();
        }
        return mInstance;
    }

    public boolean AddCoffeeList(Coffee coffee) {
       /* com.alibaba.fastjson.JSONArray jsonArray = (com.alibaba.fastjson.JSONArray) com.alibaba.fastjson.JSONObject.toJSON(coffeeList);
        jsonArray.toString();*/
        return coffeeList.add(coffee);
    }

    public void RemoveCoffeeList(int position) {
        coffeeList.remove(position);
    }

    public List<Coffee> getCoffeeList() {
        return coffeeList;
    }
}
