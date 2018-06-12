package fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.bigkoo.alertview.AlertView;
import com.example.zhazhijiguanlixitong.Activity.SearchAccountLogActivity;
import com.example.zhazhijiguanlixitong.Activity.TradeSearchActivity;
import com.example.zhazhijiguanlixitong.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ViewUtils.ObjectAnimatorUtil;
import ViewUtils.Rotate3dAnimation;
import adapter.UserListAdapter;
import contentprovider.UserMessage;
import httputil.ConstanceMethod;


public class TradeManagerFragment extends Fragment {
    private View cacheView;
    private ListView trade_list;
    private UserListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (cacheView == null) {
            cacheView = inflater.inflate(R.layout.fragment_trade_manager, null);
            init();
        }
        ViewGroup parent = (ViewGroup) cacheView.getParent();
        if (parent != null) {
            parent.removeView(cacheView);
        }
        return cacheView;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    private void ShowViewAnim(float star, float to, float end) {
        ObjectAnimator button2fade = ObjectAnimatorUtil.FadeInOutAnimator(cacheView, star, to, end);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(button2fade);
        animSet.setDuration(500);
        animSet.start();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            ShowViewAnim(0f, 0.5f, 1f);
            return new Rotate3dAnimation(-180f, 0f, 500);
        } else {
            ShowViewAnim(1f, 0.5f, 0f);
            return new Rotate3dAnimation(-180f, 0f, 500);
        }
    }

    private void init() {
        trade_list = (ListView) cacheView.findViewById(R.id.trade_list);
        adapter = new UserListAdapter(getActivity(), getList());
        trade_list.setAdapter(adapter);
        trade_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = (Map<String, Object>) adapter.getItem(position);
                int code = (int) map.get("code");
                switch (code) {
                    case 1:
                        switch (UserMessage.getManagerType()) {
                            case "3":
                                new AlertView(getString(R.string.tishi), getString(R.string.jiqiguanliyuan_bunengchakanjiaoyi), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                                break;
                            case "2":
                                new AlertView(getString(R.string.tishi), getString(R.string.dailishangbunengchakanruzhangjilu), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                                break;
                            default:
                                ConstanceMethod.startIntent(getActivity(), SearchAccountLogActivity.class, null);
                                break;
                        }

                        break;
                    case 0:
                        ConstanceMethod.startIntent(getActivity(), TradeSearchActivity.class, null);
                        break;

                }
            }
        });
    }

    private List<Map<String, Object>> getList() {
        List<Map<String, Object>> lists = new ArrayList<>();
        lists.add(createManager(getString(R.string.query_Trade_list), R.drawable.query, 0));
        lists.add(createManager(getString(R.string.query_AccountLog_list), R.drawable.num, 1));
        return lists;
    }

    private Map<String, Object> createManager(String text, Integer img, int code) {
        Map<String, Object> create = new HashMap<String, Object>();
        create.put("text", text);
        create.put("imgIcon", img);
        create.put("code", code);
        return create;
    }

}
