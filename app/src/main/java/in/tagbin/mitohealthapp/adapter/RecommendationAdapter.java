package in.tagbin.mitohealthapp.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.tagbin.mitohealthapp.activity.DailyDetailsActivity;
import in.tagbin.mitohealthapp.activity.FoodDetailsActivity;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.RecommendationModel;
import in.tagbin.mitohealthapp.model.SetFoodLoggerModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aasaqt on 10/9/16.
 */
public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.ViewHolder> {
    static Context mContext;
    List<RecommendationModel> mModel;
    String mType;
    int mPos;
    static GifImageView mProgressBar;
    private static final int TYPE_ITEM = 1;

    public RecommendationAdapter(Context pContext, List<RecommendationModel> pModel, String pType, List<String> measuring_units, GifImageView progressBar){
        mContext = pContext;
        mModel = pModel;
        mType = pType;
        if (measuring_units.size() >0) {
            for (int i = 0; i < measuring_units.size(); i++) {
                if (pType.equals(measuring_units.get(i))) {
                    mPos = i;
                }
            }
        }
        mProgressBar = progressBar;
    }
    @Override
    public RecommendationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_food_recommended,parent,false);
            ViewHolder vhItem = new ViewHolder(v,viewType);
            return vhItem;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecommendationAdapter.ViewHolder holder, int position) {
        if (mModel.get(mPos).getMeals().get(position).getFlag() == 0) {
            holder.itemView.setVisibility(View.VISIBLE);
            ((RecommendationAdapter.ViewHolder) holder).populateData(mModel.get(mPos).getMeals().get(position), mContext, mType, mProgressBar);
        }else{
            holder.itemView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (!mType.isEmpty() && mType != null && mModel.size() >0) {
            try {
                return mModel.get(mPos).getMeals().size();
            }catch (IndexOutOfBoundsException e){
                return 0;
            }
        }else
            return 0;
    }
    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context mContext;
        RecommendationModel.MealsModel mModel;
        TextView foodName,quantity,calories;
        ImageView accept,decline,refresh;
        String mType;
        GifImageView mProgressBar;
        RelativeLayout view;
        CircleImageView circleImageView;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            foodName = (TextView) itemView.findViewById(R.id.tvFoodName);
            quantity = (TextView) itemView.findViewById(R.id.tvFoodQuantity);
            calories = (TextView) itemView.findViewById(R.id.tvFoodCalories);
            accept = (ImageView) itemView.findViewById(R.id.ivFoodAccept);
            decline = (ImageView) itemView.findViewById(R.id.ivFoodDecline);
            refresh = (ImageView) itemView.findViewById(R.id.ivFoodRefresh);
            view = (RelativeLayout) itemView.findViewById(R.id.relativeViewRecommend);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.civFoodLogger);
            accept.setOnClickListener(this);
            decline.setOnClickListener(this);
            refresh.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        public void populateData(RecommendationModel.MealsModel pModel, Context pContext, String pType,GifImageView progressBar){
            mModel = pModel;
            mContext = pContext;
            mType = pType;
            mProgressBar = progressBar;
            foodName.setText(pModel.getComponent().getName());
            quantity.setText(pModel.getAmount()+" "+pModel.getComponent().getServing_type().getServing_type());
            calories.setText(new DecimalFormat("##").format(pModel.getComponent().getTotal_energy()*pModel.getAmount()).toString()+" calories");
            Picasso.with(mContext).load(mModel.getComponent().getImage()).into(circleImageView);
            Calendar[] dates = new Calendar[4];
            int i = 0;
            while (i < 4){
                Calendar selDate = Calendar.getInstance();
                selDate.add(Calendar.DAY_OF_MONTH, -i);
                dates[i] = selDate;
                i++;
            }
            int day,month,year;
            PrefManager pref = new PrefManager(mContext);
            if (pref.getKeyDay() != 0 && pref.getKeyMonth() != 0 && pref.getKeyYear() != 0){
                day = pref.getKeyDay();
                month = pref.getKeyMonth();
                year = pref.getKeyYear();
            }else{
                Calendar calendar = Calendar.getInstance();
                day = calendar.get(Calendar.DAY_OF_MONTH);
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
            }
            Date date = new Date(year-1900,month,day);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            if (calendar.getTimeInMillis() > dates[0].getTimeInMillis() || calendar.getTimeInMillis() < dates[3].getTimeInMillis()){
                accept.setVisibility(View.GONE);
                decline.setVisibility(View.GONE);
                refresh.setVisibility(View.GONE);
            }else {
                //mSheetLayout.expandFab();
                accept.setVisibility(View.VISIBLE);
                decline.setVisibility(View.VISIBLE);
                refresh.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ivFoodAccept:
                    Intent i1 = new Intent(mContext, FoodDetailsActivity.class);
                    i1.putExtra("response", JsonUtils.jsonify(mModel));
                    mContext.startActivity(i1);
                    break;
                case R.id.ivFoodDecline:
                    final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(mContext,R.style.AppCompatAlertDialogStyle);
                    alertDialog1.setTitle("Delete Recommended food");
                    alertDialog1.setMessage(" Are you sure you want to delete this recommended food?");
                    alertDialog1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SetFoodLoggerModel setFoodLoggerModel1 = new SetFoodLoggerModel();
                            setFoodLoggerModel1.setLtype("food");
                            setFoodLoggerModel1.setC_id(mModel.getComponent().getId());
                            Calendar calendar1 = Calendar.getInstance();
                            long time1 = calendar1.getTime().getTime()/1000L;
                            Log.d("timesatmap",""+time1);
                            setFoodLoggerModel1.setTime_consumed(time1);
                            setFoodLoggerModel1.setServing_unit(mModel.getComponent().getServing_type().getId());
                            setFoodLoggerModel1.setAmount(mModel.getAmount());
                            setFoodLoggerModel1.setFlag(2);
                            setFoodLoggerModel1.setMeal_id(mModel.getMeal_id());
                            mProgressBar.setVisibility(View.VISIBLE);
                            Controller.setLogger(mContext,setFoodLoggerModel1,mFoodLoggerListener1);
                        }
                    });
                    alertDialog1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog1.show();

                    break;
                case R.id.ivFoodRefresh:
                    final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(mContext,R.style.AppCompatAlertDialogStyle);
                    alertDialog2.setTitle("Refresh Recommended food");
                    alertDialog2.setMessage(" Are you sure you want to refresh this recommended food?");
                    alertDialog2.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SetFoodLoggerModel setFoodLoggerModel2 = new SetFoodLoggerModel();
                            setFoodLoggerModel2.setLtype("food");
                            setFoodLoggerModel2.setC_id(mModel.getComponent().getId());
                            Calendar calendar2 = Calendar.getInstance();
                            long time2 = calendar2.getTime().getTime()/1000L;
                            Log.d("timesatmap",""+time2);
                            setFoodLoggerModel2.setTime_consumed(time2);
                            setFoodLoggerModel2.setAmount(mModel.getAmount());
                            setFoodLoggerModel2.setServing_unit(mModel.getComponent().getServing_type().getId());
                            setFoodLoggerModel2.setFlag(3);
                            setFoodLoggerModel2.setMeal_id(mModel.getMeal_id());
                            mProgressBar.setVisibility(View.VISIBLE);
                            Controller.setLogger(mContext,setFoodLoggerModel2,mFoodLoggerListener2);
                        }
                    });
                    alertDialog2.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog2.show();
                    break;
                case R.id.relativeViewRecommend:
                    Intent i = new Intent(mContext, FoodDetailsActivity.class);
                    i.putExtra("response",JsonUtils.jsonify(mModel));
                    mContext.startActivity(i);
                    break;
            }
        }
    }
    static RequestListener mFoodLoggerListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("food logger",responseObject.toString());
            Intent i = new Intent(mContext,DailyDetailsActivity.class);
            i.putExtra("selection",0);
            mContext.startActivity(i);
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(mContext,"Food successfully logged",Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("food logger error",message);
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
    static RequestListener mFoodLoggerListener1 = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("food logger",responseObject.toString());
            Intent i = new Intent(mContext,DailyDetailsActivity.class);
            i.putExtra("selection",0);
            mContext.startActivity(i);
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(mContext,"Food removed successfully",Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("food logger error",message);
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
    static RequestListener mFoodLoggerListener2 = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("food logger",responseObject.toString());
            Intent i = new Intent(mContext,DailyDetailsActivity.class);
            i.putExtra("selection",0);
            mContext.startActivity(i);
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(mContext,"Food successfully refreshed",Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("food logger error",message);
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
