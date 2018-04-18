package example.jni.com.coffeeseller.contentprovider;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import example.jni.com.coffeeseller.bean.Coffee;

/**
 * Created by Administrator on 2018/4/16.
 */

public class SingleMaterialLsit {
    private static SingleMaterialLsit mInstance;

    private List<Coffee> coffeeList;
    private String TAG = "SingleMaterialLsit";

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
        JSONArray jsonArray = (JSONArray) JSONObject.toJSON(coffeeList);
        String array = jsonArray.toString();
        Log.d(TAG, array);
        return coffeeList.add(coffee);
    }

    public void RemoveCoffeeList(int position) {
        coffeeList.remove(position);
    }

    public List<Coffee> getCoffeeList() {
        return coffeeList;
    }
}
