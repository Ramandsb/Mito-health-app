package in.tagbin.mitohealthapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import in.tagbin.mitohealthapp.Pojo.DataItems;

/**
 * Created by admin pc on 15-07-2016.
 */
public class DatabaseOperations extends SQLiteOpenHelper {


    public DatabaseOperations(Context context) {
        super(context, TableData.Tableinfo.DATABASE_NAME, null, database_version);
    }


    public static final int database_version = 2;
    public String CREATE_TABLE= "CREATE TABLE " + TableData.Tableinfo.TABLE_NAME_FOOD + "(" + TableData.Tableinfo.ID + " TEXT," + TableData.Tableinfo.FOOD_ID + " TEXT," + TableData.Tableinfo.FOOD_NAME + " TEXT," + TableData.Tableinfo.TIME_CONSUMED + " TEXT," + TableData.Tableinfo.AMOUNT + " TEXT," + TableData.Tableinfo.DATE + " TEXT," + TableData.Tableinfo.SYNCED + " TEXT)";
    public String CREATE_SLEEP_TABLE= "CREATE TABLE " + TableData.Tableinfo.TABLE_NAME_SLEEP + "(" + TableData.Tableinfo.SLEEP_UNIQUE_ID + " TEXT," + TableData.Tableinfo.START_TIME + " TEXT," + TableData.Tableinfo.END_TIME + " TEXT," + TableData.Tableinfo.SLEEP_DATE + " TEXT," + TableData.Tableinfo.SLEEP_HOURS + " TEXT," + TableData.Tableinfo.SLEEP_QUALITY + " TEXT)";
    public String CREATE_EXERCISE_TABLE= "CREATE TABLE " + TableData.Tableinfo.TABLE_NAME_EXERCISE + "(" + TableData.Tableinfo.EXER_UNIQUE_ID + " TEXT," + TableData.Tableinfo.EXER_NAME + " TEXT," + TableData.Tableinfo.WEIGHT + " TEXT," + TableData.Tableinfo.SETS + " TEXT," + TableData.Tableinfo.REPS + " TEXT," + TableData.Tableinfo.EXER_ID + " TEXT," + TableData.Tableinfo.EXER_DATE + " TEXT)";

    @Override
    public void onCreate(SQLiteDatabase sdb) {
        sdb.execSQL(CREATE_TABLE);
        sdb.execSQL(CREATE_SLEEP_TABLE);
        sdb.execSQL(CREATE_EXERCISE_TABLE);

        Log.d("Database operations", "Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableData.Tableinfo.TABLE_NAME_FOOD);
        onCreate(db);
    }

    public void putInformation(DatabaseOperations dop, String id, String food_id, String food_name,String time_consumed,String amount,String date,String synced) {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableData.Tableinfo.ID, id);
        cv.put(TableData.Tableinfo.FOOD_ID, food_id);
        cv.put(TableData.Tableinfo.FOOD_NAME, food_name);
        cv.put(TableData.Tableinfo.TIME_CONSUMED, time_consumed);
        cv.put(TableData.Tableinfo.AMOUNT, amount);
        cv.put(TableData.Tableinfo.DATE, date);
        cv.put(TableData.Tableinfo.SYNCED, synced);
        long k = SQ.insert(TableData.Tableinfo.TABLE_NAME_FOOD, null, cv);
        Log.d("Database Created", "true");

    }
    public void putExerciseInformation(DatabaseOperations dop, String unique_id, String exer_id, String exer_name,String date,String weight_consumed,String sets,String reps) {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableData.Tableinfo.EXER_UNIQUE_ID, unique_id);
        cv.put(TableData.Tableinfo.EXER_NAME, exer_name);
        cv.put(TableData.Tableinfo.EXER_ID, exer_id);
        cv.put(TableData.Tableinfo.EXER_DATE, date);
        cv.put(TableData.Tableinfo.WEIGHT, weight_consumed);
        cv.put(TableData.Tableinfo.SETS, sets);
        cv.put(TableData.Tableinfo.REPS, reps);
        long k = SQ.insert(TableData.Tableinfo.TABLE_NAME_EXERCISE, null, cv);
        Log.d("Database Created", "true");

    }
    public void putSleepInformation(DatabaseOperations dop, String id, String start, String end,String date,String hours,String quality) {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableData.Tableinfo.SLEEP_UNIQUE_ID, id);
        cv.put(TableData.Tableinfo.START_TIME, start);
        cv.put(TableData.Tableinfo.END_TIME, end);
        cv.put(TableData.Tableinfo.SLEEP_DATE, date);
        cv.put(TableData.Tableinfo.SLEEP_HOURS, hours);
        cv.put(TableData.Tableinfo.SLEEP_QUALITY, quality);
        long k = SQ.insert(TableData.Tableinfo.TABLE_NAME_SLEEP, null, cv);
        Log.d("Database Created", "true");

    }

    public Cursor getslInformation(DatabaseOperations dop,String  date){
        SQLiteDatabase SQ = dop.getReadableDatabase();
//        Cursor cursor = SQ.rawQuery("SELECT * from " + TableData.Tableinfo.TABLE_NAME_SLEEP, null, null);
        Cursor cursor=  SQ.rawQuery("Select * FROM " + TableData.Tableinfo.TABLE_NAME_SLEEP + " WHERE " + TableData.Tableinfo.SLEEP_DATE + "='" + date + "'", null);

        return cursor;
          }

    public ArrayList<DataItems> getInformation(DatabaseOperations dop,String date){
        ArrayList<DataItems> listData = new ArrayList<>();

        SQLiteDatabase SQ = dop.getReadableDatabase();
        Log.d("DatabasRead", "is it null  //  "+date);
        Cursor cursor=  SQ.rawQuery("Select * FROM " + TableData.Tableinfo.TABLE_NAME_FOOD + " WHERE " + TableData.Tableinfo.DATE + "='" + date + "'", null);

//        String[] coloumns = {TableData.Tableinfo.DISH, TableData.Tableinfo.DATE,TableData.Tableinfo.TIME,TableData.Tableinfo.DISH_ID,};
//        Cursor cursor = SQ.rawQuery("SELECT * from " + TableData.Tableinfo.TABLE_NAME_FOOD , null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                //create a new movie object and retrieve the data from the cursor to be stored in this movie object
                DataItems item = new DataItems();
                item.setId(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.ID)));
                item.setFood_id(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.FOOD_ID)));
                item.setFood_name(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.FOOD_NAME)));
                item.setAmount(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.AMOUNT)));
                item.setTime_consumed(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.TIME_CONSUMED)));
                item.setDate(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.DATE)));
                item.setSynced(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.SYNCED)));
                Log.d("Database read", listData.toString());
                listData.add(item);

            }
            while (cursor.moveToNext());
        }
        return listData;

    }
    public ArrayList<DataItems> getExerciseInformation(DatabaseOperations dop,String date){
        ArrayList<DataItems> listData = new ArrayList<>();

        SQLiteDatabase SQ = dop.getReadableDatabase();
        Log.d("DatabasRead", "is it null  //  "+date);
        Cursor cursor=  SQ.rawQuery("Select * FROM " + TableData.Tableinfo.TABLE_NAME_EXERCISE + " WHERE " + TableData.Tableinfo.EXER_DATE + "='" + date + "'", null);

//        String[] coloumns = {TableData.Tableinfo.DISH, TableData.Tableinfo.DATE,TableData.Tableinfo.TIME,TableData.Tableinfo.DISH_ID,};
//        Cursor cursor = SQ.rawQuery("SELECT * from " + TableData.Tableinfo.TABLE_NAME_FOOD , null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                //create a new movie object and retrieve the data from the cursor to be stored in this movie object
                DataItems item = new DataItems();
                item.setExercise_uniq_id(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.EXER_UNIQUE_ID)));
                item.setExercise_id(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.EXER_ID)));
                item.setExercise_name(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.EXER_NAME)));
                item.setExercise_date(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.EXER_DATE)));
                item.setExercise_weight(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.WEIGHT)));
                item.setExercise_sets(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.SETS)));
                item.setExercise_reps(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.REPS)));
                Log.d("Database read", listData.toString());
                listData.add(item);

            }
            while (cursor.moveToNext());
        }
        return listData;

    }

    public ArrayList<DataItems> getsleepInformation(DatabaseOperations dop,String date){
        ArrayList<DataItems> listData = new ArrayList<>();

        SQLiteDatabase SQ = dop.getReadableDatabase();
        Log.d("DatabasRead", "is it null  //  "+date);
        Cursor cursor=  SQ.rawQuery("Select * FROM " + TableData.Tableinfo.TABLE_NAME_SLEEP + " WHERE " + TableData.Tableinfo.SLEEP_DATE + "='" + date + "'", null);

//        String[] coloumns = {TableData.Tableinfo.DISH, TableData.Tableinfo.DATE,TableData.Tableinfo.TIME,TableData.Tableinfo.DISH_ID,};
//        Cursor cursor = SQ.rawQuery("SELECT * from " + TableData.Tableinfo.TABLE_NAME_FOOD , null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                //create a new movie object and retrieve the data from the cursor to be stored in this movie object
                DataItems item = new DataItems();
                item.setSleep_unique(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.SLEEP_UNIQUE_ID)));
                item.setSleep_start(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.START_TIME)));
                item.setSleep_end(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.END_TIME)));
                item.setSleep_date(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.SLEEP_DATE)));
                item.setSleep_hours(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.SLEEP_HOURS)));
                item.setSleep_quality(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.SLEEP_QUALITY)));
                Log.d("Database read", listData.toString());
                listData.add(item);

            }
            while (cursor.moveToNext());
        }
        return listData;

    }

    public int getProfilesCount(DatabaseOperations dop,String id) {
        SQLiteDatabase SQ = dop.getReadableDatabase();
        Cursor cursor=  SQ.rawQuery("Select * FROM " + TableData.Tableinfo.TABLE_NAME_SLEEP + " WHERE " + TableData.Tableinfo.SLEEP_UNIQUE_ID + "='" + id + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public  void updateRow(DatabaseOperations dop,ContentValues cv,String id){
        SQLiteDatabase SQ = dop.getWritableDatabase();
        SQ.update(TableData.Tableinfo.TABLE_NAME_FOOD, cv, TableData.Tableinfo.ID + "=" + id, null);
        Log.d("update","true"+cv.toString()+"///"+id);
    }

    public  void updateSleepRow(DatabaseOperations dop,ContentValues cv,String id){
        SQLiteDatabase SQ = dop.getWritableDatabase();
        SQ.update(TableData.Tableinfo.TABLE_NAME_SLEEP, cv, TableData.Tableinfo.SLEEP_UNIQUE_ID + "=" + id, null);
        Log.d("update","true"+cv.toString()+"///"+id);
    }
    public  void updateExerRow(DatabaseOperations dop,ContentValues cv,String id){
        SQLiteDatabase SQ = dop.getWritableDatabase();
        SQ.update(TableData.Tableinfo.TABLE_NAME_EXERCISE, cv, TableData.Tableinfo.EXER_UNIQUE_ID + "=" + id, null);
        Log.d("update","true"+cv.toString()+"///"+id);
    }

    public  void deleteRow(DatabaseOperations dop,String tableName,String param,String id){
        SQLiteDatabase SQ = dop.getWritableDatabase();
//        SQ.update(TableData.Tableinfo.TABLE_NAME_SLEEP, cv, TableData.Tableinfo.SLEEP_UNIQUE_ID + "=" + id, null);
        SQ.delete(tableName,param + "=" + id, null);
        Log.d("deleted","true"+"///"+id);
    }


}