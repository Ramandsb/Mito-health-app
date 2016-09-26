package in.tagbin.mitohealthapp.model;

/**
 * Created by aasaqt on 14/9/16.
 */
public class SetFoodLoggerModel {
    String ltype;
    int c_id,amount,meal_id,flag,serving_unit;
    long time_consumed;

    public void setServing_unit(int serving_unit) {
        this.serving_unit = serving_unit;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public void setLtype(String ltype) {
        this.ltype = ltype;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setMeal_id(int meal_id) {
        this.meal_id = meal_id;
    }

    public void setTime_consumed(long time_consumed) {
        this.time_consumed = time_consumed;
    }
}
