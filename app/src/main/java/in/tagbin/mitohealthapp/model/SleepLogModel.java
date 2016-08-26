package in.tagbin.mitohealthapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 8/23/2016.
 */
public class SleepLogModel {



    List<SleepTimeconsumed> data= new ArrayList<>();

    public List<SleepTimeconsumed> getList() {
        return data;
    }

    public void setList(List<SleepTimeconsumed> list) {
        this.data = list;
    }




}
