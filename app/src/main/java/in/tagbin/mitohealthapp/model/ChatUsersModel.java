package in.tagbin.mitohealthapp.model;

/**
 * Created by aasaqt on 22/8/16.
 */
public class ChatUsersModel {
    String username;
    String time;
    String content;
    public String getUsername(){
        return this.username;
    }
    public String getTime(){
        return this.time;
    }
    public String getContent(){
        return this.content;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setTime(String time){
        this.time = time;
    }
    public void setContent(String content){
        this.content = content;
    }
}
