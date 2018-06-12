package httputil;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.zhazhijiguanlixitong.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by WH on 2017/7/21.
 */

public class XUtils {
    private Context mContext;
    public Handler mHander;

    public static XUtils newInstance() {
        XUtils utils = new XUtils();
        return utils;
    }

    public void doPost(String url, RequestParams params) {
        final String httpUrl = url;
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 10);
        http.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Toast.makeText(mContext, R.string.tijiaocuowu_wangluoshibai, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> info) {
                Log.d("debug", "info= " + info.result);
                JSONObject message = new JsonParseUtil().parseD(info.result, mContext);
                if (message != null) {
                    if (mHander != null) {
                        Message msg = new Message();
                        if (httpUrl.equals(Constance.GET_MACHINE_LIST_URL)) {
                            msg.what = 0x123;
                        }
                        msg.obj = message;
                        mHander.sendMessage(msg);
                    }
                }
            }
        });
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void setHandler(Handler handler) {
        mHander = handler;
    }
}
