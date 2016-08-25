package in.tagbin.mitohealthapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import in.tagbin.mitohealthapp.model.ParticipantModel;

/**
 * Created by aasaqt on 13/8/16.
 */
public class PrefManager {
    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    private static Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "MITO_APP";
    private static final String KEY_TUT_SHOWN= "tut_shown";
    private static final String KEY_MASTER_IMAGE = "master_image";
    private static final String KEY_USER_PIC1 = "user_pic1";
    private static final String KEY_USER_PIC2 = "user_pic2";
    private static final String KEY_USER_PIC3 = "user_pic3";
    private static final String KEY_USER_PIC4 = "user_pic4";
    private static final String KEY_USER_PIC5 = "user_pic5";
    private static final String KEY_USER_PIC6 = "user_pic6";
    private static final String KEY_INTERESTS = "key_interests";

    public PrefManager(Context ctx){
        _context = ctx;
        pref = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
    }
    public static void init(Context ctx) {
        _context = ctx;
        pref = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
    }
    public void setTutorial(boolean tokenToServer){
        editor.putBoolean(KEY_TUT_SHOWN,tokenToServer);
        editor.commit();
    }
    public boolean isTutorialShown(){
        return pref.getBoolean(KEY_TUT_SHOWN,false);
    }

    public void setKeyUserPic1(String keyUserPic1) {
        editor.putString(KEY_USER_PIC1,keyUserPic1);
        editor.commit();
    }
    public void setKeyUserPic2(String keyUserPic1) {
        editor.putString(KEY_USER_PIC2,keyUserPic1);
        editor.commit();
    }
    public void setKeyUserPic3(String keyUserPic1) {
        editor.putString(KEY_USER_PIC3,keyUserPic1);
        editor.commit();
    }
    public void setKeyUserPic4(String keyUserPic1) {
        editor.putString(KEY_USER_PIC4,keyUserPic1);
        editor.commit();
    }
    public void setKeyUserPic5(String keyUserPic1) {
        editor.putString(KEY_USER_PIC5,keyUserPic1);
        editor.commit();
    }
    public void setKeyUserPic6(String keyUserPic1) {
        editor.putString(KEY_USER_PIC6,keyUserPic1);
        editor.commit();
    }
    public void setKeyMasterImage(String keyUserPic1) {
        editor.putString(KEY_MASTER_IMAGE,keyUserPic1);
        editor.commit();
    }

    public String getKeyMasterImage() {
        return pref.getString(KEY_MASTER_IMAGE,null);
    }

    public String getKeyUserPic1() {
        return pref.getString(KEY_USER_PIC1,null);
    }
    public String getKeyUserPic2() {
        return pref.getString(KEY_USER_PIC2,null);
    }
    public String getKeyUserPic3() {
        return pref.getString(KEY_USER_PIC3,null);
    }
    public String getKeyUserPic4() {
        return pref.getString(KEY_USER_PIC4,null);
    }
    public String getKeyUserPic5() {
        return pref.getString(KEY_USER_PIC5,null);
    }
    public String getKeyUserPic6() {
        return pref.getString(KEY_USER_PIC6,null);
    }
    public void setKeyInterests(List<Integer> interests){
        editor.putString(KEY_INTERESTS,JsonUtils.jsonify(interests));
        editor.commit();
    }
    public List<Integer> getInterests(){
        String response =  pref.getString(KEY_INTERESTS,null);
        Type collectionType = new TypeToken<List<Integer>>() {}.getType();
        List<Integer> interests = (List<Integer>) new Gson() .fromJson(response, collectionType);
        return interests;
    }
}
