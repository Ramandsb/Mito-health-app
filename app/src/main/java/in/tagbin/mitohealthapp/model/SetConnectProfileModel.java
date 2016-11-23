package in.tagbin.mitohealthapp.model;

import java.util.ArrayList;

/**
 * Created by aasaqt on 15/8/16.
 */
public class SetConnectProfileModel {
    String gender;
    double[] location = {77.8880, 29.8543};
    Images1Model images;
    int[] age_range;
    int maximum_distance;
    String occupation,description,home_town;

    public void setAge_range(int[] age_range) {
        this.age_range = age_range;
    }

    public void setMaximum_distance(int maximum_distance) {
        this.maximum_distance = maximum_distance;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public void setHometown(String hometown) {
        this.home_town = hometown;
    }

    public SetConnectProfileModel(){
        images = new Images1Model();
    }
    public class Images1Model{
        String master;
        ArrayList<String> others = new ArrayList<String>();
        public void setMaster(String master) {
            this.master = master;
        }

        public void setOther(ArrayList<String> other) {
            this.others = other;
        }

        public ArrayList<String> getOther() {
            return others;
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
