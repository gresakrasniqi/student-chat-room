package com.example.pramadani.projekti_v21;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

public class MainActivity extends Fragment {

    public MainActivity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_main, container, false);

        ListView lvChatRoom = (ListView) v.findViewById(R.id.chats_list);

        ArrayList<String> alChatRooms = new ArrayList<String>();
        alChatRooms.add("Inxhinieri Softuerike");
        alChatRooms.add("Elektonike");
        alChatRooms.add("Baza e te dhenave");
        alChatRooms.add("Mikrokontrollere dhe Mikroprocesore");

        ArrayAdapter<String> allItemsAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
               R.layout.list_view_row, alChatRooms);
        lvChatRoom.setAdapter(allItemsAdapter);

        lvChatRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        getActivity().startActivity(new Intent(getContext(), Message_activity.class));
                        break;
                }
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the tablayout, the listview, and the FloatingActionsMenu
        final TabLayout tabLayout = getActivity().findViewById(R.id.tabs);
        final ListView lv = view.findViewById(R.id.chats_list);
        FloatingActionsMenu floatingActionsMenu = view.findViewById(R.id.floating_action_menu);
        FloatingActionButton actionJoinChat = view.findViewById(R.id.action_join_chat);
        FloatingActionButton actionNewChat = view.findViewById(R.id.action_new_chat);

        floatingActionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                lv.setAlpha((float) 0.5);
                tabLayout.setAlpha((float) 0.5);
            }

            @Override
            public void onMenuCollapsed() {
                lv.setAlpha(1);
                tabLayout.setAlpha(1);
            }
        });

        actionJoinChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Enter chat ID");
                //alertDialog.setMessage("");
                final EditText input;
                input = new EditText(getContext());
                alertDialog.setView(input);
                alertDialog.setPositiveButton("Join", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Connected firebase
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });

        actionNewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getContext(), CreateNewChatActivity.class));
            }
        });
    }
}