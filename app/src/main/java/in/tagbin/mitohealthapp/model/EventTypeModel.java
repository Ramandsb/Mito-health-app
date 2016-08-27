package in.tagbin.mitohealthapp.model;

import java.util.List;

/**
 * Created by aasaqt on 27/8/16.
 */
public class EventTypeModel {
    List<InterestListModel> list;
    public class InterestListModel{
        String title;
        int id;

        public String getTitle() {
            return title;
        }

        public int getId(){
            return this.id;
        }
    }
    public List<InterestListModel> getList(){
        return this.list;
    }
}
