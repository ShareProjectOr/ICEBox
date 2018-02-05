package com.example.shareiceboxms.models.helpers;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.adapters.MachineStockProductAdapter;
import com.example.shareiceboxms.models.beans.ItemMachine;
import com.example.shareiceboxms.models.beans.product.ItemStockProduct;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.JsonDataParse;
import com.example.shareiceboxms.models.contants.RequestParamsContants;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;
import com.example.shareiceboxms.models.widget.ListViewForScrollView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by WH on 2017/12/19.
 */

public class MachineItemAddViewHelper {
    private int curPage, requestNum, totalNum, totalPage;
    private List<ItemStockProduct> itemProducts;
    private MachineStockProductAdapter adapter;
    private ItemMachine itemMachine;
    private Context context;
    private ListView listView;
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private StocksPageHelper stocksPageHelper;

    public MachineItemAddViewHelper(List<ItemStockProduct> itemProducts, MachineStockProductAdapter adapter, ItemMachine itemMachine) {
        this.itemProducts = itemProducts;
        this.adapter = adapter;
        this.itemMachine = itemMachine;
    }

    private Map<String, Object> getParams() {
        Map<String, Object> params = RequestParamsContants.getInstance().getMachineStockProductParams();
        return params;
    }

    public void getDatas(Map<String, Object> params) {
        MachineStockProductTask task = new MachineStockProductTask(params);
        task.execute();
    }

    public void setPageHelper(StocksPageHelper stocksPageHelper) {
        this.stocksPageHelper = stocksPageHelper;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
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

    public List<ItemStockProduct> getItemProducts() {
        return itemProducts;
    }

    public void setItemProducts(List<ItemStockProduct> itemProducts) {
        this.itemProducts = itemProducts;
    }

    public MachineStockProductAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(MachineStockProductAdapter adapter) {
        this.adapter = adapter;
    }

    public void setView(ListView listView, ScrollView scrollView) {
        this.listView = listView;
        this.scrollView = scrollView;
    }

    public void setLinearLayout(LinearLayout layout) {
        this.linearLayout = layout;
    }

    /**
     * //获取机器库存商品异步任务
     */

    public class MachineStockProductTask extends AsyncTask<Void, Void, Boolean> {
        private String response;
        private String err = "net_work_err";
        private List<ItemStockProduct> products;
        private Map<String, Object> params;
        private Dialog dialog;


        public MachineStockProductTask(Map<String, Object> params) {
            this.params = params;
            products = new ArrayList<>();
            dialog = MyDialog.loadDialog(context);
        }

        @Override
        protected void onPreExecute() {
            products.clear();
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Log.e("request params: ", JsonUtil.mapToJson(this.params));
                response = OkHttpUtil.post(HttpRequstUrl.MACHINE_StockGoods_URL, JsonUtil.mapToJson(this.params));
/*              被移动至JsonDataParse的getArrayList 方法中
                JSONObject jsonObject = new JSONObject(response.toString());
                JSONObject jsonD = jsonObject.getJSONObject("d");
                totalNum = jsonD.getInt("t");
                curPage = jsonD.getInt("p");
                requestNum = jsonD.getInt("n");
                totalPage = totalNum / requestNum + (totalNum % requestNum > 0 ? 1 : 0);
                JSONArray jsonList = jsonD.getJSONArray("list");*/
                products = ItemStockProduct.bindProductList(JsonDataParse.getInstance().getArrayList(response.toString()), itemMachine);
                totalNum = JsonDataParse.getInstance().getTotalNum();
                curPage = JsonDataParse.getInstance().getCurPage();
                requestNum = JsonDataParse.getInstance().getRequestNum();
                totalPage = JsonDataParse.getInstance().getTotalPage();
                Log.e("MachineItemAddVHelper", "products.size==" + products.size() + "   ----totalPage=" + totalPage);
                Log.e("response", response.toString());
                return true;
            } catch (IOException e) {
                Log.e("erro", e.toString());
                if (dialog != null) {
                    dialog.dismiss();
                }
                err = RequstTips.getErrorMsg(e.getMessage());
            } catch (JSONException e) {
                Log.e("erro", e.toString());
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

                //   adapter.notifyDataSetChanged();
                // ListViewForScrollView.setListViewHeightBasedOnChildren(listView, scrollView);
                if (linearLayout != null) {
                    Log.d("-----------------", "ChildCount=" + linearLayout.getChildCount());
                    linearLayout.removeAllViews();
                    itemProducts.clear();
                }
                itemProducts.addAll(products);
                for (int i = 0; i < itemProducts.size(); i++) {

                    View view = LayoutInflater.from(context).inflate(R.layout.machine_detail_prod_list_item, linearLayout, false);
                    TextView productName = (TextView) view.findViewById(R.id.productName);
                    TextView productPrice = (TextView) view.findViewById(R.id.productPrice);
                    TextView productSpecPrice = (TextView) view.findViewById(R.id.productSpecPrice);
                    TextView timeLimit = (TextView) view.findViewById(R.id.timeLimit);
                    productName.setText(itemProducts.get(i).goodsName);
                    productPrice.setText(itemProducts.get(i).price + "");
                    if ("".equals(itemProducts.get(i).activityPrice) || "null".equals(itemProducts.get(i).activityPrice)) {
                        productSpecPrice.setText("无");
                    } else {
                        productSpecPrice.setText(itemProducts.get(i).activityPrice + "");
                    }
                    if (itemProducts.get(i).residueStorageTime != null) {
                        String[] secondToDate = SecondToDate.formatLongToTimeStr(Long.valueOf(itemProducts.get(i).residueStorageTime));
                        timeLimit.setText(secondToDate[0] + "天" + secondToDate[1] + "时" + secondToDate[2] + "分" + secondToDate[3] + "秒");
                    }
                    linearLayout.addView(view);
                    if (stocksPageHelper != null) {
                        stocksPageHelper.bindDatas(totalPage, totalNum);
                    }
                }

            } else {
                Log.e("request error :", response + "");
                if (context != null)
                    Toast.makeText(context, err, Toast.LENGTH_SHORT).show();
            }
            if (dialog != null) {
                dialog.dismiss();
            }
        }


    }
}
