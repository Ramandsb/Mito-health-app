package in.tagbin.mitohealthapp;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.stetho.Stetho;

import org.joda.time.LocalDateTime;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class AppController extends Application {

    private RequestQueue mRequestQueue;
    public static final String TAG = AppController.class
			.getSimpleName();

    public LocalDateTime setDate,selectedDate;

    private static AppController mInstance;


    @Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
//        printKeyHash();

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());


    }

	public static synchronized AppController getInstance() {
		return mInstance;
	}



    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public void setDate(LocalDateTime setDate)
    {
        this.setDate=setDate;
    }

    /*getting the current week*/
    public LocalDateTime getDate()
    {
        return setDate;
    }

    /*getting the selected week*/

    public LocalDateTime getSelected()
    {
        return selectedDate;
    }


    /**
     * Setting selected week
     *
     * @param selectedDate
     */
    public void setSelected(LocalDateTime selectedDate)
    {
        this.selectedDate=selectedDate;
    }


    public String printKeyHash() {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key_Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }


    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
}
