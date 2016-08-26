package in.tagbin.mitohealthapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aasaqt on 26/8/16.
 */
public class ExploreModel {
    List<ConnectProfileModel> nearby_user_list;
    public ExploreModel(){
        nearby_user_list = new ArrayList<ConnectProfileModel>();
    }
    public List<ConnectProfileModel> getNearby_user_list() {
        return nearby_user_list;
    }

    public void setNearby_user_list(List<ConnectProfileModel> nearby_user_list) {
        this.nearby_user_list = nearby_user_list;
    }
}
