package in.tagbin.mitohealthapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import in.tagbin.mitohealthapp.model.ChatAccounts;

/**
 * Created by aasaqt on 22/9/16.
 */
public class ChatRequestsDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME="ChatUsersRequests";
    private static final String TABLE_CHATUSERS ="chatuserrequests";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_ID = "user_id";
    private static final String KEY_USER ="user_user";
    private static final String KEY_STATUS ="user_status";
    private static final String KEY_TYPE = "user_type";
    private static final String KEY_PRESENCE ="user_presence";
    private static final String KEY_PRESENCE_STATUS = "user_presence_status";

    private final ArrayList<ChatAccounts> YC_LIST = new ArrayList<>();
    public ChatRequestsDatabase(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CHATUSERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +KEY_USER + " TEXT UNIQUE," + KEY_NAME+ " TEXT,"
                + KEY_STATUS +" TEXT,"+ KEY_TYPE +" TEXT,"+ KEY_PRESENCE+" TEXT,"+KEY_PRESENCE_STATUS+" TEXT"+ ")";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHATUSERS);
        onCreate(db);
    }
    public void addChat(ChatAccounts hb)
    {
        try {

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_NAME, hb.getName());
            values.put(KEY_USER, hb.getUser());
            values.put(KEY_STATUS, hb.getStatus());
            values.put(KEY_TYPE, hb.getType());
            values.put(KEY_PRESENCE, hb.getPresence());
            values.put(KEY_PRESENCE_STATUS, hb.getPresence_status());
            db.insertWithOnConflict(TABLE_CHATUSERS, null, values,SQLiteDatabase.CONFLICT_IGNORE);
            db.close();

        } catch (SQLiteConstraintException e) {

            Log.e("99999999999999999999999999999999999999999", "9999999999999999999999999999999999999999");
            e.printStackTrace();
        }
    }
    public ArrayList<ChatAccounts> getChatUsers() {
        try {
            YC_LIST.clear();
            String selectQuery = "SELECT  * FROM " + TABLE_CHATUSERS+" WHERE "+KEY_USER+" IN ( SELECT DISTINCT ( "+KEY_USER+" ) FROM "+TABLE_CHATUSERS+" )";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    ChatAccounts chatModel = new ChatAccounts();
                    chatModel.setName(cursor.getString(1));
                    chatModel.setUser(cursor.getString(2));
                    chatModel.setStatus(cursor.getString(3));
                    chatModel.setType(cursor.getString(4));
                    chatModel.setPresence(cursor.getString(5));
                    chatModel.setPresence_status(cursor.getString(6));
//                    OfferNotificationModel dbhBean = new OfferNotificationModel();
//                    dbhBean.setGspot(cursor.getString(0));
//                    dbhBean.setDescription(cursor.getString(1));
//                    dbhBean.setTitle(cursor.getString(2));
//                    dbhBean.setDate(cursor.getString(3));
//                    dbhBean.setRest_name(cursor.getString(4));
//                    dbhBean.setShort_address(cursor.getString(5));
//                    dbhBean.setVtype(cursor.getString(6));
                    YC_LIST.add(chatModel);
                } while (cursor.moveToNext());
            }

            // return contact list
            cursor.close();
            db.close();
            return YC_LIST;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("=========================================================@@@@@@@@@@@@@@@@@", "" +"");
        }

        return YC_LIST;
    }
}

