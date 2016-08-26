package in.tagbin.mitohealthapp.helper;

import android.util.SparseArray;

/**
 * Created by aasaqt on 9/8/16.
 */
public class UrlResolver {
    public static final String BASE_URL = "http://api.mitoapp.com/";
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
        endPointMapper.put(EndPoints.CONNECT_PROFILE,"v1/connect/profile/");
        endPointMapper.put(EndPoints.INTEREST,"v1/interest/list");
        endPointMapper.put(EndPoints.WATERLOG,"v1/logger/");
        endPointMapper.put(EndPoints.DATERANGEDATA,"v1/users/history/");
        endPointMapper.put(EndPoints.INTEREST,"v1/interest/");
        endPointMapper.put(EndPoints.UPLOAD,"v1/uploads/");
        endPointMapper.put(EndPoints.SLEEPLOG,"v1/logger/sleep/mass/");
    }

    public interface EndPoints {
        int NONE = -1;
        int EVENTS = 0;
        int USERS = 1;
        int CONNECT_PROFILE = 2;
        int INTEREST= 3;
        int WATERLOG= 4;
        int DATERANGEDATA =5;
        int UPLOAD = 6;
        int SLEEPLOG = 7;
    }
}
