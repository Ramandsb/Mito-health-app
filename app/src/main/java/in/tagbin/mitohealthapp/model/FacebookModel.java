package in.tagbin.mitohealthapp.model;

/**
 * Created by aasaqt on 5/9/16.
 */
public class FacebookModel {
    int id;
    String facebook_id,facebook_email,facebook_access_token,home_town,occupation,description;
    boolean facebook_connected;
    Images4Model images;
    public String getHome_town() {
        return home_town;
    }

    public int getId() {
        return id;
    }

    public String getFacebook_access_token() {
        return facebook_access_token;
    }

    public String getFacebook_email() {
        return facebook_email;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public String getOccupation() {
        return occupation;
    }

    public boolean isFacebook_connected() {
        return facebook_connected;
    }
    public class Images4Model{
        String master;

        public String getMaster() {
            return master;
        }
    }

    public String getDescription() {
        return description;
    }
}
