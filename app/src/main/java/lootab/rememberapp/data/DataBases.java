package lootab.rememberapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by ichung-gi on 2016. 1. 19..
 */
public final class DataBases {

    public static final String TAG = "DataBases";

    public static DataBases dataBases;

    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String DAY = "day";
    public static final String NUMBER = "number";
    public static final String _TABLENAME = "remember";

    private static final String DATABASE_NAME = "remember.db";

    public static int DATABASE_VERSION = 1;

    private DatabaseHelper dbHelper;

    private SQLiteDatabase db;

    private Context context;

    private DataBases (Context context) {this.context = context;}

    public static DataBases getInstance(Context context){
        if (dataBases == null){
            dataBases = new DataBases(context);
        }
        return dataBases;
    }

    public boolean open(){
        println("opening database [" + _TABLENAME + "].");

        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();

        return true;
    }

    public void close() {
        println("closing database [" + DATABASE_NAME + "].");
        db.close();

        dataBases = null;
    }

    public Cursor rawQuery(String SQL) {
        println("\nexecuteQuery called.\n");

        Cursor c1 = null;
        try {
            c1 = db.rawQuery(SQL, null);
            println("cursor count : " + c1.getCount());
        } catch(Exception ex) {
            Log.e(TAG, "Exception in executeQuery", ex);
        }

        return c1;
    }

    public boolean execSQL(String SQL) {
        println("\nexecute called.\n");

        try {
            Log.d(TAG, "SQL : " + SQL);
            db.execSQL(SQL);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in executeQuery", ex);
            return false;
        }

        return true;
    }

    private void println(String msg) {
        Log.d(TAG, msg);
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            println("creating database [" + DATABASE_NAME + "].");

            // TABLE_MEMO
            println("creating table [" + _TABLENAME + "].");

            // drop existing table
            String DROP_SQL = "drop table if exists " + _TABLENAME;
            try {
                db.execSQL(DROP_SQL);
            } catch(Exception ex) {
                Log.e(TAG, "Exception in DROP_SQL", ex);
            }

            String CREATE_SQL =
                    "CREATE TABLE "+_TABLENAME+"( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            +TITLE+" TEXT , "
                            +CONTENT+" TEXT , "
                            +DAY+" TEXT , "
                            +NUMBER+" TEXT);";

            try {
                db.execSQL(CREATE_SQL);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in CREATE_SQL", ex);
            }

        }

        public void onOpen(SQLiteDatabase db)
        {
            println("opened database [" + DATABASE_NAME + "].");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion)
        {
            println("Upgrading database from version " + oldVersion + " to " + newVersion + ".");
        }
    }

    public static final class CreateDB implements BaseColumns {
        public static final String TITLE = "title";
        public static final String CONTENT = "content";
        public static final String DAY = "day";
        public static final String NUMBER = "number";
        public static final String _TABLENAME = "remember";
        public static final String _CREATE =
                "CREATE TABLE "+_TABLENAME+"( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        +TITLE+" TEXT , "
                        +CONTENT+" TEXT , "
                        +DAY+" TEXT , "
                        +NUMBER+" TEXT);";
    }
}

