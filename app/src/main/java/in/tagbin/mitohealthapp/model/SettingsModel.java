package in.tagbin.mitohealthapp.model;

/**
 * Created by aasaqt on 3/9/16.
 */
public class SettingsModel {
    boolean meals_notification,water_notification,sleep_notification,exercise_notification,weekly_progress_notification;
    boolean daily_health_tips_notification,show_me_on_explore,chat_notification;
    int /*people_connect_preference,maximum_distance,*/total_coins;
    //int[] age_range;

//    public int getPeople_connect_preference() {
//        return people_connect_preference;
//    }

    public int getTotal_coins() {
        return total_coins;
    }

//    public int[] getAge_range() {
//        return age_range;
//    }
//
//    public int getMaximum_distance() {
//        return maximum_distance;
//    }
//
//    public void setAge_range(int[] age_range) {
//        this.age_range = age_range;
//    }

    public void setChat_notification(boolean chat_notification) {
        this.chat_notification = chat_notification;
    }

    public void setDaily_health_tips_notification(boolean daily_health_tips_notification) {
        this.daily_health_tips_notification = daily_health_tips_notification;
    }

    public void setExercise_notification(boolean exercise_notification) {
        this.exercise_notification = exercise_notification;
    }

    public void setMeals_notification(boolean meals_notification) {
        this.meals_notification = meals_notification;
    }

//    public void setMaximum_distance(int maximum_distance) {
//        this.maximum_distance = maximum_distance;
//    }
//
//    public void setPeople_connect_preference(int people_connect_preference) {
//        this.people_connect_preference = people_connect_preference;
//    }

    public void setShow_me_on_explore(boolean show_me_on_explore) {
        this.show_me_on_explore = show_me_on_explore;
    }

    public void setSleep_notification(boolean sleep_notification) {
        this.sleep_notification = sleep_notification;
    }

    public void setWater_notification(boolean water_notification) {
        this.water_notification = water_notification;
    }

    public void setWeekly_progress_notification(boolean weekly_progress_notification) {
        this.weekly_progress_notification = weekly_progress_notification;
    }

    public boolean isShow_me_on_explore() {
        return show_me_on_explore;
    }

    public boolean isExercise_notification() {
        return exercise_notification;
    }

    public boolean isChat_notification() {
        return chat_notification;
    }

    public boolean isDaily_health_tips_notification() {
        return daily_health_tips_notification;
    }

    public boolean isMeals_notification() {
        return meals_notification;
    }

    public boolean isSleep_notification() {
        return sleep_notification;
    }

    public boolean isWater_notification() {
        return water_notification;
    }

    public boolean isWeekly_progress_notification() {
        return weekly_progress_notification;
    }
}
