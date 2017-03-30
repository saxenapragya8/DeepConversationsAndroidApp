package deepconversations.fste.com.deepconversations.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import deepconversations.fste.com.deepconversations.R;
import deepconversations.fste.com.deepconversations.firebase.model.UserData;
import deepconversations.fste.com.deepconversations.preferences.PreferenceManager;

/**
 * Created by Pragya on 2/27/2017.
 */

public class RealtimeDbWriter {

    private  Context ctx;
    private static RealtimeDbWriter instance;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private RealtimeDbWriter(Context context){ this.ctx = context;}

    public static synchronized RealtimeDbWriter getInstance(Context context, Boolean forceNewObjCreation)
    {
        if (null == instance || forceNewObjCreation)
            instance = new RealtimeDbWriter(context);
        return instance;
    }

    public DatabaseReference getDbReference(){
        return database;
    }

    public void addDataChangeListeners(){
//        DatabaseReference userConvNodeRef = database.child(RealtimeDbConstants.USER_NODE).child(getUserId()).child(RealtimeDbConstants.APP_ID).child(RealtimeDbConstants.CONVERSATIONS);
//        userConvNodeRef.orderByKey().addChildEventListener(new UserConversationsDataChangeListener(ctx));
//
//        DatabaseReference userFriendList = database.child(RealtimeDbConstants.USER_NODE).child(getUserId()).child(RealtimeDbConstants.APP_ID).child(RealtimeDbConstants.FRIENDS);
//        userFriendList.addValueEventListener(new FriendNodeDataListener(ctx));
    }

    public void writeUserDataToFirebase(UserData data){
        // Write a message to the database
        PreferenceManager.getInstance(ctx).storeUserIdInPrefs(data.getuId(), data.getUserName(), data.getEmail());
        DatabaseReference userAddRef = database.child(RealtimeDbConstants.USER_NODE).child(data.getuId());
        if(userAddRef != null) {
            userAddRef.child(RealtimeDbConstants.EMAIL).setValue(data.getEmail());
            userAddRef.child(RealtimeDbConstants.USER_NAME).setValue(data.getUserName());
        }
    }



//    public void writeNewConversationToDb(String subject, String inspiration, String emails, String content) {
//        DatabaseReference conversationsNode = database.child(RealtimeDbConstants.APP_ID).child(RealtimeDbConstants.CONVERSATIONS);
//
//        String conversationKey = conversationsNode.push().getKey();
//        DatabaseReference commentNode = database.child(RealtimeDbConstants.APP_ID).child(RealtimeDbConstants.CONVERSATIONS).child(conversationKey).child(RealtimeDbConstants.COMMENTS);
//        String commentKey = commentNode.push().getKey();
//
//        CommentData commentData = new CommentData();
//        commentData.setCommentContent(content);
//        commentData.setCommentCreatedByID(getUserId());
//        commentData.setCommentCreatedByName(getUserDisplayName());
//
//        ConversationsData conversationData = new ConversationsData();
//        conversationData.setSubject(subject);
//        conversationData.setInspiration(inspiration);
//        conversationData.setCreatedByID(getUserId());
//        conversationData.setCreatedBy(getUserDisplayName());
//        conversationData.addCommentNodeData(commentKey, commentData);
//        Map<String, Object> conversationMap = conversationData.toMap();
//
//        conversationsNode.child(conversationKey).updateChildren(conversationMap);
//
//        DatabaseReference userIdNode = database.child(RealtimeDbConstants.USER_NODE).child(getUserId()).child(RealtimeDbConstants.APP_ID).child(RealtimeDbConstants.CONVERSATIONS);
//        Map<String, Object> userConversationData = new HashMap<>();
//        userConversationData.put(conversationKey, "");
//        userIdNode.updateChildren(userConversationData);
//        Toast.makeText(ctx, " Added the new conversation " , Toast.LENGTH_LONG).show();
//    }
//
//    public void addNewComment(String convId, String content) {
//        CommentData commentData = new CommentData();
//        commentData.setCommentContent(content);
//        commentData.setCommentCreatedByID(getUserId());
//        commentData.setCommentCreatedByName(getUserDisplayName());
//        DatabaseReference commentNode = database.child(RealtimeDbConstants.APP_ID).child(RealtimeDbConstants.CONVERSATIONS).child(convId).child(RealtimeDbConstants.COMMENTS);
//        commentNode.push().setValue(commentData);
//    }
//
    public void addFriend(String friendUserId, String friendUserName, String status){
        DatabaseReference userFriendList = database.child(RealtimeDbConstants.USER_NODE).child(PreferenceManager.getInstance(ctx).getUserId()).child(RealtimeDbConstants.APP_ID).child(RealtimeDbConstants.FRIENDS);
        userFriendList.child(friendUserId).child(RealtimeDbConstants.FRIEND_STATUS).setValue(status);
        userFriendList.child(friendUserId).child(RealtimeDbConstants.FRIEND_NAME).setValue(friendUserName);
    }

    public void addUserToFriendNode(String friendUserId, String status){
        DatabaseReference friendsFriendList = database.child(RealtimeDbConstants.USER_NODE).child(friendUserId).child(RealtimeDbConstants.APP_ID).child(RealtimeDbConstants.FRIENDS);
        friendsFriendList.child(PreferenceManager.getInstance(ctx).getUserId()).child(RealtimeDbConstants.FRIEND_STATUS).setValue(status);
        friendsFriendList.child(PreferenceManager.getInstance(ctx).getUserId()).child(RealtimeDbConstants.FRIEND_NAME).setValue(PreferenceManager.getInstance(ctx).getUserDisplayName());
    }

    public void addNewInviteIds(String[] ids){
        DatabaseReference newInvitesList = database.child(RealtimeDbConstants.APP_ID).child(RealtimeDbConstants.INVITES);
        for(String id: ids) {
            newInvitesList.child(id).child(PreferenceManager.getInstance(ctx).getUserId()).setValue(PreferenceManager.getInstance(ctx).getUserDisplayName());
        }
    }
}
