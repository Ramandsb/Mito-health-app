package in.tagbin.mitohealthapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aasaqt on 12/8/16.
 */
public class UserListModel {
    public int drawable,age;
    public String name,sex,distance,interests;
    public List<String> hobbies;
    public UserListModel(int age,String name,String sex,String distance, String interests){
        this.age = age;
        this.name = name;
        this.distance = distance;
        this.sex = sex;
        this.interests = interests;
        this.hobbies = new ArrayList<String>();
        this.hobbies.add("Swimming");
        this.hobbies.add("Playing");
        this.hobbies.add("Watching");
        this.hobbies.add("Jogging");
        this.hobbies.add("Reading");
    }
    public int getAge(){
        return this.age;
    }
    public String getName(){
        return this.name;
    }
    public String getSex(){
        return this.sex;
    }
    public String getDistance(){
        return this.distance;
    }
    public String getInterests(){
        return this.interests;
    }
    public List<String> getHobbies(){
        return this.hobbies;
    }
}
