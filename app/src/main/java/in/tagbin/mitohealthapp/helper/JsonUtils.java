package in.tagbin.mitohealthapp.helper;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by aasaqt on 9/8/16.
 */
public class JsonUtils {
    private static final String LOG_TAG = JsonUtils.class.getSimpleName();
    private static Gson M_GSON = new Gson();

    public static String jsonify(Object object) {
        return M_GSON.toJson(object);
    }

    public static <T> T objectify(String pJson, Class<T> pType) {
        try {
            return M_GSON.fromJson(pJson, pType);
        } catch (JsonSyntaxException e) {
            Log.d("catch", e.toString());
        }
        catch (Exception e){
            Log.d("catch", e.toString());
        }
        return null;
    }
}
