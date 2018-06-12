package fragment;

import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.zhazhijiguanlixitong.Activity.HomeActivity;
import com.example.zhazhijiguanlixitong.R;

import adapter.HomeGridViewAdapter;
import contentprovider.UserMessage;


public class Fragment_Home extends BaseFragment implements TextToSpeech.OnInitListener {


    private View cacheView;
    private HomeActivity mHomeActivity;
    private TextToSpeech tts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (cacheView == null) {
            cacheView = inflater.inflate(R.layout.fragment_home, null);
            init();
        }
        ViewGroup parent = (ViewGroup) cacheView.getParent();
        if (parent != null) {
            parent.removeView(cacheView);
        }
        return cacheView;
    }


    private void init() {
        mHomeActivity = (HomeActivity) getActivity();
        GridView mGridView = (GridView) cacheView.findViewById(R.id.home_items);
        TextView name = (TextView) cacheView.findViewById(R.id.name);
        name.setText(UserMessage.getManagerName() + " " + getManagerTypeStr());

        HomeGridViewAdapter mAdapter;
        mAdapter = new HomeGridViewAdapter(mHomeActivity);

        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mHomeActivity.swicthFragmentItem(position);
            }
        });
    }


    private String getManagerTypeStr() {
        String str = null;
        switch (UserMessage.getManagerType()) {
            case "0":
                str = getString(R.string.superManager);
                break;
            case "1":
                str = getString(R.string.sysManager);
                break;
            case "2":
                str = getString(R.string.agent);
                break;
            case "3":
                str = getString(R.string.machineManager);
                break;
        }
        return str;
    }


    @Override
    public void onInit(int status) {

    }
}
