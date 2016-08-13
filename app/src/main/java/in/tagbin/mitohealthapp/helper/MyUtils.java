package in.tagbin.mitohealthapp.helper;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.DisplayMetrics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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
    public static String getCityName(Context context,String location){
        int indexStartLong = location.indexOf("(");
        int endindexLong = location.indexOf("0 ");
        int indexStartLat = location.indexOf(")");
        double MyLong = Double.parseDouble(location.substring(indexStartLong+1,endindexLong+1));
        double MyLat = Double.parseDouble(location.substring(endindexLong+1,indexStartLat));
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String cityName;
        if (addresses != null)
            cityName = addresses.get(0).getAddressLine(0);
        else
            cityName = "";
        return cityName;
    }
}
