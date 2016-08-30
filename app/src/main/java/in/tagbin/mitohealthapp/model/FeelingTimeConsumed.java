package in.tagbin.mitohealthapp.model;

/**
 * Created by hp on 8/25/2016.
 */
public class FeelingTimeConsumed {
    String stress,happiness,energy,confidence,time;

    public String getConfidence() {
        return confidence;
    }

    public String getEnergy() {
        return energy;
    }

    public String getHappiness() {
        return happiness;
    }

    public String getStress() {
        return stress;
    }

    public String getTime() {
        return time;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public void setEnergy(String energy) {
        this.energy = energy;
    }

    public void setHappiness(String happiness) {
        this.happiness = happiness;
    }

    public void setStress(String stress) {
        this.stress = stress;
    }

    public void setTime(String time) {
        this.time = time;
    }
}