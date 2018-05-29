package example.jni.com.coffeeseller.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import example.jni.com.coffeeseller.MachineConfig.DealRecorder;
import example.jni.com.coffeeseller.utils.MyLog;

/**
 * Created by WH on 2018/4/23.
 * 将订单信息保存在数据库中
 */

public class DealOrderInfoManager {
    String TAG = "DealOrderInfoManager";
    Context mContext;
    DataBaseHelper mDataBaseHelper;
    static DealOrderInfoManager mInstance;


    public static DealOrderInfoManager getInstance(Context context) {

        if (mInstance == null) {

            mInstance = new DealOrderInfoManager(context);
        }
        return mInstance;
    }


    public DealOrderInfoManager(Context context) {

        mContext = context;
        mDataBaseHelper = new DataBaseHelper(mContext);
    }

    public void addToTable(DealRecorder orderRecorder) {

        synchronized (this) {
            SQLiteDatabase localSQLiteDatabase = mDataBaseHelper.getWritableDatabase();

            Object[] arrayOfObject = new Object[14];
            arrayOfObject[0] = orderRecorder.getOrder();
            arrayOfObject[1] = orderRecorder.getRqcup();
            arrayOfObject[2] = orderRecorder.getPrice();
            arrayOfObject[3] = orderRecorder.getTasteRadio();
            arrayOfObject[4] = orderRecorder.getRqTempFormat();
            arrayOfObject[5] = orderRecorder.isPayed() ? 1 : 0;
            arrayOfObject[6] = orderRecorder.isMakeSuccess() ? 1 : 0;
            arrayOfObject[7] = orderRecorder.getCustomerId();
            arrayOfObject[8] = orderRecorder.getFormulaID();
            arrayOfObject[9] = orderRecorder.getBunkers();
            arrayOfObject[10] = orderRecorder.getPayTime();
            arrayOfObject[11] = orderRecorder.isReportSuccess() ? 1 : 0;
            arrayOfObject[12] = orderRecorder.getReportMsg();
            arrayOfObject[13] = orderRecorder.getUploadCount();

            localSQLiteDatabase.execSQL("insert into order_info ( trade_code ,cup,price,taste_redio,temp_format, payed ," +
                    "make_success,customer_id,formula_id,bunkers,pay_time,is_report_success,report_msg ,upload_count) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)", arrayOfObject);

            MyLog.W(TAG, "add order into table order_info where orderCode= " + orderRecorder.getOrder());
            localSQLiteDatabase.close();

        }
    }

    public void delete(String order) {
        synchronized (this) {

            SQLiteDatabase localSQLiteDatabase = mDataBaseHelper.getWritableDatabase();
            localSQLiteDatabase.delete("order_info", "trade_code = ? ", new String[]{order});
            localSQLiteDatabase.close();
        }
    }

    public void update(DealRecorder bean) {

        synchronized (this) {

            if (bean == null) {
                return;
            }
            SQLiteDatabase localSQLiteDatabase = mDataBaseHelper.getReadableDatabase();
            MyLog.W(TAG, " update order_info " + "isPayed=" + bean.isPayed() + ", isMakeSuccess=" + bean.isMakeSuccess()
                    + " ,isReportSuccess=  " + bean.isReportSuccess() + " ,getReportMsg=  " + bean.getReportMsg()
                    + " ,bunkers= " + bean.getBunkers() + " ,upload_count= " + bean.getUploadCount());

            ContentValues contentValues = new ContentValues();
            contentValues.put("payed", bean.isPayed() ? 1 : 0);
            contentValues.put("make_success", bean.isMakeSuccess() ? 1 : 0);
            contentValues.put("is_report_success", bean.isReportSuccess() ? 1 : 0);
            contentValues.put("report_msg", bean.getReportMsg());
            contentValues.put("taste_redio", bean.getTasteRadio());
            contentValues.put("temp_format", bean.getRqTempFormat());
            contentValues.put("bunkers", bean.getBunkers());
            contentValues.put("upload_count", bean.getUploadCount());

            localSQLiteDatabase.update("order_info", contentValues, "trade_code = ? ", new String[]{bean.getOrder()});

            localSQLiteDatabase.close();
        }
        MyLog.W(TAG, "update is over");
    }


    public List<DealRecorder> getLocalTableDatas() {

        synchronized (this) {
            ArrayList<DealRecorder> mList = new ArrayList<DealRecorder>();
            SQLiteDatabase localSQLiteDatabase = mDataBaseHelper.getReadableDatabase();

            Cursor localCursor = localSQLiteDatabase.rawQuery("select * from order_info order by id desc", null);

            while (localCursor.moveToNext()) {

                DealRecorder mBean = new DealRecorder();
                mBean.setOrder(localCursor.getString(localCursor.getColumnIndex("trade_code")));
                mBean.setRqcup(localCursor.getInt(localCursor.getColumnIndex("cup")));
                mBean.setRqTempFormat(localCursor.getString(localCursor.getColumnIndex("temp_format")));
                mBean.setTasteRadio(localCursor.getString(localCursor.getColumnIndex("taste_redio")));
                mBean.setPrice(localCursor.getString(localCursor.getColumnIndex("price")));
                mBean.setPayTime(localCursor.getString(localCursor.getColumnIndex("pay_time")));
                mBean.setCustomerId(localCursor.getString(localCursor.getColumnIndex("customer_id")));
                mBean.setFormulaID(localCursor.getInt(localCursor.getColumnIndex("formula_id")));
                mBean.setBunkers(localCursor.getString(localCursor.getColumnIndex("bunkers")));
                mBean.setReportMsg(localCursor.getString(localCursor.getColumnIndex("report_msg")));
                mBean.setUploadCount(localCursor.getInt(localCursor.getColumnIndex("upload_count")));


                boolean isPay = localCursor.getInt(localCursor.getColumnIndex("payed")) == 0 ? false : true;

                mBean.setPayed(isPay);

                boolean isMakeSuccess = localCursor.getInt(localCursor.getColumnIndex("make_success")) == 0 ? false : true;

                mBean.setMakeSuccess(isMakeSuccess);

                boolean isReportSuccess = localCursor.getInt(localCursor.getColumnIndex("is_report_success")) == 0 ? false : true;

                mBean.setReportSuccess(isReportSuccess);

                mList.add(mBean);
            }
            localSQLiteDatabase.close();
            return mList;
        }
    }

    public boolean deleteAllContent() {

        if (getLocalTableDatas().size() != 0) {
           // SQLiteDatabase db = this.getWritableDatabase();
            SQLiteDatabase localSQLiteDatabase = mDataBaseHelper.getWritableDatabase();
         //   localSQLiteDatabase.delete(MATERIALS_TABLE_NAME, null, null);
            localSQLiteDatabase.close();
        }

        return true;
    }

    /*
    * 正序
    * */
    public List<DealRecorder> getLocalTableDatasByASC() {

        synchronized (this) {
            ArrayList<DealRecorder> mList = new ArrayList<DealRecorder>();
            SQLiteDatabase localSQLiteDatabase = mDataBaseHelper.getReadableDatabase();

            Cursor localCursor = localSQLiteDatabase.rawQuery("select * from order_info", null);

            while (localCursor.moveToNext()) {

                DealRecorder mBean = new DealRecorder();
                mBean.setOrder(localCursor.getString(localCursor.getColumnIndex("trade_code")));
                mBean.setRqcup(localCursor.getInt(localCursor.getColumnIndex("cup")));
                mBean.setRqTempFormat(localCursor.getString(localCursor.getColumnIndex("temp_format")));
                mBean.setTasteRadio(localCursor.getString(localCursor.getColumnIndex("taste_redio")));
                mBean.setPrice(localCursor.getString(localCursor.getColumnIndex("price")));
                mBean.setPayTime(localCursor.getString(localCursor.getColumnIndex("pay_time")));
                mBean.setCustomerId(localCursor.getString(localCursor.getColumnIndex("customer_id")));
                mBean.setFormulaID(localCursor.getInt(localCursor.getColumnIndex("formula_id")));
                mBean.setBunkers(localCursor.getString(localCursor.getColumnIndex("bunkers")));
                mBean.setReportMsg(localCursor.getString(localCursor.getColumnIndex("report_msg")));
                mBean.setUploadCount(localCursor.getInt(localCursor.getColumnIndex("upload_count")));


                boolean isPay = localCursor.getInt(localCursor.getColumnIndex("payed")) == 0 ? false : true;

                mBean.setPayed(isPay);

                boolean isMakeSuccess = localCursor.getInt(localCursor.getColumnIndex("make_success")) == 0 ? false : true;

                mBean.setMakeSuccess(isMakeSuccess);

                boolean isReportSuccess = localCursor.getInt(localCursor.getColumnIndex("is_report_success")) == 0 ? false : true;

                mBean.setReportSuccess(isReportSuccess);

                mList.add(mBean);
            }
            localSQLiteDatabase.close();
            return mList;
        }
    }

    public int getSqlSize() {
        synchronized (this) {

            SQLiteDatabase localSQLiteDatabase = mDataBaseHelper.getWritableDatabase();
            Cursor localCursor = localSQLiteDatabase.rawQuery("select * from order_info order by id desc", null);
            int size = localCursor.getCount();
            localSQLiteDatabase.close();
            return size;
        }
    }

    /*
    * 如果数据库数量超过200，先删除已上报成功的，然后按时间删除为上报成功的
    * */
    public void deleteIfSizeOver200(int sizeLimit) {
        if (getSqlSize() <= sizeLimit) {
            return;
        }

        int deleteCount = 0;
        List<DealRecorder> dealRecorders = getLocalTableDatasByASC();
        List<DealRecorder> uploadSuccessRecorder = new ArrayList<>();
        List<DealRecorder> uploadFailedRecorder = new ArrayList<>();

        deleteCount = dealRecorders.size() - sizeLimit;

        for (DealRecorder dealRecorder : dealRecorders) {
            if (dealRecorder.isReportSuccess()) {

                uploadSuccessRecorder.add(dealRecorder);
            } else {

                uploadFailedRecorder.add(dealRecorder);
            }
        }
        int uploadSuccessDeleteCount = 0;
        int uploadeFailedDeleteCount = 0;


        if (uploadSuccessRecorder.size() > deleteCount) {
            uploadSuccessDeleteCount = deleteCount;
        } else {
            uploadSuccessDeleteCount = uploadSuccessRecorder.size();
            uploadeFailedDeleteCount = deleteCount - uploadSuccessDeleteCount;
        }

        for (int i = 0; i < uploadSuccessDeleteCount; i++) {
            DealRecorder dealRecorder = uploadSuccessRecorder.get(i);
            delete(dealRecorder.getOrder());
        }

        for (int i = 0; i < uploadeFailedDeleteCount; i++) {
            DealRecorder dealRecorder = uploadFailedRecorder.get(i);
            delete(dealRecorder.getOrder());
        }
    }
}
