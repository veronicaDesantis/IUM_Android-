package it.veronica.coursemanagement.controllers.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import it.veronica.coursemanagement.model.Teacher;
import it.veronica.coursemanagement.model.Teacher_course;
import it.veronica.coursemanagement.model.dbManager;
import it.veronica.coursemanagement.utility.FormEnum;
import it.veronica.coursemanagement.utility.PreferencesManager;

public class DisponibilityRegistryFragment extends Fragment {

    private View root = null;
    private dbManager  db = null;
    private Context myContext = null;
    private FormEnum formEnum = FormEnum.DETAIL;
    private Teacher teacher;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_disponibility_registry, container, false);
        ((RootActivity) getActivity()).getSupportActionBar().setTitle(R.string.disponibility_page);
        myContext = this.getContext();
        db = new dbManager(myContext);
        ((RootActivity)getActivity()).getSupportActionBar().show();

        FloatingActionButton backButton = root.findViewById(R.id.backButton);
        backButton.setOnTouchListener(backButtonListener);
        Button editButton = root.findViewById(R.id.editButton);
        editButton.setOnClickListener(editButtonListener);
        Button saveButton = root.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(saveButtonListener);
        Button deleteButton = root.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(deleteButtonListener);

        TextInputLayout dateTextInput = root.findViewById(R.id.dateTextInput);
        TextInputLayout slotTextInput = root.findViewById(R.id.slotTextInput);
        TextInputLayout courseTextInput = root.findViewById(R.id.courseTextInput);

        PreferencesManager preferencesManager = PreferencesManager.getInstance(getResources().getString(R.string.preferencesManager), myContext);
        int user_id = Integer.parseInt(preferencesManager.GetPreferenceByKey(getResources().getString(R.string.user_id)));
        teacher = db.GetTeacherByUserId(user_id);
        Course[] courses = db.GetAllCourse(teacher.getId());
        ArrayList<Course> list1 = new ArrayList<Course>();
        Collections.addAll(list1, courses);
        CourseAdapter adapter = new CourseAdapter(getActivity(), list1, true);
        Spinner courseList = root.findViewById(R.id.courseList);
        courseList.setAdapter(adapter);
        courseList.setVisibility(View.GONE);

        Bundle bundle = this.getArguments();
        int i = 0;
        if (bundle != null) {
            i = bundle.getInt(getResources().getString(R.string.disponibility_id), 0);
            if (i != 0)
            {
                formEnum = FormEnum.DETAIL;
                //Detail
                Disponibility disponibility = db.GetDisponibilityById(i);
                TextInputLayout idTextInput = root.findViewById(R.id.idTextInput);
                dateTextInput.getEditText().setText(disponibility.getDatetime());
                slotTextInput.getEditText().setText(disponibility.getSlotTime());
                idTextInput.getEditText().setText(String.valueOf(disponibility.getId()));
                courseTextInput.getEditText().setText(disponibility.getCourse().getTitle());
                courseList.setSelection(GetIndex(courses, disponibility.getCourse()));
            }
            String j = bundle.getString(getResources().getString(R.string.date_paremeter));
            if (j != null)
            {
                dateTextInput.getEditText().setText(j);
            }
            j = bundle.getString(getResources().getString(R.string.slot_parameter));
            if (j != null)
            {
                slotTextInput.getEditText().setText(j);
            }
        }
        dateTextInput.setFocusable(false);
        dateTextInput.setEnabled(false);
        slotTextInput.setFocusable(false);
        slotTextInput.setEnabled(false);
        courseTextInput.setFocusable(false);
        courseTextInput.setEnabled(false);
        ToogleDetail(formEnum);
        return root;
    }

    private void ToogleDetail(FormEnum formEnum)
    {
        TextView title = root.findViewById(R.id.title);
        Button editButton = root.findViewById(R.id.editButton);
        Button saveButton = root.findViewById(R.id.saveButton);
        Button deleteButton = root.findViewById(R.id.deleteButton);
        TextInputLayout idTextInput = root.findViewById(R.id.idTextInput);
        TextInputLayout courseTextInput = root.findViewById(R.id.courseTextInput);
        Spinner courseList = root.findViewById(R.id.courseList);

        switch (formEnum) {
            case MODIFY:
                title.setText(R.string.disponibility_edit);
                editButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.VISIBLE);
                if (Integer.parseInt(idTextInput.getEditText().getText().toString()) != -1) {
                    deleteButton.setVisibility(View.VISIBLE);
                }
                courseList.setVisibility(View.VISIBLE);
                courseTextInput.setVisibility(View.GONE);
                break;
            case DETAIL:
                title.setText(R.string.disponibility_detail);
                editButton.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
                courseList.setVisibility(View.GONE);
                courseTextInput.setVisibility(View.VISIBLE);
                break;
        }
    }

    //#region LISTENER

    //Listener sul bottone della modifica
    private View.OnClickListener editButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            formEnum = FormEnum.MODIFY;
            ToogleDetail(formEnum);
        }
    };

    //Listener sul bottone del salvataggio
    private View.OnClickListener saveButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextInputLayout idTextInput = root.findViewById(R.id.idTextInput);
            TextInputLayout dateTextInput = root.findViewById(R.id.dateTextInput);
            TextInputLayout slotTextInput = root.findViewById(R.id.slotTextInput);

            String datetime = dateTextInput.getEditText().getText().toString();
            String starttime = slotTextInput.getEditText().getText().toString().split(" - ")[0];
            String endtime = slotTextInput.getEditText().getText().toString().split(" - ")[1];
            Spinner courseList = root.findViewById(R.id.courseList);

            int id = Integer.parseInt(idTextInput.getEditText().getText().toString());
            Course course = (Course)courseList.getSelectedItem();
            if (id == -1)
            {
                //Inserisco la disponibilità
                Teacher_course teacher_course = db.GetTeacherCourseByTeacherIdCourseId(teacher.getId(), course.getId());
                db.InsertDisponibility(teacher_course.getId(), datetime, starttime, endtime);
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.create_done), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            else
            {
                //la aggiorno
                Teacher_course teacher_course = db.GetTeacherCourseByTeacherIdCourseId(teacher.getId(), course.getId());
                db.UpdateDisponibility(id, teacher_course.getId());
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.edit_done), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            TextInputLayout courseTextInput = root.findViewById(R.id.courseTextInput);
            courseTextInput.getEditText().setText(course.getTitle());
            formEnum = FormEnum.DETAIL;
            ToogleDetail(formEnum);
        }
    };

    //Listener sul bottone di eliminazione
    private View.OnClickListener deleteButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextInputLayout idTextInput = root.findViewById(R.id.idTextInput);
            int id = Integer.parseInt(idTextInput.getEditText().getText().toString());
            db.DeleteDisponibility(id);
            Fragment disponibility = new DisponibilityFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(getResources().getString(R.string.deleted), true);
            disponibility.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, disponibility, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(DisponibilityFragment.class.getName()) // name can be null
                    .commit();
        }
    };

    //Listener sul bottone del torna indietro
    private View.OnTouchListener backButtonListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, DisponibilityFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(DisponibilityFragment.class.getName()) // name can be null
                    .commit();
            return  true;
        }
    };

    //#endregion

    private int GetIndex(Course[] courses, Course course)
    {
        for(int i = 0; i < courses.length; i++)
        {
            if (courses[i].getId() == course.getId())
            {
                return i;
            }
        }
        return -1;
    }
}