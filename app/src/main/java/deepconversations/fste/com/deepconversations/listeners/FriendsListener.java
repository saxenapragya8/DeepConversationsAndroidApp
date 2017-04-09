package deepconversations.fste.com.deepconversations.listeners;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import deepconversations.fste.com.deepconversations.NavigationActivity;
import deepconversations.fste.com.deepconversations.firebase.RealtimeDbConstants;
import deepconversations.fste.com.deepconversations.firebase.model.FriendStatus;

/**
 * Created by Pragya on 4/4/2017.
 */

public class FriendsListener implements ValueEventListener {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Iterator iter = dataSnapshot.getChildren().iterator();
        if(iter.hasNext()) {
            DataSnapshot obj = (DataSnapshot) iter.next();
            String friendUserId = obj.getKey();
            String friendUserName = (String)obj.child(RealtimeDbConstants.USER_NAME).getValue();
            String friendEmail = (String)obj.child(RealtimeDbConstants.EMAIL).getValue();
            Long createdAt = (Long)obj.child(RealtimeDbConstants.CREATED_AT).getValue();
            FriendStatus friendStatus = new FriendStatus();
            friendStatus.setUserId(friendUserId);
            friendStatus.setName(friendUserName);
            friendStatus.setEmail(friendEmail);

            friendStatus.setCreatedAt(createdAt);
            friendStatus.setStatus(RealtimeDbConstants.ACCEPT_INVITE);
            NavigationActivity.addFriend(friendStatus);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
