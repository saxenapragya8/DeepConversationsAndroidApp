package deepconversations.fste.com.deepconversations.listeners;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import deepconversations.fste.com.deepconversations.NavigationActivity;
import deepconversations.fste.com.deepconversations.firebase.RealtimeDbConstants;
import deepconversations.fste.com.deepconversations.firebase.model.FriendStatus;

/**
 * Created by Pragya on 4/15/2017.
 */

public class FriendsNodeChangedListener implements ChildEventListener {
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            String friendUserId = dataSnapshot.getKey();
            String friendUserName = (String)dataSnapshot.child(RealtimeDbConstants.USER_NAME).getValue();
            String friendEmail = (String)dataSnapshot.child(RealtimeDbConstants.EMAIL).getValue();
            Long createdAt = (Long)dataSnapshot.child(RealtimeDbConstants.CREATED_AT).getValue();
            String status = (String)dataSnapshot.child(RealtimeDbConstants.FRIEND_STATUS).getValue();
            FriendStatus friendStatus = new FriendStatus();
            friendStatus.setUserId(friendUserId);
            friendStatus.setName(friendUserName);
            friendStatus.setEmail(friendEmail);
            friendStatus.setCreatedAt(createdAt);
            friendStatus.setStatus(status);
            NavigationActivity.friendChangeOrAdd(friendStatus);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String friendUserId = dataSnapshot.getKey();
            String friendUserName = (String)dataSnapshot.child(RealtimeDbConstants.USER_NAME).getValue();
            String friendEmail = (String)dataSnapshot.child(RealtimeDbConstants.EMAIL).getValue();
            String status = (String)dataSnapshot.child(RealtimeDbConstants.FRIEND_STATUS).getValue();
            Long createdAt = (Long)dataSnapshot.child(RealtimeDbConstants.CREATED_AT).getValue();
        FriendStatus friendStatus = new FriendStatus();
        friendStatus.setUserId(friendUserId);
        friendStatus.setName(friendUserName);
        friendStatus.setCreatedAt(createdAt);
        friendStatus.setEmail(friendEmail);
        friendStatus.setStatus(status);

        if(status.equals(RealtimeDbConstants.ACCEPT) || status.equals(RealtimeDbConstants.DECLINE) || status.equals(RealtimeDbConstants.DECLINED)){
            NavigationActivity.removeFriend(friendStatus);
        } else {
            NavigationActivity.friendChangeOrAdd(friendStatus);
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
            String friendUserId = dataSnapshot.getKey();
            String friendUserName = (String)dataSnapshot.child(RealtimeDbConstants.USER_NAME).getValue();
            String friendEmail = (String)dataSnapshot.child(RealtimeDbConstants.EMAIL).getValue();
            FriendStatus friendStatus = new FriendStatus();
            friendStatus.setUserId(friendUserId);
            friendStatus.setName(friendUserName);
            friendStatus.setEmail(friendEmail);
            NavigationActivity.removeFriend(friendStatus);
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
