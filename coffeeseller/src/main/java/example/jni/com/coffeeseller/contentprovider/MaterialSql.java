package example.jni.com.coffeeseller.contentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import example.jni.com.coffeeseller.bean.CommitMaterialObject;
import example.jni.com.coffeeseller.bean.bunkerData;
import example.jni.com.coffeeseller.model.adapters.MaterialRecycleListAdapter;

/**
 * Created by Administrator on 2018/4/11.
 */

public class MaterialSql extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Material.db";
    public static final String MATERIALS_TABLE_NAME = "Material";
    public static final String MATERIALS_COLUMN_ID = "id";
    public static final String MATERIALS_COLUMN_BUNKERSID = "bunkersID"; //料仓编号
    public static final String MATERIALS_COLUMN_BUNKERTYPE = "bunkerType"; //料仓编号 0:主料仓；1:辅料仓；2：净水仓；3：污水仓；4：包装仓
    public static final String MATERIALS_COLUMN_MATERIALID = "MaterialID";  //原料编号
    public static final String MATERIALS_COLUMN_MATERIALTYPE = "MaterialType"; //原料种类
    public static final String MATERIALS_COLUMN_MATERIALNAME = "MaterialName"; //原料名字
    public static final String MATERIALS_COLUMN_MATERIALUNIT = "MaterialUnit"; //原料单位
    public static final String MATERIALS_COLUMN_MATERIALSTOCK = "MaterialStock"; //原料剩余量
    public static final String MATERIALS_COLUMN_MATERIALDORPSPEED = "MaterialDropSpeed";//单位落料量
    public static final String MATERIALS_COLUMN_CONTAINERID = "containerID";//机器真正的料仓编号
    public static final String MATERIALS_COLUMN_ADDMATERIALTIME = "addMaterialTime"; //最后补料时间
    private String TAG = "MaterialSql";
    private MaterialRecycleListAdapter adapter;

    public MaterialSql(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + MATERIALS_TABLE_NAME
                + "(id integer primary key ,bunkersID text, bunkerType text , MaterialID text, MaterialType text, MaterialName text, MaterialUnit text, MaterialStock text ," +
                " MaterialDropSpeed text ,containerID text , addMaterialTime text )");
    }

    public boolean deleteAllContent() {

        if (getAllbunkersIDs().size() != 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(MATERIALS_TABLE_NAME, null, null);
            db.close();
        }

        return true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + MATERIALS_TABLE_NAME);
        onCreate(db);
    }

    public String getContainerIDByBunkerID(String bunkesID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String ContainerID = "";
        Cursor res = db.rawQuery("select * from " + MATERIALS_TABLE_NAME + " where " + MATERIALS_COLUMN_BUNKERSID + " = " + bunkesID + "", null);
        if (res.moveToFirst()) {
            ContainerID = res.getString(res.getColumnIndex(MATERIALS_COLUMN_CONTAINERID));
        }

        res.close();
        db.close();
        return ContainerID;
    }

    public String getContainerIDByMaterialID(String MaterialID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + MATERIALS_TABLE_NAME + " where " + MATERIALS_COLUMN_MATERIALID + " = " + MaterialID + " limit 1 ", null);
        res.moveToFirst();
        String ContainerID = res.getString(res.getColumnIndex(MATERIALS_COLUMN_CONTAINERID));
        res.close();
        db.close();
        return ContainerID;
    }


    public String getBunkersNameByID(String bunkesID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + MATERIALS_TABLE_NAME + " where " + MATERIALS_COLUMN_BUNKERSID + " = " + bunkesID + "", null);
        res.moveToFirst();
        String MaterialName = res.getString(res.getColumnIndex(MATERIALS_COLUMN_MATERIALNAME));
        res.close();
        db.close();
        return MaterialName + "仓";

    }

    public ArrayList<String> getAllmaterialID() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + MATERIALS_TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Log.e("原料编号", res.getString(res.getColumnIndex(MATERIALS_COLUMN_MATERIALID)) + "");
            array_list.add(res.getString(res.getColumnIndex(MATERIALS_COLUMN_MATERIALID)));
            res.moveToNext();
        }
        res.close();
        db.close();
        return array_list;
    }

    public String getMaterialDropSpeedBycontainerID(String containerID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + MATERIALS_TABLE_NAME + " where " + MATERIALS_COLUMN_CONTAINERID + " = " + containerID + "", null);
        res.moveToFirst();
        String MaterialDropSpeed = res.getString(res.getColumnIndex(MATERIALS_COLUMN_MATERIALDORPSPEED));
        res.close();
        db.close();
        return MaterialDropSpeed;
    }

    public String getBunkerIDByMaterialD(String MaterialD) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + MATERIALS_TABLE_NAME + " where " + MATERIALS_COLUMN_MATERIALID + " = " + MaterialD + "", null);
        res.moveToFirst();
        String MaterialDropSpeed = res.getString(res.getColumnIndex(MATERIALS_COLUMN_BUNKERSID));
        res.close();
        db.close();
        return MaterialDropSpeed;
    }

    public String getStorkByBunkersID(String bunkesID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + MATERIALS_TABLE_NAME + " where " + MATERIALS_COLUMN_BUNKERSID + " = " + bunkesID + "", null);
        res.moveToFirst();
        String MaterialStock = res.getString(res.getColumnIndex(MATERIALS_COLUMN_MATERIALSTOCK));
        res.close();
        db.close();
        return MaterialStock;
    }

    public String getMaterialDropSpeedByBunkersID(String bunkesID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + MATERIALS_TABLE_NAME + " where " + MATERIALS_COLUMN_BUNKERSID + " = " + bunkesID + "", null);
        res.moveToFirst();
        String MaterialStock = res.getString(res.getColumnIndex(MATERIALS_COLUMN_MATERIALDORPSPEED));
        db.close();
        res.close();
        return MaterialStock;
    }

    public String getMaterialDropSpeedByContainerID(String ContainerID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + MATERIALS_TABLE_NAME + " where " + MATERIALS_COLUMN_CONTAINERID + " = " + ContainerID + "", null);
        res.moveToFirst();
        String MaterialDropSpeed = res.getString(res.getColumnIndex(MATERIALS_COLUMN_MATERIALDORPSPEED));
        db.close();
        res.close();
        return MaterialDropSpeed;
    }

    public String getStorkByMaterialID(String MaterialID) {
        Log.e(TAG, "materialID is " + MaterialID);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + MATERIALS_TABLE_NAME + " where " + MATERIALS_COLUMN_MATERIALID + " = " + MaterialID + "", null);
        res.moveToFirst();
        String MaterialStock = res.getString(res.getColumnIndex(MATERIALS_COLUMN_MATERIALSTOCK));
        res.close();
        db.close();
        return MaterialStock;
    }

    public String getMaterialIDByBunkerID(String BunkerID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + MATERIALS_TABLE_NAME + " where " + MATERIALS_COLUMN_BUNKERSID + " = " + BunkerID + "", null);
        res.moveToFirst();
        String MaterialID = res.getString(res.getColumnIndex(MATERIALS_COLUMN_MATERIALID));
        res.close();
        db.close();
        return MaterialID;
    }

    //获取查询条件的指针位置
    public Cursor getDataCursor(String whereKey, Object whereValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + MATERIALS_TABLE_NAME + " where " + whereKey + " = " + whereValue + "", null);
        return res;
    }


    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "Material");
        return numRows;
    }

    /* insert Material() 插入数据库操作
    *  bunkersID
    *  MaterialID
    *  MaterialType
    *  MaterialName
    *  MaterialUnit
    *  MaterialStock
    *  MaterialDropSpeed
    */
    public boolean insertContact(String bunkersID, String bunkerType, String MaterialID, String MaterialType, String MaterialName, String MaterialUnit, String MaterialStock, String MaterialDropSpeed, String containerID, String addMaterialTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MATERIALS_COLUMN_BUNKERSID, bunkersID);
        contentValues.put(MATERIALS_COLUMN_BUNKERTYPE, bunkerType);
        contentValues.put(MATERIALS_COLUMN_MATERIALID, MaterialID);
        contentValues.put(MATERIALS_COLUMN_MATERIALTYPE, MaterialType);
        contentValues.put(MATERIALS_COLUMN_MATERIALNAME, MaterialName);
        contentValues.put(MATERIALS_COLUMN_MATERIALUNIT, MaterialUnit);
        contentValues.put(MATERIALS_COLUMN_MATERIALSTOCK, MaterialStock);
        contentValues.put(MATERIALS_COLUMN_MATERIALDORPSPEED, MaterialDropSpeed);
        contentValues.put(MATERIALS_COLUMN_CONTAINERID, containerID);
        contentValues.put(MATERIALS_COLUMN_ADDMATERIALTIME, addMaterialTime);
        long result = db.insert(MATERIALS_TABLE_NAME, null, contentValues);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    //删除所有行
    public boolean clearSql() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MATERIALS_TABLE_NAME, null, new String[]{"1"});
        db.close();
        return true;
    }

    /*
    * 更新数据库操作
    * */
    public boolean updateContact(String bunkersID, String bunkerType, String MaterialID, String MaterialType, String MaterialName, String MaterialUnit, String MaterialStock, String MaterialDropSpeed, String containerID, String addMaterialTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Log.e(TAG, "update: bunkersID is " + bunkersID + "and  MaterialName is " + MaterialName + " and MaterialID is " + MaterialID);
        if (!bunkerType.isEmpty()) {
            contentValues.put(MATERIALS_COLUMN_BUNKERTYPE, bunkerType);
        }
        if (!MaterialID.isEmpty()) {
            contentValues.put("MaterialID", MaterialID);
        }
        if (!MaterialType.isEmpty()) {
            contentValues.put("MaterialType", MaterialType);
        }

        if (!MaterialName.isEmpty()) {
            contentValues.put("MaterialName", MaterialName);
        }

        if (!MaterialUnit.isEmpty()) {
            contentValues.put("MaterialUnit", MaterialUnit);
        }
        if (!MaterialStock.isEmpty()) {
            contentValues.put("MaterialStock", MaterialStock);
        }
        if (!MaterialDropSpeed.isEmpty()) {
            contentValues.put("MaterialDropSpeed", MaterialDropSpeed);
        }
        if (!containerID.isEmpty()) {
            contentValues.put("containerID", containerID);
        }
        if (!addMaterialTime.isEmpty()) {
            contentValues.put("addMaterialTime", addMaterialTime);
        }
        db.update(MATERIALS_TABLE_NAME, contentValues, "" + MATERIALS_COLUMN_BUNKERSID + "=?", new String[]{bunkersID});
        db.close();
        return true;
    }


    /*
   * 更新数据库操作
   * */
    public boolean updateContactByMap(String materialID, Map<String, String> updateParams) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (String key : updateParams.keySet()) {
            if (!TextUtils.isEmpty(updateParams.get(key))) {
                contentValues.put(key, updateParams.get(key));
            }
        }
        db.update(MATERIALS_TABLE_NAME, contentValues, MATERIALS_COLUMN_MATERIALID + " = ? ", new String[]{materialID});
        db.close();
        return true;
    }


    /*
 * 更新数据库原料剩余量
 * */
    public boolean updateMaterialStockByMaterialId(String materialID, String materialStock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MATERIALS_COLUMN_MATERIALSTOCK, materialStock);
        db.update(MATERIALS_TABLE_NAME, contentValues, MATERIALS_COLUMN_MATERIALID + " = ? ", new String[]{materialID});
        db.close();
        return true;
    }

    public Integer deleteContact(String bunkersID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MATERIALS_TABLE_NAME, "bunkersID = ? ", new String[]{bunkersID});
    }

    public void deleteAllMaterials(int size) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 1; i <= size; i++) {
            db.delete(MATERIALS_TABLE_NAME, "id = ? ", new String[]{Integer.toString(i)});
        }
    }

    public ArrayList<String> getAllcontainerID() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + MATERIALS_TABLE_NAME, null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                Log.e("料仓实际编号", res.getString(res.getColumnIndex(MATERIALS_COLUMN_CONTAINERID)) + "");
                array_list.add(res.getString(res.getColumnIndex(MATERIALS_COLUMN_CONTAINERID)));
                res.moveToNext();
            }
        }
        db.close();
        res.close();
        return array_list;
    }

    public ArrayList<String> getAllbunkersIDs() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + MATERIALS_TABLE_NAME, null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {

                array_list.add(res.getString(res.getColumnIndex(MATERIALS_COLUMN_BUNKERSID)));
                res.moveToNext();
            }
        }
        db.close();
        res.close();
        Log.e(TAG, "location allAllbunkersID is" + array_list.toString());
        return array_list;
    }

    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + MATERIALS_TABLE_NAME, null);
        if (res.moveToFirst()) {
            while (!res.isAfterLast()) {
                Log.e("数据库ID", res.getInt(res.getColumnIndex(MATERIALS_COLUMN_ID)) + "");
                int index = res.getColumnIndex(MATERIALS_COLUMN_ID);
                array_list.add(res.getString(index));
                res.moveToNext();
            }
        }
        db.close();
        res.close();
        return array_list;
    }

    public bunkerData getbunkerDataBycontainerID(String containerID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + MATERIALS_TABLE_NAME + " where " + MATERIALS_COLUMN_CONTAINERID + " = " + containerID + "", null);
        res.moveToFirst();
        String bunkerID = res.getString(res.getColumnIndex(MATERIALS_COLUMN_BUNKERSID));
        String bunkerType = res.getString(res.getColumnIndex(MATERIALS_COLUMN_BUNKERTYPE));
        String MaterialID = res.getString(res.getColumnIndex(MATERIALS_COLUMN_MATERIALID));
        String MaterialType = res.getString(res.getColumnIndex(MATERIALS_COLUMN_MATERIALTYPE));
        String MaterialName = res.getString(res.getColumnIndex(MATERIALS_COLUMN_MATERIALNAME));
        String MaterialUnit = res.getString(res.getColumnIndex(MATERIALS_COLUMN_MATERIALUNIT));
        String MaterialStock = res.getString(res.getColumnIndex(MATERIALS_COLUMN_MATERIALSTOCK));
        Log.e(TAG, "剩余量:" + MaterialStock);
        String MaterialDropSpeed = res.getString(res.getColumnIndex(MATERIALS_COLUMN_MATERIALDORPSPEED));
        //   String containerID = res.getString(res.getColumnIndex(MATERIALS_COLUMN_CONTAINERID));
        String lastLoadingTime = res.getString(res.getColumnIndex(MATERIALS_COLUMN_ADDMATERIALTIME));
        bunkerData bunkerdata = new bunkerData();
        switch (containerID) {
            case "0":
                bunkerdata.setBunkersName("豆仓(主料仓)");
                break;
            case "1":
                bunkerdata.setBunkersName("辅料仓1");
                break;
            case "2":
                bunkerdata.setBunkersName("辅料仓2");
                break;
            case "3":
                bunkerdata.setBunkersName("辅料仓3");
                break;
            case "4":
                bunkerdata.setBunkersName("辅料仓4");
                break;
            case "5":
                bunkerdata.setBunkersName("辅料仓5");
                break;
            case "6":
                bunkerdata.setBunkersName("辅料仓6");
                break;
            case "7":
                bunkerdata.setBunkersName("温水仓");
                break;
            case "8":
                bunkerdata.setBunkersName("纸杯仓");
                break;
        }

        bunkerdata.setBunkerID(bunkerID);
        bunkerdata.setBunkerType(bunkerType);
        bunkerdata.setMaterialID(MaterialID);
        bunkerdata.setMaterialType(MaterialType);
        bunkerdata.setMaterialName(MaterialName);
        bunkerdata.setMaterialUnit(MaterialUnit);
        bunkerdata.setMaterialStock(MaterialStock);
        bunkerdata.setMaterialDropSpeed(MaterialDropSpeed);
        bunkerdata.setContainerID(containerID);
        bunkerdata.setLastLoadingTime(lastLoadingTime);
        res.close();
        db.close();
        return bunkerdata;
    }

    private String bankesName[] = {"豆仓(主料仓)", "辅料仓1", "辅料仓2", "辅料仓3", "辅料仓4", "辅料仓5", "辅料仓6", "温水仓", "纸杯仓"};

    public List<CommitMaterialObject> getCommitMaterialObjectList() {
        List<CommitMaterialObject> list = new ArrayList<>();
        List<String> AllbunkersIDs = getAllbunkersIDs();
        for (int i = 0; i < AllbunkersIDs.size(); i++) {
            CommitMaterialObject object = new CommitMaterialObject();
            String bunkerID = AllbunkersIDs.get(i);
            object.setBunkerID(Integer.parseInt(bunkerID));
            object.setMaterialStock(Integer.parseInt(getStorkByBunkersID(bunkerID)));
            if (getMaterialDropSpeedByBunkersID(bunkerID).equals("null") || getMaterialDropSpeedByBunkersID(bunkerID).equals("null")) {
                object.setOutput(null);
            } else {
                object.setOutput(Integer.parseInt(getMaterialDropSpeedByBunkersID(bunkerID)));
            }

            if (getMaterialIDByBunkerID(bunkerID).equals("null")) {
                object.setMaterialID(null);
            } else {
                object.setMaterialID(Integer.parseInt(getMaterialIDByBunkerID(bunkerID)));
            }

            list.add(object);
        }
        Log.e(TAG, "commit list is " + list.size());
        return list;
    }

    public List<bunkerData> getRecycleBunkersList() {
        List<String> AllcontainerID = getAllcontainerID();
        if (AllcontainerID.size() == 0) {
            return null;
        } else {
            Log.e(TAG, "location AllcontainerID is " + AllcontainerID.toString());
            List<bunkerData> bunkersList = new ArrayList<>();

            if (AllcontainerID.contains("0")) { //如果含有这个料仓则从数据库中找到数据加入
                bunkersList.add(0, getbunkerDataBycontainerID("0"));
            } else {//如果不含有这个料仓则加入空的数据
                bunkerData data = new bunkerData();
                data.setBunkerID("");
                data.setMaterialID("");
                data.setMaterialType("");
                data.setMaterialName("未启用");
                data.setMaterialUnit("");
                data.setMaterialStock("0");
                data.setMaterialDropSpeed("");
                data.setContainerID("0");
                data.setLastLoadingTime("");
                data.setBunkersName(bankesName[0]);
                bunkersList.add(0, data);
            }
            if (AllcontainerID.contains("1")) { //如果含有这个料仓则从数据库中找到数据加入
                bunkersList.add(1, getbunkerDataBycontainerID("1"));
            } else {//如果不含有这个料仓则加入空的数据
                bunkerData data = new bunkerData();
                data.setBunkerID("");
                data.setMaterialID("");
                data.setMaterialType("");
                data.setMaterialName("未启用");
                data.setMaterialUnit("");
                data.setMaterialStock("0");
                data.setMaterialDropSpeed("");
                data.setContainerID("1");
                data.setLastLoadingTime("");
                data.setBunkersName(bankesName[1]);
                bunkersList.add(1, data);
            }

            if (AllcontainerID.contains("2")) { //如果含有这个料仓则从数据库中找到数据加入
                bunkersList.add(2, getbunkerDataBycontainerID("2"));
            } else {//如果不含有这个料仓则加入空的数据
                bunkerData data = new bunkerData();
                data.setBunkerID("");
                data.setMaterialID("");
                data.setMaterialType("");
                data.setMaterialName("未启用");
                data.setMaterialUnit("");
                data.setMaterialStock("0");
                data.setMaterialDropSpeed("");
                data.setContainerID("2");
                data.setLastLoadingTime("");
                data.setBunkersName(bankesName[2]);
                bunkersList.add(2, data);
            }

            if (AllcontainerID.contains("3")) { //如果含有这个料仓则从数据库中找到数据加入
                bunkersList.add(3, getbunkerDataBycontainerID("3"));
            } else {//如果不含有这个料仓则加入空的数据
                bunkerData data = new bunkerData();
                data.setBunkerID("");
                data.setMaterialID("");
                data.setMaterialType("");
                data.setMaterialName("未启用");
                data.setMaterialUnit("");
                data.setMaterialStock("0");
                data.setMaterialDropSpeed("");
                data.setContainerID("3");
                data.setLastLoadingTime("");
                data.setBunkersName(bankesName[3]);
                bunkersList.add(3, data);
            }
            if (AllcontainerID.contains("4")) { //如果含有这个料仓则从数据库中找到数据加入
                bunkersList.add(4, getbunkerDataBycontainerID("4"));
            } else {//如果不含有这个料仓则加入空的数据
                bunkerData data = new bunkerData();
                data.setBunkerID("");
                data.setMaterialID("");
                data.setMaterialType("");
                data.setMaterialName("未启用");
                data.setMaterialUnit("");
                data.setMaterialStock("0");
                data.setMaterialDropSpeed("");
                data.setContainerID("4");
                data.setLastLoadingTime("");
                data.setBunkersName(bankesName[4]);
                bunkersList.add(4, data);
            }

            if (AllcontainerID.contains("5")) { //如果含有这个料仓则从数据库中找到数据加入
                bunkersList.add(5, getbunkerDataBycontainerID("5"));
            } else {//如果不含有这个料仓则加入空的数据
                bunkerData data = new bunkerData();
                data.setBunkerID("");
                data.setMaterialID("");
                data.setMaterialType("");
                data.setMaterialName("未启用");
                data.setMaterialUnit("");
                data.setMaterialStock("0");
                data.setMaterialDropSpeed("");
                data.setContainerID("5");
                data.setLastLoadingTime("");
                data.setBunkersName(bankesName[5]);
                bunkersList.add(5, data);
            }

            if (AllcontainerID.contains("6")) { //如果含有这个料仓则从数据库中找到数据加入
                bunkersList.add(6, getbunkerDataBycontainerID("6"));
            } else {//如果不含有这个料仓则加入空的数据
                bunkerData data = new bunkerData();
                data.setBunkerID("");
                data.setMaterialID("");
                data.setMaterialType("");
                data.setMaterialName("未启用");
                data.setMaterialUnit("");
                data.setMaterialStock("0");
                data.setMaterialDropSpeed("");
                data.setContainerID("6");
                data.setLastLoadingTime("");
                data.setBunkersName(bankesName[6]);
                bunkersList.add(6, data);
            }
            if (AllcontainerID.contains("7")) { //如果含有这个料仓则从数据库中找到数据加入
                bunkersList.add(6, getbunkerDataBycontainerID("7"));
            } else {//如果不含有这个料仓则加入空的数据
                bunkerData data = new bunkerData();
                data.setBunkerID("");
                data.setMaterialID("");
                data.setMaterialType("");
                data.setMaterialName("未启用");
                data.setMaterialUnit("");
                data.setMaterialStock("0");
                data.setMaterialDropSpeed("");
                data.setContainerID("7");
                data.setLastLoadingTime("");
                data.setBunkersName(bankesName[7]);
                bunkersList.add(7, data);
            }
            if (AllcontainerID.contains("8")) { //如果含有这个料仓则从数据库中找到数据加入
                bunkersList.add(8, getbunkerDataBycontainerID("8"));
            } else {//如果不含有这个料仓则加入空的数据
                bunkerData data = new bunkerData();
                data.setBunkerID("");
                data.setMaterialID("");
                data.setMaterialType("");
                data.setMaterialName("未启用");
                data.setMaterialUnit("");
                data.setMaterialStock("0");
                data.setMaterialDropSpeed("");
                data.setContainerID("8");
                data.setLastLoadingTime("");
                data.setBunkersName(bankesName[8]);
                bunkersList.add(8, data);
            }
            return bunkersList;
        }
    }

    public List<bunkerData> getBunkersList() {
        if (getAllbunkersIDs().size() == 0) {
            return null;
        } else {
            List<bunkerData> bunkersList = new ArrayList<>();

            //    int id = getAllCotacts().size();
            for (int id = 1; id < getAllCotacts().size() + 1; id++) {

                Cursor cursor = getDataCursor(MATERIALS_COLUMN_ID, id);
                cursor.moveToFirst();
                String bunkerID = cursor.getString(cursor.getColumnIndex(MATERIALS_COLUMN_BUNKERSID));
                String MaterialID = cursor.getString(cursor.getColumnIndex(MATERIALS_COLUMN_MATERIALID));
                String MaterialType = cursor.getString(cursor.getColumnIndex(MATERIALS_COLUMN_MATERIALTYPE));
                String MaterialName = cursor.getString(cursor.getColumnIndex(MATERIALS_COLUMN_MATERIALNAME));
                String MaterialUnit = cursor.getString(cursor.getColumnIndex(MATERIALS_COLUMN_MATERIALUNIT));
                String MaterialStock = cursor.getString(cursor.getColumnIndex(MATERIALS_COLUMN_MATERIALSTOCK));
                String MaterialDropSpeed = cursor.getString(cursor.getColumnIndex(MATERIALS_COLUMN_MATERIALDORPSPEED));
                String containerID = cursor.getString(cursor.getColumnIndex(MATERIALS_COLUMN_CONTAINERID));
                String lastLoadingTime = cursor.getString(cursor.getColumnIndex(MATERIALS_COLUMN_ADDMATERIALTIME));
                bunkerData bunkerdata = new bunkerData();
                bunkerdata.setBunkerID(bunkerID);
                bunkerdata.setMaterialID(MaterialID);
                bunkerdata.setMaterialType(MaterialType);
                bunkerdata.setMaterialName(MaterialName);
                bunkerdata.setMaterialUnit(MaterialUnit);
                bunkerdata.setMaterialStock(MaterialStock);
                bunkerdata.setMaterialDropSpeed(MaterialDropSpeed);
                bunkerdata.setContainerID(containerID);
                bunkerdata.setLastLoadingTime(lastLoadingTime);
                bunkersList.add(bunkerdata);
                cursor.close();
            }

            return bunkersList;
        }
    }

    public void setAdapter(MaterialRecycleListAdapter materialRecycleListAdapter) {
        adapter = materialRecycleListAdapter;
    }

    public String getBunkerIDByContainerID(String containerID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + MATERIALS_TABLE_NAME + " where " + MATERIALS_COLUMN_CONTAINERID + " = " + containerID+"", null);
        res.moveToFirst();
        String bunkerID = res.getString(res.getColumnIndex(MATERIALS_COLUMN_BUNKERSID));
        res.close();
        db.close();
        return bunkerID;
    }
}
