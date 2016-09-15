package in.tagbin.mitohealthapp.helper;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.DisplayMetrics;
import android.util.Log;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * Created by aasaqt on 10/8/16.
 */
public class MyUtils {
    Context _context;

    public MyUtils(Context mContext) {
        _context = mContext;
    }

    public static int dpToPx(Context mCtx, int dp) {
        DisplayMetrics displayMetrics = mCtx.getResources().getDisplayMetrics();
        int px = Math.round(dp
                * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
    public static String getValidTime(String validDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName()));
            Date date = simpleDateFormat.parse(validDate);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("hh:mm a");
            String time = dateFormat1.format(date);
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getValidTimeForMeal(String validDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName()));
            Date date = simpleDateFormat.parse(validDate);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("hh:mm a");
            String time = dateFormat1.format(date);
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getValidDate(String validDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName()));
            Date date = simpleDateFormat.parse(validDate);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            String da = dateFormat.format(date);
            return da;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getDateFormat(Date date,String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String da = dateFormat.format(date);
        return da;
    }
    public static long getTimeinMillis(String validDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName()));
            Date date = simpleDateFormat.parse(validDate);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static long getUtcTimestamp(String validDate,String source) {
        Log.d("Date to Convert",validDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long da = 0;
        try {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = simpleDateFormat.parse(validDate);
            if (source.equals("s")){
                da= date.getTime()/1000;
            }else if (source.equals("m")){
                da= date.getTime();
            }

            return da;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return da;
    }
    public static String getCityName(Context context,String location){
        String[] parts1 = location.split(Pattern.quote("("));
        String location1 = parts1[1];
        String[] parts2 = location1.split(Pattern.quote(" "));
        String latitude = parts2[1];
        String[] parts3 = latitude.split(Pattern.quote(")"));
        double MyLong = Double.parseDouble(parts2[0]);
        double MyLat = Double.parseDouble(parts3[0]);
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String cityName;
        if (addresses != null)
            cityName = addresses.get(0).getAddressLine(1);
        else
            cityName = "";
        return cityName;
    }
    public static double getLongitude(Context context,String location){
        String[] parts1 = location.split(Pattern.quote("("));
        String location1 = parts1[1];
        String[] parts2 = location1.split(Pattern.quote(" "));
        String latitude = parts2[1];
        String[] parts3 = latitude.split(Pattern.quote(")"));
        double MyLong = Double.parseDouble(parts2[0]);
        return MyLong;
    }
    public static double getLatitude(Context context,String location){
        String[] parts1 = location.split(Pattern.quote("("));
        String location1 = parts1[1];
        String[] parts2 = location1.split(Pattern.quote(" "));
        String latitude = parts2[1];
        String[] parts3 = latitude.split(Pattern.quote(")"));
        double MyLat = Double.parseDouble(parts3[0]);
        return MyLat;
    }
    public static String getCityNameFromLatLng(Context context,double latitude,double longitude){

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String cityName;
        if (addresses != null)
            cityName = addresses.get(0).getAddressLine(0)+", "+addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2);
        else
            cityName = "";
        return cityName;
    }
    public static String getMinute(String validDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName()));
            Date date = simpleDateFormat.parse(validDate);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("mm");
            String time = dateFormat1.format(date);
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getHour(String validDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName()));
            Date date = simpleDateFormat.parse(validDate);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH");
            String time = dateFormat1.format(date);
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getDay(String validDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName()));
            Date date = simpleDateFormat.parse(validDate);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd");
            String time = dateFormat1.format(date);
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getYear(String validDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName()));
            Date date = simpleDateFormat.parse(validDate);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy");
            String time = dateFormat1.format(date);
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getMonth(String validDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName()));
            Date date = simpleDateFormat.parse(validDate);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM");
            String time = dateFormat1.format(date);
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "M") {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static  double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static void sendEvent(Context mContext, String eventName, HashMap<String,Object> eventHashmap){
        CleverTapAPI cleverTap = null;
        try {
            cleverTap = CleverTapAPI.getInstance(mContext);
            cleverTap.setDebugLevel(1);
        } catch (CleverTapMetaDataNotFoundException e) {
            e.printStackTrace();
        } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
            cleverTapPermissionsNotSatisfied.printStackTrace();
        }
        if (eventHashmap == null)
            cleverTap.event.push(eventName);
        else
            cleverTap.event.push(eventName,eventHashmap);
    }
}
