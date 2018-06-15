package example.jni.com.coffeeseller.contentprovider;

import android.content.Context;
import android.util.Log;

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
import example.jni.com.coffeeseller.utils.MyLog;

/**
 * Created by Administrator on 2018/4/16.
 */

public class SingleMaterialList {
    private static SingleMaterialList mInstance;

    private List<Coffee> coffeeList;
    private String TAG = "SingleMaterialList";
    private Context mContext;
    private List<Coffee> youbaoCoffeeList = new ArrayList<>();

    private org.json.JSONArray coffeeArray;

    private SingleMaterialList(Context context) {
        mContext = context;
        coffeeList = new ArrayList<>();
        setyoubaoList();
    }

    private synchronized List<Coffee> setCoffeeList() {
        List<Coffee> coffees = new ArrayList<>();
/*        if (coffeeArray == null) {
            return;
        }
        if (coffeeList.size() != 0) {
            coffeeList.clear();
        }*/
        try {
            MaterialSql sql = new MaterialSql(mContext);
            Log.d(TAG, "数据库长度" + sql.getAllbunkersIDs().size());
            for (int i = 0; i < coffeeArray.length(); i++) {
                org.json.JSONObject coffeeObject = (org.json.JSONObject) coffeeArray.opt(i);
                Coffee coffee = new Coffee();
                coffee.setCacheUrl(coffeeObject.getString("imageSource"));
                coffee.setName(coffeeObject.getString("name"));
                coffee.setPrice(coffeeObject.getString("price"));
                coffee.setActivitiesPrice(coffeeObject.getString("activityPrice"));
                coffee.setFormulaID(coffeeObject.getInt("formulaID"));
                coffee.setOver(false);
                org.json.JSONArray stepArray = coffeeObject.getJSONArray("process");

                for (int j = 0; j < stepArray.length(); j++) {//计算每一步所需的原料的用量在当前的机器料仓里是否还充足
                    org.json.JSONObject stepObject = (org.json.JSONObject) stepArray.opt(j);
                    String materialID = stepObject.getJSONObject("material").getString("materialID");

                    Log.d(TAG, "currentStep need materialID is " + materialID);
                    org.json.JSONArray taste = stepObject.getJSONArray("taste");
                    if (taste.length() != 0) { //有口味的步骤
                        int amount = 300;
                        for (int k = 0; k < taste.length(); k++) { //遍历取出最小百分比
                            org.json.JSONObject tasteObject = (org.json.JSONObject) taste.opt(k);
                            if (amount > tasteObject.getInt("amount")) {
                                amount = tasteObject.getInt("amount");
                            }
                        }
                        String materialStock = sql.getStorkByMaterialID(materialID);// 找到对应原料编号的剩余量
                        long intMaterialStock = Long.parseLong(materialStock);//转化为long型
                        long reallyAmount = stepObject.getInt("amount");
                        long lessetAmount = reallyAmount * amount / 100;  //得出最少口味需要的原料
                        if (intMaterialStock < lessetAmount) {
                            coffee.setOver(true);
                        } else {
                            coffee.setOver(false);
                        }
                    } else {   //无口味的步骤
                        Log.d(TAG, "location all materialID is " + sql.getAllmaterialID().toString());
                        if (!sql.getAllmaterialID().contains(materialID)) { //本地料仓里面不含有这个原料
                            Log.d(TAG, "location have not this material");
                            Log.d(TAG, "此时的配方为:" + coffeeObject.getString("name"));
                            coffee.setOver(true);
                            continue;
                        }
                        String materialStock = sql.getStorkByMaterialID(materialID);// 找到对应原料编号的剩余量

                        if (materialStock.equals("0")) { // 有原料但剩余量为0 则为售罄
                            Log.d(TAG, "location materialStock is 0  ");
                            coffee.setOver(true);
                            break;
                        }
                        long intMaterialStock = Long.parseLong(materialStock);
                        Log.d(TAG, "location MaterialStock is" + intMaterialStock);
                        int needAccount = stepObject.getInt("amount");
                        Log.d(TAG, "need MaterialStock is" + needAccount);
                        if (needAccount > intMaterialStock) { //需要量 大于剩余量时
                            coffee.setOver(true);
                            Log.d(TAG, "location materialStock is not enough");
                            break;
                        }
                    }

                }


                List<Step> stepList = new ArrayList<>();

                for (int j = 0; j < stepArray.length(); j++) {
                    org.json.JSONObject stepObject = (org.json.JSONObject) stepArray.opt(j);
                    Step step = new Step();
                    if (!sql.getAllmaterialID().contains(stepObject.getJSONObject("material").getString("materialID"))) { //本地料仓里面不含有这个原料
                        Log.d(TAG, "location have not this material");
                        coffee.setOver(true);
                        break;
                    }
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
                    containerConfig.setWater_capacity(stepObject.getInt("water") * 10);//将ml转化为0.1ml
                    int amount = stepObject.getInt("amount");
                    step.setAmount(amount);
                    Log.d(TAG, "出料总量=" + amount);
                    String MaterialID = stepObject.getJSONObject("material").getString("materialID");
                    Log.d(TAG, "MaterialID is " + MaterialID);
                    String containerID = sql.getContainerIDByMaterialID(MaterialID);

                    Log.d(TAG, "containerID is " + containerID);
                    String MaterialDropSpeed;
                    String outPut = sql.getMaterialDropSpeedBycontainerID(containerID); //校准值
                    String defultOutPut = stepObject.getJSONObject("material").getString("output");

                    if (outPut.equals("null") || outPut.equals("") || outPut.equals("0")) {
                        //没有校准值时 ,采用默认值
                        MaterialDropSpeed = defultOutPut;
                    } else {
                        //有校准值时,采用校准值
                        MaterialDropSpeed = outPut;
                    }


                    Log.d(TAG, "默认落料速度=" + defultOutPut + " 校准值= " + outPut + "--此时的原料为:" + stepObject.getJSONObject("material").getString("name") + "落料总量为:" + amount);
                    int DropSpeed = Integer.parseInt(MaterialDropSpeed);
                    Log.d(TAG, "DropSpeed is " + DropSpeed);
                    double loadPercent = new BigDecimal((float) (stepObject.getInt("loadingSpeed") / 127)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    Log.d(TAG, "double loadPercent is " + loadPercent);
                    double materialTime = new BigDecimal((float) amount / DropSpeed).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    Log.d(TAG, "double materialTime is " + materialTime);
                    Double dealprecentTime = materialTime * loadPercent * 5 * 10;
                    Log.d(TAG, "Double dealprecentTime is " + dealprecentTime);

                    int intmaterialTime = dealprecentTime.intValue();
                    Log.d(TAG, "intmaterialTime is " + intmaterialTime);
                    containerConfig.setMaterial_time(intmaterialTime);
                    containerConfig.setRotate_speed(stepObject.getInt("loadingSpeed"));

                    MyLog.d(TAG, "name = " + containerID + "waterType = " + stepObject.getInt("waterType"));

                    containerConfig.setStir_speed(stepObject.getInt("mixingSpeed"));
                    if (stepObject.getInt("waterType") == 0) {//出冷饮的步骤
                        if (containerID.equals("0")) {  //如果是咖啡豆要出冷饮,则直接规定为出热饮
                            containerConfig.setWater_type(WaterType.HOT_WATER);
                        } else {   //原料不为咖啡豆时
                            if (!containerID.equals("1") && !containerID.equals("2")) { //假如出冷饮的这一步原料所在的料仓不为1并且也不在2号仓时 没法出冷饮认为是售罄
                                Log.d(TAG, "current containerID is " + containerID);
                                coffee.setOver(true);
                                break;
                            } else {
                                containerConfig.setWater_type(WaterType.COLD_WATER);
                            }
                        }


                    } else if (stepObject.getInt("waterType") == 1) {//出热饮的步骤


                        containerConfig.setWater_type(WaterType.HOT_WATER);


                    } else {//第三种出水,不存在,直接认为售罄
                        coffee.setOver(true);

                        break;
                    }

                 /*   if (stepObject.getInt("waterType") == 0) {
                        containerConfig.setWater_type(WaterType.COLD_WATER);
                    } else {
                        containerConfig.setWater_type(WaterType.HOT_WATER);
                    }*/
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
                coffees.add(coffee);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return coffees;
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

    public static synchronized SingleMaterialList getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SingleMaterialList(context);
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


    public List<Coffee> setCoffeeSellOut(int formulaID) {
        if (coffeeList.size() == 0) {
            return coffeeList;
        }
        for (int i = 0; i < coffeeList.size(); i++) {
            if (formulaID == coffeeList.get(i).getFormulaID()) {//遍历找到对应需要禁用的项
                coffeeList.get(i).setOver(true);
            }
        }
        return coffeeList;
    }

    public synchronized List<Coffee> getCoffeeList() {


        return formatCoffeeList(setCoffeeList());

//        return coffeeList;
    }

    /*
    * 将没有售罄的Coffee排在前面
    * */
    private List<Coffee> formatCoffeeList(List<Coffee> coffees) {

        MyLog.d(TAG, "-----------------");

        int notOverCount = 0;
        int coffeeCount = coffees.size();
        for (int i = 0; i < coffeeCount; i++) {
            Coffee coffee = coffees.get(i);
            if (!coffee.isOver) {
                coffees.remove(coffee);
                coffees.add(notOverCount, coffee);
                notOverCount++;
            }
        }
        return coffees;
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
        Log.d(TAG, "all coffee list size is " + coffeeArray.length());
        this.coffeeArray = coffeeArray;
    }


    public List<Coffee> getyoubaoCoffeeList() {
        return youbaoCoffeeList;
    }


}
