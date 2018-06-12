package ViewUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zhazhijiguanlixitong.R;

import java.lang.reflect.Field;

import adapter.MyAdapter;

/**
 * Created by Administrator on 2017/4/11 0011.
 */

public class DiaLogUtil {

    public static void IsCommitDialog(DialogInterface dialog, boolean b) {
        try {
            Field field = dialog.getClass()
                    .getSuperclass().getDeclaredField(
                            "mShowing");
            field.setAccessible(true);
            // 将mShowing变量设为false，表示对话框已关闭
            field.set(dialog, b);
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //加载数据时的dialog
    public static Dialog createLoadingDialog(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_loding, null);
        ProgressBar bar = (ProgressBar) view.findViewById(R.id.progressBar);
        TextView msgtext = (TextView) view.findViewById(R.id.msg);
        if (msg != null) {
            msgtext.setText(msg);
        }
        return new AlertDialog.Builder(context).setView(view).create();
    }

    //统计管理的dialog
    public static Dialog createLoadingDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_loding, null);
        return new AlertDialog.Builder(context).setView(view).create();
    }

}
