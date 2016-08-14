package in.tagbin.mitohealthapp.Interfaces;

import android.content.Context;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import in.tagbin.mitohealthapp.app.Controller;


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

