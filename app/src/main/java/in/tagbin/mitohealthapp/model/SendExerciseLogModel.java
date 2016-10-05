package in.tagbin.mitohealthapp.model;

/**
 * Created by aasaqt on 4/10/16.
 */

public class SendExerciseLogModel {
    String ltype,time_consumed;
    int c_id;
    float mets,calorie,amount;

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public void setCalorie(float calorie) {
        this.calorie = calorie;
    }

    public void setLtype(String ltype) {
        this.ltype = ltype;
    }

    public void setMets(float mets) {
        this.mets = mets;
    }

    public void setTimestamp(String timestamp) {
        this.time_consumed = timestamp;
    }
}
