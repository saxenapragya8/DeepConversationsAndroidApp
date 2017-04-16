package deepconversations.fste.com.deepconversations.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

import deepconversations.fste.com.deepconversations.R;
import deepconversations.fste.com.deepconversations.firebase.model.FriendStatus;
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
//        DatabaseReference friendsNode = database.child(RealtimeDbConstants.USER_NODE).child(PreferenceManager.getInstance(ctx).getUserId()).child(RealtimeDbConstants.APP_ID).child(RealtimeDbConstants.FRIENDS);
//        friendsNode.orderByKey()..addChildEventListener(new UserConversationsDataChangeListener(ctx));
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

    public void acceptFriend(FriendStatus friendData){
        DatabaseReference friendNode = database.child(RealtimeDbConstants.USER_NODE).child(friendData.getUserId()).child(RealtimeDbConstants.APP_ID);
        friendNode.child(RealtimeDbConstants.FRIENDS).child(PreferenceManager.getInstance(ctx).getUserId()).child(RealtimeDbConstants.FRIEND_STATUS).setValue(RealtimeDbConstants.ACCEPT);
        DatabaseReference userNode = database.child(RealtimeDbConstants.USER_NODE).child(PreferenceManager.getInstance(ctx).getUserId()).child(RealtimeDbConstants.APP_ID);
        userNode.child(RealtimeDbConstants.FRIENDS).child(friendData.getUserId()).child(RealtimeDbConstants.FRIEND_STATUS).setValue(RealtimeDbConstants.ACCEPT);

        DatabaseReference groupNode = database.child(RealtimeDbConstants.APP_ID).child(RealtimeDbConstants.GROUPS);
        String groupId = groupNode.push().getKey();
        groupNode.child(groupId).child(RealtimeDbConstants.GROUP_NAME).setValue("dummyName");
        groupNode.child(groupId).child(RealtimeDbConstants.USER_NODE).child(friendData.getUserId()).setValue(RealtimeDbConstants.ACCEPT);
        groupNode.child(groupId).child(RealtimeDbConstants.USER_NODE).child(PreferenceManager.getInstance(ctx).getUserId()).setValue(RealtimeDbConstants.ACCEPT);
        friendNode.child(RealtimeDbConstants.GROUPS).child(groupId).child(RealtimeDbConstants.FRIEND_STATUS).setValue(RealtimeDbConstants.ACCEPT);
        friendNode.child(RealtimeDbConstants.GROUPS).child(groupId).child(RealtimeDbConstants.CREATED_AT).setValue(ServerValue.TIMESTAMP);
        userNode.child(RealtimeDbConstants.GROUPS).child(groupId).child(RealtimeDbConstants.FRIEND_STATUS).setValue(RealtimeDbConstants.ACCEPT);
        userNode.child(RealtimeDbConstants.GROUPS).child(groupId).child(RealtimeDbConstants.CREATED_AT).setValue(ServerValue.TIMESTAMP);
    }

    public void declineFriendStatus(FriendStatus friendData){
        DatabaseReference friendNode = database.child(RealtimeDbConstants.USER_NODE).child(friendData.getUserId()).child(RealtimeDbConstants.APP_ID);
        friendNode.child(RealtimeDbConstants.FRIENDS).child(PreferenceManager.getInstance(ctx).getUserId()).child(RealtimeDbConstants.FRIEND_STATUS).setValue(RealtimeDbConstants.DECLINE);
        DatabaseReference userNode = database.child(RealtimeDbConstants.USER_NODE).child(PreferenceManager.getInstance(ctx).getUserId()).child(RealtimeDbConstants.APP_ID);
        userNode.child(RealtimeDbConstants.FRIENDS).child(friendData.getUserId()).child(RealtimeDbConstants.FRIEND_STATUS).setValue(RealtimeDbConstants.DECLINED);
    }

    public void deleteFriendRequest(FriendStatus friendData){
        DatabaseReference friendNode = database.child(RealtimeDbConstants.USER_NODE).child(friendData.getUserId()).child(RealtimeDbConstants.APP_ID);
        friendNode.child(RealtimeDbConstants.FRIENDS).child(PreferenceManager.getInstance(ctx).getUserId()).removeValue();
        DatabaseReference userNode = database.child(RealtimeDbConstants.USER_NODE).child(PreferenceManager.getInstance(ctx).getUserId()).child(RealtimeDbConstants.APP_ID);
        userNode.child(RealtimeDbConstants.FRIENDS).child(friendData.getUserId()).removeValue();
    }

    public void addNewInviteIds(String[] ids){
        DatabaseReference newInvitesList = database.child(RealtimeDbConstants.APP_ID).child(RealtimeDbConstants.INVITES);
        for(String id: ids) {
            DatabaseReference ref = newInvitesList.child(id);
            FriendStatus status = new FriendStatus();
            status.setEmail(PreferenceManager.getInstance(ctx).getUserEmail());
            status.setName(PreferenceManager.getInstance(ctx).getUserDisplayName());
            status.setUserId(PreferenceManager.getInstance(ctx).getUserId());

            ref.updateChildren(status.toMapWithUserId());
        }
    }

    public void addFriend(String userToAddTo, FriendStatus friendStatus){
        DatabaseReference ref = database.child(RealtimeDbConstants.USER_NODE).child(userToAddTo).child(RealtimeDbConstants.APP_ID).child(RealtimeDbConstants.FRIENDS);
        ref.child(friendStatus.getUserId()).updateChildren(friendStatus.toMap());
    }
}
