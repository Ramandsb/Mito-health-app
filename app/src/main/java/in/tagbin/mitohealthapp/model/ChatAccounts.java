package in.tagbin.mitohealthapp.model;

/**
 * Created by hp on 8/27/2016.
 */
public class ChatAccounts {

    String name,user,status,type,presence,presence_status;

    public String getName() {
        return name;
    }

    public String getPresence() {
        return presence;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setPresence(String presence) {
        this.presence = presence;
    }

    public String getPresence_status() {
        return presence_status;
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
}
