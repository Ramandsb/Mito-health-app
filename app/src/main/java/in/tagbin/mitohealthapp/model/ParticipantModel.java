package in.tagbin.mitohealthapp.model;

import java.util.ArrayList;

/**
 * Created by aasaqt on 9/8/16.
 */
public class ParticipantModel {

    int id;
    boolean confirm,decline;
    UserModel user;
    DataObject event;

    public ParticipantModel(int id,int drawable,String name,String profession,String hobbies,int age,int user_id){
        this.id = id;
        user = new UserModel();
    }

    public DataObject getEvent() {
        return event;
    }

    public int getId(){
        return this.id;
    }
    public UserModel getUser(){
        return this.user;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public boolean isDecline() {
        return decline;
    }

    public class UserModel{
        public UserModel(){
            profile = new ProfileModel();
            interest = new ArrayList<InterestModel5>();
        }
        ProfileModel profile;
        int id;
        ArrayList<InterestModel5> interest;
        public ArrayList<InterestModel5> getInterests(){
            return this.interest;
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
        public class InterestModel5{
            int user,id;
            InterestModel6 interest;
            public InterestModel5(){
                interest = new InterestModel6();
            }
            public int getId() {
                return id;
            }

            public int getUser() {
                return user;
            }

            public InterestModel6 getInterest() {
                return interest;
            }

            public class InterestModel6{
                String name;
                int id,created_by;
                boolean verified;

                public int getId() {
                    return id;
                }

                public int getCreated_by() {
                    return created_by;
                }

                public String getName() {
                    return name;
                }

                public boolean isVerified() {
                    return verified;
                }
            }
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
            String[] other;
            public String[] getOthers(){
                return this.other;
            }
        }
    }
}
