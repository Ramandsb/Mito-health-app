package in.tagbin.mitohealthapp.helper;

import android.util.SparseArray;

/**
 * Created by aasaqt on 9/8/16.
 */
public class UrlResolver {
    public static final String BASE_URL = "https://api.mitoapp.com/";
    public static SparseArray<String> endPointMapper = null;

    public static final String withAppendedPath(int endPoint) {
        if (endPointMapper == null)
            populateMapper();
        return BASE_URL + endPointMapper.get(endPoint);

    }

    private static void populateMapper() {
        endPointMapper = new SparseArray<String>();
        endPointMapper.put(EndPoints.EVENTS, "v1/events/");
        endPointMapper.put(EndPoints.USERS,"v1/users/");
        endPointMapper.put(EndPoints.CONNECT_PROFILE,"v1/connect/");
        endPointMapper.put(EndPoints.LOGGER,"v1/logger/");
        endPointMapper.put(EndPoints.ENERGY,"v1/users/energy/");
        endPointMapper.put(EndPoints.INTEREST,"v1/interest/");
        endPointMapper.put(EndPoints.UPLOAD,"v1/uploads/");
        endPointMapper.put(EndPoints.SLEEPLOG,"v1/logger/sleep/mass/");
        endPointMapper.put(EndPoints.FEELINGSLOG,"v1/logger/feeling/mass/");
        endPointMapper.put(EndPoints.GOALS,"v1/goals/");
        endPointMapper.put(EndPoints.DIET_PREFERENCE,"v1/diet_pref/");
        endPointMapper.put(EndPoints.CUISINES,"v1/cuisines/");
        endPointMapper.put(EndPoints.RECIPE,"v1/recipe/");
        endPointMapper.put(EndPoints.FOOD,"v1/food/");
        endPointMapper.put(EndPoints.GCM,"v1/users/carrier/");
        endPointMapper.put(EndPoints.EXERCISE,"v1/exercise/");
        endPointMapper.put(EndPoints.NUTRITIONIS,"v1/users/nutritionist/");
        endPointMapper.put(EndPoints.EVENTTYPE,"v1/eventtype/");
        endPointMapper.put(EndPoints.TRACK_WEIGHT,"v1/graphs/weight_graph/");
    }

    public interface EndPoints {
        int NONE = -1;
        int EVENTS = 0;
        int USERS = 1;
        int CONNECT_PROFILE = 2;
        int INTEREST= 3;
        int LOGGER = 4;
        int ENERGY =5;
        int UPLOAD = 6;
        int SLEEPLOG = 7;
        int FEELINGSLOG = 10;
        int GOALS = 11;
        int DIET_PREFERENCE = 12;
        int CUISINES = 13;
        int RECIPE = 14;
        int FOOD = 15;
        int GCM = 16;
        int EXERCISE = 17;
        int NUTRITIONIS = 18;
        int EVENTTYPE = 19;
        int TRACK_WEIGHT = 20;
    }
}
