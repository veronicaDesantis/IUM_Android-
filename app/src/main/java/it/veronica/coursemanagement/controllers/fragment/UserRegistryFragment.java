package it.veronica.coursemanagement.controllers.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.coursemanagement.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import it.veronica.coursemanagement.controllers.RootActivity;
import it.veronica.coursemanagement.model.Course;
import it.veronica.coursemanagement.model.User;
import it.veronica.coursemanagement.model.User_type;
import it.veronica.coursemanagement.model.dbManager;
import it.veronica.coursemanagement.utility.AesCrypt;
import it.veronica.coursemanagement.utility.FormEnum;

public class UserRegistryFragment extends Fragment {

    private View root = null;
    private dbManager  db = null;
    private Context myContext = null;
    private FormEnum formEnum = FormEnum.CREATION;
    private int user_type_id = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_user_registry, container, false);
        ((RootActivity) getActivity()).getSupportActionBar().setTitle(R.string.user_page);
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
            i = bundle.getInt(getResources().getString(R.string.user_id), 0);
            if (i != 0)
            {
                formEnum = FormEnum.DETAIL;
                //Detail
                User user = db.GetUserById(i);
                TextInputLayout nameTextInput = root.findViewById(R.id.nameTextInput);
                TextInputLayout surnameTextInput = root.findViewById(R.id.surnameTextInput);
                TextInputLayout mailTextInput = root.findViewById(R.id.mailTextInput);
                TextInputLayout idTextInput = root.findViewById(R.id.idTextInput);
                nameTextInput.getEditText().setText(user.getName());
                nameTextInput.setFocusable(false);
                nameTextInput.setEnabled(false);
                surnameTextInput.getEditText().setText(user.getSurname());
                surnameTextInput.setFocusable(false);
                surnameTextInput.setEnabled(false);
                mailTextInput.getEditText().setText(user.getEmail());
                mailTextInput.setFocusable(false);
                mailTextInput.setEnabled(false);
                idTextInput.getEditText().setText(String.valueOf(user.getId()));
                user_type_id = user.getUser_type_id();
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
        TextInputLayout nameTextInput = root.findViewById(R.id.nameTextInput);
        TextInputLayout surnameTextInput = root.findViewById(R.id.surnameTextInput);
        TextInputLayout mailTextInput = root.findViewById(R.id.mailTextInput);
        Chip chip = root.findViewById(R.id.chip);
        RadioGroup user_type = root.findViewById(R.id.user_type);
        Boolean isEditable = false;
        switch (formEnum) {
            case MODIFY:
                isEditable = true;
                title.setText(R.string.user_edit);
                createButton.setVisibility(View.GONE);
                editButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                chip.setVisibility(View.GONE);
                user_type.setVisibility(View.VISIBLE);
                if (user_type_id == User_type.STUDENT.getValue()) {
                    user_type.check(R.id.user_type_student);
                }
                else if (user_type_id == User_type.TEACHER.getValue()) {
                    user_type.check(R.id.user_type_teacher);
                }
                else if (user_type_id == User_type.ADMIN.getValue()) {
                    user_type.check(R.id.user_type_admin);
                }
                break;
            case DETAIL:
                title.setText(R.string.user_detail);
                createButton.setVisibility(View.GONE);
                editButton.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
                chip.setVisibility(View.VISIBLE);
                user_type.setVisibility(View.GONE);
                if (user_type_id == User_type.STUDENT.getValue()) {
                    chip.setChipIcon(getContext().getResources().getDrawable(R.drawable.ic_customer));
                }
                else if (user_type_id == User_type.TEACHER.getValue()) {
                    chip.setChipIcon(getContext().getResources().getDrawable(R.drawable.ic_coaching));
                }
                else
                {
                    chip.setChipIcon(getContext().getResources().getDrawable(R.drawable.ic_admin));
                }
                break;
            case CREATION:
                isEditable = true;
                title.setText(R.string.user_new);
                createButton.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
                chip.setVisibility(View.GONE);
                user_type.setVisibility(View.VISIBLE);
                user_type.check(R.id.user_type_student);
                break;
        }
        nameTextInput.setFocusable(isEditable);
        nameTextInput.setEnabled(isEditable);
        surnameTextInput.setFocusable(isEditable);
        surnameTextInput.setEnabled(isEditable);
        mailTextInput.setFocusable(isEditable);
        mailTextInput.setEnabled(isEditable);
    }

    //#region LISTENER

    //Listener sul bottone della creazione
    private View.OnClickListener createButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextInputLayout nameTextInput = root.findViewById(R.id.nameTextInput);
            TextInputLayout surnameTextInput = root.findViewById(R.id.surnameTextInput);
            TextInputLayout mailTextInput = root.findViewById(R.id.mailTextInput);
            RadioGroup user_type = root.findViewById(R.id.user_type);
            String name = nameTextInput.getEditText().getText().toString();
            String surname = surnameTextInput.getEditText().getText().toString();
            String email = mailTextInput.getEditText().getText().toString();

            //Verify fields
            if (name.equals(""))
            {
                nameTextInput.setError(getResources().getString(R.string.required));
            }
            else if (surname.equals("")) {
                surnameTextInput.setError(getResources().getString(R.string.required));
            }
            else if (email.equals("")) {
                mailTextInput.setError(getResources().getString(R.string.required));
            }
            else if (user_type.getCheckedRadioButtonId() == View.NO_ID) {
                RadioButton radioButton = root.findViewById(R.id.user_type_student);
                radioButton.setError(getResources().getString(R.string.required));
            }
            else {
                int user_type_clicked = user_type.getCheckedRadioButtonId();
                if (user_type_clicked == R.id.user_type_student)
                {
                    user_type_id = User_type.STUDENT.getValue();
                }
                else if (user_type_clicked == R.id.user_type_teacher)
                {
                    user_type_id = User_type.TEACHER.getValue();
                }
                else{
                    user_type_id = User_type.ADMIN.getValue();
                }
                //Creo l'utente
                String encryptedPassword = AesCrypt.encrypt(name);
                User user = new User(name, surname, email, encryptedPassword, user_type_id);
                int user_id = db.InsertUser(user);
                TextInputLayout id = root.findViewById(R.id.idTextInput);
                id.getEditText().setText(String.valueOf(user_id));
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
            TextInputLayout nameTextInput = root.findViewById(R.id.nameTextInput);
            TextInputLayout surnameTextInput = root.findViewById(R.id.surnameTextInput);
            TextInputLayout mailTextInput = root.findViewById(R.id.mailTextInput);
            RadioGroup user_type = root.findViewById(R.id.user_type);
            String name = nameTextInput.getEditText().getText().toString();
            String surname = surnameTextInput.getEditText().getText().toString();
            String email = mailTextInput.getEditText().getText().toString();
            int id = Integer.parseInt(idTextInput.getEditText().getText().toString());
            //Verify fields
            if (name.equals(""))
            {
                nameTextInput.setError(getResources().getString(R.string.required));
            }
            else if (surname.equals("")) {
                surnameTextInput.setError(getResources().getString(R.string.required));
            }
            else if (email.equals("")) {
                mailTextInput.setError(getResources().getString(R.string.required));
            }
            else if (user_type.getCheckedRadioButtonId() == View.NO_ID) {
                RadioButton radioButton = root.findViewById(R.id.user_type_student);
                radioButton.setError(getResources().getString(R.string.required));
            }
            else {
                //Aggiorno l'utenza
                db.UpdateUser(id, name, surname, email);
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
            db.DeleteUser(id);
            Fragment userFragment = new UserFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(getResources().getString(R.string.deleted), true);
            userFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, userFragment, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(UserFragment.class.getName()) // name can be null
                    .commit();
        }
    };

    //Listener sul bottone del torna indietro
    private View.OnTouchListener backButtonListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, UserFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(UserFragment.class.getName()) // name can be null
                    .commit();
            return  true;
        }
    };
    //#endregion


}