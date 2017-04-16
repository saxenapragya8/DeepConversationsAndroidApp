package deepconversations.fste.com.deepconversations.firebase.model;

import java.util.Date;
import java.util.List;

/**
 * Created by Pragya on 4/3/2017.
 */

public class GroupData {
    String groupName;
    String currentTopic;
    String firstUnreadComment;
    Date firstUnreadDate;
    int unreadCommentCount;


    public int getUnreadCommentCount() {
        return unreadCommentCount;
    }

    public void setUnreadCommentCount(int unreadCommentCount) {
        this.unreadCommentCount = unreadCommentCount;
    }

    public Date getFirstUnreadDate() {
        return firstUnreadDate;
    }

    public void setFirstUnreadDate(Date firstUnreadDate) {
        this.firstUnreadDate = firstUnreadDate;
    }

    public String getFirstUnreadComment() {
        return firstUnreadComment;
    }

    public void setFirstUnreadComment(String firstUnreadComment) {
        this.firstUnreadComment = firstUnreadComment;
    }

    public String getCurrentTopic() {
        return currentTopic;
    }

    public void setCurrentTopic(String currentTopic) {
        this.currentTopic = currentTopic;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
