package in.tagbin.mitohealthapp.helper;

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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.tagbin.mitohealthapp.CollapsableLogging;
import in.tagbin.mitohealthapp.FoodDetails;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.RecommendationModel;
import in.tagbin.mitohealthapp.model.SetFoodLoggerModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aasaqt on 15/9/16.
 */
public class FoodLoggerAdapter extends RecyclerView.Adapter<FoodLoggerAdapter.ViewHolder> {
    static Context mContext;
    List<RecommendationModel.MealsModel> mModel;
    GifImageView mProgressBar;
    private static final int TYPE_ITEM = 1;

    public FoodLoggerAdapter(Context pContext, List<RecommendationModel.MealsModel> pModel,GifImageView pProgressBar){
        mContext = pContext;
        mModel = pModel;
        mProgressBar = pProgressBar;
    }
    @Override
    public FoodLoggerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_food_recommended,parent,false);
            ViewHolder vhItem = new ViewHolder(v,viewType);
            return vhItem;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(FoodLoggerAdapter.ViewHolder holder, int position) {
        holder.itemView.setVisibility(View.VISIBLE);
        ((FoodLoggerAdapter.ViewHolder) holder).populateData(mModel.get(position), mContext,mProgressBar);

    }

    @Override
    public int getItemCount() {
        return mModel.size();
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
        CircleImageView circleImageView;
        GifImageView mProgressBar;
        RelativeLayout view;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            foodName = (TextView) itemView.findViewById(R.id.tvFoodName);
            quantity = (TextView) itemView.findViewById(R.id.tvFoodQuantity);
            calories = (TextView) itemView.findViewById(R.id.tvFoodCalories);
            accept = (ImageView) itemView.findViewById(R.id.ivFoodAccept);
            decline = (ImageView) itemView.findViewById(R.id.ivFoodDecline);
            refresh = (ImageView) itemView.findViewById(R.id.ivFoodRefresh);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.civFoodLogger);
            view = (RelativeLayout) itemView.findViewById(R.id.relativeViewRecommend);
            accept.setVisibility(View.GONE);
            decline.setVisibility(View.VISIBLE);
            refresh.setVisibility(View.GONE);
            view.setOnClickListener(this);
            decline.setOnClickListener(this);
        }

        public void populateData(RecommendationModel.MealsModel pModel, Context pContext,GifImageView pProgressBar){
            mModel = pModel;
            mContext = pContext;
            mProgressBar = pProgressBar;
            foodName.setText(pModel.getComponent().getName());
            quantity.setText(pModel.getAmount()+" "+pModel.getComponent().getServing_type().getServing_type());
            calories.setText(new DecimalFormat("##.#").format(pModel.getComponent().getTotal_energy()).toString()+" calories");
            Picasso.with(mContext).load(mModel.getComponent().getImage()).into(circleImageView);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.relativeViewRecommend:
                    Intent i = new Intent(mContext, FoodDetails.class);
                    i.putExtra("response",JsonUtils.jsonify(mModel));
                    i.putExtra("logger","logger");
                    mContext.startActivity(i);
                    break;
                case R.id.ivFoodDecline:
                    final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(mContext,R.style.AppCompatAlertDialogStyle);
                    alertDialog1.setTitle("Delete logged food");
                    alertDialog1.setMessage(" Are you sure you want to delete this logged food?");
                    alertDialog1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mProgressBar.setVisibility(View.VISIBLE);
                            Controller.deleteLogFood(mContext,mModel.getId(),mDeleteListener);
                        }
                    });
                    alertDialog1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog1.show();
                    break;
            }
        }
        RequestListener mDeleteListener = new RequestListener() {
            @Override
            public void onRequestStarted() {

            }

            @Override
            public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
                Log.d("food deleted",responseObject.toString());
                Intent i = new Intent(mContext,CollapsableLogging.class);
                i.putExtra("selection",0);
                mContext.startActivity(i);
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(mContext,"Food successfully deleted",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onRequestError(int errorCode, String message) {
                Log.d("food delete error",message);
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
