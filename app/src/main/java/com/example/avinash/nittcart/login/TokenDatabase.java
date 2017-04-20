package com.example.avinash.nittcart.login;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by AVINASH on 7/1/2016.
 */
public class TokenDatabase extends SQLiteOpenHelper {
    public static final String dbname="USER_TOKEN";

    public TokenDatabase(Context c) {
        super(c, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user_auth" + "(id integer primary key,auth_token String, user_id String," +
                "firebase_token_id String,cart_count int" +
                ",notification_count int,dashboard_count int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST cinfo");
        onCreate(db);
    }

    public boolean insertToken(String auth_token,String user_id, String roll_no,
                               int cart_count,int notification_count,int dashboard_count){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("auth_token",auth_token);
        cv.put("user_id",user_id);
        //cv.put("roll_no",roll_no);
        cv.put("cart_count",cart_count);
        cv.put("notification_count",notification_count);
        cv.put("dashboard_count", dashboard_count);
        db.insert("user_auth", null, cv);
        return true;
    }

    public boolean insertFirebaseToken(String firebase_token_id){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("firebase_token_id", firebase_token_id);
        db.update("user_auth", cv, "id=?", new String[]{Integer.toString(0)});
        return true;
    }

    public boolean update_dash_count(int dashboard_count){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("dashboard_count",dashboard_count);
        db.update("user_auth", cv, "id=?", new String[]{Integer.toString(0)});
        return true;
    }

    public boolean update_cart_count(int dashboard_count){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("cart_count",dashboard_count);
        db.update("user_auth", cv, "id=?", new String[]{Integer.toString(0)});
        return true;
    }
    public boolean update_notification_count(int dashboard_count){
        Log.d("update",""+dashboard_count);
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("notification_count",dashboard_count);
        db.update("user_auth", cv, "id=?", new String[]{Integer.toString(0)});
        return true;
    }

    public Cursor getToken(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cr=db.rawQuery("select * from user_auth",null);
        return cr;
    }
    public boolean updateToken(Integer id,String auth_token,String user_id,int cart_count,int notification_count,int dashboard_count){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("auth_token",auth_token);
        cv.put("cart_count",cart_count);
        cv.put("notification_count",notification_count);
        cv.put("dashboard_count",dashboard_count);
        db.update("user_auth",cv,"id=?",new String[]{Integer.toString(id)});
        return true;
    }
    public int row(){
        SQLiteDatabase db=this.getReadableDatabase();
        int rownum=(int) DatabaseUtils.queryNumEntries(db, "user_auth");
        return rownum;
    }
    public void drop(){
        SQLiteDatabase db=this.getReadableDatabase();
        db.execSQL("delete from user_auth");
    }
}
