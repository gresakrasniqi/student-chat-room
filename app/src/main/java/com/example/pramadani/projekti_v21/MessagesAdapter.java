package com.example.pramadani.projekti_v21;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MessagesAdapter extends BaseAdapter {
    private Context mContext;

    private List<ChatMessage> mTextList;

    public MessagesAdapter(Context mContext, List<ChatMessage> text) {
        this.mContext = mContext;
        this.mTextList = text;
    }

    @Override
    public int getCount() {
        return mTextList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTextList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View message= View.inflate(mContext,R.layout.list_messages,null);
        TextView user=message.findViewById(R.id.message_user);
        TextView time=message.findViewById(R.id.message_time);
        TextView text=message.findViewById(R.id.message_text);

        user.setText(mTextList.get(position).getUser().getFirstname() );
        text.setText(mTextList.get(position).getMessage());
        time.setText(mTextList.get(position).getTime());

        return message;
    }
}
