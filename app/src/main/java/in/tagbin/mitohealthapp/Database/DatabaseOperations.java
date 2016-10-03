package in.tagbin.mitohealthapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import in.tagbin.mitohealthapp.model.DataItems;
import in.tagbin.mitohealthapp.model.FoodHeaders;
import in.tagbin.mitohealthapp.model.ChatAccounts;
import in.tagbin.mitohealthapp.model.DateRangeDataModel;

/**
 * Created by admin pc on 15-07-2016.
 */
public class DatabaseOperations extends SQLiteOpenHelper {


    public DatabaseOperations(Context context) {
        super(context, TableData.Tableinfo.DATABASE_NAME, null, database_version);
    }


    public static final int database_version = 2;
    public String CREATE_TABLE= "CREATE TABLE " + TableData.Tableinfo.TABLE_NAME_FOOD + "(" + TableData.Tableinfo.ID + " TEXT," + TableData.Tableinfo.FOOD_ID + " TEXT," + TableData.Tableinfo.FOOD_NAME + " TEXT," + TableData.Tableinfo.TIME_CONSUMED + " TEXT," + TableData.Tableinfo.AMOUNT + " TEXT," + TableData.Tableinfo.DATE + " TEXT," + TableData.Tableinfo.SYNCED + " TEXT)";
    public String CREATE_SLEEP_TABLE= "CREATE TABLE " + TableData.Tableinfo.TABLE_NAME_SLEEP + "(" + TableData.Tableinfo.SLEEP_UNIQUE_ID + " TEXT," + TableData.Tableinfo.START_TIME + " TEXT," + TableData.Tableinfo.END_TIME + " TEXT," + TableData.Tableinfo.SLEEP_DATE + " TEXT," + TableData.Tableinfo.SLEEP_HOURS + " TEXT," + TableData.Tableinfo.SLEEP_QUALITY + " TEXT," + TableData.Tableinfo.START_TIME_STAMP + " TEXT," + TableData.Tableinfo.END_TIME_STAMP + " TEXT)";
    public String CREATE_EXERCISE_TABLE= "CREATE TABLE " + TableData.Tableinfo.TABLE_NAME_EXERCISE + "(" + TableData.Tableinfo.EXER_UNIQUE_ID + " TEXT," + TableData.Tableinfo.EXER_NAME + " TEXT," + TableData.Tableinfo.WEIGHT + " TEXT," + TableData.Tableinfo.SETS + " TEXT," + TableData.Tableinfo.REPS + " TEXT," + TableData.Tableinfo.EXER_ID + " TEXT," + TableData.Tableinfo.EXER_DATE + " TEXT)";
    public String CREATE_WATER_TABLE= "CREATE TABLE " + TableData.Tableinfo.TABLE_NAME_WATER + "(" + TableData.Tableinfo.WATER_UNIQUE_ID + " TEXT," + TableData.Tableinfo.WATER_DATE + " TEXT," + TableData.Tableinfo.GLASSES + " TEXT," + TableData.Tableinfo.ML + " TEXT," + TableData.Tableinfo.GLASS_SIZE + " TEXT," + TableData.Tableinfo.WATER_SYNCED + " TEXT)";
    public String CREATE_CHART_TABLE= "CREATE TABLE " + TableData.Tableinfo.TABLE_NAME_CHART + "(" + TableData.Tableinfo.CHART_DATE + " TEXT," + TableData.Tableinfo.CHART_TIMESTAMP + " TEXT," + TableData.Tableinfo.CHART_FOODCAL_REQ + " TEXT," + TableData.Tableinfo.CHART_FOODCAL_CONSUMED + " TEXT," + TableData.Tableinfo.CHART_WATER_REQ + " TEXT," + TableData.Tableinfo.CHART_WATER_CONSUMED + " TEXT," + TableData.Tableinfo.CHART_EXERCAL_REQ + " TEXT," + TableData.Tableinfo.CHART_EXERCAL_BURNED + " TEXT," + TableData.Tableinfo.CHART_SLEEP_REQ + " TEXT," + TableData.Tableinfo.CHART_SLEEP_CONSUMED + " TEXT)";
    public String CREATE_CHAT_TABLE= "CREATE TABLE IF NOT EXISTS " + TableData.Tableinfo.TABLE_NAME_CHAT + " (" + TableData.Tableinfo.CHAT_NAME + " TEXT," + TableData.Tableinfo.CHAT_USER + " TEXT NOT NULL UNIQUE," + TableData.Tableinfo.CHAT_STATUS + " TEXT," + TableData.Tableinfo.CHAT_TYPE + " TEXT," + TableData.Tableinfo.CHAT_PRESENCE + " TEXT," + TableData.Tableinfo.CHAT_PRESENCE_STATUS + " TEXT)";
    public String CREATE_CM_TABLE= "CREATE TABLE IF NOT EXISTS " + TableData.Tableinfo.TABLE_NAME_CM + " (" + TableData.Tableinfo.CM_SOURCE + " TEXT," + TableData.Tableinfo.CM_TIME + " TEXT," + TableData.Tableinfo.CM_MESSAGES + " TEXT," + TableData.Tableinfo.CM_USER + " TEXT)";
    public String CREATE_FEELINGS_TABLE= "CREATE TABLE IF NOT EXISTS " + TableData.Tableinfo.TABLE_NAME_FEELINGS + " (" + TableData.Tableinfo.STRESS + " TEXT," + TableData.Tableinfo.HAPPINESS + " TEXT," + TableData.Tableinfo.ENERGY + " TEXT," + TableData.Tableinfo.CONFIDENCE + " TEXT," + TableData.Tableinfo.AVERAGE + " TEXT," + TableData.Tableinfo.FEELINGS_DATE + " TEXT NOT NULL UNIQUE," + TableData.Tableinfo.FEELING_SYNCED + " TEXT)";

    @Override
    public void onCreate(SQLiteDatabase sdb) {
        sdb.execSQL(CREATE_TABLE);
        sdb.execSQL(CREATE_SLEEP_TABLE);
        sdb.execSQL(CREATE_EXERCISE_TABLE);
        sdb.execSQL(CREATE_WATER_TABLE);
        sdb.execSQL(CREATE_CHART_TABLE);
        sdb.execSQL(CREATE_CHAT_TABLE);
        sdb.execSQL(CREATE_CM_TABLE);
        sdb.execSQL(CREATE_FEELINGS_TABLE);

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
    public void putCMInformation(DatabaseOperations dop, String source, String time, String messages,String user) {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableData.Tableinfo.CM_SOURCE, source);
        cv.put(TableData.Tableinfo.CM_TIME, time);
        cv.put(TableData.Tableinfo.CM_MESSAGES, messages);
        cv.put(TableData.Tableinfo.CM_USER, user);
        long k = SQ.insert(TableData.Tableinfo.TABLE_NAME_CM, null, cv);
        Log.d("Database Created", "true");

    }
    public void putFeelingsInformation(DatabaseOperations dop, String date, String stress, String happiness,String energy,String confidence,String average,String synced) {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableData.Tableinfo.FEELINGS_DATE, date);
        cv.put(TableData.Tableinfo.STRESS, stress);
        cv.put(TableData.Tableinfo.HAPPINESS, happiness);
        cv.put(TableData.Tableinfo.ENERGY, energy);
        cv.put(TableData.Tableinfo.CONFIDENCE, confidence);
        cv.put(TableData.Tableinfo.AVERAGE, average);
        cv.put(TableData.Tableinfo.FEELING_SYNCED, synced);
        long k = SQ.insert(TableData.Tableinfo.TABLE_NAME_FEELINGS, null, cv);
        Log.d("Database Created", "true");

    }
    public void putChatInformation(DatabaseOperations dop, String name, String user, String status,String type,String presence,String presence_type) {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableData.Tableinfo.CHAT_NAME, name);
        cv.put(TableData.Tableinfo.CHAT_USER, user);
        cv.put(TableData.Tableinfo.CHAT_STATUS, status);
        cv.put(TableData.Tableinfo.CHAT_TYPE, type);
        cv.put(TableData.Tableinfo.CHAT_PRESENCE, presence);
        cv.put(TableData.Tableinfo.CHAT_PRESENCE_STATUS, presence_type);
        long k = SQ.insert(TableData.Tableinfo.TABLE_NAME_CHAT, null, cv);
        Log.d("Database Created", "true");

    }

    public void ClearChartTable(DatabaseOperations dop){
        SQLiteDatabase SQ = dop.getWritableDatabase();
        SQ.delete(TableData.Tableinfo.TABLE_NAME_CHART,null,null);
    }
    public void putChartInformation(DatabaseOperations dop, String date, String timestamp, String fcal_req,String fcal_con,String wat_req,String water_con,String exer_req,String exer_con,String sleep_req,String sleep_con) {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableData.Tableinfo.CHART_DATE, date);
        cv.put(TableData.Tableinfo.CHART_TIMESTAMP, timestamp);
        cv.put(TableData.Tableinfo.CHART_FOODCAL_REQ, fcal_req);
        cv.put(TableData.Tableinfo.CHART_FOODCAL_CONSUMED, fcal_con);
        cv.put(TableData.Tableinfo.CHART_WATER_REQ, wat_req);
        cv.put(TableData.Tableinfo.CHART_WATER_CONSUMED, water_con);
        cv.put(TableData.Tableinfo.CHART_EXERCAL_REQ, exer_req);
        cv.put(TableData.Tableinfo.CHART_EXERCAL_BURNED, exer_con);
        cv.put(TableData.Tableinfo.CHART_SLEEP_REQ, sleep_req);
        cv.put(TableData.Tableinfo.CHART_SLEEP_CONSUMED, sleep_con);
        long k = SQ.insert(TableData.Tableinfo.TABLE_NAME_CHART, null, cv);
        Log.d("Database Created", "true");

    }
    public void putWaterInformation(DatabaseOperations dop, String unique_id, String water_date, String glasses,String ml,String glass_size,String synced) {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableData.Tableinfo.WATER_UNIQUE_ID, unique_id);
        cv.put(TableData.Tableinfo.WATER_DATE, water_date);
        cv.put(TableData.Tableinfo.GLASSES, glasses);
        cv.put(TableData.Tableinfo.ML, ml);
        cv.put(TableData.Tableinfo.GLASS_SIZE, glass_size);
        cv.put(TableData.Tableinfo.WATER_SYNCED, synced);
        long k = SQ.insert(TableData.Tableinfo.TABLE_NAME_WATER, null, cv);
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
    public void putSleepInformation(DatabaseOperations dop, String id, String start, String end,String date,String hours,String quality,String sts,String ets) {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableData.Tableinfo.SLEEP_UNIQUE_ID, id);
        cv.put(TableData.Tableinfo.START_TIME, start);
        cv.put(TableData.Tableinfo.END_TIME, end);
        cv.put(TableData.Tableinfo.SLEEP_DATE, date);
        cv.put(TableData.Tableinfo.SLEEP_HOURS, hours);
        cv.put(TableData.Tableinfo.SLEEP_QUALITY, quality);
        cv.put(TableData.Tableinfo.START_TIME_STAMP, sts);
        cv.put(TableData.Tableinfo.END_TIME_STAMP, ets);
        long k = SQ.insert(TableData.Tableinfo.TABLE_NAME_SLEEP, null, cv);
        Log.d("Database Created", "true");

    }

    public Cursor getslInformation(DatabaseOperations dop,String  date){
        SQLiteDatabase SQ = dop.getReadableDatabase();
//        Cursor cursor = SQ.rawQuery("SELECT * from " + TableData.Tableinfo.TABLE_NAME_SLEEP, null, null);
        Cursor cursor=  SQ.rawQuery("Select * FROM " + TableData.Tableinfo.TABLE_NAME_SLEEP + " WHERE " + TableData.Tableinfo.SLEEP_DATE + "='" + date + "'", null);

        return cursor;
          }

    public Cursor getWaterInformation(DatabaseOperations dop,String  date){
        SQLiteDatabase SQ = dop.getReadableDatabase();
//        Cursor cursor = SQ.rawQuery("SELECT * from " + TableData.Tableinfo.TABLE_NAME_SLEEP, null, null);
        Cursor cursor=  SQ.rawQuery("Select * FROM " + TableData.Tableinfo.TABLE_NAME_WATER + " WHERE " + TableData.Tableinfo.WATER_DATE + "='" + date + "'", null);

        return cursor;
    }

    public Cursor getFeelingsInformation(DatabaseOperations dop,String source){
        SQLiteDatabase SQ = dop.getReadableDatabase();
//        Cursor cursor = SQ.rawQuery("SELECT * from " + TableData.Tableinfo.TABLE_NAME_FEELINGS, null, null);
//        Cursor cursor=  SQ.rawQuery("Select * FROM " + TableData.Tableinfo.TABLE_NAME_FEELINGS + " WHERE " + TableData.Tableinfo.FEELINGS_DATE + "='" + date + "'", null);
      Cursor  cursor=  SQ.rawQuery("Select * FROM " + TableData.Tableinfo.TABLE_NAME_FEELINGS + " WHERE " + TableData.Tableinfo.FEELING_SYNCED + "='" + source + "'", null);

        return cursor;
    }
    public Cursor getFeelingsInformationByDate(DatabaseOperations dop,String date){
        SQLiteDatabase SQ = dop.getReadableDatabase();
//        Cursor cursor = SQ.rawQuery("SELECT * from " + TableData.Tableinfo.TABLE_NAME_FEELINGS, null, null);
//        Cursor cursor=  SQ.rawQuery("Select * FROM " + TableData.Tableinfo.TABLE_NAME_FEELINGS + " WHERE " + TableData.Tableinfo.FEELINGS_DATE + "='" + date + "'", null);
        Cursor  cursor=  SQ.rawQuery("Select * FROM " + TableData.Tableinfo.TABLE_NAME_FEELINGS + " WHERE " + TableData.Tableinfo.FEELINGS_DATE + "='" + date + "'", null);

        return cursor;
    }

    public Cursor getCompleteWaterInformation(DatabaseOperations dop,String source){
        Log.d("source",source);
        Cursor cursor = null;

        SQLiteDatabase SQ = dop.getReadableDatabase();
//        Cursor cursor = SQ.rawQuery("SELECT * from " + TableData.Tableinfo.TABLE_NAME_SLEEP, null, null);
        if (source.equals("all")){
            cursor=  SQ.rawQuery("Select * FROM " + TableData.Tableinfo.TABLE_NAME_WATER , null);
return cursor;

        }else if (source.equals("no")){
            cursor=  SQ.rawQuery("Select * FROM " + TableData.Tableinfo.TABLE_NAME_WATER + " WHERE " + TableData.Tableinfo.WATER_SYNCED + "='" + source + "'", null);
return cursor;
        }



        return cursor;
    }

    public Cursor getCompleteFeelingInformation(DatabaseOperations dop,String source){
        Log.d("source",source);
        Cursor cursor = null;

        SQLiteDatabase SQ = dop.getReadableDatabase();
//        Cursor cursor = SQ.rawQuery("SELECT * from " + TableData.Tableinfo.TABLE_NAME_SLEEP, null, null);
        if (source.equals("all")){

            cursor=  SQ.rawQuery("Select * FROM " + TableData.Tableinfo.TABLE_NAME_FEELINGS , null);
            return cursor;

        }else if (source.equals("no")){
            cursor=  SQ.rawQuery("Select * FROM " + TableData.Tableinfo.TABLE_NAME_FEELINGS + " WHERE " + TableData.Tableinfo.FEELING_SYNCED + "='" + source + "'"+" ORDER BY "+ TableData.Tableinfo.FEELINGS_DATE +" ASC", null);
            return cursor;
        }



        return cursor;
    }
    public Cursor getCompleteSleepInformation(DatabaseOperations dop){
        SQLiteDatabase SQ = dop.getReadableDatabase();
//        Cursor cursor = SQ.rawQuery("SELECT * from " + TableData.Tableinfo.TABLE_NAME_SLEEP, null, null);
        Cursor cursor=  SQ.rawQuery("Select * FROM " + TableData.Tableinfo.TABLE_NAME_SLEEP, null);

        return cursor;
    }
//    public Cursor getCompleteFeelingInformation(DatabaseOperations dop){
//        SQLiteDatabase SQ = dop.getReadableDatabase();
////        Cursor cursor = SQ.rawQuery("SELECT * from " + TableData.Tableinfo.TABLE_NAME_SLEEP, null, null);
//        Cursor cursor=  SQ.rawQuery("Select * FROM " + TableData.Tableinfo.TABLE_NAME_FEELINGS, null);
//
//        return cursor;
//    }

    public ArrayList<DataItems> getInformation(DatabaseOperations dop,String date){
        ArrayList<DataItems> listData = new ArrayList<>();

        int i=0;
        boolean b=false,l=false,d=false;
        SQLiteDatabase SQ = dop.getReadableDatabase();
        Log.d("DatabasRead", "is it null  //  "+date);
        Cursor cursor=  SQ.rawQuery("Select * FROM " + TableData.Tableinfo.TABLE_NAME_FOOD + " WHERE " + TableData.Tableinfo.DATE + "='" + date + "' ORDER BY "+ TableData.Tableinfo.TIME_CONSUMED +" ASC", null);

//        String[] coloumns = {TableData.Tableinfo.DISH, TableData.Tableinfo.DATE,TableData.Tableinfo.TIME,TableData.Tableinfo.DISH_ID,};
//        Cursor cursor = SQ.rawQuery("SELECT * from " + TableData.Tableinfo.TABLE_NAME_FOOD , null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                //create a new movie object and retrieve the data from the cursor to be stored in this movie object
                DataItems item = new DataItems();
                FoodHeaders headers= new FoodHeaders();

                item.setId(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.ID)));
                item.setFood_id(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.FOOD_ID)));
                item.setFood_name(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.FOOD_NAME)));
                item.setAmount(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.AMOUNT)));
                item.setTime_consumed(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.TIME_CONSUMED)));
                item.setDate(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.DATE)));
                item.setSynced(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.SYNCED)));

//                headers.setMealtype("headers");
//                item.setMealtype("item");
                Log.d("Database read", listData.toString());


                if (compareTime(item.getTime_consumed(),"1473152434","1473156034")){

                    headers.setHeader("header");
                    headers.setMealtype("BreakFast");
                    listData.add(headers);

                }else if (compareTime(item.getTime_consumed(),"1473123634","1473127234")){
                    headers.setHeader("header");
                    headers.setMealtype("Lunch");
                    listData.add(headers);

                }else {
                    headers.setHeader("header");
                    headers.setMealtype("Meal "+(i++));
                    listData.add(headers);


                }


                listData.add(item);

            }
            while (cursor.moveToNext());
        }
        return listData;

    }

    public boolean compareTime(String time,String lower,String higher){
        int input =Integer.valueOf(time);
        int lowerinput =Integer.valueOf(lower);
        int higherinput =Integer.valueOf(higher);

        if (input>=lowerinput && input<=higherinput){

            return true;

        }
        return false;
    }



//    public ArrayList<MessagesModel> getCMInformation(DatabaseOperations dop, String user){
//        ArrayList<MessagesModel> listData = new ArrayList<>();
//
//        SQLiteDatabase SQ = dop.getReadableDatabase();
//        Log.d("DatabasRead", "is it null  //  "+user);
//        Cursor cursor=  SQ.rawQuery("Select * FROM " + TableData.Tableinfo.TABLE_NAME_CM + " WHERE " + TableData.Tableinfo.CM_USER + "='" + user + "'", null);
//
////        String[] coloumns = {TableData.Tableinfo.DISH, TableData.Tableinfo.DATE,TableData.Tableinfo.TIME,TableData.Tableinfo.DISH_ID,};
////        Cursor cursor = SQ.rawQuery("SELECT * from " + TableData.Tableinfo.TABLE_NAME_FOOD , null, null);
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                //create a new movie object and retrieve the data from the cursor to be stored in this movie object
//                MessagesModel item = new MessagesModel();
//                item.setSource(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CM_SOURCE)));
//                item.setTime(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CM_TIME)));
//                item.setMessages(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CM_MESSAGES)));
//                item.setUser_id(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CM_USER)));
//                Log.d("Database read", listData.toString());
//                listData.add(item);
//
//            }
//            while (cursor.moveToNext());
//        }
//        return listData;
//
//    }

    public ArrayList<ChatAccounts> getChatInformation(DatabaseOperations dop){
        ArrayList<ChatAccounts> listData = new ArrayList<>();

        SQLiteDatabase SQ = dop.getReadableDatabase();
        Log.d("DatabasRead", "is it null  //  ");
        Cursor cursor=  SQ.rawQuery("Select * FROM " + TableData.Tableinfo.TABLE_NAME_CHAT , null);

//        String[] coloumns = {TableData.Tableinfo.DISH, TableData.Tableinfo.DATE,TableData.Tableinfo.TIME,TableData.Tableinfo.DISH_ID,};
//        Cursor cursor = SQ.rawQuery("SELECT * from " + TableData.Tableinfo.TABLE_NAME_FOOD , null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                //create a new movie object and retrieve the data from the cursor to be stored in this movie object
                ChatAccounts item = new ChatAccounts();
                item.setName(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHAT_NAME)));
                item.setUser(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHAT_USER)));
                item.setStatus(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHAT_STATUS)));
                item.setType(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHAT_TYPE)));
                item.setPresence(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHAT_PRESENCE)));
                item.setPresence_status(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHAT_PRESENCE_STATUS)));
                Log.d("Database read", listData.toString());
                listData.add(item);

            }
            while (cursor.moveToNext());
        }
        return listData;

    }
    public Cursor getCompleteChartInfo(DatabaseOperations dop,String date){
        date=date+" 00:00:00";
        SQLiteDatabase SQ = dop.getReadableDatabase();
//        Cursor cursor = SQ.rawQuery("SELECT * from " + TableData.Tableinfo.TABLE_NAME_SLEEP, null, null);
        Cursor cursor=  SQ.rawQuery("Select * FROM " + TableData.Tableinfo.TABLE_NAME_CHART + " WHERE " + TableData.Tableinfo.CHART_DATE + "='" + date + "'", null);

        return cursor;
    }


    public ArrayList<DateRangeDataModel> getChartInformation(DatabaseOperations dop, String date){
        ArrayList<DateRangeDataModel> listData = new ArrayList<>();
String da="2016-08-23 00:00:00";
        SQLiteDatabase SQ = dop.getReadableDatabase();
        Log.d("DatabasRead", "is it null  //  "+da);
//        Cursor cursor=  SQ.rawQuery("Select * FROM " + TableData.Tableinfo.TABLE_NAME_CHART + " WHERE " + TableData.Tableinfo.CHART_DATE + "='" + da + "'", null);
        Cursor cursor=  SQ.rawQuery("Select * FROM " + TableData.Tableinfo.TABLE_NAME_CHART , null,null);
//        Cursor cursor = SQ.rawQuery("SELECT * from " + TableData.Tableinfo.TABLE_NAME_FOOD , null, null);
//        String[] coloumns = {TableData.Tableinfo.DISH, TableData.Tableinfo.DATE,TableData.Tableinfo.TIME,TableData.Tableinfo.DISH_ID,};
//        Cursor cursor = SQ.rawQuery("SELECT * from " + TableData.Tableinfo.TABLE_NAME_FOOD , null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                //create a new movie object and retrieve the data from the cursor to be stored in this movie object
                DateRangeDataModel item = new DateRangeDataModel();
                item.setDate(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHART_DATE)));
                item.setTimestamp(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHART_TIMESTAMP)));
                item.setFood_req(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHART_FOODCAL_REQ)));
                item.setFood_con(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHART_FOODCAL_CONSUMED)));
                item.setWater_req(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHART_WATER_REQ)));
                item.setWater_con(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHART_WATER_CONSUMED)));
                item.setExer_req(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHART_EXERCAL_REQ)));
                item.setExer_con(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHART_EXERCAL_BURNED)));
                item.setSleep_req(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHART_SLEEP_REQ)));
                item.setSleep_con(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHART_SLEEP_CONSUMED)));
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
    public int getCount(DatabaseOperations dop,String table_name,String unique_column,String id) {
        SQLiteDatabase SQ = dop.getReadableDatabase();
        Cursor cursor=  SQ.rawQuery("Select * FROM " + table_name + " WHERE " + unique_column + "='" + id + "'", null);
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
    public  void updateWaterRow(DatabaseOperations dop,ContentValues cv,String id){
        SQLiteDatabase SQ = dop.getWritableDatabase();
        SQ.update(TableData.Tableinfo.TABLE_NAME_WATER, cv, TableData.Tableinfo.WATER_UNIQUE_ID + "=" + id, null);
        Log.d("update","true"+cv.toString()+"///"+id);
    }
    public  void updateExerRow(DatabaseOperations dop,ContentValues cv,String id){
        SQLiteDatabase SQ = dop.getWritableDatabase();
        SQ.update(TableData.Tableinfo.TABLE_NAME_EXERCISE, cv, TableData.Tableinfo.EXER_UNIQUE_ID + "=" + id, null);
        Log.d("update","true"+cv.toString()+"///"+id);
    }
    public  void updateAnyRow(DatabaseOperations dop,String tablename,ContentValues cv,String unique_column,String id){
        SQLiteDatabase SQ = dop.getWritableDatabase();
        SQ.update(tablename, cv, unique_column + "=" + id, null);
        Log.d("update","true"+cv.toString()+"///"+id);
    }

    public  void deleteRow(DatabaseOperations dop,String tableName,String param,String id){
        SQLiteDatabase SQ = dop.getWritableDatabase();
//        SQ.update(TableData.Tableinfo.TABLE_NAME_SLEEP, cv, TableData.Tableinfo.SLEEP_UNIQUE_ID + "=" + id, null);
        SQ.delete(tableName,param + "=" + id, null);
        Log.d("deleted","true"+"///"+id);
    }


}