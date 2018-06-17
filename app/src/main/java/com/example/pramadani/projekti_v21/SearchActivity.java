package com.example.pramadani.projekti_v21;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class SearchActivity extends Fragment {

    private EditText editTextInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_search, container, false);

        editTextInput = v.findViewById(R.id.search_web_edittext);

        v.findViewById(R.id.web_search_button).setOnClickListener(new HandleClick());

        return v;
    }

    private class HandleClick implements View.OnClickListener {
        public void onClick(View arg0) {
            String searchFor = editTextInput.getText().toString();
            Intent viewSearch = new Intent(Intent.ACTION_WEB_SEARCH);
            viewSearch.putExtra(SearchManager.QUERY, searchFor);
            startActivity(viewSearch);
        }
    }
}
