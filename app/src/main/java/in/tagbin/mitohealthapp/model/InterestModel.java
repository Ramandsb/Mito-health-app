package in.tagbin.mitohealthapp.model;

import java.util.List;

/**
 * Created by aasaqt on 14/8/16.
 */
public class InterestModel {
    List<InterestListModel> list;
    public class InterestListModel{
        String name,title;
        int id;

        public String getTitle() {
            return title;
        }

        public String getName(){
            return this.name;
        }
        public int getId(){
            return this.id;
        }
    }
    public List<InterestListModel> getList(){
        return this.list;
    }
}
