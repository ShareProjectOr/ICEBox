package example.jni.com.coffeeseller.model;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.views.customviews.BuyDialog;

/**
 * Created by WH on 2018/4/30.
 */

public class Help implements View.OnClickListener {
    private View view;
    private Context context;
    private BuyDialog buyDialog;
    private ImageView cancle;

    public Help(Context context, BuyDialog dialog) {
        this.context = context;
        this.buyDialog = dialog;
        init();
    }

    private void init() {
        initView();
    }

    private void initView() {
        view = LayoutInflater.from(context).inflate(R.layout.help, null);
        cancle = (ImageView) view.findViewById(R.id.cancel);
        cancle.setOnClickListener(this);
    }

    public View getView() {
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:

                if (buyDialog != null) {
                    buyDialog.disDialog();
                }
                break;
        }
    }
}
