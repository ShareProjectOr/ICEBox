package example.jni.com.coffeeseller.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import example.jni.com.coffeeseller.utils.MyLog;

public class DataBaseHelper extends SQLiteOpenHelper {


    String sql_order_info = "create table order_info( id integer primary key," +
            "trade_code text,cup integer,price varchar(255),taste_redio varchar(255),temp_format varchar(255)," +
            "payed integer,make_success integer,customer_id varchar(255),formula_id integer,bunkers text,pay_time varchar(255)," +
            "is_report_success integer,report_msg varchar(255))";

    static DataBaseHelper mInstance = null;
    static String TAG = "DataBaseHelper";

    public DataBaseHelper(Context context) {
        super(context, "coffee.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        MyLog.d(TAG, "--------------------");
        db.execSQL(sql_order_info);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }


}
