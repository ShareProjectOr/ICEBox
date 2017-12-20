package com.example.shareiceboxms.models.contants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class Sql extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_LOGINPASSWORD = "loginpassword";
    public static final String CONTACTS_COLUMN_LOGINACCOUNT = "loginAccount";
    public static final String CONTACTS_COLUMN_USERID = "userID";

    public Sql(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    /*@Override onCreate()
     * ����һ�����ݱ�
     *
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

//		db.execSQL("create table contacts "
//				+ "(id integer primary key, name text,phone text,email text, password text,place text)");
        db.execSQL("create table contacts "
                + "(id integer primary key ,userID text, loginAccount text, loginpassword text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    /* insertContact( )�������뷽��
     *  name
     *  phone
     *   password
     */
    public boolean insertContact(String userID, String loginAccount, String loginPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("userID", userID);
//		contentValues.put("email", email);
        contentValues.put("loginAccount", loginAccount);
        contentValues.put("loginpassword", loginPassword);
        db.insert("contacts", null, contentValues);
        return true;
    }

    //����ID
    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contacts where id=" + id + "", null);
        return res;
    }

    public Cursor getData(String userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contacts where userID=" + userID + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "user_info");
        return numRows;
    }

    //�����޸ĸ������ݱ���
    public boolean updateContact(String userID, String loginAccount, String loginPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //     contentValues.put("userID", userID);
        contentValues.put("loginAccount", loginAccount);
        contentValues.put("loginpassword", loginPassword);
        db.update("contacts", contentValues, "userID=?", new String[]{userID});
        return true;
    }

    public Integer deleteContact(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts", "id = ? ", new String[]{Integer.toString(id)});
    }

    public void deleteAllContact(int size) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 1; i <= size; i++) {
            db.delete("contacts", "id = ? ", new String[]{Integer.toString(i)});
        }
    }

    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contacts", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Log.e("数据ID", res.getInt(res.getColumnIndex("id")) + "");
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_LOGINACCOUNT)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllUserID() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contacts", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {//�ж��Ƿ����������
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_USERID)));
            res.moveToNext();
        }
        return array_list;
    }
}
