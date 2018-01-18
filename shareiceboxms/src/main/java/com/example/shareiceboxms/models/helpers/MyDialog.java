package com.example.shareiceboxms.models.helpers;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.beans.ItemPerson;
import com.example.shareiceboxms.models.beans.PerSonMessage;
import com.example.shareiceboxms.models.contants.ConstanceMethod;
import com.example.shareiceboxms.models.contants.Sql;
import com.example.shareiceboxms.models.factories.FragmentFactory;
import com.example.shareiceboxms.views.activities.HomeActivity;
import com.example.shareiceboxms.views.activities.LoginActivity;
import com.example.shareiceboxms.views.fragments.trade.CreateAccountFragment;
import com.example.shareiceboxms.views.fragments.trade.TradeAccountFragment;
import com.example.shareiceboxms.views.fragments.trade.TradeRecordDetailFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by WH on 2017/11/30.
 */

public class MyDialog {
    private Context context;

    public MyDialog(Context context) {
        this.context = context;
    }

    public AlertDialog getLogoutDialog(final HomeActivity activity) {
        return new AlertDialog.Builder(activity).setMessage("您确定退出吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog1, int which) {
                dialog1.dismiss();
                //清空缓存数据
                Dosql();
                ConstanceMethod.isFirstLogin(activity, true);
                ConstanceMethod.clearActivityFromStack(activity, LoginActivity.class);
                PerSonMessage.isexcit = true;
                activity.finish();
            }
        }).setNegativeButton("取消", null).create();

    }

    private void Dosql() {
        Sql sql = new Sql(context);
        sql.deleteAllContact(sql.getAllCotacts().size());
    }

    public static Dialog loadDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.load_dialog_layout);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams lp = window.getAttributes();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //6.0以下
            lp.width = Util.getScreenWidth(context) / 2;

        }
        window.setTitle(null);
        lp.alpha = 0.6f;
        window.setAttributes(lp);//设置透明度
        //设置圆矩背景，dialog是为矩形
        window.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.shape_loading_layout));
        dialog.setCancelable(true);//dialog可以取消
        dialog.setCanceledOnTouchOutside(false);//dialog框外不能取消

        return dialog;
    }

    public AlertDialog getMachineTeleControlDialog(String showMsg, final String url, final Map<String, Object> params) {
        return new AlertDialog.Builder(context).setMessage(showMsg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog1, int which) {
                TeleControlHelper.getInstance().setContext(context);
                TeleControlHelper.getInstance().getDatas(url, params);
            }
        }).setNegativeButton("取消", null).create();
    }

    public void showDialog(Dialog dialog) {
        dialog.show();
        if (dialog instanceof AlertDialog) {
            AlertDialog alertDialog = (AlertDialog) dialog;
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.blue));
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        }

    }

    /*
* 批量退款dialog
* */
    public static AlertDialog getRefundDialog(int count, final String wellBeRefundRFIDs, final HomeActivity activity, final TradeRecordDetailFragment tradeRecordDetailFragment) {
        View view;
        view = LayoutInflater.from(activity).inflate(R.layout.refund_more, null);
        final AlertDialog dialog = new AlertDialog.Builder(activity).setView(view).create();

        final TextView refundNum = (TextView) view.findViewById(R.id.refundNum);
        final EditText refundCause = (EditText) view.findViewById(R.id.refundCause);
        Button cancle = (Button) view.findViewById(R.id.cancle);
        Button commit = (Button) view.findViewById(R.id.commit);
        refundNum.setText(count + "");
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交退款操作
                String cause = refundCause.getText().toString().trim();
                if (cause.length() <= 0) {
                    Toast.makeText(activity, "退款原因必填", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.equals(wellBeRefundRFIDs, "")) {
                    Toast.makeText(activity, "没有可退款项！", Toast.LENGTH_SHORT).show();
                    return;
                }
                RefundMoreHelper.getInstance().setContext(activity);
                RefundMoreHelper.getInstance().setParams(wellBeRefundRFIDs, cause);
                RefundMoreHelper.getInstance().setTradeRecordDetailFragment(tradeRecordDetailFragment);
                RefundMoreHelper.getInstance().getDatas();
                if (dialog.isShowing()) {
                    dialog.dismiss();
//                    String refundResult = RefundMoreHelper.getInstance().isRefundSuccessed()?"退款成功":"退款失败！";
//                    Toast.makeText(activity, refundResult, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return dialog;
    }

    /*
    * 获取代理商数据
    * */
    public static AlertDialog getAgentsDialog(final HomeActivity activity, final List<ItemPerson> agents, final TradeAccountFragment tradeAccountFragment, final CreateAccountFragment.CreateAccountLisenner createAccountLisenner) {
        if (agents == null || agents.size() <= 0) {
            Toast.makeText(activity, "没有代理商，无法创建工单", Toast.LENGTH_SHORT).show();
            return null;
        }
        AlertDialog dialog;
        String[] agentNames = new String[agents.size()];
        final List<ItemPerson> agentIds = new ArrayList<ItemPerson>();
        for (int i = 0; i < agents.size(); i++) {
            ItemPerson person = agents.get(i);
            agentNames[i] = person.name;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //设置对话框的标题
        builder.setTitle("请选择结算代理商");
        builder.setMultiChoiceItems(agentNames, null, new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                agentIds.add(agents.get(which));
            }
        });
        final CreateAccountFragment fragment = new CreateAccountFragment();
        fragment.setCreateAccountLisenner(createAccountLisenner);
        builder.setNeutralButton("全选", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FragmentFactory.getInstance().getSavedBundle().putSerializable("CREATE_AGENTS_ACCOUNT", (Serializable) agents);
                dialog.dismiss();
                tradeAccountFragment.addFrameFragment(fragment);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(" 确 定 ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                FragmentFactory.getInstance().getSavedBundle().putSerializable("CREATE_AGENTS_ACCOUNT", (Serializable) agentIds);
                dialog.dismiss();
                tradeAccountFragment.addFrameFragment(fragment);
            }
        });
        //创建一个复选框对话框
        dialog = builder.create();
        return dialog;
    }
}
