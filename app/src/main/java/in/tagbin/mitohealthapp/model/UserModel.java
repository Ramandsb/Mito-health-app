package in.tagbin.mitohealthapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aasaqt on 7/9/16.
 */
public class UserModel {
    ProfileModel profile;
    List<String> energy;
    UserDetailsModel user;
    public UserModel(){
        profile = new ProfileModel();
        energy = new ArrayList<String>();
        user = new UserDetailsModel();
    }

    public List<String> getEnergy() {
        return energy;
    }

    public ProfileModel getProfile() {
        return profile;
    }

    public UserDetailsModel getUser() {
        return user;
    }

    public class ProfileModel{
        int id,user,age,goal_time,total_coins;
        float weight,waist,height,goal_weight;
        String phone_number,created_timestamp,gender,dob,updated_timestamp,email_hash;
        boolean is_nutritionist,email_varified;
        SetGoalModel goal;
        PrefernceModel preferences;
        List<CuisineModel> cuisines;
        ImagesModel images;
        public ProfileModel(){
            images = new ImagesModel();
        }
        public int getId() {
            return id;
        }

        public SetGoalModel getGoal() {
            return goal;
        }

        public PrefernceModel getPreferences() {
            return preferences;
        }

        public int getTotal_coins() {
            return total_coins;
        }

        public List<CuisineModel> getCuisines() {
            return cuisines;
        }

        public int getAge() {
            return age;
        }

        public int getGoal_time() {
            return goal_time;
        }

        public float getGoal_weight() {
            return goal_weight;
        }

        public float getHeight() {
            return height;
        }

        public float getUser() {
            return user;
        }

        public float getWaist() {
            return waist;
        }

        public ImagesModel getImages() {
            return images;
        }

        public float getWeight() {
            return weight;
        }

        public String getCreated_timestamp() {
            return created_timestamp;
        }

        public String getDob() {
            return dob;
        }

        public String getEmail_hash() {
            return email_hash;
        }

        public String getGender() {
            return gender;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public String getUpdated_timestamp() {
            return updated_timestamp;
        }

        public boolean is_nutritionist() {
            return is_nutritionist;
        }

        public boolean isEmail_varified() {
            return email_varified;
        }

        public class ImagesModel{
            String master;
            List<String> other;

            public List<String> getOther() {
                return other;
            }

            public String getMaster() {
                return master;
            }
        }
    }
    public class UserDetailsModel{
        int id;
        String username,first_name,last_name,email;

        public int getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getFirst_name() {
            return first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public String getUsername() {
            return username;
        }
    }
}
