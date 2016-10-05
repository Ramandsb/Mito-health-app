package in.tagbin.mitohealthapp.model;

/**
 * Created by aasaqt on 4/10/16.
 */

public class ExerciseLogModel {
    float mets,energy_burned,amount;
    ExerciseModel component;
    String time,type;
    int id;
    public ExerciseLogModel(){
        component = new ExerciseModel();
    }

    public int getId() {
        return id;
    }

    public ExerciseModel getComponent() {
        return component;
    }

    public float getAmount() {
        return amount;
    }

    public float getEnergy_burned() {
        return energy_burned;
    }

    public float getMets() {
        return mets;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }
}
