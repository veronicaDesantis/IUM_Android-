package it.veronica.coursemanagement.controllers.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.coursemanagement.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collections;

import it.veronica.coursemanagement.adapters.CourseAdapter;
import it.veronica.coursemanagement.controllers.RootActivity;
import it.veronica.coursemanagement.model.Course;
import it.veronica.coursemanagement.model.Disponibility;
import it.veronica.coursemanagement.model.Reservation;
import it.veronica.coursemanagement.model.Teacher;
import it.veronica.coursemanagement.model.Teacher_course;
import it.veronica.coursemanagement.model.dbManager;
import it.veronica.coursemanagement.utility.FormEnum;
import it.veronica.coursemanagement.utility.PreferencesManager;

public class ReservationRegistryFragment extends Fragment {

    private View root = null;
    private dbManager  db = null;
    private Context myContext = null;
    private FormEnum formEnum = FormEnum.DETAIL;
    private Boolean editable;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_reservation_registry, container, false);
        ((RootActivity) getActivity()).getSupportActionBar().setTitle(R.string.disponibility_page);
        myContext = this.getContext();
        db = new dbManager(myContext);
        ((RootActivity)getActivity()).getSupportActionBar().show();

        FloatingActionButton backButton = root.findViewById(R.id.backButton);
        backButton.setOnTouchListener(backButtonListener);
        Button deleteButton = root.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(deleteButtonListener);



        Bundle bundle = this.getArguments();
        int i = 0;
        if (bundle != null) {
            String title = bundle.getString(getResources().getString(R.string.course_title_course));
            String teacher = bundle.getString(getResources().getString(R.string.teacher_name_teacher));
            String date = bundle.getString(getResources().getString(R.string.disponibility_date));
            String slot = bundle.getString(getResources().getString(R.string.disponibility_slot));
            int id = bundle.getInt(getResources().getString(R.string.reservation_id));
            TextInputLayout titleTextInput = root.findViewById(R.id.titleTextInput);
            TextInputLayout teacherTextInput = root.findViewById(R.id.teacherTextInput);
            TextInputLayout dateTextInput = root.findViewById(R.id.dateTextInput);
            TextInputLayout hourTextInput = root.findViewById(R.id.hourTextInput);
            TextInputLayout idTextInput = root.findViewById(R.id.idTextInput);
            titleTextInput.getEditText().setText(title);
            teacherTextInput.getEditText().setText(teacher);
            dateTextInput.getEditText().setText(date);
            hourTextInput.getEditText().setText(slot);
            idTextInput.getEditText().setText(String.valueOf(id));
            titleTextInput.setFocusable(false);
            titleTextInput.setEnabled(false);
            teacherTextInput.setFocusable(false);
            teacherTextInput.setEnabled(false);
            dateTextInput.setFocusable(false);
            dateTextInput.setEnabled(false);
            hourTextInput.setFocusable(false);
            hourTextInput.setEnabled(false);
            editable = bundle.getBoolean(getResources().getString(R.string.editable));
        }
        if (!editable)
        {
            deleteButton.setVisibility(View.GONE);
        }
        return root;
    }

    //#region LISTENER

    //Listener sul bottone di eliminazione
    private View.OnClickListener deleteButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextInputLayout idTextInput = root.findViewById(R.id.idTextInput);
            int id = Integer.parseInt(idTextInput.getEditText().getText().toString());
            Reservation reservation = db.GetReservationById(id);
            db.UpdateAvailability(reservation.getDisponibility_id(), 1);
            db.UpdateReservation(id);
            Fragment reservationFragment = new ReservationFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(getResources().getString(R.string.deleted), true);
            reservationFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, reservationFragment, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(ReservationFragment.class.getName()) // name can be null
                    .commit();
        }
    };

    //Listener sul bottone del torna indietro
    private View.OnTouchListener backButtonListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, ReservationFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(ReservationFragment.class.getName()) // name can be null
                    .commit();
            return  true;
        }
    };

    //#endregion
}