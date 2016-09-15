package in.tagbin.mitohealthapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.model.RecommendationModel;


public class RecipeDetailsFrag extends Fragment {
    TextView time,recipeDetails;
    String response;
    RecommendationModel.MealsModel data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_recipe_details, container, false);
        time =  (TextView) view.findViewById(R.id.preparation_time);
        recipeDetails =  (TextView) view.findViewById(R.id.set_recipe);
        if (getArguments().getString("response") != null){
            response = getArguments().getString("response");
            data = JsonUtils.objectify(response, RecommendationModel.MealsModel.class);
            time.setText(data.getComponent().getPreparation_time()+" mins");
            recipeDetails.setText(Html.fromHtml(data.getComponent().getRecipe()));
        }
        return view;
    }
}
