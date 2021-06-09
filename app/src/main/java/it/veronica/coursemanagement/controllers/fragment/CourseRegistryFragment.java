package it.veronica.coursemanagement.controllers.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.coursemanagement.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import it.veronica.coursemanagement.controllers.RootActivity;
import it.veronica.coursemanagement.model.Course;
import it.veronica.coursemanagement.model.Teacher;
import it.veronica.coursemanagement.model.User;
import it.veronica.coursemanagement.model.User_type;
import it.veronica.coursemanagement.model.dbManager;
import it.veronica.coursemanagement.utility.AesCrypt;
import it.veronica.coursemanagement.utility.FormEnum;

public class CourseRegistryFragment extends Fragment {

    private View root = null;
    private dbManager  db = null;
    private Context myContext = null;
    private FormEnum formEnum = FormEnum.CREATION;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_course_registry, container, false);
        ((RootActivity) getActivity()).getSupportActionBar().setTitle(R.string.course_page);
        myContext = this.getContext();
        db = new dbManager(myContext);
        ((RootActivity)getActivity()).getSupportActionBar().show();

        FloatingActionButton backButton = root.findViewById(R.id.backButton);
        backButton.setOnTouchListener(backButtonListener);
        Button createButton = root.findViewById(R.id.createButton);
        createButton.setOnClickListener(createButtonListener);
        Button editButton = root.findViewById(R.id.editButton);
        editButton.setOnClickListener(editButtonListener);
        Button saveButton = root.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(saveButtonListener);
        Button deleteButton = root.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(deleteButtonListener);

        Bundle bundle = this.getArguments();
        int i = 0;
        if (bundle != null) {
            i = bundle.getInt(getResources().getString(R.string.course_id), 0);
            if (i != 0)
            {
                formEnum = FormEnum.DETAIL;
                //Detail
                Course course = db.GetCourseById(i);
                TextInputLayout codeTextInput = root.findViewById(R.id.codeTextInput);
                TextInputLayout titleTextInput = root.findViewById(R.id.titleTextInput);
                TextInputLayout descriptionTextInput = root.findViewById(R.id.descriptionTextInput);
                TextInputLayout cfuTextInput = root.findViewById(R.id.cfuTextInput);
                TextInputLayout idTextInput = root.findViewById(R.id.idTextInput);
                codeTextInput.getEditText().setText(course.getCode());
                codeTextInput.setFocusable(false);
                codeTextInput.setEnabled(false);
                titleTextInput.getEditText().setText(course.getTitle());
                titleTextInput.setFocusable(false);
                titleTextInput.setEnabled(false);
                descriptionTextInput.getEditText().setText(course.getDescription());
                descriptionTextInput.setFocusable(false);
                descriptionTextInput.setEnabled(false);
                cfuTextInput.getEditText().setText(String.valueOf(course.getCfu()));
                cfuTextInput.setFocusable(false);
                cfuTextInput.setEnabled(false);
                idTextInput.getEditText().setText(String.valueOf(course.getId()));
            }
        }
        ToogleDetail(formEnum);
        return root;
    }

    private void ToogleDetail(FormEnum formEnum)
    {
        TextView title = root.findViewById(R.id.title);
        Button createButton = root.findViewById(R.id.createButton);
        Button editButton = root.findViewById(R.id.editButton);
        Button saveButton = root.findViewById(R.id.saveButton);
        Button deleteButton = root.findViewById(R.id.deleteButton);
        TextInputLayout codeTextInput = root.findViewById(R.id.codeTextInput);
        TextInputLayout titleTextInput = root.findViewById(R.id.titleTextInput);
        TextInputLayout descriptionTextInput = root.findViewById(R.id.descriptionTextInput);
        TextInputLayout cfuTextInput = root.findViewById(R.id.cfuTextInput);
        Boolean isEditable = false;
        switch (formEnum) {
            case MODIFY:
                isEditable = true;
                title.setText(R.string.course_edit);
                createButton.setVisibility(View.GONE);
                editButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                break;
            case DETAIL:
                title.setText(R.string.course_detail);
                createButton.setVisibility(View.GONE);
                editButton.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
                break;
            case CREATION:
                isEditable = true;
                title.setText(R.string.course_new);
                createButton.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
                break;
        }
        codeTextInput.setFocusable(isEditable);
        codeTextInput.setEnabled(isEditable);
        titleTextInput.setFocusable(isEditable);
        titleTextInput.setEnabled(isEditable);
        descriptionTextInput.setFocusable(isEditable);
        descriptionTextInput.setEnabled(isEditable);
        cfuTextInput.setFocusable(isEditable);
        cfuTextInput.setEnabled(isEditable);
    }

    //#region LISTENER

    //Listener sul bottone della creazione
    private View.OnClickListener createButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextInputLayout codeTextInput = root.findViewById(R.id.codeTextInput);
            TextInputLayout titleTextInput = root.findViewById(R.id.titleTextInput);
            TextInputLayout descriptionTextInput = root.findViewById(R.id.descriptionTextInput);
            TextInputLayout cfuTextInput = root.findViewById(R.id.cfuTextInput);
            String code = codeTextInput.getEditText().getText().toString();
            String title = titleTextInput.getEditText().getText().toString();
            String description = descriptionTextInput.getEditText().getText().toString();
            String cfu = cfuTextInput.getEditText().getText().toString();
            //Verify fields
            if (code.equals(""))
            {
                codeTextInput.setError(getResources().getString(R.string.required));
            }
            else if (title.equals("")) {
                titleTextInput.setError(getResources().getString(R.string.required));
            }
            else if (description.equals("")) {
                descriptionTextInput.setError(getResources().getString(R.string.required));
            }
            else if (cfu.equals("")) {
                cfuTextInput.setError(getResources().getString(R.string.required));
            }
            else {
                //Creo il corso
                Course course = new Course(code, title, description, Integer.parseInt(cfu));
                int course_id = db.InsertCourse(course);
                TextInputLayout id = root.findViewById(R.id.idTextInput);
                id.getEditText().setText(String.valueOf(course_id));
                formEnum = FormEnum.DETAIL;
                ToogleDetail(formEnum);
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.create_done), Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        }
    };

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
            TextInputLayout codeTextInput = root.findViewById(R.id.codeTextInput);
            TextInputLayout titleTextInput = root.findViewById(R.id.titleTextInput);
            TextInputLayout descriptionTextInput = root.findViewById(R.id.descriptionTextInput);
            TextInputLayout cfuTextInput = root.findViewById(R.id.cfuTextInput);
            String code = codeTextInput.getEditText().getText().toString();
            String title = titleTextInput.getEditText().getText().toString();
            String description = descriptionTextInput.getEditText().getText().toString();
            String cfu = cfuTextInput.getEditText().getText().toString();
            int id = Integer.parseInt(idTextInput.getEditText().getText().toString());
            //Verify fields
            if (code.equals(""))
            {
                codeTextInput.setError(getResources().getString(R.string.required));
            }
            else if (title.equals("")) {
                titleTextInput.setError(getResources().getString(R.string.required));
            }
            else if (description.equals("")) {
                descriptionTextInput.setError(getResources().getString(R.string.required));
            }
            else if (cfu.equals("")) {
                cfuTextInput.setError(getResources().getString(R.string.required));
            }
            else {
                //Aggiorno il corso
                db.UpdateCourse(id, code, title, description, cfu);
                formEnum = FormEnum.DETAIL;
                ToogleDetail(formEnum);
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.edit_done), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    };

    //Listener sul bottone di eliminazione
    private View.OnClickListener deleteButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextInputLayout idTextInput = root.findViewById(R.id.idTextInput);
            int id = Integer.parseInt(idTextInput.getEditText().getText().toString());
            db.DeleteCourse(id);
            Fragment courseFragment = new CourseFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(getResources().getString(R.string.deleted), true);
            courseFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, courseFragment, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(CourseFragment.class.getName()) // name can be null
                    .commit();
        }
    };

    //Listener sul bottone del torna indietro
    private View.OnTouchListener backButtonListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, CourseFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(CourseFragment.class.getName()) // name can be null
                    .commit();
            return  true;
        }
    };
    //#endregion


}