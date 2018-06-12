package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhazhijiguanlixitong.Activity.AddressActivity;
import com.example.zhazhijiguanlixitong.Activity.CreateActivity;
import com.example.zhazhijiguanlixitong.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import AddressCodeUtil.ProvenceAndCode;
import AddressCodeUtil.entity.ItemArea;
import AddressCodeUtil.entity.ItemBase;
import AddressCodeUtil.entity.ItemCity;
import AddressCodeUtil.entity.ItemProvence;
import adapter.AddressAdapter;

import httputil.Constance;
import jxl.read.biff.BiffException;

/**
 * Created by WH on 2017/7/24.
 */

public class FragmentAddress extends Fragment {
    private View cacheView;
    private ListView mListView;
    private AddressAdapter mAdapter;
    private List<ItemProvence> mProvences;
    private List<ItemBase> mItemAddress;
    private List<ItemCity> mCities;
    private List<ItemArea> mAreas;
    private StringBuffer mAddress;
    private int flags;

    public static FragmentAddress newInstance() {
        FragmentAddress contentFragment = new FragmentAddress();
        return contentFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (cacheView == null) {
            cacheView = inflater.inflate(R.layout.activity_choose_provence, null);
        }
        ViewGroup parent = (ViewGroup) cacheView.getParent();
        if (parent != null) {
            parent.removeView(cacheView);
        }
        initView();
        return cacheView;
    }

    private void initView() {
        mListView = (ListView) cacheView.findViewById(R.id.address_list);
        mAddress = new StringBuffer();
        mItemAddress = new ArrayList<>();
        try {
            mProvences = new ProvenceAndCode().readFromExcel(getActivity(), Constance.ADDRESS_EXCEL_NAME);
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mItemAddress.addAll(mProvences);
        mAdapter = new AddressAdapter(getActivity(), mItemAddress);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取市区的数据，然后更新list
                TextView textView = (TextView) view.findViewById(R.id.name);

                if (mAdapter.getItem(position) instanceof ItemProvence) {
                    mCities = ((ItemProvence) mAdapter.getItem(position)).getCites();
                    mItemAddress.removeAll(mProvences);
                    mItemAddress.addAll(mCities);
                    mAdapter.notifyDataSetChanged();
                    mAddress.append(textView.getText().toString());
                    mAddress.append("-");
                    return;
                }
                if (!(mAdapter.getItem(position) instanceof ItemProvence) && (mAdapter.getItem(position) instanceof ItemCity)) {
                    mAreas = ((ItemCity) mAdapter.getItem(position)).getAreas();
                    mItemAddress.removeAll(mCities);
                    mItemAddress.addAll(mAreas);
                    mAdapter.notifyDataSetChanged();
                    mAddress.append(textView.getText().toString());
                    mAddress.append("-");
                    return;
                }
                if (!(mAdapter.getItem(position) instanceof ItemCity) && (mAdapter.getItem(position) instanceof ItemArea)) {
                    mAddress.append(textView.getText().toString());
                    Intent intent = new Intent();
                    //510703|四川-绵阳-涪城|xxx街道(自定义)
                    intent.putExtra("address", mAddress.toString());
                    intent.putExtra("id", ((ItemArea) mAdapter.getItem(position)).getId());
                    intent.setClass(getActivity(), CreateActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                    mItemAddress.removeAll(mAreas);
                    mAdapter.notifyDataSetChanged();
                    return;
                }
            }
        });
        mListView.setAdapter(mAdapter);
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public AddressActivity.AddressCallBack getCallBack() {
        return new AddressActivity.AddressCallBack() {

            @Override
            public boolean onKeyDown() {
                if (mItemAddress == null) {
                    getActivity().finish();
                    return true;
                }
                if (mItemAddress.containsAll(mProvences)) {
                    getActivity().finish();
                    return true;
                }
                if ((!(mItemAddress.containsAll(mProvences))) && (mItemAddress.containsAll(mCities))) {
                    if (mProvences != null) {
                        mItemAddress.removeAll(mCities);
                        mItemAddress.addAll(mProvences);
                        mAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
                if ((!(mItemAddress.containsAll(mCities))) && (mItemAddress.containsAll(mAreas))) {
                    if (mCities != null) {
                        mItemAddress.removeAll(mAreas);
                        mItemAddress.addAll(mCities);
                        mAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
                return true;
            }
        };
    }
}
