package in.tagbin.mitohealthapp.model;

/**
 * Created by chetan on 22/11/16.
 */

public class UserChangePasswordModel {
    String old_password,password;

    public void setNew_password(String new_password) {
        this.password= new_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }
}
