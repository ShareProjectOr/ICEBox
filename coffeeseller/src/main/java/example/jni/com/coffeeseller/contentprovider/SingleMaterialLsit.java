package example.jni.com.coffeeseller.contentprovider;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cof.ac.inter.ContainerConfig;
import example.jni.com.coffeeseller.bean.Coffee;

/**
 * Created by Administrator on 2018/4/16.
 */

public class SingleMaterialLsit {
    private static SingleMaterialLsit mInstance;

    private List<Coffee> coffeeList;
    private String TAG = "SingleMaterialLsit";
    private Context mContext;

    public SingleMaterialLsit(Context context) {
        mContext = context;
        String arrayString = SharedPreferencesManager.getInstance(context).getCoffeeListArray();
        Log.d(TAG, arrayString);
        if (SharedPreferencesManager.getInstance(context).getCoffeeListArray().isEmpty()) {
            coffeeList = new ArrayList<>();
        } else {
            Log.d(TAG, SharedPreferencesManager.getInstance(mContext).getCoffeeListArray());
            JSONArray array = JSON.parseArray(SharedPreferencesManager.getInstance(mContext).getCoffeeListArray());
            coffeeList = array.toJavaList(Coffee.class);

        }

    }

    public static synchronized SingleMaterialLsit getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SingleMaterialLsit(context);
        }
        return mInstance;
    }

    public boolean AddCoffeeList(Coffee coffee) {

        if (coffeeList.add(coffee)) {
            JSONArray jsonArray = (JSONArray) JSONObject.toJSON(coffeeList);
            String array = jsonArray.toString();
            Log.d(TAG, "list size=" + coffeeList.size() + " array is " + array);
            SharedPreferencesManager.getInstance(mContext).SetCoffeeList(array);
            return true;
        } else {
            return false;
        }
    }

    public void RemoveCoffeeList(int position) {
        coffeeList.remove(position);
    }

    public List<Coffee> getCoffeeList() {

        return coffeeList;
    }
}
