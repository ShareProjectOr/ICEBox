package com.example.shareiceboxms.models.helpers;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.example.shareiceboxms.models.adapters.MachineStockProductAdapter;
import com.example.shareiceboxms.models.beans.ItemProduct;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.RequestParamsContants;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by WH on 2017/12/19.
 */

public class MachineItemAddViewHelper {
    private int curPage, requestNum, totalNum, totalPage;
    private List<ItemProduct> itemProducts;
    private MachineStockProductAdapter adapter;

    public MachineItemAddViewHelper(List<ItemProduct> itemProducts, MachineStockProductAdapter adapter) {
        this.itemProducts = itemProducts;
        this.adapter = adapter;
    }

    private Map<String, Object> getParams() {
        Map<String, Object> params = RequestParamsContants.getInstance().getMachineStockProductParams();
        return params;
    }

    public void getDatas(Map<String, Object> params) {
        MachineStockProductTask task = new MachineStockProductTask(params);
        task.execute();
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<ItemProduct> getItemProducts() {
        return itemProducts;
    }

    public void setItemProducts(List<ItemProduct> itemProducts) {
        this.itemProducts = itemProducts;
    }

    public MachineStockProductAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(MachineStockProductAdapter adapter) {
        this.adapter = adapter;
    }
    /*
    *
    * 机器远程控制异步任务
    * */

    /**
     * //获取机器库存商品异步任务
     */
    public class MachineStockProductTask extends AsyncTask<Void, Void, Boolean> {
        private String response;
        private String err = "net_work_err";
        private List<ItemProduct> products;
        private Map<String, Object> params;


        public MachineStockProductTask(Map<String, Object> params) {
            this.params = params;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Log.e("request params: ", JsonUtil.mapToJson(this.params));
                response = OkHttpUtil.post(HttpRequstUrl.MACHINE_StockGoods_URL, JsonUtil.mapToJson(this.params));
                JSONObject jsonObject = new JSONObject(response.toString());
                JSONObject jsonD = jsonObject.getJSONObject("d");
                totalNum = jsonD.getInt("t");
                curPage = jsonD.getInt("p");
                requestNum = jsonD.getInt("n");
                totalPage = totalNum / requestNum + (totalNum % requestNum > 0 ? 1 : 0);
                JSONArray jsonList = jsonD.getJSONArray("list");
                products = ItemProduct.bindProductList(jsonList);
                Log.e("products.size==", products.size() + "");
                Log.e("response", response.toString());
                return true;
            } catch (IOException e) {
                Log.e("erro", e.toString());
            } catch (JSONException e) {
                Log.e("erro", e.toString());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                itemProducts.addAll(products);
                adapter.notifyDataSetChanged();
            } else {
                Log.e("request error :", response + "");
            }
        }


    }
}
