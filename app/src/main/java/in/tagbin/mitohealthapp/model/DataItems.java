package in.tagbin.mitohealthapp.model;

/**
 * Created by admin pc on 15-07-2016.
 */
public class DataItems {
    String id, food_id, food_name, time_consumed, amount, date, synced, sleep_unique, sleep_start, sleep_end, sleep_date,sleep_hours,sleep_quality;
    private String exercise_name,exercise_id,exercise_uniq_id,exercise_date,exercise_weight,exercise_sets,exercise_reps,mealtype;


    public String getMealtype() {
        return mealtype;
    }

    public void setMealtype(String mealtype) {
        this.mealtype = mealtype;
    }

    public String getId() {
        return id;
    }


    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getFood_id() {
        return food_id;
    }

    public String getFood_name() {
        return food_name;
    }

    public String getSynced() {
        return synced;
    }

    public String getTime_consumed() {
        return time_consumed;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public void setSynced(String synced) {
        this.synced = synced;
    }

    public void setTime_consumed(String time_consumed) {
        this.time_consumed = time_consumed;
    }

    public String getSleep_date() {
        return sleep_date;
    }

    public String getSleep_end() {
        return sleep_end;
    }

    public String getSleep_start() {
        return sleep_start;
    }

    public String getSleep_unique() {
        return sleep_unique;
    }

    public void setSleep_date(String sleep_date) {
        this.sleep_date = sleep_date;
    }

    public void setSleep_end(String sleep_end) {
        this.sleep_end = sleep_end;
    }

    public void setSleep_start(String sleep_start) {
        this.sleep_start = sleep_start;
    }

    public void setSleep_unique(String sleep_unique) {
        this.sleep_unique = sleep_unique;
    }
    public void setExerciseName(String exercise_name) {
        this.exercise_name = exercise_name;
    }
    public String getExerciseName() {
        return exercise_name;
    }

    public String getExercise_date() {
        return exercise_date;
    }

    public String getExercise_id() {
        return exercise_id;
    }

    public String getExercise_name() {
        return exercise_name;
    }

    public String getSleep_hours() {
        return sleep_hours;
    }

    public void setSleep_hours(String sleep_hours) {
        this.sleep_hours = sleep_hours;
    }

    public String getSleep_quality() {
        return sleep_quality;
    }

    public void setSleep_quality(String sleep_quality) {
        this.sleep_quality = sleep_quality;
    }

    public String getExercise_reps() {
        return exercise_reps;

    }

    public String getExercise_sets() {
        return exercise_sets;
    }

    public String getExercise_uniq_id() {
        return exercise_uniq_id;
    }

    public String getExercise_weight() {
        return exercise_weight;
    }

    public void setExercise_date(String exercise_date) {
        this.exercise_date = exercise_date;
    }

    public void setExercise_id(String exercise_id) {
        this.exercise_id = exercise_id;
    }

    public void setExercise_name(String exercise_name) {
        this.exercise_name = exercise_name;
    }

    public void setExercise_reps(String exercise_reps) {
        this.exercise_reps = exercise_reps;
    }

    public void setExercise_sets(String exercise_sets) {
        this.exercise_sets = exercise_sets;
    }

    public void setExercise_uniq_id(String exercise_uniq_id) {
        this.exercise_uniq_id = exercise_uniq_id;
    }

    public void setExercise_weight(String exercise_weight) {
        this.exercise_weight = exercise_weight;
    }
}
