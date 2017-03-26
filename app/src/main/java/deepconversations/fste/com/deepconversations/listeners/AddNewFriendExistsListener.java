package deepconversations.fste.com.deepconversations.listeners;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import deepconversations.fste.com.deepconversations.firebase.RealtimeDbConstants;
import deepconversations.fste.com.deepconversations.firebase.RealtimeDbWriter;

/**
 * Created by Pragya on 3/26/2017.
 */

public class AddNewFriendExistsListener implements ValueEventListener {
    private Context ctx;

    public AddNewFriendExistsListener(Context ctx){
        this.ctx = ctx;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        Iterator friendData = dataSnapshot.getChildren().iterator();
        if(friendData.hasNext()){
            DataSnapshot obj = (DataSnapshot) friendData.next();
            String friendUserId = obj.getKey();
            String friendUserName = (String)obj.child(RealtimeDbConstants.USER_NAME).getValue();

            RealtimeDbWriter.getInstance(ctx).addUserToFriendNode(friendUserId);
            RealtimeDbWriter.getInstance(ctx).addFriend(friendUserId, friendUserName);
        } else {
            //no friend already found in the user db
//            Intent intent = new Intent(ctx, SendEmailInviteActivity.class);
//            ctx.startActivity(intent);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
