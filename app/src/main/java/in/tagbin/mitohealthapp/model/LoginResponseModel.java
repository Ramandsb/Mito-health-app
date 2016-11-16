package in.tagbin.mitohealthapp.model;

/**
 * Created by aasaqt on 22/9/16.
 */
public class LoginResponseModel {
    String chat_credential,message,user_id,key;
    boolean signup;

    public boolean isSignup() {
        return signup;
    }

    public String getChat_credential() {
        return chat_credential;
    }

    public String getKey() {
        return key;
    }

    public String getMessage() {
        return message;
    }

    public String getUser_id() {
        return user_id;
    }
}
