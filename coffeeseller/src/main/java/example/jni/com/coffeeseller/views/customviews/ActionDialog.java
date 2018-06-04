package example.jni.com.coffeeseller.views.customviews;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cof.ac.inter.DebugAction;
import example.jni.com.coffeeseller.R;

/**
 * Created by zxx on 2018/5/14.
 * <p>
 * 调试动作 输入参数 弹窗
 */

public class ActionDialog extends Dialog {

    Context context;
    private List<HashMap<String, Object>> listAction = new ArrayList<>();
    HashMap<String, Object> mapAction;
    int position;
    TextView tvHint;
    EditText inputParam;
    DebugAction action;

    private IActionParamConfirm confirmListener;

    public ActionDialog(final Context context, final HashMap map, final int position, IActionParamConfirm listener) {
        super(context);
        this.context = context;
        mapAction = map;
        this.confirmListener = listener;
        this.position = position;
        setCanceledOnTouchOutside(true);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_action_param, null);
        inputParam = (EditText) contentView.findViewById(R.id.input_param);
        tvHint = (TextView) contentView.findViewById(R.id.tv_param_hint);
        contentView.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    parseParam();
                    confirmListener.onConfirm(mapAction, position);
                    dismiss();
                } catch (Exception e) {
                    Toast.makeText(context, "请输入整数", Toast.LENGTH_SHORT).show();
                }

            }
        });
        initHint();
        setTitle((String) mapAction.get("name"));
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(contentView, params);
    }

    private void parseParam() {
        int input = Integer.parseInt(inputParam.getText().toString());
        if (action != DebugAction.CLEAR_MODULE) {
            if (input > 255) {
                int v1 = input % 256;
                int v2 = input / 256;
                mapAction.put("param2", v2);
                mapAction.put("param1", v1);
                return;
            }else{
                mapAction.put("param1", input);
            }
        }

    }

    private void initHint() {
        action = (DebugAction) mapAction.get("action");
        String hint = "";
        switch (action) {
            case DEVIDE_CUP:
                tvHint.setText("分杯测试数量（单位：个 ：");
                inputParam.setText("1");
                break;
            case OUT_HOTWATER:
                tvHint.setText("出热水量（单位：0.1ml）：");
                inputParam.setText("400");
                break;
//            case OUT_INGREDIENT:
//                tvHint.setText("料盒掉粉时间（单位:0.1s,取值范围：1-25）：");
//                inputParam.setText("10");
//                break;
//            case CRUSH_BEAN:
//                tvHint.setText("分杯测试数量（个）：");
//                inputParam.setText("1");
//                break;

            case CUP_MOVE_SYSTEM:
                tvHint.setText("运杯测试数量（单位：个：");
                inputParam.setText("1");
                break;
            case CLEAR_MODULE:
                tvHint.setText("清洗次数（单位：次，取值1-255）：");
                inputParam.setText("1");
                break;
        }
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        //p.height = (int) (d.getHeight() * 0.6); // 改变的是dialog框在屏幕中的位置而不是大小
        p.width = (int) (d.getWidth() * 0.3); // 宽度设置为屏幕的0.65
        window.setAttributes(p);
    }

    public interface IActionParamConfirm {
        void onConfirm(HashMap map, int position);
    }
}


