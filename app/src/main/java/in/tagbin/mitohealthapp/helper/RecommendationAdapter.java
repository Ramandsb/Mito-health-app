package in.tagbin.mitohealthapp.helper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import in.tagbin.mitohealthapp.FoodDetails;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.model.RecommendationModel;

/**
 * Created by aasaqt on 10/9/16.
 */
public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.ViewHolder> {
    Context mContext;
    List<RecommendationModel> mModel;
    String mType;
    int mPos;
    private static final int TYPE_ITEM = 1;

    public RecommendationAdapter(Context pContext, List<RecommendationModel> pModel, String pType,int pos){
        mContext = pContext;
        mModel = pModel;
        mType = pType;
        mPos = pos;
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
            holder.itemView.setVisibility(View.VISIBLE);
            ((RecommendationAdapter.ViewHolder) holder).populateData(mModel.get(mPos).getMeals().get(position), mContext, mType);

    }

    @Override
    public int getItemCount() {
        if (!mType.isEmpty() && mType != null && mModel.size() >0)
            return mModel.get(mPos).getMeals().size();
        else
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
            accept.setOnClickListener(this);
            decline.setOnClickListener(this);
            refresh.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        public void populateData(RecommendationModel.MealsModel pModel, Context pContext, String pType){
            mModel = pModel;
            mContext = pContext;
            mType = pType;
            foodName.setText(pModel.getComponent().getName());
            quantity.setText(pModel.getAmount()+" "+pModel.getComponent().getServing_type().getServing_type());
            calories.setText(new DecimalFormat("##.#").format(pModel.getComponent().getTotal_energy()).toString()+" calories");
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ivFoodAccept:

                    break;
                case R.id.ivFoodDecline:

                    break;
                case R.id.ivFoodRefresh:

                    break;
                case R.id.relativeViewRecommend:
                    Intent i = new Intent(mContext, FoodDetails.class);
                    i.putExtra("response",JsonUtils.jsonify(mModel));
                    mContext.startActivity(i);
                    break;
            }
        }
    }
}
