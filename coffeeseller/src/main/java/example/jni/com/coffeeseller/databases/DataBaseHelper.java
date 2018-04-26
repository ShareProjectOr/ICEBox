package example.jni.com.coffeeseller.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {


    String sql_order_info = "create table order_info(_id integer primary key autoincrement," +
            "order text,rqcup integer,price text,taste_redio text,temp_format text," +
            "payed integer,make_success integer,customer_id text,formula_id integer,pay_time text," +
            "is_report_success integer,report_msg text)";

    static DataBaseHelper mInstance = null;
    static String TAG = "DataBaseHelper";


    protected static DataBaseHelper getInstance(Context context) {

        if (mInstance == null) {

            mInstance = new DataBaseHelper(context, "coffee.db", null, 1);
        }
        return mInstance;
    }

    private DataBaseHelper(Context context, String name, CursorFactory factory,
                           int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        db.execSQL(sql_order_info);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }


}
