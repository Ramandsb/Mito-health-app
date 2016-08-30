package in.tagbin.mitohealthapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 8/23/2016.
 */
public class FeelingLogModel {



    List<FeelingTimeConsumed> data= new ArrayList<>();

    public List<FeelingTimeConsumed> getList() {
        return data;
    }

    public void setList(List<FeelingTimeConsumed> list) {
        this.data = list;
    }




}
