package in.tagbin.mitohealthapp.model;

/**
 * Created by aasaqt on 15/8/16.
 */
public class SetConnectProfileModel {
    String gender;
    double[] location = {77.8880, 29.8543};
    Images1Model images;
    public SetConnectProfileModel(){
        images = new Images1Model();
    }
    public class Images1Model{
        String master = "http://2.bp.blogspot.com/-Wsm4_g8ra9s/TzUtuGQWs6I/AAAAAAAAAj0/Ji-gtwukVP8/s1600/django_admin_action.png";
    }
    public void setGender(String gender){
        this.gender = gender;
    }
    public void setImages(Images1Model images){
        this.images = images;
    }
}
