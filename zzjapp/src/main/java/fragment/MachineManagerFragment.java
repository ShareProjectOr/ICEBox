package fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.zhazhijiguanlixitong.Activity.AddMachineActivity;
import com.example.zhazhijiguanlixitong.Activity.PersonalInfoActivity;
import com.example.zhazhijiguanlixitong.Activity.SearchExceptionActivity;
import com.example.zhazhijiguanlixitong.Activity.SearchMachineActivity;
import com.example.zhazhijiguanlixitong.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ViewUtils.ObjectAnimatorUtil;
import ViewUtils.Rotate3dAnimation;
import adapter.UserListAdapter;
import contentprovider.UserMessage;
import httputil.Constance;
import httputil.ConstanceMethod;


public class MachineManagerFragment extends Fragment implements OnItemClickListener {
    private View cacheView;
    private String NameArray[];
    private List<String> MachineManagerCodeArray = new ArrayList<>();
    private AlertView mAlertView;
    private TextView mNeedDealEx;
    private UserListAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (cacheView == null) {
            cacheView = inflater.inflate(R.layout.fragment_machine_manager, null);
            initview();
            initListener();

        }
        ViewGroup parent = (ViewGroup) cacheView.getParent();
        if (parent != null) {
            parent.removeView(cacheView);
        }
        return cacheView;
    }

    private void initListener() {

    }


    @Override
    public void onStart() {
        super.onStart();
    }

    private void initview() {
        ListView machineList = (ListView) cacheView.findViewById(R.id.machine_list);
        mNeedDealEx = (TextView) cacheView.findViewById(R.id.needdealex);
        mAdapter = new UserListAdapter(getActivity(), getList());
        machineList.setAdapter(mAdapter);
        machineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = (Map<String, Object>) mAdapter.getItem(position);
                int code = (int) map.get("code");
                switch (code) {
                    case 0:
                        ConstanceMethod.startIntent(getActivity(), SearchMachineActivity.class, null);
                        break;
                    case 1:
                        ConstanceMethod.startIntent(getActivity(), SearchExceptionActivity.class, null);
                        break;
                    case 2:
                        if (UserMessage.getManagerType().equals("2") || UserMessage.getManagerType().equals("3")) {
                            new AlertView(getString(R.string.tishi), getString(R.string.meiyouquanxianjiazaijiqi), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                            return;
                        }
                        ConstanceMethod.startIntent(getActivity(), AddMachineActivity.class, null);
                        break;
                    case 3:
                        switch (UserMessage.getManagerType()) {
                            case "2":
                                showAll();
                                break;
                            case "3":
                                RequestParams params = new RequestParams();
                                params.addBodyParameter("tsy", UserMessage.getTsy());
                                params.addBodyParameter("managerID", UserMessage.getManagerId());
                                GetAllException(params, Constance.GET_NEED_DEAL_EXCEPTION, 0);
                                break;
                            default:
                                new AlertView(getString(R.string.tishi), getString(R.string.wufahuoquyichangzhongshu), null, new String[]{getString(R.string.queding)}, null, getActivity(), AlertView.Style.Alert, null).setCancelable(true).show();
                                break;
                        }
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
        lists.add(createManager(getString(R.string.query_machine_list), R.drawable.query, 0));
        lists.add(createManager(getString(R.string.query_machine_fault_record), R.drawable.query, 1));
        lists.add(createManager(getString(R.string.add_machine), R.drawable.create, 2));
        lists.add(createManager(getString(R.string.get_fault_count_nodeal), R.drawable.num, 3));
        return lists;
    }

    private Map<String, Object> createManager(String text, Integer img, int code) {
        Map<String, Object> create = new HashMap<>();
        create.put("text", text);
        create.put("imgIcon", img);
        create.put("code", code);
        return create;
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

    private void showAll() {
        final Map<String, String> post = new HashMap<>();
        post.put("P", "1");
        post.put("activatedType", "1");
        post.put("W[managerType]", "3");
        post.put("W[agentID]", UserMessage.getManagerId());
        post.put("tsy", UserMessage.getTsy());
        if (MachineManagerCodeArray.size() > 1) {
            if (mAlertView.isShowing()) {
                mAlertView.dismiss();
            } else {
                mAlertView = new AlertView(null, null, getString(R.string.quxiao), null,
                        NameArray,
                        getActivity(), AlertView.Style.ActionSheet, this).setCancelable(true);
                mAlertView.show();
            }

            return;
        }
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), null, getString(R.string.jiazaijiqiguanliyuanlibiao), false, false);
        new AsyncTask<Void, Void, Boolean>() {
            String error;

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    JSONObject object = new JSONObject(httputil.HttpRequest.postString(
                            Constance.GET_USER_LIST_URL, post));
                    error = object.getString("err");
                    if (!error.equals("")) {
                        return false;
                    }

                    JSONObject d = object.getJSONObject("d");
                    String N = d.getString("t");
                    Map<String, String> post2 = new HashMap<>();
                    post2.put("P", "1");
                    post2.put("activatedType", "1");
                    post2.put("N", N);
                    post2.put("tsy", UserMessage.getTsy());
                    switch (UserMessage.getManagerType()) {
                        case "2":
                            post2.put("W[managerType]", "3");
                            post2.put("W[agentID]", UserMessage.getManagerId());
                            break;
                        default:
                            post2.put("W[managerType]", "2");
                            break;
                    }
                    JSONObject all = new JSONObject(httputil.HttpRequest.postString(Constance.GET_USER_LIST_URL, post2));
                    error = all.getString("err");
                    if (!error.equals("")) {
                        return false;
                    }
                    JSONArray L = all.getJSONObject("d").getJSONArray("l");
                    NameArray = new String[L.length() + 1];
                    NameArray[0] = getString(R.string.chakanzijide);
                    for (int i = 0; i < L.length(); i++) {
                        JSONObject itemobject = (JSONObject) L.opt(i);
                        MachineManagerCodeArray.add(itemobject.getString("managerID"));
                        NameArray[i + 1] = itemobject.getString("managerName");

                    }
                } catch (JSONException e) {
                    error = getString(R.string.fuwuqiqingqiucuowu) + e;
                    return false;
                } catch (IOException e) {
                    error = getString(R.string.wangluobugeili) + e;
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (dialog != null) {
                    dialog.dismiss();
                }

                mAlertView = new AlertView(null, null, getString(R.string.quxiao), null,
                        NameArray,
                        getActivity(), AlertView.Style.ActionSheet, MachineManagerFragment.this).setCancelable(true);
                mAlertView.show();

            }
        }.execute();
    }

    @Override
    public void onItemClick(Object o, int position) {
        mAlertView.dismiss();
        RequestParams params = new RequestParams();
        params.addBodyParameter("tsy", UserMessage.getTsy());
        switch (position) {
            case 0:
                params.addBodyParameter("managerID", UserMessage.getManagerId());
                break;
            case -1:
                mAlertView.dismiss();
                return;
            default:
                params.addBodyParameter("managerID", MachineManagerCodeArray.get(position - 1));
                break;
        }
        GetAllException(params, Constance.GET_NEED_DEAL_EXCEPTION, position);
    }

    private void GetAllException(RequestParams params, String url, final int positon) {
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(10000);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    Log.e("TAG", responseInfo.result);
                    JSONObject object = new JSONObject(responseInfo.result);
                    switch (positon) {
                        case 0:
                            mNeedDealEx.setText(UserMessage.getManagerName() + getString(R.string.weichuliyichangzhongshu) + object.getJSONObject("d").getString("needDeal"));
                            break;
                        default:
                            mNeedDealEx.setText(NameArray[positon] + getString(R.string.weichuliyichangzhongshu) + object.getJSONObject("d").getString("needDeal"));
                            break;
                    }

                } catch (JSONException e) {
                    Toast.makeText(getActivity().getApplication(), getString(R.string.cuowu_3) + e, Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getActivity().getApplication(), getString(R.string.wangluobugeili) + e + s, Toast.LENGTH_LONG).show();
            }
        });
    }
}
