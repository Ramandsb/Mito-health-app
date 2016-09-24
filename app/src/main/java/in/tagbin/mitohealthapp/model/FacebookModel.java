package in.tagbin.mitohealthapp.model;

/**
 * Created by aasaqt on 5/9/16.
 */
public class FacebookModel {
    int id,total_coins;
    String facebook_id,facebook_email,facebook_access_token,home_town,occupation,description;
    boolean facebook_connected;
    Images4Model images;
    public String getHome_town() {
        return home_town;
    }

    public int getId() {
        return id;
    }

    public int getTotal_coins() {
        return total_coins;
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

    public Images4Model getImages() {
        return images;
    }

    public boolean isFacebook_connected() {
        return facebook_connected;
    }
    public class Images4Model{
        String master;
        String[] others;
        public String getMaster() {
            return master;
        }

        public String[] getOthers() {
            return others;
        }
    }

    public String getDescription() {
        return description;
    }
}
