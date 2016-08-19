package in.tagbin.mitohealthapp.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.Interfaces.VolleyErrorListener;
import in.tagbin.mitohealthapp.MainPage;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.NetworkUtils;
import in.tagbin.mitohealthapp.helper.UrlResolver;
import in.tagbin.mitohealthapp.model.ConfirmParticipantModel;
import in.tagbin.mitohealthapp.model.CreateEventSendModel;
import in.tagbin.mitohealthapp.model.JoinEventModel;
import in.tagbin.mitohealthapp.model.SetConnectProfileModel;

/**
 * Created by aasaqt on 9/8/16.
 */
public class Controller {
    private static Context mContext;
    static SharedPreferences loginDetails;
    private static final RetryPolicy DEFAULT_RETRY_POLICY = new RetryPolicy() {
        @Override
        public void retry(VolleyError error) throws VolleyError {
            if(error.networkResponse.statusCode == 401)
                throw new VolleyError(error);
        }

        @Override
        public int getCurrentTimeout() {
            return 0;
        }

        @Override
        public int getCurrentRetryCount() {
            return 3;
        }
    };
    private static RequestQueue mRequestQueue;

    private static NetworkResponse defNoInternetNetworkResponse = new NetworkResponse(
            ERROR_CODES.NO_INTERNET, "Network Not Available".getBytes(), null,
            false);

    public static final void init(Context context) {
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    private static Request<String> bundleToVolleyRequestNoCaching(
            final Context context, int request_method_type,
            final Object newRequest, String url, final RequestListener mListener) {
        StringBuffer buffer = new StringBuffer(url);
        buffer.replace(0, UrlResolver.BASE_URL.length() - 1, "response--");
        final String url_recieved = buffer.toString();
        // end

        Request<String> tempRequest = new JsonRequest<String>(
                request_method_type, url, JsonUtils.jsonify(newRequest),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                    }
                }, new VolleyErrorListener(context, url_recieved, mListener)) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Response<String> mResponse;
                if (response.statusCode == 200 || response.statusCode == 201) {
                    String responseBody = new String(response.data);
                    if (mListener != null)
                        mListener.onRequestCompleted(responseBody);
                    mResponse = Response.success(responseBody,
                            HttpHeaderParser.parseCacheHeaders(response));
                } else if(response.statusCode >= 400 && response.statusCode<= 500) {
                    parseNetworkError(new VolleyError(response));

                    mResponse = Response.error(new VolleyError(response));
                }else {
                    parseNetworkError(new VolleyError(response));
                    mResponse = Response.error(new VolleyError(response.toString()));
                }
                return mResponse;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                loginDetails= context.getSharedPreferences(MainPage.LOGIN_DETAILS,0);
                String key = loginDetails.getString("key",null);
                headers.put("Authorization","JWT "+key);
                return headers;
            }

        };
        return tempRequest;
    }

    private static void dispatchToQueue(Request<String> request,
                                        Context mContext) {
        if (!NetworkUtils.isNetworkConnected(mContext)) {
            VolleyError error = new VolleyError(defNoInternetNetworkResponse);
            if (request.getMethod() != Request.Method.GET) {
                request.deliverError(error);
            } else {
                request.setRetryPolicy(DEFAULT_RETRY_POLICY);
                mRequestQueue.add(request);
            }
        } else {
            request.setRetryPolicy(DEFAULT_RETRY_POLICY);
            mRequestQueue.add(request);
        }
    }
    public static void getEventsByMe(Context context,
                                       RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.EVENTS);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getAllEventsNearby(Context context,
                                       RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.EVENTS);
        url = url+"/location?location=[77.0266, 28.4595]";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getAllEvents(Context context,
                                       RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.EVENTS);
        url = url+"all/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void createEvent(Context context,CreateEventSendModel createEventSendModel,
                                      RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.EVENTS);

        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.POST, createEventSendModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void joinEvent(Context context,int event_id,
                                   RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.EVENTS);
        url = url+"join/";
        JoinEventModel joinEventModel = new JoinEventModel();
        joinEventModel.setEvent_id(event_id);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.POST, joinEventModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void editEvent(Context context,
                                   RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.EVENTS);
        url = url+"3/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.PUT, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getParticipants(Context context,int id,
                                 RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.EVENTS);
        url = url+"5/users/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void confirmParticipant(Context context,int event_id,int partipant_id,
                                       RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.EVENTS);
        url = url+event_id+"/users/";
        ConfirmParticipantModel confirmParticipantModel = new ConfirmParticipantModel();
        confirmParticipantModel.setId(partipant_id);
        confirmParticipantModel.setConfirm("1");
        confirmParticipantModel.setDecline("0");
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.PUT, confirmParticipantModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getUsersNearBy(Context context,
                                       RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.USERS);
        url = url+"nearby/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getConnectProfile(Context context,
                                      RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.CONNECT_PROFILE);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void setConnectProfile(Context context,SetConnectProfileModel setConnectProfileModel,
                                         RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.CONNECT_PROFILE);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.PUT, setConnectProfileModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getInterests(Context context,
                                         RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.INTEREST);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public interface ERROR_CODES {
        int BAD_REQUEST = 400;
        int UNAUTHORISED = 401;
        int UNAUTHORIZED_ACCESS = 403;
        int NOT_FOUND = 404;
        int USERNAME_NOT_AVAILABLE = 409;
        int SOURCE_FILE_DOESNT_EXIST = 920;
        int CUSTOM_ERROR_CODE = 1001;
        int SHIT_HAPPENED = 1022;
        int FILES_MISSING = 1023;
        int NO_INTERNET = 1025;
        int EMPTY_RESULTS = 1026;
        int API_FAILURE = 1027;

    }

}
