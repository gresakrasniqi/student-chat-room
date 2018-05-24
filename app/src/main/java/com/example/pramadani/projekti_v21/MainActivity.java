package com.example.pramadani.projekti_v21;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Fragment {
    ArrayList alChatRoom = new ArrayList();

    public MainActivity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_main, container, false);
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

//    public class Adapteri extends ArrayAdapter<ChatRoom>
//    {
//        public Adapteri() {
//            super(getApplicationContext(), R.layout.activity_main, alChatRoom);
//        }
//
//        public clsHolder objHolder;
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            View v = convertView;
//
//            if(v==null)
//            {
//                LayoutInflater layInflator =(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                v = layInflator.inflate(R.layout.list_view_row, null);
//                objHolder = new clsHolder();
//                objHolder.setTxtPershkrimi((TextView) findViewById(R.id.txtRreshti));
//                v.setTag(objHolder);
//            }
//            else {
//                objHolder = (clsHolder)v.getTag();
//            }
//            objHolder.getTxtPershkrimi().setText(((ChatRoom)alChatRoom.get(position)).getPershkrimi());
//            return v;
//        }
//    }
}