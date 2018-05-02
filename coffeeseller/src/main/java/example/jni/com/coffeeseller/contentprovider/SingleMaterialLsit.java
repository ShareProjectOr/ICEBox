package example.jni.com.coffeeseller.contentprovider;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cof.ac.inter.ContainerConfig;
import cof.ac.inter.ContainerType;
import cof.ac.inter.WaterType;
import example.jni.com.coffeeseller.bean.Coffee;
import example.jni.com.coffeeseller.bean.Material;
import example.jni.com.coffeeseller.bean.Step;
import example.jni.com.coffeeseller.bean.Taste;

/**
 * Created by Administrator on 2018/4/16.
 */

public class SingleMaterialLsit {
    private static SingleMaterialLsit mInstance;

    private List<Coffee> coffeeList;
    private String TAG = "SingleMaterialLsit";
    private Context mContext;
    private List<Coffee> youbaoCoffeeList = new ArrayList<>();
    private MaterialSql sql;

    private org.json.JSONArray coffeeArray;

    public SingleMaterialLsit(Context context) {
        mContext = context;

        coffeeList = new ArrayList<>();
       /* String arrayString = SharedPreferencesManager.getInstance(context).getCoffeeListArray();
        Log.d(TAG, arrayString);
        if (SharedPreferencesManager.getInstance(context).getCoffeeListArray().isEmpty()) {
            coffeeList = new ArrayList<>();
        } else {
            Log.d(TAG, SharedPreferencesManager.getInstance(mContext).getCoffeeListArray());
            JSONArray array = JSON.parseArray(SharedPreferencesManager.getInstance(mContext).getCoffeeListArray());
            coffeeList = array.toJavaList(Coffee.class);

        }*/
        setyoubaoList();
    }

    public synchronized void setCoffeeList() {

        if (coffeeArray == null) {
            return;
        }
        if (coffeeList.size() != 0) {
            coffeeList.clear();
        }
        try {
            for (int i = 0; i < coffeeArray.length(); i++) {
                org.json.JSONObject coffeeObject = (org.json.JSONObject) coffeeArray.opt(i);
                Coffee coffee = new Coffee();
                coffee.setCacheUrl(coffeeObject.getString("imageSource"));
                coffee.setName(coffeeObject.getString("name"));
                coffee.setPrice(coffeeObject.getString("suggestedPrice"));
                coffee.setFormulaID(coffeeObject.getInt("formulaID"));
                org.json.JSONArray stepArray = coffeeObject.getJSONArray("process");
                for (int j = 0; j < stepArray.length(); j++) {
                    org.json.JSONObject stepObject = (org.json.JSONObject) stepArray.opt(j);
                    String materialID = stepObject.getJSONObject("material").getString("materialID");
                  /*  int containerID = stepObject.getInt("containerID");
                    Cursor cursor = sql.getDataCursor("containerID", containerID);
                    cursor.moveToFirst();*/
                    sql = new MaterialSql(mContext);
                    Log.e(TAG, "数据库长度" + sql.getAllbunkersIDs().size());
                    String materialStock = sql.getStorkByMaterialID(materialID);//找到对应原料编号的剩余量

               /* for (String haveMaterialID : sql.getAllmaterialID()) {
                    if (materialID)
                }*/
                    Log.e(TAG, "currentStep need materialID is " + materialID);
                    Log.e(TAG, "location all materialID is " + sql.getAllmaterialID().toString());
                    if (!sql.getAllmaterialID().contains(materialID)) { //本地料仓里面不含有这个原料
                        Log.e(TAG, "location have not this material");
                        coffee.setOver(true);
                        break;
                    }

                    if (materialStock.equals("0")) { // 有原料但剩余量为0 则为售罄
                        Log.e(TAG, "location materialStock is 0  ");
                        coffee.setOver(true);
                        break;
                    }
                    long intMaterialStock = Long.parseLong(materialStock);
                    Log.e(TAG, "location MaterialStock is" + intMaterialStock);
                    int needAccount = stepObject.getInt("amount");
                    Log.e(TAG, "need MaterialStock is" + needAccount);
                    if (needAccount > intMaterialStock) { //需要量 大于剩余量时
                        coffee.setOver(true);
                        Log.e(TAG, "location materialStock is not enough");
                        break;
                    }
                }
                List<Step> stepList = new ArrayList<>();
                for (int j = 0; j < stepArray.length(); j++) {
                    org.json.JSONObject stepObject = (org.json.JSONObject) stepArray.opt(j);
                    Step step = new Step();
                    ContainerConfig containerConfig = new ContainerConfig();
                    switch (sql.getContainerIDByMaterialID(stepObject.getJSONObject("material").getString("materialID"))) {
                        case "0":
                            containerConfig.setContainer(ContainerType.BEAN_CONTAINER);
                            break;
                        case "1":
                            containerConfig.setContainer(ContainerType.NO_ONE);
                            break;
                        case "2":
                            containerConfig.setContainer(ContainerType.NO_TOW);
                            break;
                        case "3":
                            containerConfig.setContainer(ContainerType.NO_THREE);
                            break;
                        case "4":
                            containerConfig.setContainer(ContainerType.NO_FOUR);
                            break;
                        case "5":
                            containerConfig.setContainer(ContainerType.NO_FIVE);
                            break;
                        case "6":
                            containerConfig.setContainer(ContainerType.NO_FIVE);
                            break;
                        case "7":
                            containerConfig.setContainer(ContainerType.HOTWATER_CONTAINER);
                            break;
                    }
                    containerConfig.setWater_interval(stepObject.getInt("timeOut"));
                    //   int Water_capacity = stepObject.getInt("water");
                    containerConfig.setWater_capacity(stepObject.getInt("water"));
                    int amount = stepObject.getInt("amount");
                    Log.e(TAG, "出料总量=" + amount);
                    String MaterialID = stepObject.getJSONObject("material").getString("materialID");
                    Log.e(TAG, "MaterialID is " + MaterialID);
                    String containerID = sql.getContainerIDByMaterialID(MaterialID);

                    Log.e(TAG, "containerID is " + containerID);
                    String MaterialDropSpeed = sql.getMaterialDropSpeedBycontainerID(containerID);
                    Log.e(TAG, "默认落料速度=" + MaterialDropSpeed + "--此时的原料为:" + stepObject.getJSONObject("material").getString("name"));
                    int DropSpeed = Integer.parseInt(MaterialDropSpeed);
                    Log.e(TAG, "DropSpeed is " + DropSpeed);
                    double loadPercent = new BigDecimal((float) (stepObject.getInt("loadingSpeed") / 127)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    Log.e(TAG, "double loadPercent is " + loadPercent);
                    double materialTime = new BigDecimal((float) amount / DropSpeed).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    Log.e(TAG, "double materialTime is " + materialTime);
                    Double dealprecentTime = materialTime * loadPercent * 5 * 10;
                    Log.e(TAG, "Double dealprecentTime is " + dealprecentTime);

                    int intmaterialTime = dealprecentTime.intValue();
                    Log.e(TAG, "intmaterialTime is " + intmaterialTime);
                    containerConfig.setMaterial_time(intmaterialTime);
                    containerConfig.setRotate_speed(stepObject.getInt("loadingSpeed"));
                    containerConfig.setStir_speed(stepObject.getInt("mixingSpeed"));
                    switch (stepObject.getInt("waterType")) {
                        case 0:
                            containerConfig.setWater_type(WaterType.COLD_WATER);
                            break;
                        case 1:
                            containerConfig.setWater_type(WaterType.HOT_WATER);
                            break;
                    }
                    step.setContainerConfig(containerConfig);
                    org.json.JSONArray tasteArray = stepObject.getJSONArray("taste");
                    List<Taste> tastesList = new ArrayList<>();
                    for (int k = 0; k < tasteArray.length(); k++) {
                        org.json.JSONObject tasteObject = (org.json.JSONObject) tasteArray.opt(k);
                        Taste taste = new Taste();
                        taste.setAmount(tasteObject.getInt("amount"));
                        taste.setRemark(tasteObject.getString("remark"));
                        tastesList.add(k, taste);
                    }
                    step.setTastes(tastesList);
                    Material material = new Material();
                    org.json.JSONObject materialObject = stepObject.getJSONObject("material");
                    material.setName(materialObject.getString("name"));
                    material.setMaterialID(materialObject.getInt("materialID"));
                    material.setOutput(materialObject.getInt("output"));
                    material.setType(materialObject.getInt("type"));
                    material.setUnit(materialObject.getString("unit"));
                    step.setMaterial(material);
                    stepList.add(step);
                }
                coffee.setStepList(stepList);
                coffeeList.add(coffee);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        moka.setName("摩卡(mocha)");
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
        mokaProcess3.setMaterial_time(15 * 2);
        mokaProcess3.setWater_type(WaterType.HOT_WATER);
        mokaProcess3.setRotate_speed(127);
        mokaProcess3.setStir_speed(127);
        mokaProcess.add(3, mokaProcess3);
        moka.setProcessList(mokaProcess);


        kabuqiluo.setName("卡布奇洛(Card butch los)");
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
        kabuqiluoProcess2.setMaterial_time(17 * 2);
        kabuqiluoProcess2.setStir_speed(127);
        kabuqiluoProcess2.setRotate_speed(127);
        kabuqiluoProcess2.setWater_type(WaterType.HOT_WATER);
        kabuqiluoProcessList.add(2, kabuqiluoProcess2);
        kabuqiluo.setProcessList(kabuqiluoProcessList);

        chunxiangniunai.setName("醇香牛奶(Mellow milk)");
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

        qiaokeliniunai.setName("巧克力牛奶(Chocolate milk)");
        qiaokeliniunai.setPrice("0.2");
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

        xiangnongqiaokeli.setName("香浓巧克力(Fragrant chocolate)");
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
        yishinongka.setName("意式浓卡(Italian thick card)");
        List<ContainerConfig> yishinongkaProcessList = new ArrayList<>();
        ContainerConfig yishinongkaProcess0 = new ContainerConfig();
        yishinongkaProcess0.setContainer(ContainerType.BEAN_CONTAINER);
        yishinongkaProcess0.setWater_interval(0);
        yishinongkaProcess0.setWater_capacity(1100);
        yishinongkaProcess0.setMaterial_time(17 * 2);
        yishinongkaProcess0.setRotate_speed(127);
        yishinongkaProcess0.setStir_speed(127);
        yishinongkaProcess0.setWater_type(WaterType.HOT_WATER);
        yishinongkaProcessList.add(0, yishinongkaProcess0);
        yishinongka.setProcessList(yishinongkaProcessList);

        natie.setName("拿铁(latte)");
        natie.setPrice("0.1");
        List<ContainerConfig> natieProcessList = new ArrayList<>();
        ContainerConfig natieProcess0 = new ContainerConfig();
        natieProcess0.setContainer(ContainerType.BEAN_CONTAINER);
        natieProcess0.setWater_interval(0);
        natieProcess0.setWater_capacity(600);
        natieProcess0.setMaterial_time(17 * 2);
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

        mieshikafei.setName("美式咖啡(American coffee)");
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
        meishikafeiProcess1.setMaterial_time(17 * 2);
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


    public List<Coffee> RemoveCoffeeList(int formulaID) {
        if (coffeeList.size() == 0) {
            return null;
        }
        for (int i = 0; i < coffeeList.size(); i++) {
            if (formulaID == coffeeList.get(i).getFormulaID()) {//遍历找到对应需要删除的项
                coffeeList.remove(i);
            }
        }
        return coffeeList;
    }

    public synchronized List<Coffee> getCoffeeList() {

        coffeeList.clear();
        setCoffeeList();


        return coffeeList;
    }

    public boolean isCoffeeSellOut(int position) {
        Coffee coffee = coffeeList.get(position);
        MaterialSql sql = new MaterialSql(mContext);
        List<Step> steps = coffee.getStepList();
        for (int i = 0; i < steps.size(); i++) {
            Step step = steps.get(i);
            String materialID = "" + step.getMaterial().getMaterialID();
            String storck = sql.getStorkByMaterialID(materialID);
            if (storck.equals("0")) {
                return true;
            }

        }
        return false;
    }


    public org.json.JSONArray getCoffeeArray() {
        return coffeeArray;
    }

    public synchronized void setCoffeeArray(org.json.JSONArray coffeeArray) {
        Log.e(TAG, "all coffee list size is " + coffeeArray.length());
        this.coffeeArray = coffeeArray;
    }


    public List<Coffee> getyoubaoCoffeeList() {
        return youbaoCoffeeList;
    }


}
