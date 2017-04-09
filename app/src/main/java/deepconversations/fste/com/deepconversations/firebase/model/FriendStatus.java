package deepconversations.fste.com.deepconversations.firebase.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ServerValue;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import deepconversations.fste.com.deepconversations.firebase.RealtimeDbConstants;

/**
 * Created by Pragya on 4/3/2017.
 */
@IgnoreExtraProperties
public class FriendStatus {
    private String userId;
    private String name;
    private String email;
    private Long createdAt;
    private String status;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Exclude
    public String getCreationDate(){
        Date date = new Date(createdAt);
        Format format = new SimpleDateFormat("MM/dd/yyyy");
        return format.format(date);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        if(createdAt == null)
            result.put(RealtimeDbConstants.CREATED_AT, ServerValue.TIMESTAMP);
        else
            result.put(RealtimeDbConstants.CREATED_AT, createdAt);
        result.put(RealtimeDbConstants.USER_NAME, name);
        result.put(RealtimeDbConstants.EMAIL, email);
        if(status != null)
            result.put(RealtimeDbConstants.FRIEND_STATUS, status);
        return result;
    }

    @Exclude
    public Map<String, Object> toMapWithUserId() {
        Map<String, Object> result = toMap();
        result.put(RealtimeDbConstants.USER_ID, userId);
        return result;
    }
}
