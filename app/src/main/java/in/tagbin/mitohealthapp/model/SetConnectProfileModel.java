package in.tagbin.mitohealthapp.model;

import java.util.ArrayList;

/**
 * Created by aasaqt on 15/8/16.
 */
public class SetConnectProfileModel {
    String gender;
    double[] location = {77.8880, 29.8543};
    Images1Model images;
    String occupation,description;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public SetConnectProfileModel(){
        images = new Images1Model();
    }
    public class Images1Model{
        String master = "http://2.bp.blogspot.com/-Wsm4_g8ra9s/TzUtuGQWs6I/AAAAAAAAAj0/Ji-gtwukVP8/s1600/django_admin_action.png";
        ArrayList<String> other = new ArrayList<String>();
        public void setMaster(String master) {
            this.master = master;
        }

        public void setOther(ArrayList<String> other) {
            this.other = other;
        }

        public ArrayList<String> getOther() {
            return other;
        }

        public String getMaster() {
            return master;
        }
    }
    public void setGender(String gender){
        this.gender = gender;
    }
    public void setImages(Images1Model images){
        this.images = images;
    }

    public Images1Model getImages() {
        return images;
    }
}
