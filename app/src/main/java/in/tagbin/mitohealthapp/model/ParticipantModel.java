package in.tagbin.mitohealthapp.model;

/**
 * Created by aasaqt on 9/8/16.
 */
public class ParticipantModel {

    int id;
    boolean confirm,decline;
    UserModel user;
    public ParticipantModel(int id,int drawable,String name,String profession,String hobbies,int age,int user_id){
        this.id = id;
        user =new UserModel(drawable,name,profession,hobbies,age,user_id);
    }
    public int getId(){
        return this.id;
    }
    public UserModel getUser(){
        return this.user;
    }

    public class UserModel{

        int id,drawable,age;
        String first_name,last_name,email,username,profession,hobbies;
        public UserModel(int drawable,String name,String profession,String hobbies,int age,int user_id){
            this.drawable = drawable;
            this.first_name = name;
            this.profession = profession;
            this.hobbies = hobbies;
            this.age = age;
            this.id = user_id;
        }
        public int getDrawable(){
            return this.drawable;
        }
        public String getFirst_name(){
            return this.first_name;
        }
        public String getProfession(){
            return this.profession;
        }
        public String getHobbies(){
            return this.hobbies;
        }
        public int getAge(){
            return this.age;
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
    }
}
