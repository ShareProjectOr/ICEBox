package com.example.shareiceboxms.models.helpers;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.shareiceboxms.models.beans.ItemMachine;
import com.example.shareiceboxms.models.beans.product.ItemProduct;
import com.example.shareiceboxms.models.beans.product.ItemSellProduct;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.JsonDataParse;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by WH on 2017/12/25.
 * 交易记录详情中售卖商品的辅助类
 * 拉取所有数据，不考虑分页的情况
 */

public class RecordDetailProductHelper {
    private static String TAG = "RecordDetailProductHelper";
    private Context context;
    private ItemMachine itemMachine;
    private List<ItemSellProduct> itemProducts;
    private ProductResponseLisenner productResponseLisenner;

    /*
    * 获取已售出和已退款的商品
    * */
    public RecordDetailProductHelper(Context context, ProductResponseLisenner productResponseLisenner) {
        this.context = context;
        this.productResponseLisenner = productResponseLisenner;
        itemProducts = new ArrayList<>();
    }

    public void getDatas(Map<String, Object> params) {
        RecordDetailProductTask task = new RecordDetailProductTask(params);
        task.execute();
    }


    public void setMachine(ItemMachine itemMachine) {
        this.itemMachine = itemMachine;
    }
//
//    public void setListView(ListView listView) {
//        this.listView = listView;
//    }
//
//    public void setScrollView(ScrollView scrollView) {
//        this.scrollView = scrollView;
//    }
//
//    public void setAdapter(TradeRecordDetailAdapter adapter) {
//        this.adapter = adapter;
//    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        if (scrollView.getChildAt(0).getHeight() == scrollView.getHeight() +
//                scrollView.getScrollY() - scrollView.getPaddingTop() - scrollView.getPaddingBottom()) {
//            if (!isLoading) {
//                isLoading = true;
//            } else {
//                return false;
//            }
//            if (curPage < totalPage) {
//                Map<String, Object> params = RequestParamsContants.getInstance().getMachineStockProductParams();
//                params.put("p", curPage + 1);
//                getDatas(params, itemProducts);
//                adapter.notifyDataSetChanged();
//                ListViewForScrollView.setListViewHeightBasedOnChildren(listView, scrollView);
//            } else {
//                Toast.makeText(context, "偷偷告诉你,数据已经全部加载...", Toast.LENGTH_SHORT).show();
//            }
//        }
//        return false;
//    }

    public class RecordDetailProductTask extends AsyncTask<Void, Void, Boolean> {
        private String response;
        private String err = "";
        private List<ItemSellProduct> products;
        private Map<String, Object> params;
        private List<ItemSellProduct> itemProducts;
        private Dialog dialog;


        public RecordDetailProductTask(Map<String, Object> params) {
            this.params = params;
            products = new ArrayList<>();
            dialog = MyDialog.loadDialog(context);
        }

        @Override
        protected void onPreExecute() {
            itemProducts = new ArrayList<>();
            products.clear();
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Log.e(TAG, " request url: " + HttpRequstUrl.TRADE_RECOR_DETAIL_PRODUCT_URL);
                Log.e(TAG, "request params: " + JsonUtil.mapToJson(this.params));
                response = OkHttpUtil.post(HttpRequstUrl.TRADE_RECOR_DETAIL_PRODUCT_URL, JsonUtil.mapToJson(this.params));
                products = ItemSellProduct.bindProductList(JsonDataParse.getInstance().getArrayList(response.toString()), itemMachine);
                Log.e(TAG, "products.size==" + products.size() + "");
                Log.e(TAG, "response:" + response.toString());
                return true;
            } catch (IOException e) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                err = RequstTips.getErrorMsg(e.getMessage());
            } catch (JSONException e) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                err = RequstTips.JSONException_Tip;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                dialog.dismiss();
                itemProducts.addAll(products);
                if (productResponseLisenner != null) {
                    productResponseLisenner.getProducts(itemProducts);
                }
            } else {
                if (context != null)
                    Toast.makeText(context, err, Toast.LENGTH_SHORT).show();
            }
            if (dialog != null) {
                dialog.dismiss();
            }
        }


    }

    public interface ProductResponseLisenner {
        void getProducts(List<ItemSellProduct> itemProducts);
    }
}
