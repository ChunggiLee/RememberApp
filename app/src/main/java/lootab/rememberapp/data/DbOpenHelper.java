package lootab.rememberapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ichung-gi on 2016. 1. 19..
 */
public class DbOpenHelper {

    private static final String DATABASE_NAME = "remember.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper {

        // 생성자
        public DatabaseHelper(Context context, String title,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, title, factory, version);
        }

        // 최초 DB를 만들때 한번만 호출된다.
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("asdf DbOpenHelper", " " + DataBases.CreateDB._CREATE);
        }

        // 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+DataBases.CreateDB._TABLENAME);
            onCreate(db);
        }
    }

    public DbOpenHelper(Context context){
        this.mCtx = context;
    }

    public DbOpenHelper open() throws SQLException {
        try {
            mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
            mDB = mDBHelper.getWritableDatabase();

        } catch (SQLiteException e) {
            Log.d("DbOpenHelper", "open Error");
        };
        return this;
    }

    public void close(){
        mDB.close();
    }

    // Insert DB
    public long insertColumn(String title, String content, String day, String number){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.TITLE, title);
        values.put(DataBases.CreateDB.CONTENT, content);
        values.put(DataBases.CreateDB.DAY, day);
        values.put(DataBases.CreateDB.NUMBER, number);
        return mDB.insert(DataBases.CreateDB._TABLENAME, null, values);
    }

    // Update DB
    public boolean updateColumn(long id , String title, String content, String day, String number){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.TITLE, title);
        values.put(DataBases.CreateDB.CONTENT, content);
        values.put(DataBases.CreateDB.DAY, day);
        values.put(DataBases.CreateDB.NUMBER, number);
        return mDB.update(DataBases.CreateDB._TABLENAME, values, "_id=" + id, null) > 0;
    }

    // Delete ID
    public boolean deleteColumn(long id){
        return mDB.delete(DataBases.CreateDB._TABLENAME, "_id=" + id, null) > 0;
    }

    // Delete Contact
    public boolean deleteColumn(String content){
        return mDB.delete(DataBases.CreateDB._TABLENAME, "content="+content, null) > 0;
    }

    // Select All
    public Cursor getAllColumns(){
        return mDB.query(DataBases.CreateDB._TABLENAME, null, null, null, null, null, null);
    }

    // ID 컬럼 얻어 오기
    public Cursor getColumn(long id){
        Cursor c = mDB.query(DataBases.CreateDB._TABLENAME, null,
                "_id="+id, null, null, null, null);
        if(c != null && c.getCount() != 0)
            c.moveToFirst();
        return c;
    }

    // 이름 검색 하기 (rawQuery)
    public Cursor getMatchName(String title){
        Cursor c = mDB.rawQuery( "select * from address where title=" + "'" + title + "'" , null);
        return c;
    }


}