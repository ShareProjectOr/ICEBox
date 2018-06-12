package customview;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.example.zhazhijiguanlixitong.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import AddressCodeUtil.ProvenceAndCode;
import AddressCodeUtil.entity.ItemArea;
import AddressCodeUtil.entity.ItemBase;
import AddressCodeUtil.entity.ItemCity;
import AddressCodeUtil.entity.ItemProvence;
import adapter.MenuDialogAdapter;
import adapter.MyPagerAdapter;
import httputil.Constance;
import jxl.read.biff.BiffException;

/**
 * 三级菜单列表
 * Created by LaiYingtang on 2016/5/25.
 */
public class ThreeMenuDialog extends SecondMenuDialog implements View.OnClickListener {

    private MyViewPager mViewPager; //滑动viewPager
    private LinearLayout mRootView;    //需要显示的layout
    private View view1, view2, view3;    //三个菜单级view
    private ListView mListView1, mListView2, mListView3;  //每个菜单列表都是一个listView
    private MenuDialogAdapter mListView1Adapter, mListView2Adapter, mListView3Adapter; //列表显示数据必须要的adapter
    private List<View> views = new ArrayList<View>(); //数据集合
    private MenuItemClickListener menuItemClickListener;   //接口，点击监听
    private List<ItemProvence> AllArray = new ArrayList<>();
    private List<ItemBase> ProvinceArray = new ArrayList<>();
    private List<ItemBase> CityArray = new ArrayList<>();
    private List<ItemBase> AreaArray = new ArrayList<>();
    private String province = "";
    private String city = "";
    private String area = "";
    private String code = "";
    private TextView choose_OK;
    private boolean istitlevisible = true;

    public ThreeMenuDialog(Context context, boolean b) {
        super(context);
        mContentView = LayoutInflater.from(context).inflate(R.layout.three_menu_dialog, null);
        //初始化控件及对控件操作
        istitlevisible = b;
        initViews();
        setTitle(context.getString(R.string.sanjiliebiao));//设置title
    }

    private void initViews() {
        mRootView = (LinearLayout) findViewById(R.id.rootview);
        mViewPager = (MyViewPager) findViewById(R.id.viewpager);
        choose_OK = (TextView) findViewById(R.id.choose_ok);
        choose_OK.setOnClickListener(this);
        if (istitlevisible) {
            choose_OK.setVisibility(View.VISIBLE);
        } else {
            choose_OK.setVisibility(View.GONE);
        }

        mViewPager.setOffscreenPageLimit(2);//显示2页
        try {
            AllArray = new ProvenceAndCode().readFromExcel(mContext, Constance.ADDRESS_EXCEL_NAME);
        } catch (BiffException | IOException e) {
            e.printStackTrace();
        }
        //为view加载layout,由于三个级的菜单都是只有一个listView，这里就只xie一个了
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view1 = inflater.inflate(R.layout.pager_number, null);
        view2 = inflater.inflate(R.layout.pager_number, null);
        view3 = inflater.inflate(R.layout.pager_number, null);

        //获取id
        mListView1 = (ListView) view1.findViewById(R.id.listview);
        mListView2 = (ListView) view2.findViewById(R.id.listview);
        mListView3 = (ListView) view3.findViewById(R.id.listview);

        //获取列表数据了
        // List<MenuData> list = mDictDataManager.getTripleColumnData(mContext, 0);
        final List<ItemBase> provinceArray = new ArrayList<>();
        if (ProvinceArray.size() != 0) {
            ProvinceArray.clear();
        }
        ProvinceArray.addAll(AllArray);
        /*try {
            provinceArray.addAll(new ProvenceAndCodeUtil().readFromExcel(mContext, Constance.ADDRESS_EXCEL_NAME));
        } catch (BiffException | IOException e) {
            e.printStackTrace();
        }*/
        //关联adapter
        mListView1Adapter = new MenuDialogAdapter(mContext, ProvinceArray);
        mListView1Adapter.setSelectedBackgroundResource(R.drawable.select_white);//选中时的背景
        mListView1Adapter.setHasDivider(false);
        mListView1Adapter.setNormalBackgroundResource(R.color.menudialog_bg_gray);//未选中
        mListView1.setAdapter(mListView1Adapter);


        views.add(view1);
        views.add(view2);//当前是第三级菜单，所以前面已经存在第一，第二菜单了

        //关联
        mViewPager.setAdapter(new MyPagerAdapter(views));
        //触屏监听
        mRootView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mViewPager.dispatchTouchEvent(event);
            }
        });

        //view1的listView的点击事件
        //点击事件
        mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListView1Adapter != null)
                    mListView1Adapter.setSelectedPos(position);
                if (mListView2Adapter != null)
                    mListView2Adapter.setSelectedPos(-1);

                if (views.contains(view3)) {
                    views.remove(view3);
                    mViewPager.getAdapter().notifyDataSetChanged();//立即更新adapter数据
                }
                //  ItemBase menuData = (ItemBase) parent.getItemAtPosition(position);//得到第position个menu子菜单

                //  List<ItemBase> cityArray = new ArrayList<ItemBase>();
                if (CityArray.size() != 0) {
                    CityArray.clear();
                }
                CityArray.addAll(((ItemProvence) ProvinceArray.get(position)).getCites());
                province = "";
                city = "";
                area = "";//初始化省市区
                province = ProvinceArray.get(position).getName();
                code = ProvinceArray.get(position).getId();
                if (mListView2Adapter == null) {
                    mListView2Adapter = new MenuDialogAdapter(mContext, CityArray);
                    mListView2Adapter.setNormalBackgroundResource(R.color.white);
                    mListView2.setAdapter(mListView2Adapter);
                } else {
                    mListView2Adapter.setData(CityArray);
                    mListView2Adapter.notifyDataSetChanged();
                }
            }
        });


        //view2的listView点击
        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListView2Adapter != null) {
                    mListView2Adapter.setSelectedPos(position);
                    mListView2Adapter.setSelectedBackgroundResource(R.drawable.select_gray);
                }

                if (views.contains(view3)) {
                    views.remove(view3);
                }

                //从第二级菜单的基础上加载第三级菜单
             /*   MenuData menuData = (MenuData) parent.getItemAtPosition(position);
                List<MenuData> list3 = mDictDataManager.getTripleColumnData(mContext, menuData.id);*/

                if (AreaArray.size() != 0) {
                    AreaArray.clear();
                }
                AreaArray.addAll(((ItemCity) CityArray.get(position)).getAreas());
                city = ((ItemCity) CityArray.get(position)).getName();
                code = ((ItemCity) CityArray.get(position)).getId();
                if (mListView3Adapter == null) {
                    mListView3Adapter = new MenuDialogAdapter(mContext, AreaArray);
                    mListView3Adapter.setHasDivider(false);
                    mListView3Adapter.setNormalBackgroundResource(R.color.menudialog_bg_gray);
                    mListView3.setAdapter(mListView3Adapter);
                } else {
                    mListView3Adapter.setData(AreaArray);
                    mListView3Adapter.notifyDataSetChanged();
                }

                //放入第三级菜单列表
                views.add(view3);
                mViewPager.getAdapter().notifyDataSetChanged();

                if (mViewPager.getCurrentItem() != 1) {
                    mViewPager.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mViewPager.setCurrentItem(1);//选一个
                        }
                    }, 300);
                }
            }
        });

        //最后就是第三级菜单的点击了
        mListView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                area = ((ItemArea) AreaArray.get(position)).getName();
                code = ((ItemArea) AreaArray.get(position)).getId();
                Bundle bundle = new Bundle();
                bundle.putString("prince", province);
                bundle.putString("city", city);
                bundle.putString("area", area);
                bundle.putString("code", code);
                setDictItemClickListener(bundle);
            }
        });

    }

    private void setDictItemClickListener(Bundle bundle) {
        if (menuItemClickListener != null) {
            menuItemClickListener.onMenuItemClick(bundle);
        }
        dismiss();
    }

    public final void setonItemClickListener(MenuItemClickListener listener) {
       menuItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("prince", province);
        bundle.putString("city", city);
        bundle.putString("area", area);
        bundle.putString("code", code);
        setDictItemClickListener(bundle);
    }

    public interface MenuItemClickListener {
       void onMenuItemClick(Bundle bundle);
    }

}
