package deepconversations.fste.com.deepconversations.firebase.model;

/**
 * Created by Pragya on 2/27/2017.
 */

public class UserData {
    private String uId;
    private String email;
    private String userName;


    public UserData(String uId, String email, String userName){
        this.uId = uId;
        this.email = email;
        this.userName = userName;
    }

    public String getuId() {
        return uId;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }
}
