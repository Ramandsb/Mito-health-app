package in.tagbin.mitohealthapp.model;

/**
 * Created by aasaqt on 11/8/16.
 */
public class ConfirmParticipantModel {
    int id;
    String confirm,decline;
    public void setId(int id){
        this.id = id;
    }
    public void setConfirm(String confirm){
        this.confirm = confirm;
    }
    public void setDecline(String decline){
        this.decline = decline;
    }
    public String getConfirm(){
        return this.confirm;
    }
    public String getDecline(){
        return this.decline;
    }
    public int getId(){
        return this.id;
    }
}
