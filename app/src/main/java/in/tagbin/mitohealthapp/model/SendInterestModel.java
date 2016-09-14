package in.tagbin.mitohealthapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aasaqt on 7/9/16.
 */
public class SendInterestModel {
    List<Integer> add;
    List<Integer> delete;

    public SendInterestModel(){
        add = new ArrayList<Integer>();
        delete = new ArrayList<Integer>();
    }
    public List<Integer> getAdd() {
        return add;
    }

    public List<Integer> getDelete() {
        return delete;
    }

    public void setAdd(List<Integer> add) {
        this.add = add;
    }

    public void setDelete(List<Integer> delete) {
        this.delete = delete;
    }
}
