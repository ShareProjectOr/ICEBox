package example.jni.com.coffeeseller.views.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cof.ac.inter.CoffMsger;
import cof.ac.inter.DebugAction;
import cof.ac.inter.Result;
import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.factory.FragmentEnum;
import example.jni.com.coffeeseller.utils.MyLog;
import example.jni.com.coffeeseller.views.activities.HomeActivity;
import example.jni.com.coffeeseller.views.customviews.ActionDialog;

/**
 * Created by zxx on 2018/5/14.
 * 机器调试动作界面
 */

public class DebugActionFragment extends BasicFragment implements ActionDialog.IActionParamConfirm {
    private TextView tvMessage;
    private GridView gridTest;
    Context context;
    Adapter adapter;
    private ActionDialog paramDialog;
    Set<DebugAction> setAction = new HashSet<>();
    private List<HashMap<String, Object>> listAction = new ArrayList<>();
    @Nullable
    private HomeActivity homeActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = getContext();
        View contentView = LayoutInflater.from(context).inflate(R.layout.fragment_debug_action, null);
        contentView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.replaceFragment(FragmentEnum.DebugActionFragment, FragmentEnum.ConfigFragment);
            }
        });
        tvMessage = (TextView) contentView.findViewById(R.id.tv_message);
        gridTest = (GridView) contentView.findViewById(R.id.gridview_test);
        homeActivity = HomeActivity.getInstance();
        initGridView();
        return contentView;
    }

    private void initGridView() {
        listAction.clear();
        gridTest.setNumColumns(4);
        for (DebugAction action : DebugAction.values()) {
            String text = "";
            switch (action) {
                case WASH_MACHINE:
                    text = context.getString(R.string.wash_machine);
                    createItem(text, action, 0, 0);
                    break;
//                case STERILIZE_MACHINE:
//                    text = context.getString(R.string.sterilize_machine);
//                    createItem(text, action, 0, 0);
//                    break;
                case DEVIDE_CUP:
                    text = context.getString(R.string.divide_cup);
                    createItem(text, action, 1, 0);
                    setAction.add(action);
                    break;
                case OUT_HOTWATER:
                    setAction.add(action);
                    text = context.getString(R.string.out_hot_water);
                    createItem(text + "", action, 100, 0);
                    break;
                case OPEN_DOOR:
                    text = context.getString(R.string.open_door);
                    createItem(text, action, 0, 1);
                    text = context.getString(R.string.close_door);
                    createItem(text, action, 0, 0);
                    break;
                case RESET:
                    text = context.getString(R.string.reset);
                    createItem(text, action, 0, 0);
                    break;
                case FLUSH_CTR:
                    text = getString(R.string.brew_up);
                    createItem(text, action, 0, 2);
                    text = getString(R.string.brew_out);
                    createItem(text, action, 0, 0);
                    text = getString(R.string.brew_test);
                    createItem(text, action, 0, 1);

                    break;
                case OUT_INGREDIENT:

                    for (int i = 0; i < 5; i++) {
                        text = String.format(getString(R.string.container_test), i + 1);
                        //   text = getString(R.string.container_test) + (i + 1) + "掉粉2秒";
                        createItem(text, action, i + 1, 20);
                    }
                    break;
                case CRUSH_BEAN:
                    text = context.getString(R.string.crush_bean);
                    createItem(text, action, 0, 20);
                    break;
                case MOVE_TRAY:
                    text = context.getString(R.string.move_tray_out);
                    createItem(text, action, 0, 1);
                    text = context.getString(R.string.move_tray_in);
                    createItem(text, action, 0, 0);
                    break;
//                case DOWN_TEA:
//                    text = context.getString(R.string.down_tea);
//                    createItem(text + "(2秒)", action, 0, 20);
//                    break;
                case CUP_MOVE_SYSTEM:
                    setAction.add(action);
                    text = context.getString(R.string.move_cup);
                    createItem(text, action, 1, 0);
                    //      createItem(text + "H", action, 0, 1);
                    break;
                case CTR_LITTLEDOOR:
                    text = context.getString(R.string.control_little_door_open);
                    createItem(text, action, 0, 1);
                    text = context.getString(R.string.control_little_door_close);
                    createItem(text, action, 0, 0);
                    break;
                case CLEAR_WATERBOX:
                    text = context.getString(R.string.clear_water_box);
                    createItem(text, action, 0, 0);
                    break;
                case CLEAR_POT:
                    text = context.getString(R.string.clear_pot);
                    createItem(text, action, 0, 0);
                    break;
//                case TEST_AIRPUMP:
//                    text = context.getString(R.string.air_pump_test);
//                    createItem(text, action, 0, 0);
//                    break;
                case CLEAR_MODULE:
                    setAction.add(action);
                    text = getString(R.string.clear_brew);
                    createItem(text, action, 0xAA, 1);
//                    text = "清洗泡茶器";
//                    createItem(text, action, 0xCC, 1);
                    for (int i = 0; i < 3; i++) {
                        text = getString(R.string.clear_mix) + (i + 1);
                        createItem(text, action, i+1, 1);
                    }
                    break;
            }

        }
        adapter = new Adapter();
        gridTest.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        gridTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelection(position);
                HashMap<String, Object> map = listAction.get(position);
                DebugAction action = (DebugAction) map.get("action");
                if (setAction.contains(action)) {
                    paramDialog = new ActionDialog(getContext(), map, position, DebugActionFragment.this);
                    paramDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            paramDialog = null;
                        }
                    });
                    paramDialog.show();
                    return;
                }
                tvMessage.setText(listAction.get(position).get("name").toString());
                tvMessage.setText("");
                MyLog.d("CoffeeDebug", "debugaction name:+" + map.get("name") + "action:" + ((DebugAction) map.get("action")).name() + " param1:" + map.get("param1") + " param2:" + map.get("param2"));
                Result result = CoffMsger.getInstance().Debug((DebugAction) map.get("action"), (int) map.get("param1"), (int) map.get("param2"));
                String message = result.success() ? getString(R.string.success) : (getString(R.string.failure) + result.getErrDes());
                tvMessage.setText((String) listAction.get(position).get("name") + message);
            }
        });
    }

    private void createItem(String name, DebugAction action, int param1, int param2) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("action", action);
        map.put("param1", param1);
        map.put("param2", param2);
        listAction.add(map);
    }

    @Override
    public void onConfirm(HashMap map, int position) {
        tvMessage.setText(listAction.get(position).get("name").toString());
        tvMessage.setText("");
        Result result = CoffMsger.getInstance().Debug((DebugAction) map.get("action"), (int) map.get("param1"), (int) map.get("param2"));
        MyLog.d("CoffeeDebug", "debugaction name:+" + map.get("name") + "action:" + ((DebugAction) map.get("action")).name() + " param1:" + map.get("param1") + " param2:" + map.get("param2"));
        String message = result.success() ? getString(R.string.success) : (getString(R.string.failure) + result.getErrDes());
        tvMessage.setText((String) listAction.get(position).get("name") + message);
    }

    class Adapter extends BaseAdapter {

        private int selection = -1;

        public void setSelection(int index) {
            selection = index;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return listAction.size();
        }

        @Override
        public Object getItem(int position) {
            return listAction.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View contentView = LayoutInflater.from(context).inflate(R.layout.item_test, null);
            TextView tvTest = (TextView) contentView.findViewById(R.id.tv_test_name);
            tvTest.setText((String) listAction.get(position).get("name"));

            tvTest.setSelected(position == selection);
            return contentView;
        }
    }

}
