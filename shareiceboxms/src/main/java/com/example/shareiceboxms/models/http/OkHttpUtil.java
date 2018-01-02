package com.example.shareiceboxms.models.http;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by WH on 2017/12/6.
 */

public class OkHttpUtil {
    private static final OkHttpClient mOkHttpClient = new OkHttpClient();

    static {
        mOkHttpClient.setConnectTimeout(20, TimeUnit.SECONDS);
    }

    /**
     *      * 不开启异步线程。
     *      * @param request
     *      * @return
     *      * @throws IOException
     *      
     */
    public static Response execute(Request request) throws IOException {
        return mOkHttpClient.newCall(request).execute();
    }

    /*
    *开启异步线程访问网络
    * @param request
    * @param responseCallback
    * */
    public static void enquene(Request request, Callback responseCallback) {
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }

    /*
    *开启异步线程访问网络, 且不在意返回结果（实现空callback）
    * @param request
    * */
    public static void enquene(Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }

    /*
    * 从服务器拉取数据，不传递任何参数
    * param url
    * */
    public static String getStringFromServer(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            return request.body().toString();
        } else {
            throw new IOException("Unexpected code :" + response);
        }
    }

    private static final String CHARSET_NAME = "UTF-8";

    /**
     *  这里使用了HttpClinet的API。只是为了方便
     *  @param params
     *  @return
     */
    public static String formatParams(List<BasicNameValuePair> params) {
        return URLEncodedUtils.format(params, CHARSET_NAME);
    }

    /**
     *      * 为HttpGet 的 url 方便的添加多个name value 参数。
     *      * @param url
     *      * @param params
     *      * @return
     *      
     */
    public static String attachHttpGetParams(String url, List<BasicNameValuePair> params) {
        return url + "?" + formatParams(params);
    }

    /**
     *      * 为HttpGet 的 url 方便的添加1个name value 参数。
     *      * @param url
     *      * @param name
     *      * @param value
     *      * @return
     *      
     */
    public static String attachHttpGetParams(String url, String name, String value) {
        return url + "?" + name + "=" + value;
    }

    /*
    * POST提交Json数据
    * @param url
    * @param json
    * */
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static String post(String url, String json) throws IOException {
//        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request request = new Request.Builder().url(url).addHeader("content-type", "application/json;charset:utf-8").post(requestBody).build();
        Response response = mOkHttpClient.newCall(request).execute();//mOkHttpClient
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            Log.e("Unexpected code:", response + "");
            Log.e("Unexpected code:", response.code() + "");
            throw new IOException("Unexpected code :" + response.code());
        }
    }

    /*
    * POST提交键值对
    * @param url
    * @param params
    * */
    public static String post(String url, Map<String, String> params) throws IOException {
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        for (String key : params.keySet()) {
            formEncodingBuilder.add(key, params.get(key));
        }
        RequestBody requestBody = formEncodingBuilder.build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            return response.body().toString();
        } else {
            throw new IOException("Unexpected code :" + response);
        }
    }
}
