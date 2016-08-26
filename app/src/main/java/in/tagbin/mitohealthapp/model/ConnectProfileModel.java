package in.tagbin.mitohealthapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aasaqt on 14/8/16.
 */
public class ConnectProfileModel {
    int id;
    String gender;
    ImagesModel images;
    String occupation,description,last_login,location;
    int age,compatibility;

    List<InterestProfileModel> interests;
    User1Model user;
    public ConnectProfileModel(){
        user = new User1Model();
        images = new ImagesModel();
        interests = new ArrayList<InterestProfileModel>();
    }

    public List<InterestProfileModel> getInterests() {
        return interests;
    }

    public String getLast_login() {
        return last_login;
    }

    public int getCompatibility() {
        return compatibility;
    }

    public User1Model getUser(){
        return this.user;
    }
    public int getId(){
        return this.id;
    }
    public int getAge(){
        return this.age;
    }
    public String getGender(){
        return this.gender;
    }
    public String getLocation(){
        return this.location;
    }

    public String getDescription() {
        return description;
    }

    public String getOccupation() {
        return occupation;
    }

    public ImagesModel getImages(){
        return this.images;
    }

    public class ImagesModel{
        String master;
        String[] other;
        public String getMaster(){
            return this.master;
        }
        public String[] getOthers(){
            return this.other;
        }
    }
    public class User1Model{
        int id;
        String first_name,last_name,username,email;
        public String getFirst_name(){
            return this.first_name;
        }
        public String getLast_name(){
            return this.last_name;
        }
        public String getUsername(){
            return this.username;
        }
        public String getEmail(){
            return this.email;
        }
        public int getId(){
            return this.id;
        }
    }
    public class InterestProfileModel{
        int user,id;
        Interest1Model interest;

        public int getId() {
            return id;
        }

        public int getUser() {
            return user;
        }

        public Interest1Model getInterest() {
            return interest;
        }

        public class Interest1Model{
            String name;
            int id;

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }
        }
    }
}
