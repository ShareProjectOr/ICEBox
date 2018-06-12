package fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.zhazhijiguanlixitong.Activity.CreateActivity;
import com.example.zhazhijiguanlixitong.Activity.PersonalInfoActivity;
import com.example.zhazhijiguanlixitong.Activity.SearchUserActivity;
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

/**
 * Created by WH on 2017/7/14.
 */

public class UserManagerFragment extends BaseFragment {
    private View cacheView;
    private ListView mListView;
    private UserListAdapter mAdapter;
    private AnimatorSet animatorSet;
    private String managerType;
    private static String[] texts = {};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (cacheView == null) {
            cacheView = inflater.inflate(R.layout.fragment_user_manager, null);
            initView();
        }
        ViewGroup parent = (ViewGroup) cacheView.getParent();
        if (parent != null) {
            parent.removeView(cacheView);
        }

        return cacheView;
    }
    private void initView() {
        mListView = (ListView) cacheView.findViewById(R.id.user_list);
        mAdapter = new UserListAdapter(getActivity(), getList());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = (Map<String, Object>) mAdapter.getItem(position);
                int code = (int) map.get("code");
                switch (code) {
                    case 0:
                        ConstanceMethod.startIntent(getActivity(), CreateActivity.class, 1);
                        break;
                    case 1:
                        ConstanceMethod.startIntent(getActivity(), CreateActivity.class, 2);
                        break;
                    case 2:
                        ConstanceMethod.startIntent(getActivity(), SearchUserActivity.class, null);

                        break;
                    case 3:
                        ConstanceMethod.startIntent(getActivity(), CreateActivity.class, 3);
                        break;
                    case 4:
                        ConstanceMethod.startIntent(getActivity(), PersonalInfoActivity.class, 0);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private List<Map<String, Object>> getList() {
        List<Map<String, Object>> lists = new ArrayList<>();
        lists.add(createManager(getString(R.string.query), R.drawable.query, 2));
        switch (Integer.parseInt(UserMessage.getManagerType())) {
            case 0:
                lists.add(createManager(getString(R.string.create_sys_manager), R.drawable.create, 0));
                lists.add(createManager(getString(R.string.create_agent), R.drawable.create, 1));
                lists.add(createManager(getString(R.string.create_machine_manager), R.drawable.create, 2));
                break;
            case 1:
                lists.add(createManager(getString(R.string.create_agent), R.drawable.create, 1));
                lists.add(createManager(getString(R.string.create_machine_manager), R.drawable.create, 2));
                break;
            case 2:
                lists.add(createManager(getString(R.string.create_machine_manager), R.drawable.create, 3));
                break;
            case 3:
                break;
            default:
                break;
        }

        lists.add(createManager(getString(R.string.person_info), R.drawable.personal, 4));
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
