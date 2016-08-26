package in.tagbin.mitohealthapp.model;

/**
 * Created by hp on 8/25/2016.
 */
public class SleepTimeconsumed {
    String start="";
    String end="";
    double qos=0.0;

    public double getQos() {
        return qos;
    }

    public String getEnd() {
        return end;
    }

    public String getStart() {
        return start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setQos(double qos) {
        this.qos = qos;
    }

    public void setStart(String start) {
        this.start = start;
    }
}