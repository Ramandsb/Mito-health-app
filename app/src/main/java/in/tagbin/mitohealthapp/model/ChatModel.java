package in.tagbin.mitohealthapp.model;

/**
 * Created by aasaqt on 22/9/16.
 */
public class ChatModel {
    String name,user,status,type,presence,presence_status;
    public ChatModel(){

    }
    public ChatModel(String name,String user,String status,String type,String presence,String presence_status){
        this.name = name;
        this.user = user;
        this.status = status;
        this.type = type;
        this.presence = presence;
        this.presence_status = presence_status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPresence(String presence) {
        this.presence = presence;
    }

    public void setPresence_status(String presence_status) {
        this.presence_status = presence_status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPresence() {
        return presence;
    }

    public String getName() {
        return name;
    }

    public String getPresence_status() {
        return presence_status;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getUser() {
        return user;
    }
}
