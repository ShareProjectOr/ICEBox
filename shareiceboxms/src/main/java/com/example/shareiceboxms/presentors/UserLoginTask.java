package com.example.shareiceboxms.presentors;

import android.os.AsyncTask;
import android.util.Log;

import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.contants.HttpRequstUrl;
import com.example.shareiceboxms.models.contants.RequstTips;
import com.example.shareiceboxms.models.http.JsonUtil;
import com.example.shareiceboxms.models.http.OkHttpUtil;
import com.example.shareiceboxms.views.activities.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/10.
 */

//登录异步任务
public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private final String mEmail;
    private final String mPassword;
    private String response;
    private String err = RequstTips.Server_ERROR_404;
    private JSONObject userJson;
    private loginStateCallBack mloginStateCallBack;

    public UserLoginTask(String email, String password) {
        mEmail = email;
        mPassword = password;
//            msgMap = new HashMap<>();
        userJson = null;
    }

    public void setLoginStateCallBack(loginStateCallBack loginStateCallBack) {
        if (loginStateCallBack != null) {
            mloginStateCallBack = loginStateCallBack;
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        Map<String, Object> body = new HashMap<>();
        body.put("loginAccount", mEmail);
        body.put("loginPassword", mPassword);

        try {

            response = OkHttpUtil.post(HttpRequstUrl.LOGIN_URL, JsonUtil.mapToJson(body));
            Log.e("login_response", response);
            userJson = new JSONObject(response);
            err = userJson.getString("err");
            Log.e("response", response.toString());
            if (err.equals("") || err.equals("null")) {
                return true;
            }
        } catch (IOException e) {
            Log.e("response", e + "");
            err = RequstTips.getErrorMsg(e.getMessage());
        } catch (JSONException e) {
            err = RequstTips.JSONException_Tip;
        }
        return false;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            mloginStateCallBack.LoginSuccessed(response);
         /*   /loginActivity.jumpActivity(HomeActivity.class, null);
            PerSonMessage.loginPassword = mPassword;
            PerSonMessage.bindMessage(response);*/
        } else {
            mloginStateCallBack.loginFailed(err);
          /*  passEdit.setError(err);
            passEdit.requestFocus();*/
        }
    }

    @Override
    protected void onCancelled() {
        //  this = null;

    }

    public interface loginStateCallBack {
        void loginFailed(String err);

        void LoginSuccessed(String response);
    }

}