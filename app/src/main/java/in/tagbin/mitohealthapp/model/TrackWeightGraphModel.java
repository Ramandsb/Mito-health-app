package in.tagbin.mitohealthapp.model;

import java.util.List;

/**
 * Created by aasaqt on 11/11/16.
 */

public class TrackWeightGraphModel {
    List<VariationModel> variation;
    String end_date,start_date,message;
    float goal_weight,current_weight,start_weight;
    int goal_time;

    public int getGoal_time() {
        return goal_time;
    }

    public float getCurrent_weight() {
        return current_weight;
    }

    public float getGoal_weight() {
        return goal_weight;
    }

    public List<VariationModel> getVariation() {
        return variation;
    }

    public String getEnd_date() {
        return end_date;
    }

    public float getStart_weight() {
        return start_weight;
    }

    public String getMessage() {
        return message;
    }

    public String getStart_date() {
        return start_date;
    }

    public class VariationModel{
        float component_id;
        String time_consumed;

        public float getComponent_id() {
            return component_id;
        }

        public String getTime_consumed() {
            return time_consumed;
        }
    }
}
