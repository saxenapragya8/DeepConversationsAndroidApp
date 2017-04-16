package deepconversations.fste.com.deepconversations.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import deepconversations.fste.com.deepconversations.R;
import deepconversations.fste.com.deepconversations.firebase.RealtimeDbConstants;
import deepconversations.fste.com.deepconversations.firebase.model.FriendStatus;
import deepconversations.fste.com.deepconversations.firebase.model.GroupData;
import deepconversations.fste.com.deepconversations.uieventhandlers.AcceptFriendRequestListener;
import deepconversations.fste.com.deepconversations.uieventhandlers.DeclineFriendRequestListener;
import deepconversations.fste.com.deepconversations.uieventhandlers.DeleteFriendRequestListener;

/**
 * Created by Pragya on 4/3/2017.
 */

public class NavigationViewAdapter extends RecyclerView.Adapter<NavigationViewAdapter.ViewHolder>{

    List<FriendStatus> newFriends;
    List<GroupData> groupData;
    Context ctx;

    public NavigationViewAdapter(List<FriendStatus> newFriends, List<GroupData> groupData, Context ctx){
        this.newFriends = newFriends;
        this.groupData = groupData;
        this.ctx = ctx;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class NewFriendViewHolder extends ViewHolder {
        TextView friendName;
        TextView friendEmail;
        TextView statusMessage;
        TextView friendshipRequestDate;
        Button accept;
        Button decline;
        Button deleteRequest;

        public NewFriendViewHolder(View itemView) {
            super(itemView);
            friendName = (TextView)itemView.findViewById(R.id.friendName);
            friendEmail = (TextView)itemView.findViewById(R.id.friendEmail);
            statusMessage = (TextView)itemView.findViewById(R.id.statusMessage);
            accept = (Button)itemView.findViewById(R.id.acceptRequest);
            decline = (Button)itemView.findViewById(R.id.declineRequest);
            deleteRequest = (Button)itemView.findViewById(R.id.deleteRequest);
//            friendshipRequestDate = (TextView)itemView.findViewById(R.id.friendshipRequestDate);
        }
    }

    public static class GroupViewHolder extends ViewHolder{
        TextView groupName;
        TextView unreadCommentsCount;
        TextView firstUnreadCommentDate;
        TextView topic;
        TextView firstUnreadCommentContent;

        public GroupViewHolder(View itemView) {
            super(itemView);
            groupName = (TextView)itemView.findViewById(R.id.groupName);
            unreadCommentsCount = (TextView)itemView.findViewById(R.id.unreadCommentsCount);
            firstUnreadCommentDate = (TextView)itemView.findViewById(R.id.firstUnreadCommentDate);
            topic = (TextView)itemView.findViewById(R.id.topic);
            firstUnreadCommentContent = (TextView)itemView.findViewById(R.id.firstUnreadCommentContent);
        }
    }

    @Override
    public NavigationViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(viewType == 2) {
            View view = inflater.inflate(R.layout.row_friend_status, parent, false);
            NewFriendViewHolder vh = new NewFriendViewHolder(view);
            return vh;
        } else {
            View view = inflater.inflate(R.layout.row_conversation, parent, false);
            GroupViewHolder vh = new GroupViewHolder(view);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(NavigationViewAdapter.ViewHolder holder, int position) {
        if(holder instanceof GroupViewHolder){
            ((GroupViewHolder) holder).groupName.setText(groupData.get(position).getGroupName());
            ((GroupViewHolder) holder).topic.setText(groupData.get(position).getCurrentTopic());
            ((GroupViewHolder) holder).firstUnreadCommentContent.setText(groupData.get(position).getFirstUnreadComment());
            ((GroupViewHolder) holder).firstUnreadCommentDate.setText(groupData.get(position).getFirstUnreadDate().toString());
            ((GroupViewHolder) holder).unreadCommentsCount.setText(groupData.get(position).getUnreadCommentCount());
        } else if(holder instanceof NewFriendViewHolder){
            int tempPosition = position - groupData.size();
            String status = newFriends.get(tempPosition).getStatus();
            ((NewFriendViewHolder) holder).friendEmail.setText(newFriends.get(tempPosition).getEmail());
            ((NewFriendViewHolder) holder).friendName.setText(newFriends.get(tempPosition).getName());
            ((NewFriendViewHolder) holder).statusMessage.setText(status);

            if(status.equals(RealtimeDbConstants.INVITED)){
                ((NewFriendViewHolder) holder).deleteRequest.setVisibility(View.VISIBLE);
                ((NewFriendViewHolder) holder).accept.setVisibility(View.GONE);
                ((NewFriendViewHolder) holder).decline.setVisibility(View.GONE);
            } else if(status.equals(RealtimeDbConstants.ACCEPT_INVITE)){
                ((NewFriendViewHolder) holder).deleteRequest.setVisibility(View.GONE);
                ((NewFriendViewHolder) holder).accept.setVisibility(View.VISIBLE);
                ((NewFriendViewHolder) holder).decline.setVisibility(View.VISIBLE);
            }
            ((NewFriendViewHolder) holder).accept.setOnClickListener(new AcceptFriendRequestListener(ctx, newFriends.get(tempPosition)));
            ((NewFriendViewHolder) holder).decline.setOnClickListener(new DeclineFriendRequestListener(ctx, newFriends.get(tempPosition)));
            ((NewFriendViewHolder) holder).deleteRequest.setOnClickListener(new DeleteFriendRequestListener(ctx, newFriends.get(tempPosition)));
//            view.findViewById()
//            ((NewFriendViewHolder) holder).friendshipRequestDate.setText(newFriends.get(tempPosition).getCreationDate().toString());
        }
    }

    //1 type for friend data and 2 for group data
    @Override
    public int getItemViewType(int position){
        if(groupData.isEmpty())
            return 2;
        else if(newFriends.isEmpty())
            return 1;

        if(position >=  groupData.size())
            return 1;
        else
            return 2;
    }

    @Override
    public int getItemCount() {
        return newFriends.size() + groupData.size();
    }
}
