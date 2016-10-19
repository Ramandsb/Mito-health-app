package in.tagbin.mitohealthapp.Interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import in.tagbin.mitohealthapp.activity.MainActivity;
import in.tagbin.mitohealthapp.activity.SettingsActivity;
import in.tagbin.mitohealthapp.activity.SplashActivity;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.PrefManager;


/**
 * Created by Adi on 7/5/2015.
 */
public class VolleyErrorListener implements Response.ErrorListener {

    private final static String CATEGORY_ERROR = "CATEGORY_ERROR";

    private Context mContext;
    private String mApiActionPath;
    private RequestListener mRequestListener;

    public VolleyErrorListener(Context pContext, String pApiActionPath,
                               RequestListener pRequestListener) {
        this.mContext = pContext;
        this.mApiActionPath = pApiActionPath;
        this.mRequestListener = pRequestListener;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        int errorCode;
        String message;
        if (error instanceof NoConnectionError) {
            errorCode = Controller.ERROR_CODES.NO_INTERNET;
            message = "Network Not Available";
        } else if (error == null || error.networkResponse == null) {
            if(error!=null && error.getMessage().equalsIgnoreCase("com.android.volley.AuthFailureError")) {
                errorCode = 401;
                message = "Authentication Failed";
            } else {
                errorCode = Controller.ERROR_CODES.SHIT_HAPPENED;
                message = "Error object is null";
            }
        } else {
            errorCode = error.networkResponse.statusCode;
            if (errorCode == 403){
                SharedPreferences loginDetails= mContext.getSharedPreferences(MainActivity.LOGIN_DETAILS,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=loginDetails.edit();
                editor.clear();
                editor.commit();
                PrefManager pref = new PrefManager(mContext);
                pref.clearSession();
                mContext.startActivity(new Intent(mContext,SplashActivity.class));
                ((Activity) mContext).finish();
            }
            if (error.networkResponse.data != null) {
                message = new String(error.networkResponse.data);
            } else {
                message = "Response data is null";
            }
        }
        if (mRequestListener != null) {
            mRequestListener.onRequestError(errorCode, message);
        }
    }
}

