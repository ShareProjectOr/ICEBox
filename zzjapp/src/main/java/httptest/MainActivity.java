package httptest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        Button button = new Button(this);
        final EditText editText = new EditText(this);
        button.setText("�������");
        layout.addView(button);
        layout.addView(editText);
        setContentView(layout);
        new Test1("", HttpMethed1.GET, new Test1.SuccessResult() {
            @Override
            public void OnSuccess(String result) {

            }
        }, new Test1.FailedResult() {
            @Override
            public void OnFailed() {

            }
        }, "tsy", "ddddd", "P", "1", "N", "10");
        // setContentView(R.layout.activity_main);
        // HttpMethod httpMethod = new
        // HttpMethod("http://115.28.81.78:8080/lero/goods/get.do",
        // GetOrPost.GET, new SuccessResult() {
        //
        // public void OnSuccess(String result) {
        // Log.e("TAG", result);
        //
        // }
        // }, new FaildResult() {
        //
        // @Override
        // public void OnFailed() {
        // // TODO Auto-generated method stub
        //
        // }
        // }, "sn","991ed3e9-067e-4f2c-8fe2-fd41887fa2d2");

        // HttpMothed httpMothed = new
        // HttpMothed("http://115.28.81.78:8080/lero/goods/get.do", 1, new
        // SuccessCallback() {
        //
        // @Override
        // public void onSuccess(String result) {
        // // TODO Auto-generated method stub
        // Log.e("TAG", result);
        // }
        // }, new FailCallback() {
        //
        // @Override
        // public void onFail() {
        // // TODO Auto-generated method stub
        // Log.e("TAG", "111111111111");
        // }
        // }, "sn","991ed3e9-067e-4f2c-8fe2-fd41887fa2d2");
    }

}
