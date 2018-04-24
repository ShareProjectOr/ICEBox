package example.jni.com.coffeeseller.contentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import example.jni.com.coffeeseller.bean.bunkerData;

/**
 * Created by Administrator on 2018/4/11.
 */

public class MaterialSql extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Material.db";
    public static final String MATERIALS_TABLE_NAME = "Material";
    public static final String MATERIALS_COLUMN_ID = "id";
    public static final String MATERIALS_COLUMN_BUNKERSID = "bunkersID"; //料仓编号
    public static final String MATERIALS_COLUMN_MATERIALID = "MaterialID";  //原料编号
    public static final String MATERIALS_COLUMN_MATERIALTYPE = "MaterialType"; //原料种类
    public static final String MATERIALS_COLUMN_MATERIALNAME = "MaterialName"; //原料名字
    public static final String MATERIALS_COLUMN_MATERIALUNIT = "MaterialUnit"; //原料单位
    public static final String MATERIALS_COLUMN_MATERIALSTOCK = "MaterialStock"; //原料剩余量
    public static final String MATERIALS_COLUMN_MATERIALDORPSPEED = "MaterialDropSpeed";//单位落料量
    public static final String MATERIALS_COLUMN_CONTAINERID = "containerID";//机器真正的料仓编号
    public static final String MATERIALS_COLUMN_ADDMATERIALTIME = "addMaterialTime"; //最后补料时间

    public MaterialSql(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table" + MATERIALS_TABLE_NAME
                + "(id integer primary key ,bunkersID text, MaterialID text, MaterialType text, MaterialName text, MaterialUnit text, MaterialStock text ," +
                " MaterialDropSpeed text ,containerID text , addMaterialTime text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + MATERIALS_TABLE_NAME);
        onCreate(db);
    }


    //获取查询条件的指针位置
    public Cursor getDataCursor(String whereKey, Object whereValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + MATERIALS_TABLE_NAME + "where" + whereKey + "=" + whereValue + "", null);
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
    public boolean insertContact(String bunkersID, String MaterialID, String MaterialType, String MaterialName, String MaterialUnit, String MaterialStock, String MaterialDropSpeed, String containerID, String addMaterialTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("bunkersID", bunkersID);
        contentValues.put("MaterialID", MaterialID);
        contentValues.put("MaterialType", MaterialType);
        contentValues.put("MaterialName", MaterialName);
        contentValues.put("MaterialUnit", MaterialUnit);
        contentValues.put("MaterialStock", MaterialStock);
        contentValues.put("MaterialDropSpeed", MaterialDropSpeed);
        contentValues.put("containerID", containerID);
        contentValues.put("addMaterialTime", addMaterialTime);
        long result = db.insert(MATERIALS_TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    /*
    * 更新数据库操作
    * */
    public boolean updateContact(String bunkersID, String MaterialID, String MaterialType, String MaterialName, String MaterialUnit, String MaterialStock, String MaterialDropSpeed, String containerID, String addMaterialTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
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
        db.update(MATERIALS_TABLE_NAME, contentValues, bunkersID + " = ? ", new String[]{bunkersID});
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

    public ArrayList<String> getAllbunkersIDs() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + MATERIALS_TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Log.e("料仓编号", res.getInt(res.getColumnIndex(MATERIALS_COLUMN_BUNKERSID)) + "");
            array_list.add(res.getString(res.getColumnIndex(MATERIALS_COLUMN_BUNKERSID)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + MATERIALS_TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Log.e("料仓编号", res.getInt(res.getColumnIndex(MATERIALS_COLUMN_ID)) + "");
            array_list.add(res.getString(res.getColumnIndex(MATERIALS_COLUMN_ID)));
            res.moveToNext();
        }
        return array_list;
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
            }


            return bunkersList;
        }
    }
}
