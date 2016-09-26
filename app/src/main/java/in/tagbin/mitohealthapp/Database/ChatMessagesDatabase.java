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
import in.tagbin.mitohealthapp.model.MessagesModel;

/**
 * Created by aasaqt on 26/9/16.
 */
public class ChatMessagesDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME="ChatUsersMessages";
    private static final String TABLE_CHATUSERS ="chatmessages";
    //private static final String KEY_NAME = "user_name";
    private static final String KEY_ID = "user_id";
    private static final String KEY_USER ="user_user";
    private static final String KEY_MESSAGE ="user_message";
    private static final String KEY_TIME = "user_message_time";
    private static final String KEY_SOURCE ="user_message_source";

    private final ArrayList<MessagesModel> YC_LIST = new ArrayList<>();
    public ChatMessagesDatabase(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CHATUSERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +KEY_USER + " TEXT ,"+ KEY_MESSAGE+" TEXT,"+ KEY_TIME +" TEXT,"+
                KEY_SOURCE+" TEXT"+ ")";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHATUSERS);
        onCreate(db);
    }
    public void addChat(MessagesModel hb)
    {
        try {

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            //values.put(KEY_NAME, hb.getName());
            values.put(KEY_USER, hb.getName());
            values.put(KEY_MESSAGE, hb.getMessages());
            values.put(KEY_TIME, hb.getTime());
            values.put(KEY_SOURCE, hb.getSource());
//            values.put(KEY_PRESENCE_STATUS, hb.getPresence_status());
//            values.put(KEY_IMAGE, hb.getImage());
            db.insertWithOnConflict(TABLE_CHATUSERS, null, values,SQLiteDatabase.CONFLICT_IGNORE);
            db.close();

        } catch (SQLiteConstraintException e) {

            Log.e("99999999999999999999999999999999999999999", "9999999999999999999999999999999999999999");
            e.printStackTrace();
        }
    }
    public ArrayList<MessagesModel> getChatUsers(String user) {
        try {
            YC_LIST.clear();
            String selectQuery = "SELECT  * FROM " + TABLE_CHATUSERS+" WHERE "+KEY_USER+" = '"+user+"'";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    MessagesModel chatModel = new MessagesModel();
                    chatModel.setName(cursor.getString(1));
                    chatModel.setMessages(cursor.getString(2));
                    chatModel.setTime(cursor.getString(3));
                    chatModel.setSource(cursor.getString(4));
//                    chatModel.setPresence(cursor.getString(5));
//                    chatModel.setPresence_status(cursor.getString(6));
//                    chatModel.setImage(cursor.getString(7));
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

