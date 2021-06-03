package it.veronica.coursemanagement.controllers;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.coursemanagement.R;
import it.veronica.coursemanagement.model.dbManager;

public class Catalogue extends Fragment {

    private dbManager  db = null;
    private Context myContext = null;
    private PopupMenu popupMenu;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_catalogue, container, false);
        myContext = this.getContext();
        db = new dbManager(myContext);
        ((RootActivity)getActivity()).getSupportActionBar().show();
        return root;
    }
}