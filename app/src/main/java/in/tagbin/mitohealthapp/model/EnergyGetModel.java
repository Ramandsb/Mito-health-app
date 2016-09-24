package in.tagbin.mitohealthapp.model;

/**
 * Created by aasaqt on 20/9/16.
 */
public class EnergyGetModel {
    float water,calorie_consumed,calorie_required,calorie_burnt;
    int total_coins;

    public int getTotal_coins() {
        return total_coins;
    }

    public float getCalorie_burnt() {
        return calorie_burnt;
    }

    public float getCalorie_consumed() {
        return calorie_consumed;
    }

    public float getCalorie_required() {
        return calorie_required;
    }

    public float getWater() {
        return water;
    }
}
