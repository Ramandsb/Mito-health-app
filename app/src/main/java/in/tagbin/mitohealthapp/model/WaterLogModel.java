package in.tagbin.mitohealthapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 8/23/2016.
 */
public class WaterLogModel {



    List<Timeconsumed> data= new ArrayList<>();

    public List<Timeconsumed> getList() {
        return data;
    }

    public void setList(List<Timeconsumed> list) {
        this.data = list;
    }




}
