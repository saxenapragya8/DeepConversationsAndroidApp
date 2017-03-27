//package deepconversations.fste.com.deepconversations.adapters;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import java.util.List;
//
//import deepconversations.fste.com.deepconversations.R;
//import deepconversations.fste.com.deepconversations.model.UserContact;
//
///**
// * Created by Pragya on 3/26/2017.
// */
//
//public class ShowContactsAdapter extends RecyclerView.Adapter<ShowContactsAdapter.ViewHolder> {
//
//    List<UserContact> contactsList ;
//
//    public ShowContactsAdapter(List<UserContact> contacts){
//        this.contactsList = contacts;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false);
//        ViewHolder vh = new ViewHolder(itemView);
//        return vh;
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        UserContact contact = contactsList.get(position);
//        holder.contactEmail.setText(contact.getName());
//        holder.contactName.setText(contact.getEmailIds().toString());
//    }
//
//    @Override
//    public int getItemCount() {
//        return contactsList.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder{
//
//        TextView contactName;
//        TextView contactEmail;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            contactName = (TextView)itemView.findViewById(R.id.contactName);
//            contactEmail = (TextView)itemView.findViewById(R.id.contactEmail);
//        }
//    }
//}
