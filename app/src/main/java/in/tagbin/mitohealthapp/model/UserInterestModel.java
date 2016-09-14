package in.tagbin.mitohealthapp.model;

/**
 * Created by aasaqt on 7/9/16.
 */
public class UserInterestModel {
    int user,id;
    Interest2Model interest;
    public int getUser() {
        return user;
    }

    public int getId() {
        return id;
    }

    public Interest2Model getInterest() {
        return interest;
    }

    public class Interest2Model{
        String name,created_by;
        int id;
        boolean verified;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCreated_by() {
            return created_by;
        }

        public boolean isVerified() {
            return verified;
        }
    }
}
