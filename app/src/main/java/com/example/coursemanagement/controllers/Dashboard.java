package com.example.coursemanagement.controllers;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.coursemanagement.R;
import com.example.coursemanagement.model.User_type;
import com.example.coursemanagement.model.dbManager;
import com.example.coursemanagement.utility.PreferencesManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class Dashboard extends Fragment {

    private dbManager  db = null;
    private Context myContext = null;
    private PopupMenu popupMenu;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        myContext = this.getContext();
        db = new dbManager(myContext);
        int countCourse = db.CountCourse();
        int countTeacher = db.CountTeacher();
        Chip courseChip = root.findViewById(R.id.course_kpi);
        courseChip.setText(String.valueOf(countCourse));
        Chip teacherChip = root.findViewById(R.id.teacher_kpi);
        teacherChip.setText(String.valueOf(countTeacher));
        FloatingActionButton floatButton = root.findViewById(R.id.floatingActionButton);
        floatButton.setOnTouchListener(floatButtonListener);
        ((RootActivity)getActivity()).getSupportActionBar().show();
        return root;
    }

    private View.OnTouchListener floatButtonListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            //Cambio fragment per il login
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, Login.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name") // name can be null
                    .commit();
            return true;
        }
    };
}