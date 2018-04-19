package example.jni.com.coffeeseller.contentprovider;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cof.ac.inter.ContainerConfig;
import cof.ac.inter.ContainerType;
import cof.ac.inter.WaterType;
import example.jni.com.coffeeseller.bean.Coffee;

/**
 * Created by Administrator on 2018/4/16.
 */

public class SingleMaterialLsit {
    private static SingleMaterialLsit mInstance;

    private List<Coffee> coffeeList;
    private String TAG = "SingleMaterialLsit";
    private Context mContext;
    private List<Coffee> youbaoCoffeeList = new ArrayList<>();

    public SingleMaterialLsit(Context context) {
        mContext = context;
        String arrayString = SharedPreferencesManager.getInstance(context).getCoffeeListArray();
        Log.d(TAG, arrayString);
        if (SharedPreferencesManager.getInstance(context).getCoffeeListArray().isEmpty()) {
            coffeeList = new ArrayList<>();
        } else {
            Log.d(TAG, SharedPreferencesManager.getInstance(mContext).getCoffeeListArray());
            JSONArray array = JSON.parseArray(SharedPreferencesManager.getInstance(mContext).getCoffeeListArray());
            coffeeList = array.toJavaList(Coffee.class);

        }
        setyoubaoList();
    }

    private void setyoubaoList() {
        Coffee moka, kabuqiluo, chunxiangniunai, qiaokeliniunai, xiangnongqiaokeli, yishinongka, natie, mieshikafei;
        if (youbaoCoffeeList.size() != 0) {
            youbaoCoffeeList.clear();
        }

        moka = new Coffee();
        kabuqiluo = new Coffee();
        chunxiangniunai = new Coffee();
        qiaokeliniunai = new Coffee();
        xiangnongqiaokeli = new Coffee();
        yishinongka = new Coffee();
        natie = new Coffee();
        mieshikafei = new Coffee();
        moka.setName("摩卡");
        moka.setPrice("0.1");
        List<ContainerConfig> mokaProcess = new ArrayList<>();
        ContainerConfig mokaProcess0 = new ContainerConfig();
        mokaProcess0.setContainer(ContainerType.NO_THREE);
        mokaProcess0.setWater_capacity(0);
        mokaProcess0.setWater_interval(0);
        mokaProcess0.setMaterial_time(4);
        mokaProcess0.setWater_type(WaterType.HOT_WATER);
        mokaProcess0.setRotate_speed(127);
        mokaProcess0.setStir_speed(127);
        mokaProcess.add(0, mokaProcess0);
        ContainerConfig mokaProcess1 = new ContainerConfig();
        mokaProcess1.setContainer(ContainerType.NO_FOUR);
        mokaProcess1.setWater_capacity(900);
        mokaProcess1.setWater_interval(0);
        mokaProcess1.setMaterial_time(43);
        mokaProcess1.setWater_type(WaterType.HOT_WATER);
        mokaProcess1.setRotate_speed(127);
        mokaProcess1.setStir_speed(127);
        mokaProcess.add(1, mokaProcess1);
        ContainerConfig mokaProcess2 = new ContainerConfig();
        mokaProcess2.setContainer(ContainerType.NO_FIVE);
        mokaProcess2.setWater_capacity(700);
        mokaProcess2.setWater_interval(0);
        mokaProcess2.setMaterial_time(39);
        mokaProcess2.setWater_type(WaterType.HOT_WATER);
        mokaProcess2.setRotate_speed(127);
        mokaProcess2.setStir_speed(127);
        mokaProcess.add(2, mokaProcess2);
        ContainerConfig mokaProcess3 = new ContainerConfig();
        mokaProcess3.setContainer(ContainerType.BEAN_CONTAINER);
        mokaProcess3.setWater_capacity(550);
        mokaProcess3.setWater_interval(0);
        mokaProcess3.setMaterial_time(15*2);
        mokaProcess3.setWater_type(WaterType.HOT_WATER);
        mokaProcess3.setRotate_speed(127);
        mokaProcess3.setStir_speed(127);
        mokaProcess.add(3, mokaProcess3);
        moka.setProcessList(mokaProcess);

        kabuqiluo.setName("卡布奇洛");
        kabuqiluo.setPrice("0.1");
        List<ContainerConfig> kabuqiluoProcessList = new ArrayList<>();
        ContainerConfig kabuqiluoProcess0 = new ContainerConfig();
        kabuqiluoProcess0.setContainer(ContainerType.NO_THREE);
        kabuqiluoProcess0.setWater_interval(0);
        kabuqiluoProcess0.setWater_capacity(0);
        kabuqiluoProcess0.setMaterial_time(4);
        kabuqiluoProcess0.setStir_speed(127);
        kabuqiluoProcess0.setRotate_speed(127);
        kabuqiluoProcess0.setWater_type(WaterType.HOT_WATER);
        kabuqiluoProcessList.add(0, kabuqiluoProcess0);
        ContainerConfig kabuqiluoProcess1 = new ContainerConfig();
        kabuqiluoProcess1.setContainer(ContainerType.NO_FOUR);
        kabuqiluoProcess1.setWater_interval(0);
        kabuqiluoProcess1.setWater_capacity(1450);
        kabuqiluoProcess1.setMaterial_time(68);
        kabuqiluoProcess1.setStir_speed(127);
        kabuqiluoProcess1.setRotate_speed(127);
        kabuqiluoProcess1.setWater_type(WaterType.HOT_WATER);
        kabuqiluoProcessList.add(1, kabuqiluoProcess1);
        ContainerConfig kabuqiluoProcess2 = new ContainerConfig();
        kabuqiluoProcess2.setContainer(ContainerType.BEAN_CONTAINER);
        kabuqiluoProcess2.setWater_interval(0);
        kabuqiluoProcess2.setWater_capacity(700);
        kabuqiluoProcess2.setMaterial_time(17*2);
        kabuqiluoProcess2.setStir_speed(127);
        kabuqiluoProcess2.setRotate_speed(127);
        kabuqiluoProcess2.setWater_type(WaterType.HOT_WATER);
        kabuqiluoProcessList.add(2, kabuqiluoProcess2);
        kabuqiluo.setProcessList(kabuqiluoProcessList);

        chunxiangniunai.setName("醇香牛奶");
        chunxiangniunai.setPrice("0.1");
        List<ContainerConfig> chunxiangniunaiProcessList = new ArrayList<>();
        ContainerConfig chunxiangniunaiProcess0 = new ContainerConfig();
        chunxiangniunaiProcess0.setContainer(ContainerType.NO_ONE);
        chunxiangniunaiProcess0.setWater_interval(0);
        chunxiangniunaiProcess0.setWater_capacity(1850);
        chunxiangniunaiProcess0.setMaterial_time(74);
        chunxiangniunaiProcess0.setRotate_speed(127);
        chunxiangniunaiProcess0.setStir_speed(127);
        chunxiangniunaiProcess0.setWater_type(WaterType.HOT_WATER);
        chunxiangniunaiProcessList.add(0, chunxiangniunaiProcess0);
        chunxiangniunai.setProcessList(chunxiangniunaiProcessList);

        qiaokeliniunai.setName("巧克力牛奶");
        qiaokeliniunai.setPrice("0.1");
        List<ContainerConfig> qiaokeliniunaiProcessList = new ArrayList<>();
        ContainerConfig qiaokeliniunaiProcess0 = new ContainerConfig();
        qiaokeliniunaiProcess0.setContainer(ContainerType.NO_FIVE);
        qiaokeliniunaiProcess0.setWater_interval(0);
        qiaokeliniunaiProcess0.setWater_capacity(1000);
        qiaokeliniunaiProcess0.setMaterial_time(39);
        qiaokeliniunaiProcess0.setRotate_speed(127);
        qiaokeliniunaiProcess0.setStir_speed(127);
        qiaokeliniunaiProcess0.setWater_type(WaterType.HOT_WATER);
        qiaokeliniunaiProcessList.add(0, qiaokeliniunaiProcess0);
        ContainerConfig qiaokeliniunaiProcess1 = new ContainerConfig();
        qiaokeliniunaiProcess1.setContainer(ContainerType.NO_FOUR);
        qiaokeliniunaiProcess1.setWater_interval(0);
        qiaokeliniunaiProcess1.setWater_capacity(800);
        qiaokeliniunaiProcess1.setMaterial_time(56);
        qiaokeliniunaiProcess1.setRotate_speed(127);
        qiaokeliniunaiProcess1.setStir_speed(127);
        qiaokeliniunaiProcess1.setWater_type(WaterType.HOT_WATER);
        qiaokeliniunaiProcessList.add(1, qiaokeliniunaiProcess1);
        qiaokeliniunai.setProcessList(qiaokeliniunaiProcessList);

        xiangnongqiaokeli.setName("香浓巧克力");
        xiangnongqiaokeli.setPrice("0.1");
        List<ContainerConfig> xiangnongqiaokeliProcessList = new ArrayList<>();
        ContainerConfig xiangnongqiaokeliProcess0 = new ContainerConfig();
        xiangnongqiaokeliProcess0.setContainer(ContainerType.NO_FIVE);
        xiangnongqiaokeliProcess0.setWater_interval(0);
        xiangnongqiaokeliProcess0.setWater_capacity(1850);
        xiangnongqiaokeliProcess0.setMaterial_time(71);
        xiangnongqiaokeliProcess0.setStir_speed(127);
        xiangnongqiaokeliProcess0.setRotate_speed(127);
        xiangnongqiaokeliProcess0.setWater_type(WaterType.HOT_WATER);
        xiangnongqiaokeliProcessList.add(0, xiangnongqiaokeliProcess0);
        xiangnongqiaokeli.setProcessList(xiangnongqiaokeliProcessList);

        yishinongka.setPrice("0.1");
        yishinongka.setName("意式浓卡");
        List<ContainerConfig> yishinongkaProcessList = new ArrayList<>();
        ContainerConfig yishinongkaProcess0 = new ContainerConfig();
        yishinongkaProcess0.setContainer(ContainerType.BEAN_CONTAINER);
        yishinongkaProcess0.setWater_interval(0);
        yishinongkaProcess0.setWater_capacity(1100);
        yishinongkaProcess0.setMaterial_time(17*2);
        yishinongkaProcess0.setRotate_speed(127);
        yishinongkaProcess0.setStir_speed(127);
        yishinongkaProcess0.setWater_type(WaterType.HOT_WATER);
        yishinongkaProcessList.add(0, yishinongkaProcess0);
        yishinongka.setProcessList(yishinongkaProcessList);

        natie.setName("拿铁");
        natie.setPrice("0.1");
        List<ContainerConfig> natieProcessList = new ArrayList<>();
        ContainerConfig natieProcess0 = new ContainerConfig();
        natieProcess0.setContainer(ContainerType.BEAN_CONTAINER);
        natieProcess0.setWater_interval(0);
        natieProcess0.setWater_capacity(600);
        natieProcess0.setMaterial_time(17*2);
        natieProcess0.setStir_speed(127);
        natieProcess0.setRotate_speed(127);
        natieProcess0.setWater_type(WaterType.HOT_WATER);
        natieProcessList.add(0, natieProcess0);
        ContainerConfig natieProcess1 = new ContainerConfig();
        natieProcess1.setContainer(ContainerType.NO_THREE);
        natieProcess1.setWater_interval(0);
        natieProcess1.setWater_capacity(0);
        natieProcess1.setMaterial_time(5);
        natieProcess1.setStir_speed(127);
        natieProcess1.setRotate_speed(127);
        natieProcess1.setWater_type(WaterType.HOT_WATER);
        natieProcessList.add(1, natieProcess1);
        ContainerConfig natieProcess2 = new ContainerConfig();
        natieProcess2.setContainer(ContainerType.NO_FOUR);
        natieProcess2.setWater_interval(0);
        natieProcess2.setWater_capacity(1500);
        natieProcess2.setMaterial_time(80);
        natieProcess2.setStir_speed(127);
        natieProcess2.setRotate_speed(127);
        natieProcess2.setWater_type(WaterType.HOT_WATER);
        natieProcessList.add(2, natieProcess2);
        natie.setProcessList(natieProcessList);

        mieshikafei.setName("美式咖啡");
        mieshikafei.setPrice("0.1");
        List<ContainerConfig> meishikafeiProcessList = new ArrayList<>();
        ContainerConfig meishikafeiProcess0 = new ContainerConfig();
        meishikafeiProcess0.setContainer(ContainerType.NO_THREE);
        meishikafeiProcess0.setWater_interval(0);
        meishikafeiProcess0.setWater_capacity(1450);
        meishikafeiProcess0.setMaterial_time(14);
        meishikafeiProcess0.setRotate_speed(127);
        meishikafeiProcess0.setStir_speed(127);
        meishikafeiProcess0.setWater_type(WaterType.HOT_WATER);
        meishikafeiProcessList.add(0, meishikafeiProcess0);
        ContainerConfig meishikafeiProcess1 = new ContainerConfig();
        meishikafeiProcess1.setContainer(ContainerType.BEAN_CONTAINER);
        meishikafeiProcess1.setWater_interval(0);
        meishikafeiProcess1.setWater_capacity(1000);
        meishikafeiProcess1.setMaterial_time(17*2);
        meishikafeiProcess1.setRotate_speed(127);
        meishikafeiProcess1.setStir_speed(127);
        meishikafeiProcess1.setWater_type(WaterType.HOT_WATER);
        meishikafeiProcessList.add(1, meishikafeiProcess1);
        mieshikafei.setProcessList(meishikafeiProcessList);

        youbaoCoffeeList.add(natie);
        youbaoCoffeeList.add(mieshikafei);
        youbaoCoffeeList.add(moka);
        youbaoCoffeeList.add(kabuqiluo);
        youbaoCoffeeList.add(chunxiangniunai);
        youbaoCoffeeList.add(qiaokeliniunai);
        youbaoCoffeeList.add(xiangnongqiaokeli);
        youbaoCoffeeList.add(yishinongka);
    }

    public static synchronized SingleMaterialLsit getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SingleMaterialLsit(context);
        }
        return mInstance;
    }

    public boolean AddCoffeeList(Coffee coffee) {

        if (coffeeList.add(coffee)) {
            JSONArray jsonArray = (JSONArray) JSONObject.toJSON(coffeeList);
            String array = jsonArray.toString();
            Log.d(TAG, "list size=" + coffeeList.size() + " array is " + array);
            SharedPreferencesManager.getInstance(mContext).SetCoffeeList(array);
            return true;
        } else {
            return false;
        }
    }

    public void RemoveCoffeeList(int position) {
        coffeeList.remove(position);
    }

    public List<Coffee> getCoffeeList() {

        return coffeeList;
    }

    public List<Coffee> getyoubaoCoffeeList() {
        return youbaoCoffeeList;
    }
}
