package deepconversations.fste.com.deepconversations.listeners;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import deepconversations.fste.com.deepconversations.firebase.RealtimeDbConstants;
import deepconversations.fste.com.deepconversations.firebase.RealtimeDbWriter;
import deepconversations.fste.com.deepconversations.firebase.model.FriendStatus;
import deepconversations.fste.com.deepconversations.preferences.PreferenceManager;

/**
 * Created by Pragya on 3/27/2017.
 */

public class InvitedByUserListener implements ValueEventListener {
    Context ctx;

    public InvitedByUserListener(Context ctx){
        this.ctx = ctx.getApplicationContext();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Iterator iterator = dataSnapshot.getChildren().iterator();
        if(iterator.hasNext()){
            DataSnapshot obj = (DataSnapshot) iterator.next();
            String invitedByUserId = (String)obj.child(RealtimeDbConstants.USER_ID).getValue();
            Long createdAt = (Long)obj.child(RealtimeDbConstants.CREATED_AT).getValue();
            String invitedByUserName = (String)obj.child(RealtimeDbConstants.USER_NAME).getValue();
            String invitedByUserEmail = (String)obj.child(RealtimeDbConstants.EMAIL).getValue();

                FriendStatus userNode = new FriendStatus();
                userNode.setEmail(invitedByUserEmail);
                userNode.setUserId(invitedByUserId);
                userNode.setName(invitedByUserName);
                userNode.setCreatedAt(createdAt);
                userNode.setStatus(RealtimeDbConstants.ACCEPT_INVITE);
                RealtimeDbWriter.getInstance(ctx, false).addFriend(PreferenceManager.getInstance(ctx).getUserId(), userNode);
//
                FriendStatus friendNode = new FriendStatus();
                friendNode.setEmail(PreferenceManager.getInstance(ctx).getUserEmail());
                friendNode.setUserId(PreferenceManager.getInstance(ctx).getUserId());
                friendNode.setName(PreferenceManager.getInstance(ctx).getUserDisplayName());
                friendNode.setCreatedAt(userNode.getCreatedAt());
                friendNode.setStatus(RealtimeDbConstants.INVITED);
                RealtimeDbWriter.getInstance(ctx, false).addFriend(userNode.getUserId(), friendNode);

            obj.getRef().removeValue();
//            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
