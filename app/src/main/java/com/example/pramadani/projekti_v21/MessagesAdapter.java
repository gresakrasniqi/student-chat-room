package com.example.pramadani.projekti_v21;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import static com.example.pramadani.projekti_v21.R.layout.list_messages;

public class MessagesAdapter extends BaseAdapter {
    private Context mContext;

    private List<ChatMessage> messagesList;

    public MessagesAdapter(Context mContext, List<ChatMessage> messagesList) {
        this.mContext = mContext;
        this.messagesList = messagesList;
    }

    @Override
    public int getCount() {
        return messagesList.size();
    }

    @Override
    public Object getItem(int position) {
        return messagesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View message= View.inflate(mContext, list_messages,null);
        TextView user=message.findViewById(R.id.message_user);
        TextView time=message.findViewById(R.id.message_time);
        TextView text=message.findViewById(R.id.message_text);


        String username=messagesList.get(position).getUsername();
        String chatMessage=messagesList.get(position).getMessage();
        String timeMessage=messagesList.get(position).getTime();

        if(FirebaseAuth.getInstance().getCurrentUser().getDisplayName().equals(username)){
            //po menoj qe qetu duhet me rregullu ka cila ane me dal teksti
        }

        user.setText(username);
        text.setText(chatMessage);
        time.setText(timeMessage);

        return message;
    }
}
