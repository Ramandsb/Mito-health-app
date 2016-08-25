package in.tagbin.mitohealthapp.model;

/**
 * Created by hp on 8/23/2016.
 */
public class WaterLogModel {

    int c_id=0;
    double time_consumed=0.0;

    public double getTime_consumed() {
        return time_consumed;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public int getC_id() {
        return c_id;
    }

    public void setTime_consumed(double time_consumed) {
        this.time_consumed = time_consumed;
    }
}
