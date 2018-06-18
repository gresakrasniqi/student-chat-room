package com.example.pramadani.projekti_v21;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ChatRoomAdapter extends BaseAdapter {
    private Context mContext;

    private List<ChatRoom> chatRoomList;

    public ChatRoomAdapter(Context mContext, List<ChatRoom> chatRoomList) {
        this.mContext = mContext;
        this.chatRoomList = chatRoomList;
    }

    @Override
    public int getCount() {
        return chatRoomList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatRoomList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_view_row_chatroom, parent, false);
        TextView textView1 = (TextView) rowView.findViewById(R.id.text1);
        TextView textView2 = (TextView) rowView.findViewById(R.id.text2);

        textView1.setText(chatRoomList.get(position).getChatName());
        textView2.setText(chatRoomList.get(position).getClassName());

        return rowView;
    }
}
