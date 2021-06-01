package com.example.coursemanagement.controllers;

import android.content.Context;
import android.content.Intent;
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
import com.example.coursemanagement.model.dbManager;
import com.example.coursemanagement.utility.PreferencesManager;
import com.google.android.material.textfield.TextInputLayout;

public class Login extends Fragment {

    private dbManager db = null;
    private Context myContext = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        myContext = this.getContext();
        db = new dbManager(myContext);
        Button btn_login = root.findViewById(R.id.login_btn);
        btn_login.setOnClickListener(btnLoginListener);
        TextView href_register = root.findViewById(R.id.href_register);
        href_register.setOnClickListener(hrefRegisterListener);
        TextView href_forgot_password = root.findViewById(R.id.href_forgot_password);
        href_forgot_password.setOnClickListener(hrefForgotListener);
        ((RootActivity)getActivity()).getSupportActionBar().hide();
        return root;
    }

    private View.OnClickListener btnLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextInputLayout textInputEmail = v.findViewById(R.id.textInputEmail);
            TextInputLayout textInputPassword = v.findViewById(R.id.textInputPassword);
            if (textInputEmail.getEditText().getText().toString().equals(""))
            {
                textInputEmail.setError(getResources().getString(R.string.email_error));
            }
            else if (textInputPassword.getEditText().getText().toString().equals("")) {
                textInputPassword.setError(getResources().getString(R.string.password_error));
            }
            else
            {
                User dbUser = db.GetUserByMail_Password(textInputEmail.getEditText().getText().toString(), textInputPassword.getEditText().getText().toString());
                if (dbUser != null)
                {
                    PreferencesManager preferencesManager = PreferencesManager.getInstance(getResources().getString(R.string.preferencesManager), myContext);
                    preferencesManager.PutPreferenceByKey("User_id", String.valueOf(dbUser.getId()));
                    preferencesManager.PutPreferenceByKey("User_type_id", String.valueOf(dbUser.getUser_type_id()));
                    Intent intent = new Intent(getActivity(), RootActivity.class);
                    startActivity(intent);
                }
                else
                {
                    textInputPassword.setError(getResources().getString(R.string.user_error));
                }
            }
        }
    };

    private View.OnClickListener hrefRegisterListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Cambio fragment per il login
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, Register.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name") // name can be null
                    .commit();
        }
    };

    private View.OnClickListener hrefForgotListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Cambio fragment per il login
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, Forgot.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name") // name can be null
                    .commit();
        }
    };
}