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


            Object[] arrayOfObject = new Object[12];
            arrayOfObject[0] = orderRecorder.getOrder();
            arrayOfObject[1] = orderRecorder.getRqcup();
            arrayOfObject[2] = orderRecorder.getPrice();
            arrayOfObject[3] = orderRecorder.getTasteRadio();
            arrayOfObject[4] = orderRecorder.getRqTempFormat();
            arrayOfObject[5] = orderRecorder.isPayed() ? 1 : 0;
            arrayOfObject[6] = orderRecorder.isMakeSuccess() ? 1 : 0;
            arrayOfObject[7] = orderRecorder.getCustomerId();
            arrayOfObject[8] = orderRecorder.getFormulaID();
            arrayOfObject[9] = orderRecorder.getPayTime();
            arrayOfObject[10] = orderRecorder.isReportSuccess() ? 1 : 0;
            arrayOfObject[11] = orderRecorder.getReportMsg();

            MyLog.d(TAG, "arrayOfObject=" + arrayOfObject);
            localSQLiteDatabase.execSQL("insert into order_info ( trade_code ,cup,price,taste_redio,temp_format, payed ," +
                    "make_success,customer_id,formula_id,pay_time,is_report_success,report_msg ) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?)", arrayOfObject);
            localSQLiteDatabase.close();

        }
    }

    public void deleteFromTable(String order) {

        synchronized (this) {

            SQLiteDatabase localSQLiteDatabase = mDataBaseHelper.getWritableDatabase();
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = order;
            localSQLiteDatabase.execSQL("delete from order_info where order=?", arrayOfObject);
            localSQLiteDatabase.close();
        }
    }

    public void update(DealRecorder bean) {

        synchronized (this) {

            if (bean == null) {
                return;
            }
            SQLiteDatabase localSQLiteDatabase = mDataBaseHelper.getReadableDatabase();
            Object[] arrayOfObject = new Object[4];
            MyLog.d(TAG, "isPayed=" + bean.isPayed());

            arrayOfObject[0] = bean.isPayed() ? 1 : 0;
            arrayOfObject[1] = bean.isMakeSuccess() ? 1 : 0;
            arrayOfObject[2] = bean.isReportSuccess() ? 1 : 0;
            arrayOfObject[3] = bean.getReportMsg();


            MyLog.d(TAG, "isMakeSuccess=" + bean.isMakeSuccess());

            MyLog.d(TAG, "isReportSuccess=" + bean.isReportSuccess());


            MyLog.d(TAG, "getReportMsg=" + bean.getReportMsg());

            ContentValues contentValues = new ContentValues();
            contentValues.put("payed", bean.isPayed() ? 1 : 0);
            contentValues.put("make_success", bean.isMakeSuccess() ? 1 : 0);
            contentValues.put("is_report_success", bean.isReportSuccess() ? 1 : 0);
            contentValues.put("report_msg", bean.getReportMsg());
            /*localSQLiteDatabase.execSQL("update order_info set payed=" + (bean.isPayed() ? 1 : 0) +
                    ",make_success=" + (bean.isMakeSuccess() ? 1 : 0) +
                    ",is_report_success=" + (bean.isReportSuccess() ? 1 : 0) +
                    ",report_msg= " + bean.getReportMsg() +
                    " where trade_code= " + bean.getOrder());*/
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
                mBean.setCustomerId(localCursor.getString(localCursor.getColumnIndex("customer_id")));
                mBean.setFormulaID(localCursor.getInt(localCursor.getColumnIndex("formula_id")));

                boolean isPay = localCursor.getInt(localCursor.getColumnIndex("payed")) == 0 ? false : true;

                mBean.setPayed(isPay);

                boolean isMakeSuccess = localCursor.getInt(localCursor.getColumnIndex("make_success")) == 0 ? false : true;

                mBean.setMakeSuccess(isMakeSuccess);

                mList.add(mBean);
            }
            localSQLiteDatabase.close();
            return mList;
        }
    }
}
