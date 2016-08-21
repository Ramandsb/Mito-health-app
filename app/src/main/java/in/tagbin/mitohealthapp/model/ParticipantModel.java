package in.tagbin.mitohealthapp.model;

import java.util.ArrayList;

import in.tagbin.mitohealthapp.Fragments.Profile;

/**
 * Created by aasaqt on 9/8/16.
 */
public class ParticipantModel {

    int id;
    boolean confirm,decline;
    UserModel user;

    public ParticipantModel(int id,int drawable,String name,String profession,String hobbies,int age,int user_id){
        this.id = id;
        user = new UserModel();

    }
    public int getId(){
        return this.id;
    }
    public UserModel getUser(){
        return this.user;
    }

    public class UserModel{
        public UserModel(){
            profile = new ProfileModel();
        }
        ProfileModel profile;
        int id;
        ArrayList<String> interests;
        public ArrayList<String> getInterests(){
            return this.interests;
        }
        String first_name,last_name,email,username,profession,hobbies;
        public String getFirst_name(){
            return this.first_name;
        }
        public String getLast_name(){
            return this.last_name;
        }
        public String getEmail(){
            return this.email;
        }
        public String getUsername(){
            return this.username;
        }
        public int getId(){
            return this.id;
        }
        public ProfileModel getProfile(){
            return this.profile;
        }
    }
    public class ProfileModel{
        String gender;
        Images2Model images;
        String location;
        int age;
        public int getAge(){
            return this.age;
        }
        public String getLocation(){
            return this.location;
        }
        public Images2Model getImages(){
            return this.images;
        }
        public String getGender(){
            return this.gender;
        }
        public class Images2Model{
            String master;
            public String getMaster(){
                return this.master;
            }
            String[] others;
            public String[] getOthers(){
                return this.others;
            }
        }
    }
}
