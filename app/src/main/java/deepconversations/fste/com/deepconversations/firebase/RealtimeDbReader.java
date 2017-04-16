package deepconversations.fste.com.deepconversations.firebase;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import deepconversations.fste.com.deepconversations.listeners.AddFriendExistsListener;
import deepconversations.fste.com.deepconversations.listeners.FriendsNodeChangedListener;
import deepconversations.fste.com.deepconversations.listeners.InvitedByUserListener;
import deepconversations.fste.com.deepconversations.preferences.PreferenceManager;

/**
 * Created by Pragya on 3/1/2017.
 */

public class RealtimeDbReader {

    private static Context ctx;
    private static RealtimeDbReader instance;
    static DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public static void addDataReadListeners(){
        //pick up accept/decline friends 1st
//        database.child(RealtimeDbConstants.USER_NODE).child(PreferenceManager.getInstance(ctx).getUserId())
//                .child(RealtimeDbConstants.APP_ID).child(RealtimeDbConstants.FRIENDS)
//                .orderByChild(RealtimeDbConstants.FRIEND_STATUS).equalTo(RealtimeDbConstants.ACCEPT_INVITE)
//                .addListenerForSingleValueEvent(new FriendsAppLaunchListener());

        //for friends node change, get realtime updates on the node
        database.child(RealtimeDbConstants.USER_NODE).child(PreferenceManager.getInstance(ctx).getUserId())
                .child(RealtimeDbConstants.APP_ID).child(RealtimeDbConstants.FRIENDS)
                .addChildEventListener(new FriendsNodeChangedListener());
        //Pick up awaiting response next
//        database.child(RealtimeDbConstants.USER_NODE).child(PreferenceManager.getInstance(ctx).getUserId())
//                .child(RealtimeDbConstants.APP_ID).child(RealtimeDbConstants.FRIENDS)
//                .orderByChild(RealtimeDbConstants.FRIEND_STATUS).equalTo(RealtimeDbConstants.INVITED)
//                .addListenerForSingleValueEvent(new FriendsListener());
    }

    private RealtimeDbReader(Context context){ this.ctx = context;}

    public static synchronized RealtimeDbReader getInstance(Context context, boolean forceNewObjcreation)
    {
        if (null == instance || forceNewObjcreation)
            instance = new RealtimeDbReader(context);
        return instance;
    }

    public void getEmailUserId(String email){
        database.child(RealtimeDbConstants.USER_NODE).orderByChild(RealtimeDbConstants.EMAIL).equalTo(email)
                .addListenerForSingleValueEvent(new AddFriendExistsListener(ctx, email));
    }

    public void getInviteIdInviteSentByUser(String inviteId){
        database.child(RealtimeDbConstants.APP_ID).child(RealtimeDbConstants.INVITES).orderByKey().equalTo(inviteId)
                .addListenerForSingleValueEvent(new InvitedByUserListener(ctx));

    }
}
