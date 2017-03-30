package deepconversations.fste.com.deepconversations.listeners;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import deepconversations.fste.com.deepconversations.firebase.RealtimeDbConstants;
import deepconversations.fste.com.deepconversations.firebase.RealtimeDbWriter;
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
            String id = obj.getKey();
            Iterator iter = obj.getChildren().iterator();
            if(iter.hasNext()){
                DataSnapshot obj1 = (DataSnapshot) iter.next();
                String invitedByUserId = obj1.getKey();
                String invitedByUserName = (String)obj1.getValue();
                RealtimeDbWriter.getInstance(ctx, false).addFriend(invitedByUserId, invitedByUserName, RealtimeDbConstants.INVITED);
                RealtimeDbWriter.getInstance(ctx, false).addUserToFriendNode(invitedByUserId, RealtimeDbConstants.ACCEPT_INVITE);
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
