package in.tagbin.mitohealthapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.text.ParseException;
import java.util.List;

import in.tagbin.mitohealthapp.activity.MainActivity;
import in.tagbin.mitohealthapp.activity.BinderActivity;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.SetGoalModel;
import in.tagbin.mitohealthapp.model.UserModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aasaqt on 8/9/16.
 */
public class SetGoalAdpater extends RecyclerView.Adapter<SetGoalAdpater.ViewHolder> {
    Context mContext;
    List<SetGoalModel> mModel;
    GifImageView mProgressBar;
    private static final int TYPE_ITEM = 1;

    public SetGoalAdpater(Context pContext, List<SetGoalModel> pModel, GifImageView progressBar) {
        mContext = pContext;
        mModel = pModel;
        mProgressBar = progressBar;
    }

    @Override
    public SetGoalAdpater.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_set_goal, parent, false);
            ViewHolder vhItem = new ViewHolder(v, viewType);
            return vhItem;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(SetGoalAdpater.ViewHolder holder, int position) {
        ((SetGoalAdpater.ViewHolder) holder).populateData(mModel.get(position),mContext,mProgressBar);
    }

    @Override
    public int getItemCount() {
        return mModel.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Context mContext;
        SetGoalModel mModel;
        RelativeLayout relativeLayout;
        TextView mainContent,arrow;
        SharedPreferences login_details;
        String user_id;
        GifImageView mProgressBar;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            mainContent = (TextView) itemView.findViewById(R.id.tvMainContentSetGoal);
            arrow = (TextView) itemView.findViewById(R.id.tvArrow);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.rlSetGoalView);
            arrow.setText(">");



        }

        public void populateData(SetGoalModel pModel, Context pContext,GifImageView progressBar) {
            mModel = pModel;
            mProgressBar = progressBar;
            mContext = pContext;
            mainContent.setText(mModel.getGoal());
            login_details = ((Activity)mContext).getSharedPreferences(MainActivity.LOGIN_DETAILS, Context.MODE_PRIVATE);
            user_id = login_details.getString("user_id", "");
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    Controller.setGoal(mContext,mModel.getId(),user_id,mSendGoalListener);
                }
            });
        }
        RequestListener mSendGoalListener = new RequestListener() {
            @Override
            public void onRequestStarted() {

            }

            @Override
            public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
                Log.d("send goal",responseObject.toString());
                PrefManager pref = new PrefManager(mContext);
                pref.setKeyUserDetails(JsonUtils.objectify(responseObject.toString(), UserModel.class));
                Intent intent = new Intent(mContext, BinderActivity.class);
                intent.putExtra("selection", 3);
                mContext.startActivity(intent);
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onRequestError(int errorCode, String message) {
                Log.d("send goal error",message);
                if (errorCode >= 400 && errorCode < 500) {
                    final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(mContext, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(mContext, "Internet connection error", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        };
    }
}
