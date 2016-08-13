package in.tagbin.mitohealthapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

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
}
