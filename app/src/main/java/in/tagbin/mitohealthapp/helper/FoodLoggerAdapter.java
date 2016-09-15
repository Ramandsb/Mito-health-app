package in.tagbin.mitohealthapp.helper;

import android.app.Activity;
import android.content.Context;
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

import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

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
    private static final int TYPE_ITEM = 1;

    public FoodLoggerAdapter(Context pContext, List<RecommendationModel.MealsModel> pModel){
        mContext = pContext;
        mModel = pModel;
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
        ((FoodLoggerAdapter.ViewHolder) holder).populateData(mModel.get(position), mContext);

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
            view = (RelativeLayout) itemView.findViewById(R.id.relativeViewRecommend);
            accept.setVisibility(View.GONE);
            decline.setVisibility(View.GONE);
            refresh.setVisibility(View.GONE);
            view.setOnClickListener(this);
        }

        public void populateData(RecommendationModel.MealsModel pModel, Context pContext){
            mModel = pModel;
            mContext = pContext;
            foodName.setText(pModel.getComponent().getName());
            quantity.setText(pModel.getAmount()+" "+pModel.getComponent().getServing_type().getServing_type());
            calories.setText(new DecimalFormat("##.#").format(pModel.getComponent().getTotal_energy()).toString()+" calories");
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
            }
        }
    }

}
