package in.tagbin.mitohealthapp.model;

/**
 * Created by hp on 8/24/2016.
 */
public class DateRangeDataModel {
    String date="";
    String timestamp="";
    String food_req="";
    String food_con="";
    String water_req="";
    String water_con="";
    String exer_req="";
    String exer_con="";
    String sleep_req="";
    String sleep_con="";

    public String getDate() {
        return date;
    }

    public String getExer_con() {
        return exer_con;
    }

    public String getExer_req() {
        return exer_req;
    }

    public String getFood_con() {
        return food_con;
    }

    public String getFood_req() {
        return food_req;
    }

    public String getSleep_con() {
        return sleep_con;
    }

    public String getSleep_req() {
        return sleep_req;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getWater_con() {
        return water_con;
    }

    public String getWater_req() {
        return water_req;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setExer_con(String exer_con) {
        this.exer_con = exer_con;
    }

    public void setExer_req(String exer_req) {
        this.exer_req = exer_req;
    }

    public void setFood_con(String food_con) {
        this.food_con = food_con;
    }

    public void setFood_req(String food_req) {
        this.food_req = food_req;
    }

    public void setSleep_con(String sleep_con) {
        this.sleep_con = sleep_con;
    }

    public void setSleep_req(String sleep_req) {
        this.sleep_req = sleep_req;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setWater_con(String water_con) {
        this.water_con = water_con;
    }

    public void setWater_req(String water_req) {
        this.water_req = water_req;
    }
}
