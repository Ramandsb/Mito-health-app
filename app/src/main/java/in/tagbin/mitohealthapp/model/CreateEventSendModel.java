package in.tagbin.mitohealthapp.model;

/**
 * Created by aasaqt on 10/8/16.
 */
public class CreateEventSendModel {
    public int event_type;
    public String timer,title,picture,type,description;
    public long event_time;
    public int capacity;
    double[] location;

    public void setLocation(double[] location) {
        this.location = location;
    }
}
