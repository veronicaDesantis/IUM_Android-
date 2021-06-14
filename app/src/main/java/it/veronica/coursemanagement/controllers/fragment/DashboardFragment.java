package it.veronica.coursemanagement.controllers.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.coursemanagement.R;

import it.veronica.coursemanagement.controllers.RootActivity;
import it.veronica.coursemanagement.model.dbManager;
import com.google.android.material.chip.Chip;

public class DashboardFragment extends Fragment {

    private dbManager  db = null;
    private Context myContext = null;
    private PopupMenu popupMenu;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        myContext = this.getContext();
        db = new dbManager(myContext);
        ((RootActivity) getActivity()).getSupportActionBar().setTitle(R.string.dashboard_page);
        int countCourse = db.CountCourse();
        int countTeacher = db.CountTeacher();
        Chip courseChip = root.findViewById(R.id.course_kpi);
        courseChip.setText(String.valueOf(countCourse));
        Chip teacherChip = root.findViewById(R.id.teacher_kpi);
        teacherChip.setText(String.valueOf(countTeacher));
        ((RootActivity)getActivity()).getSupportActionBar().show();
        return root;
    }
}