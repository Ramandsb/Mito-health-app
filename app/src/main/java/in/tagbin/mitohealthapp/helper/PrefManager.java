package in.tagbin.mitohealthapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import in.tagbin.mitohealthapp.model.DieticainModel;
import in.tagbin.mitohealthapp.model.LocationModel;
import in.tagbin.mitohealthapp.model.LoginModel;
import in.tagbin.mitohealthapp.model.ParticipantModel;
import in.tagbin.mitohealthapp.model.PrefernceModel;
import in.tagbin.mitohealthapp.model.UserModel;

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
    private static final String KEY_TUT_SHOWN1= "tut_shown1";
    private static final String KEY_MASTER_IMAGE = "master_image";
    private static final String KEY_MASTER_CREATE = "create_image";
    private static final String KEY_USER_DETAILS = "user_model";
    private static final String KEY_USER_PIC1 = "user_pic1";
    private static final String KEY_USER_PIC2 = "user_pic2";
    private static final String KEY_USER_PIC3 = "user_pic3";
    private static final String KEY_USER_PIC4 = "user_pic4";
    private static final String KEY_USER_PIC5 = "user_pic5";
    private static final String KEY_USER_PIC6 = "user_pic6";
    private static final String KEY_COINS = "user_coins";
    private static final String KEY_INTERESTS = "key_interests";
    private static final String KEY_LOGIN = "login_object";
    private static final String KEY_DAY = "selected_day";
    private static final String KEY_MONTH = "selected_month";
    private static final String KEY_YEAR = "selected_year";
    private static String KEY_DIETICIAN = "key_deitician";
    private static String GCM_TOKEN = "gcm_token";
    private static String LOCATION_OBJECT = "location_object";
    private static String PREFERENCE = "preference_object";

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
    public void clearSession(){
        editor.clear();
        editor.commit();
    }
    public void setTutorial(boolean tokenToServer){
        editor.putBoolean(KEY_TUT_SHOWN,tokenToServer);
        editor.commit();
    }
    public boolean isTutorialShown(){
        return pref.getBoolean(KEY_TUT_SHOWN,false);
    }
    public void setTutorial1(boolean tokenToServer){
        editor.putBoolean(KEY_TUT_SHOWN1,tokenToServer);
        editor.commit();
    }
    public boolean isTutorialShown1(){
        return pref.getBoolean(KEY_TUT_SHOWN1,false);
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
    public void setKeyMasterCreate(String keyUserPic1) {
        editor.putString(KEY_MASTER_CREATE,keyUserPic1);
        editor.commit();
    }
    public void setKeyCoins(int keyUserPic1) {
        editor.putInt(KEY_COINS,keyUserPic1);
        editor.commit();
    }
    public void setKeyDay(int keyUserPic1) {
        editor.putInt(KEY_DAY,keyUserPic1);
        editor.commit();
    }
    public void setKeyMonth(int keyUserPic1) {
        editor.putInt(KEY_MONTH,keyUserPic1);
        editor.commit();
    }
    public void setKeyYear(int keyUserPic1) {
        editor.putInt(KEY_YEAR,keyUserPic1);
        editor.commit();
    }

    public static int getKeyDay() {
        return pref.getInt(KEY_DAY,0);
    }
    public static int getKeyMonth() {
        return pref.getInt(KEY_MONTH,0);
    }

    public static int getKeyYear() {
        return pref.getInt(KEY_YEAR,0);
    }
    public int getKeyCoins(){
        return pref.getInt(KEY_COINS,0);
    }
    public String getKeyMasterImage() {
        return pref.getString(KEY_MASTER_IMAGE,null);
    }
    public String getKeyMasterCreate() {
        return pref.getString(KEY_MASTER_CREATE,null);
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
    public void saveCurrentLocation (LocationModel locationModel) {
        String userJson = JsonUtils.jsonify(locationModel);
        editor.putString(LOCATION_OBJECT, userJson);
        editor.apply();
    }
    public LocationModel getCurrentLocationAsObject(){
        String userJson = pref.getString(LOCATION_OBJECT, null);
        LocationModel locationModel = JsonUtils.objectify(userJson,LocationModel.class);
        return locationModel;
    }
    public void saveCurrentPrefernces (List<PrefernceModel> locationModel) {
        String userJson = JsonUtils.jsonify(locationModel);
        editor.putString(PREFERENCE, userJson);
        editor.apply();
    }
    public List<PrefernceModel> getCurrentPreferenceAsObject(){
        String userJson = pref.getString(PREFERENCE, null);
        Type collectionType = new TypeToken<ArrayList<PrefernceModel>>() {
        }.getType();
        final List<PrefernceModel> diet_options = (ArrayList<PrefernceModel>) new Gson()
                .fromJson(userJson, collectionType);
        return diet_options;
    }
    public void saveDietician (DieticainModel locationModel) {
        String userJson = JsonUtils.jsonify(locationModel);
        editor.putString(KEY_DIETICIAN, userJson);
        editor.apply();
    }
    public DieticainModel getDietician(){
        String userJson = pref.getString(KEY_DIETICIAN, null);
        DieticainModel locationModel = JsonUtils.objectify(userJson,DieticainModel.class);
        return locationModel;
    }
    public void saveLoginModel (LoginModel loginModel) {
        String userJson = JsonUtils.jsonify(loginModel);
        editor.putString(KEY_LOGIN, userJson);
        editor.apply();
    }
    public LoginModel getLoginModel(){
        String userJson = pref.getString(KEY_LOGIN, null);
        LoginModel locationModel = JsonUtils.objectify(userJson,LoginModel.class);
        return locationModel;
    }
    public void setKeyUserDetails(UserModel userModel){
        editor.putString(KEY_USER_DETAILS,JsonUtils.jsonify(userModel));
        editor.commit();
    }
    public UserModel getKeyUserDetails(){
        String response = pref.getString(KEY_USER_DETAILS,null);
        UserModel userModel = JsonUtils.objectify(response,UserModel.class);
        return userModel;
    }
    public void sendTokenToServer(boolean tokenToServer){
        editor.putBoolean(GCM_TOKEN,tokenToServer);
        editor.commit();
    }
    public boolean isTOkenSend(){
        return pref.getBoolean(GCM_TOKEN,false);
    }
}
