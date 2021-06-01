package com.example.coursemanagement.controllers;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.coursemanagement.R;
import com.example.coursemanagement.model.User;
import com.example.coursemanagement.model.User_type;
import com.example.coursemanagement.model.dbManager;
import com.example.coursemanagement.utility.PreferencesManager;
import com.example.coursemanagement.utility.Utility;
import com.google.android.material.textfield.TextInputLayout;

public class Register extends Fragment{

    private dbManager db = null;
    private Context myContext = null;
    private View root = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_register, container, false);
        myContext = this.getContext();
        db = new dbManager(myContext);
        TextView href_login = root.findViewById(R.id.href_login);
        href_login.setOnClickListener(hrefLoginListener);
        Button loginButton = root.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(loginButtonListener);
        ((RootActivity)getActivity()).getSupportActionBar().hide();
        return root;

    }

    private View.OnClickListener hrefLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Cambio fragment per il login
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, Login.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name") // name can be null
                    .commit();
        }
    };

    private View.OnClickListener loginButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextInputLayout nameTextInput = root.findViewById(R.id.nameTextInput);
            TextInputLayout surnameTextInput = root.findViewById(R.id.surnameTextInput);
            TextInputLayout emailTextInput = root.findViewById(R.id.emailTextInput);
            TextInputLayout passwordTextInput = root.findViewById(R.id.passwordTextInput);
            TextInputLayout confirmPasswordTextInput = root.findViewById(R.id.confirmPasswordTextInput);
            String name = nameTextInput.getEditText().getText().toString();
            String surname = surnameTextInput.getEditText().getText().toString();
            String email = emailTextInput.getEditText().getText().toString();
            String password = passwordTextInput.getEditText().getText().toString();
            String confirmPassword = confirmPasswordTextInput.getEditText().getText().toString();
            //Verify password
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
            else if (Utility.IsPasswordValid(password)) {
                passwordTextInput.setError(getResources().getString(R.string.password_validity_error));
            }
            else if (!password.equals(confirmPassword)) {
                passwordTextInput.setError(getResources().getString(R.string.password_confirm_error));
                confirmPasswordTextInput.setError(getResources().getString(R.string.password_confirm_error));
            }
            else {
                User dbUser = db.GetUserByMail(email);
                if (dbUser == null) {
                    User user = new User(name, surname, email, password, User_type.STUDENT.getValue());
                    int user_id = db.InsertUser(user);
                    if (user_id == -1) {
                        emailTextInput.setError(getResources().getString(R.string.generic_error));
                    }
                    else {
                        PreferencesManager preferencesManager = PreferencesManager.getInstance(getResources().getString(R.string.preferencesManager), myContext);
                        preferencesManager.PutPreferenceByKey("User_id", String.valueOf(user_id));
                        preferencesManager.PutPreferenceByKey("User_type_id", String.valueOf(user.getUser_type_id()));
                        /*Intent intent = new Intent(RegisterActivity.this, MenuActivity.class);
                        startActivity(intent);*/
                    }
                }
                else
                {
                    emailTextInput.setError(getResources().getString(R.string.email_in_db_error));
                }
            }

        }
    };
}
