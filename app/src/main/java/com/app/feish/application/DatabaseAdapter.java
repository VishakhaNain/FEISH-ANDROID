package com.app.feish.application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * Created by lenovo on 4/20/2016.
 */
public class DatabaseAdapter {

    DBHelper dbHelper;
    ArrayAdapter<String> adapter;

    public DatabaseAdapter(Context context) {
        dbHelper = new DBHelper(context);
    }

    public long insertData(String pid, String appid,String rentry,String reexit,String drentry,String drexit,String date) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues con = new ContentValues();
        con.put(DBHelper.PID,pid);
        con.put(DBHelper.APPID, appid);
        con.put(DBHelper.RENTRY, rentry);
        con.put(DBHelper.REXIT, reexit);
        con.put(DBHelper.DRENTRY, drentry);
        con.put(DBHelper.DREXIT, drexit);
        con.put(DBHelper.DATE, date);
        long id=db.insert(DBHelper.TABLE_NAME, null, con);
        return id;
    }
    public Cursor getAllData(){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        String[]columns={DBHelper.UID,DBHelper.PID,DBHelper.APPID,DBHelper.RENTRY,DBHelper.REXIT,DBHelper.DRENTRY,DBHelper.DREXIT,DBHelper.DATE};
        Cursor cursor=db.query(DBHelper.TABLE_NAME, columns, null, null, null, null, null);
        return cursor;

    }

    public int updateContact (Integer id,String pid, String appid,String rentry,String reexit,String drentry,String drexit,String date)
    {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.PID,pid);
        contentValues.put(DBHelper.APPID, appid);
        contentValues.put(DBHelper.RENTRY, rentry);
        contentValues.put(DBHelper.REXIT, reexit);
        contentValues.put(DBHelper.DRENTRY, drentry);
        contentValues.put(DBHelper.DREXIT, drexit);
        contentValues.put(DBHelper.DATE, date);

        int count=db.update(DBHelper.TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return count;
    }

    public int deleteRow(int id) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        String[]columns={DBHelper.UID,DBHelper.PID,DBHelper.APPID,DBHelper.RENTRY,DBHelper.REXIT,DBHelper.DRENTRY,DBHelper.DREXIT,DBHelper.DATE};
        int i=db.delete(DBHelper.TABLE_NAME, DBHelper.UID+" = '"+id+"'",null);
        return i;

    }




    public Cursor getData(String date){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        String[]columns={DBHelper.UID,DBHelper.PID,DBHelper.APPID,DBHelper.RENTRY,DBHelper.REXIT,DBHelper.DRENTRY,DBHelper.DREXIT,DBHelper.DATE};
        Cursor cursor=db.query(DBHelper.TABLE_NAME,columns, DBHelper.DATE+" = '"+date+"'",null,null,null,null);

        return cursor;

    }
    public Cursor getListId(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();

        String[]columns={DBHelper.UID};
        ArrayList<Integer>namelist= new ArrayList<Integer>();
        Cursor cursor=db.query(DBHelper.TABLE_NAME, columns, null, null, null, null, null);
        return cursor;

    }



    static class DBHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "Patintreports";
        public static final String TABLE_NAME = "Report";
        public static final int DATABASE_VERSION = 4;
        public static final String UID = "id";
        public static final String PID = "p_id";
        public static final String APPID = "appointment_id";
        public static final String RENTRY = "rentry";
        public static final String REXIT = "rexit";
        public static final String DRENTRY = "drentry";
        public static final String DREXIT = "drexit";
        public static final String DATE = "date";
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT," + PID + " VARCHAR(255)," + APPID + " VARCHAR(200)," + RENTRY + " VARCHAR(200)," + REXIT + " VARCHAR(200)," + DRENTRY + " VARCHAR(200)," + DREXIT + " VARCHAR(200)," + DATE + " VARCHAR(300));";
        public static final String DROP_TABLE = "DROP TABLE if EXISTS" + TABLE_NAME;
        private Context context;


        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
         //   Message.message(context, "Construct call");
            Toast.makeText(context,"Construct call",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Toast.makeText(context,"OnCreateCalled",Toast.LENGTH_SHORT).show();

            db.execSQL(CREATE_TABLE);
          //  Message.message(context, "OnCreateCalled");


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        //    Message.message(context, "onupgade");
            Toast.makeText(context,"ondropcall",Toast.LENGTH_SHORT).show();

            db.execSQL(DROP_TABLE);
            onCreate(db);
        }
    }
}


