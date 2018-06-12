package com.example.zhazhijiguanlixitong.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.bigkoo.alertview.AlertView;
import com.example.zhazhijiguanlixitong.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.zackratos.ultimatebar.UltimateBar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ViewUtils.ObjectAnimatorUtil;
import contentprovider.UserMessage;
import httputil.Constance;
import httputil.ConstanceMethod;
import httputil.HttpRequest;
import httputil.LoginAnim;
import otherutils.Tip;


public class LoginActivity extends AppCompatActivity {


    private UserLoginTask mAuthTask = null;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    //  private LoginAnim loginAnim = new LoginAnim();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setImmersionBar();
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showloginbuttonAnima(view);
                attemptLogin();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    private void showloginbuttonAnima(View view) {
        ObjectAnimator AnimatorX = ObjectAnimatorUtil.ScaleAnimator(view, "X", 1f, 0.8f, 1f);
        ObjectAnimator AnimatorY = ObjectAnimatorUtil.ScaleAnimator(view, "Y", 1f, 0.8f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(AnimatorX).with(AnimatorY);
        animatorSet.setDuration(400);
        animatorSet.start();
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            //   loginAnim.init();
            //   Dialog dialog = loginAnim.createLoginAnim(this);
            //   dialog.show();
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private String response;
        private String err = "net_work_err";
        private String tsy;
        //        private Map<String, String> msgMap;
        private JSONObject userJson;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
//            msgMap = new HashMap<>();
            userJson = null;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Map<String, String> body = new HashMap<>();
            body.put("managerNum", mEmail);
            body.put("logonPassword", mPassword);
            try {
                Thread.sleep(1500);
                response = HttpRequest.postString(Constance.LOGIN_URL, body);
                JSONObject jsonResponse = new JSONObject(response);
                err = jsonResponse.getString("err");
                if (err.equals("")) {
                    JSONObject userMessage = jsonResponse.getJSONObject("d");
//                    msgMap = addMessageToMap(userMessage);
                    userJson = userMessage;
                    int managerType = Integer.parseInt(userJson.getString("managerType"));
                    if (managerType < 2) {
                        err = getString(R.string.denglutishi);
                        return false;
                    }
                    tsy = jsonResponse.getString("tsy");
                    return true;
                }

            } catch (InterruptedException e) {
                return false;
            } catch (IOException e) {
                err = getString(R.string.wangluobugeili);
                return false;
            } catch (JSONException e) {
                err = getString(R.string.fuwuqikaixiaocai_chongshi);
                return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            //loginAnim.destoryDialog();
            if (success) {
                UserMessage.setManagerPass(mPassword);
                UserMessage.setTsy(tsy);
                if (userJson != null) {
                    UserMessage.setUserMessage(userJson);
                }

                ConstanceMethod.startIntent(LoginActivity.this, HomeActivity.class, null);
                finish();
            } else {
                if (TextUtils.equals(err, getString(R.string.yonghubucunzai))) {
                    mEmailView.setError(err);
                    mEmailView.requestFocus();
                } else if (TextUtils.equals(err, getString(R.string.mimacuowu))) {
                    mPasswordView.setError(err);
                    mPasswordView.requestFocus();
                } else {
                    mPasswordView.setError(err);
                    mPasswordView.requestFocus();
                }

                //new AlertView("提示", err, null, new String[]{"确定"}, null, LoginActivity.this, AlertView.Style.Alert, null).setCancelable(true).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        private Map<String, String> addMessageToMap(JSONObject userMessage) throws JSONException {
            Map<String, String> msgMap = new HashMap<>();
            msgMap.put("managerID", userMessage.getString("managerID"));
            msgMap.put("managerNum", userMessage.getString("managerNum"));
            msgMap.put("managerType", userMessage.getString("managerType"));
            msgMap.put("activatedType", userMessage.getString("activatedType"));
            msgMap.put("managerName", userMessage.getString("managerName"));
            msgMap.put("managerTelephone", userMessage.getString("managerTelephone"));
            msgMap.put("managerEmail", userMessage.getString("managerEmail"));
            msgMap.put("managerAddress", userMessage.getString("managerAddress"));
            msgMap.put("managerCard", userMessage.getString("managerCard"));
            msgMap.put("managerCompany", userMessage.getString("managerCompany"));
            msgMap.put("agentID", userMessage.getString("agentID"));
            msgMap.put("agentName", userMessage.getString("agentName"));
            msgMap.put("managerBankAccount", userMessage.getString("managerBankAccount"));
            msgMap.put("companyAddress", userMessage.getString("companyAddress"));
            msgMap.put("companyNum", userMessage.getString("companyNum"));
            msgMap.put("companyNum", userMessage.getString("companyNum"));
            msgMap.put("divideProportion", userMessage.getString("divideProportion"));
            return msgMap;
        }
    }
}

