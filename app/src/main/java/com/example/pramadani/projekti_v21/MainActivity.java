package com.example.pramadani.projekti_v21;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {
    ArrayList alChatRoom = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String[] strArrayChatRoom = getResources().getStringArray(R.array.strArraySpinner);
//        for (int i=0;i<strArrayChatRoom.length;i++)
//        {
//            ChatRoom objChatRoom = new ChatRoom(strArrayChatRoom[i]);
//            alChatRoom.add(objChatRoom);
//        }

        Adapteri objAdapteri = new Adapteri();
        //setListAdapter(objAdapteri);
    }

    public class ChatRoom {
        public String Pershkrimi;

        public ChatRoom(String pershkrimi) {
            super();
            Pershkrimi = pershkrimi;
        }

        public String getPershkrimi() {
            return Pershkrimi;
        }

        public void setPershkrimi(String pershkrimi) {
            Pershkrimi = pershkrimi;
        }
    }

    public class clsHolder
    {
        public TextView txtPershkrimi;

        public TextView getTxtPershkrimi() {
            return txtPershkrimi;
        }

        public void setTxtPershkrimi(TextView txtPershkrimi) {
            this.txtPershkrimi = txtPershkrimi;
        }
    }

    public class Adapteri extends ArrayAdapter<ChatRoom>
    {
        public Adapteri() {
            super(getApplicationContext(), R.layout.activity_main, alChatRoom);
        }

        public clsHolder objHolder;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if(v==null)
            {
                LayoutInflater layInflator =(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = layInflator.inflate(R.layout.list_view_row, null);
                objHolder = new clsHolder();
                objHolder.setTxtPershkrimi((TextView) findViewById(R.id.txtRreshti));
                v.setTag(objHolder);
            }
            else {
                objHolder = (clsHolder)v.getTag();
            }
            objHolder.getTxtPershkrimi().setText(((ChatRoom)alChatRoom.get(position)).getPershkrimi());
            return v;
        }
    }
}