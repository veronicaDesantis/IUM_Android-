package it.veronica.coursemanagement.controllers.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.coursemanagement.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import it.veronica.coursemanagement.controllers.RootActivity;
import it.veronica.coursemanagement.model.Teacher;
import it.veronica.coursemanagement.model.User;
import it.veronica.coursemanagement.model.User_type;
import it.veronica.coursemanagement.model.dbManager;
import it.veronica.coursemanagement.utility.AesCrypt;
import it.veronica.coursemanagement.utility.FormEnum;
import it.veronica.coursemanagement.utility.PreferencesManager;
import it.veronica.coursemanagement.utility.Utility;

public class TeacherRegistryFragment extends Fragment {

    private View root = null;
    private dbManager  db = null;
    private Context myContext = null;
    private FormEnum formEnum = FormEnum.CREATION;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_teacher_registry, container, false);
        ((RootActivity) getActivity()).getSupportActionBar().setTitle(R.string.teacher_page);
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
            i = bundle.getInt(getResources().getString(R.string.teacher_id), 0);
            if (i != 0)
            {
                formEnum = FormEnum.DETAIL;
                //Detail
                Teacher teacher = db.GetTeacherById(i);
                TextInputLayout nameTextInput = root.findViewById(R.id.nameTextInput);
                TextInputLayout surnameTextInput = root.findViewById(R.id.surnameTextInput);
                TextInputLayout emailTextInput = root.findViewById(R.id.emailTextInput);
                TextInputLayout idTextInput = root.findViewById(R.id.idTextInput);
                TextInputLayout userIdTextInput = root.findViewById(R.id.userIdTextInput);
                nameTextInput.getEditText().setText(teacher.getName());
                nameTextInput.setFocusable(false);
                nameTextInput.setEnabled(false);
                surnameTextInput.getEditText().setText(teacher.getSurname());
                surnameTextInput.setFocusable(false);
                surnameTextInput.setEnabled(false);
                User user = db.GetUserById(teacher.getUser_id());
                emailTextInput.getEditText().setText(user.getEmail());
                emailTextInput.setFocusable(false);
                emailTextInput.setEnabled(false);
                idTextInput.getEditText().setText(String.valueOf(teacher.getId()));
                userIdTextInput.getEditText().setText(String.valueOf(teacher.getUser_id()));
            }
        }
        ToogleDetail(formEnum);
        return root;
    }

    private void ToogleDetail(FormEnum formEnum)
    {
        TextView teacher_title = root.findViewById(R.id.teacher_title);
        Button createButton = root.findViewById(R.id.createButton);
        Button editButton = root.findViewById(R.id.editButton);
        Button saveButton = root.findViewById(R.id.saveButton);
        Button deleteButton = root.findViewById(R.id.deleteButton);
        TextInputLayout nameTextInput = root.findViewById(R.id.nameTextInput);
        TextInputLayout surnameTextInput = root.findViewById(R.id.surnameTextInput);
        TextInputLayout emailTextInput = root.findViewById(R.id.emailTextInput);
        Boolean isEditable = false;
        switch (formEnum) {
            case MODIFY:
                isEditable = true;
                teacher_title.setText(R.string.teacher_edit);
                createButton.setVisibility(View.GONE);
                editButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                break;
            case DETAIL:
                teacher_title.setText(R.string.teacher_detail);
                createButton.setVisibility(View.GONE);
                editButton.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
                break;
            case CREATION:
                isEditable = true;
                teacher_title.setText(R.string.teacher_new);
                createButton.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
                break;
        }
        nameTextInput.setFocusable(isEditable);
        nameTextInput.setEnabled(isEditable);
        surnameTextInput.setFocusable(isEditable);
        surnameTextInput.setEnabled(isEditable);
        emailTextInput.setFocusable(isEditable);
        emailTextInput.setEnabled(isEditable);
    }

    //#region LISTENER

    //Listener sul bottone della creazione
    private View.OnClickListener createButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextInputLayout nameTextInput = root.findViewById(R.id.nameTextInput);
            TextInputLayout surnameTextInput = root.findViewById(R.id.surnameTextInput);
            TextInputLayout emailTextInput = root.findViewById(R.id.emailTextInput);
            String name = nameTextInput.getEditText().getText().toString();
            String surname = surnameTextInput.getEditText().getText().toString();
            String email = emailTextInput.getEditText().getText().toString();
            //Verify fields
            if (name.equals(""))
            {
                nameTextInput.setError(getResources().getString(R.string.name_error));
            }
            else if (surname.equals("")) {
                surnameTextInput.setError(getResources().getString(R.string.surname_error));
            }
            else if (email.equals("")) {
                emailTextInput.setError(getResources().getString(R.string.email_error));
            }
            else {
                //Creo l'utente normale
                String password = AesCrypt.encrypt(name);
                User dbUser = db.GetUserByMail(email);
                if (dbUser == null) {
                    String encryptedPassword = AesCrypt.encrypt(password);
                    User user = new User(name, surname, email, encryptedPassword, User_type.TEACHER.getValue());
                    int user_id = db.InsertUser(user);
                    if (user_id == -1) {
                        emailTextInput.setError(getResources().getString(R.string.generic_error));
                    }
                    else {
                        //Creo anche il docente
                        Teacher teacher = new Teacher(name, surname, user_id);
                        int teacher_id = db.InsertTeacher(teacher);
                        TextInputLayout idTextInput = root.findViewById(R.id.idTextInput);
                        idTextInput.getEditText().setText(String.valueOf(teacher_id));
                        TextInputLayout userIdTextInput = root.findViewById(R.id.userIdTextInput);
                        userIdTextInput.getEditText().setText(String.valueOf(user_id));
                        formEnum = FormEnum.DETAIL;
                        ToogleDetail(formEnum);
                        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.create_done), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
                else
                {
                    emailTextInput.setError(getResources().getString(R.string.email_in_db_error));
                }
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
            TextInputLayout userIdTextInput = root.findViewById(R.id.userIdTextInput);
            TextInputLayout nameTextInput = root.findViewById(R.id.nameTextInput);
            TextInputLayout surnameTextInput = root.findViewById(R.id.surnameTextInput);
            TextInputLayout emailTextInput = root.findViewById(R.id.emailTextInput);
            String name = nameTextInput.getEditText().getText().toString();
            String surname = surnameTextInput.getEditText().getText().toString();
            String email = emailTextInput.getEditText().getText().toString();
            int id = Integer.parseInt(idTextInput.getEditText().getText().toString());
            int user_id = Integer.parseInt(userIdTextInput.getEditText().getText().toString());
            //Verify fields
            if (name.equals(""))
            {
                nameTextInput.setError(getResources().getString(R.string.name_error));
            }
            else if (surname.equals("")) {
                surnameTextInput.setError(getResources().getString(R.string.surname_error));
            }
            else if (email.equals("")) {
                emailTextInput.setError(getResources().getString(R.string.email_error));
            }
            else {
                //Aggiorno l'utente normale
                db.UpdateMail(user_id, email);
                db.UpdateTeacher(id, name,surname);
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
            TextInputLayout userIdTextInput = root.findViewById(R.id.userIdTextInput);
            int id = Integer.parseInt(idTextInput.getEditText().getText().toString());
            int user_id = Integer.parseInt(userIdTextInput.getEditText().getText().toString());
            db.DeleteTeacher(id);
            db.DeleteUser(user_id);
            Fragment teacherFragment = new TeacherFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(getResources().getString(R.string.deleted), true);
            teacherFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, teacherFragment, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(TeacherFragment.class.getName()) // name can be null
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