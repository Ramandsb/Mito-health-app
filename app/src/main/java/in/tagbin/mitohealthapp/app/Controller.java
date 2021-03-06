package in.tagbin.mitohealthapp.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import java.text.ParseException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.Interfaces.VolleyErrorListener;
import in.tagbin.mitohealthapp.activity.MainActivity;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.NetworkUtils;
import in.tagbin.mitohealthapp.helper.UrlResolver;
import in.tagbin.mitohealthapp.model.ConfirmParticipantModel;
import in.tagbin.mitohealthapp.model.ConnectUserModel;
import in.tagbin.mitohealthapp.model.CreateEventSendModel;
import in.tagbin.mitohealthapp.model.FeelingLogModel;
import in.tagbin.mitohealthapp.model.FileUploadModel;
import in.tagbin.mitohealthapp.model.GcmSendModel;
import in.tagbin.mitohealthapp.model.JoinEventModel;
import in.tagbin.mitohealthapp.model.LoginModel;
import in.tagbin.mitohealthapp.model.RecipeFavouriteModel;
import in.tagbin.mitohealthapp.model.SendCuisineModel;
import in.tagbin.mitohealthapp.model.SendEditProfileModel;
import in.tagbin.mitohealthapp.model.SendExerciseLogModel;
import in.tagbin.mitohealthapp.model.SendGoalModel;
import in.tagbin.mitohealthapp.model.SendPrefernceModel;
import in.tagbin.mitohealthapp.model.SetConnectProfileModel;
import in.tagbin.mitohealthapp.model.SetFoodLoggerModel;
import in.tagbin.mitohealthapp.model.SetNewInterestModel;
import in.tagbin.mitohealthapp.model.SettingsModel;
import in.tagbin.mitohealthapp.model.SignUpModel;
import in.tagbin.mitohealthapp.model.SleepLogModel;
import in.tagbin.mitohealthapp.model.SocialModel;
import in.tagbin.mitohealthapp.model.UserChangePasswordModel;
import in.tagbin.mitohealthapp.model.UserDateModel;
import in.tagbin.mitohealthapp.model.UserGenderModel;
import in.tagbin.mitohealthapp.model.UserGoalWeightModel;
import in.tagbin.mitohealthapp.model.UserHeightModel;
import in.tagbin.mitohealthapp.model.UserNameModel;
import in.tagbin.mitohealthapp.model.UserWaistModel;
import in.tagbin.mitohealthapp.model.UserWeightModel;
import in.tagbin.mitohealthapp.model.WaterLogModel;

/**
 * Created by aasaqt on 9/8/16.
 */
public class Controller {
    private static Context mContext;
    static File mImageFile;
    static MultipartEntityBuilder mBuilder = MultipartEntityBuilder.create();
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

        Log.d("Post Json",JsonUtils.jsonify(newRequest));

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
                if (response.statusCode >= 200 && response.statusCode< 300) {
                    String responseBody = new String(response.data);
                    if (mListener != null)
                        try {
                            mListener.onRequestCompleted(responseBody);
                        } catch (JSONException e) {
                            Log.d("error from controller",e.toString());
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Log.d("error from controller",e.toString());
                        }
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
                loginDetails= context.getSharedPreferences(MainActivity.LOGIN_DETAILS,0);
                String key = loginDetails.getString("key",null);
                headers.put("Authorization","JWT "+key);
                return headers;
            }

        };
        return tempRequest;
    }
    private static Request<String> bundleToVolleyRequestNoCaching1(
            final Context context, int request_method_type,
            final Object newRequest, String url, final RequestListener mListener) {
        StringBuffer buffer = new StringBuffer(url);
        buffer.replace(0, UrlResolver.BASE_URL.length() - 1, "response--");
        final String url_recieved = buffer.toString();
        // end
        buildMultipartEntity();
        Request<String> tempRequest = new JsonRequest<String>(
                request_method_type, url, null,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                    }
                }, new VolleyErrorListener(context, url_recieved, mListener)) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Response<String> mResponse;
                if (response.statusCode >= 200 && response.statusCode<= 300) {
                    String responseBody = new String(response.data);
                    if (mListener != null)
                        try {
                            mListener.onRequestCompleted(responseBody);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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
                loginDetails= context.getSharedPreferences(MainActivity.LOGIN_DETAILS,0);
                String key = loginDetails.getString("key",null);
                headers.put("Authorization","JWT "+key);
                Log.d("key","JWT "+key);
                return headers;
            }

            @Override
            public byte[] getBody() {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                try {
                    mBuilder.build().writeTo(bos);
                } catch (IOException e) {
                    VolleyLog.e("IOException writing to ByteArrayOutputStream bos, building the multipart request.");
                }

                return bos.toByteArray();
            }

            @Override
            public String getBodyContentType() {
                String contentTypeHeader = mBuilder.build().getContentType().getValue();
                return contentTypeHeader;
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
    public static void setFacebookLogin(Context context,SocialModel socialModel,
                                RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.SOCIAL);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.POST, socialModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void loginByEmail(Context context,LoginModel socialModel,
                              RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.LOGIN);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.POST, socialModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void signup(Context context,SignUpModel socialModel,
                                        RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.USERS);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.POST, socialModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void forgotpassword(Context context,LoginModel socialModel,
                              RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.FORGOT);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.POST, socialModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getGoals(Context context,
                                      RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.GOALS);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void setGoal(Context context,int id,String user_id,
                                RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.USERS);
        url += user_id+"/";
        SendGoalModel sendGoalModel = new SendGoalModel();
        sendGoalModel.setGoal(id);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.PUT, sendGoalModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void setCuisines(Context context,List<Integer> selectedCuisnes,String user_id,
                               RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.USERS);
        url += user_id+"/";
        SendCuisineModel sendCuisineModel = new SendCuisineModel();
        sendCuisineModel.setCuisines(selectedCuisnes);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.PUT, sendCuisineModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void setPreferences(Context context,int prefernce_id,String user_id,
                                   RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.USERS);
        url += user_id+"/";
        SendPrefernceModel sendPrefernceModel = new SendPrefernceModel();
        sendPrefernceModel.setPreferences(prefernce_id);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.PUT, sendPrefernceModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void setUserDate(Context context,UserDateModel userDateModel,String user_id,
                                      RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.USERS);
        url += user_id+"/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.PUT, userDateModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void setUserGender(Context context, UserGenderModel userDateModel, String user_id,
                                     RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.USERS);
        url += user_id+"/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.PUT, userDateModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void setUserPassword(Context context, UserChangePasswordModel userDateModel, String user_id,
                                       RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.USERS);
        url += user_id+"/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.PUT, userDateModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void setUserName(Context context, UserNameModel userDateModel, String user_id,
                                   RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.USERS);
        url += user_id+"/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.PUT, userDateModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void setUserWeight(Context context, UserWeightModel userDateModel, String user_id,
                                     RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.USERS);
        url += user_id+"/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.PUT, userDateModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void setUserHeight(Context context, UserHeightModel userDateModel, String user_id,
                                     RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.USERS);
        url += user_id+"/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.PUT, userDateModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void setUserWaist(Context context, UserWaistModel userDateModel, String user_id,
                                    RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.USERS);
        url += user_id+"/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.PUT, userDateModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void setUserGoalWeight(Context context, UserGoalWeightModel userDateModel, String user_id,
                                         RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.USERS);
        url += user_id+"/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.PUT, userDateModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getUserDetails(Context context,String id,
                                     RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.USERS);
        url += id+"/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void setUserDetails(Context context,String id,SendEditProfileModel sendEditProfileModel,
                                      RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.USERS);
        url += id+"/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.PUT, sendEditProfileModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
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
    public static void getDietPrefernce(Context context,
                                     RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.DIET_PREFERENCE);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getCuisines(Context context,
                                        RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.CUISINES);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getAllEventsNearby(Context context,double latitude,double longitude,
                                       RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.EVENTS);
        url = url+"/location?location=["+longitude+","+latitude+"]&archive=2";
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
    public static void updateEvent(Context context,CreateEventSendModel createEventSendModel,int id,
                                   RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.EVENTS);
        url += id+"/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.PUT, createEventSendModel, url, requestListener);
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
    public static void archiveEvent(Context context,int event_id,
                                 RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.EVENTS);
        url = url+"archive/";
        JoinEventModel joinEventModel = new JoinEventModel();
        joinEventModel.setEvent_id(event_id);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.POST, joinEventModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void deleteEvent(Context context,int event_id,
                                    RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.EVENTS);
        url = url+event_id+"/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.DELETE, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getParticipants(Context context,int id,
                                 RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.EVENTS);
        url = url+id+"/users/";
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
    public static void getUsersNearBy(Context context,double longitude,double latitude,int page,
                                       RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.USERS);
        url = url+"nearby/custom?location=["+longitude+","+latitude+"]&page="+page;
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }

    public static void connectToUser(Context context,ConnectUserModel connectUserModel,
                                      RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.USERS);
        url = url+"connect/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.POST, connectUserModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getRequests(Context context,
                                     RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.USERS);
        url = url+"connect/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }

    public static void getConnectProfile(Context context,
                                      RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.CONNECT_PROFILE);
        url += "profile/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void setConnectProfile(Context context,SetConnectProfileModel setConnectProfileModel,
                                         RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.CONNECT_PROFILE);
        url += "profile/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.PUT, setConnectProfileModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getInterests(Context context,
                                         RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.INTEREST);
        url= url+"list/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getUserInterests(Context context,
                                    RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.INTEREST);
        //url= url+"list/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void setNewInterest(Context context,String name,
                                    RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.INTEREST);
        url= url+"create/";
        SetNewInterestModel setNewInterestModel = new SetNewInterestModel();
        setNewInterestModel.setName(name);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.POST, setNewInterestModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void setNewEventType(Context context,String name,
                                      RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.EVENTTYPE);
        url= url+"create/";
        SetNewInterestModel setNewInterestModel = new SetNewInterestModel();
        setNewInterestModel.setName(name);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.POST, setNewInterestModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void setNewExercise(Context context,String name,
                                       RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.EXERCISE);
        url= url+"create/";
        SetNewInterestModel setNewInterestModel = new SetNewInterestModel();
        setNewInterestModel.setName(name);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.POST, setNewInterestModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void setNewFood(Context context,String name,float calories,
                                      RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.FOOD);
        url= url+"create/";
        SetNewInterestModel setNewInterestModel = new SetNewInterestModel();
        setNewInterestModel.setName(name);
        setNewInterestModel.setTotal_calorie(calories);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.POST, setNewInterestModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getWaterLog(Context context,WaterLogModel waterLogModels,
                                    RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.LOGGER);
        url=url+"water/mass/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.POST, waterLogModels, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }

    public static void getFeelingLog(Context context,FeelingLogModel feelingLogModel,
                                   RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.FEELINGSLOG);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.POST, feelingLogModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }

    public static void getSleepLog(Context context,SleepLogModel sleepLogModel,
                                   RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.SLEEPLOG);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.POST, sleepLogModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getCaloriesDayWise(Context context, long startDate,
                                          RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.ENERGY);
        url=url+"?day="+startDate;
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getCaloriesDateWise(Context context, long startDate,long endDate,
                                          RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.ENERGY);
        url=url+"dates/?start="+startDate+"&end="+endDate;
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void setInterests(Context context,List<Integer> integers,
                                    RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.INTEREST);
        url = url+"mass/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.PUT, integers, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }

    public static void upoadPhot(Context context,FileUploadModel fileUploadModel,
                                    RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.UPLOAD);
        mImageFile = fileUploadModel.getFile();
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching1(
                context, Request.Method.POST, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    private static void buildMultipartEntity(){
        mBuilder.addBinaryBody("file", mImageFile, ContentType.create("image/jpeg"), mImageFile.getName());
        mBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        mBuilder.setLaxMode().setBoundary("xx").setCharset(Charset.forName("UTF-8"));
    }
    public static void setSettings(Context context,SettingsModel settingsModel,
                                   RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.USERS);
        url += "settings/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.PUT, settingsModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getExtendedFacebook(Context context,String accesstoken,
                                   RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.CONNECT_PROFILE);
        url += "?access_token="+accesstoken;
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getSettings(Context context,
                                   RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.USERS);
        url += "settings/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getLogger(Context context,long timestamp,
                                   RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.LOGGER);
        url += "history/?day="+timestamp+"&ltype=food";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getFoodRecommendation(Context context,long date,
                                 RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.USERS);
        url += "diet/day/?start="+date;
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void setFavourite(Context context,String like,int recipe_id,
                                             RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.RECIPE);
        url += like;
        RecipeFavouriteModel recipeFavouriteModel = new RecipeFavouriteModel();
        recipeFavouriteModel.setRecipe_id(recipe_id);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.POST, recipeFavouriteModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void setLogger(Context context,SetFoodLoggerModel setFoodLoggerModel,
                                    RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.LOGGER);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.POST, setFoodLoggerModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void updateLogFood(Context context,SetFoodLoggerModel setFoodLoggerModel,int id,
                                 RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.LOGGER);
        url += id+"/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.PUT, setFoodLoggerModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void deleteLogFood(Context context,int id,
                                     RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.LOGGER);
        url += id+"/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.DELETE, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getFoodDetails(Context context,int id,
                                     RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.FOOD);
        url += id+"/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getExerciseDetails(Context context,int id,
                                      RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.EXERCISE);
        url += id+"/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void setExerciseLogger(Context context,SendExerciseLogModel sendExerciseLogModel,
                                 RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.LOGGER);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.POST, sendExerciseLogModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getExerciseLogger(Context context,long timestamp,
                                 RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.LOGGER);
        url += "history/?day="+timestamp+"&ltype=exercise";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void updateLogExercise(Context context,SendExerciseLogModel sendExerciseLogModel,int id,
                                     RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.LOGGER);
        url += id+"/";
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.PUT, sendExerciseLogModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getDietician(Context context,
                                         RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.NUTRITIONIS);
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.GET, null, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void sendToken(Context context,String gcm_id,
                                      RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.GCM);
        GcmSendModel gcmSendModel = new GcmSendModel();
        gcmSendModel.setMobile_id(gcm_id);
        gcmSendModel.setMobile_os("Android");
        Request<String> volleyTypeRequest = bundleToVolleyRequestNoCaching(
                context, Request.Method.PUT, gcmSendModel, url, requestListener);
        volleyTypeRequest.setShouldCache(false);
        dispatchToQueue(volleyTypeRequest, context);
    }
    public static void getTrackWeight(Context context,
                                 RequestListener requestListener) {
        String url = UrlResolver
                .withAppendedPath(UrlResolver.EndPoints.TRACK_WEIGHT);
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
