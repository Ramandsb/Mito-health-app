package in.tagbin.mitohealthapp.model;

import java.util.List;

/**
 * Created by aasaqt on 8/9/16.
 */
public class SendEditProfileModel {
    String first_name,last_name,email,dob,gender,height,waist,weight,goal_weight;
    List<Integer> cuisines;
    int preferences,goal_time;
    ImagesModel images;
    public SendEditProfileModel(){
        images = new ImagesModel();
    }
    public class ImagesModel{
        String master;

        public void setMaster(String master) {
            this.master = master;
        }
    }

    public void setGoal_time(int goal_time) {
        this.goal_time = goal_time;
    }

    public int getGoal_time() {
        return goal_time;
    }

    public void setImages(ImagesModel images) {
        this.images = images;
    }

    public ImagesModel getImages() {
        return images;
    }

    public void setCuisines(List<Integer> cuisines) {
        this.cuisines = cuisines;
    }

    public void setPreferences(int preferences) {
        this.preferences = preferences;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setGoal_weight(String goal_weight) {
        this.goal_weight = goal_weight;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setWaist(String waist) {
        this.waist = waist;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }


}
