package com.example.pramadani.projekti_v21;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import static com.example.pramadani.projekti_v21.R.layout.received_messages;
import static com.example.pramadani.projekti_v21.R.layout.sent_messages;

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



        String username=messagesList.get(position).getUsername();
        String chatMessage=messagesList.get(position).getMessage();
        String timeMessage=messagesList.get(position).getTime();

        if(FirebaseAuth.getInstance().getCurrentUser().getDisplayName().equals(username)){
            View message= View.inflate(mContext, sent_messages,null);
            TextView user1=message.findViewById(R.id.sent_user);
            TextView time1=message.findViewById(R.id.sent_time);
            TextView text1=message.findViewById(R.id.sent_text);
            user1.setText(username);
            text1.setText(chatMessage);
            time1.setText(timeMessage);

            return message;
        }
        else {
            View message= View.inflate(mContext, received_messages,null);
            TextView user=message.findViewById(R.id.received_user);
            TextView time=message.findViewById(R.id.received_time);
            TextView text=message.findViewById(R.id.received_text);
            user.setText(username);
            text.setText(chatMessage);
            time.setText(timeMessage);

            return message;
        }
    }
}
