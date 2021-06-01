package com.example.coursemanagement.controllers;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.coursemanagement.R;
import com.example.coursemanagement.model.User;
import com.example.coursemanagement.model.dbManager;
import com.example.coursemanagement.utility.PreferencesManager;
import com.example.coursemanagement.utility.Utility;
import com.google.android.material.textfield.TextInputLayout;

public class Forgot extends Fragment {

    private dbManager db = null;
    private Context myContext = null;
    private Button verify_btn = null;
    private View root = null;
    private int user_id = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_forgot, container, false);
        myContext = this.getContext();
        db = new dbManager(myContext);
        verify_btn = root.findViewById(R.id.verify_btn);
        verify_btn.setOnClickListener(verifyBtnListener);
        user_id = 0;
        ((RootActivity)getActivity()).getSupportActionBar().hide();
        return root;
    }


    private View.OnClickListener verifyBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextInputLayout textInputEmail = root.findViewById(R.id.textInputEmail);
            String inputEmail = textInputEmail.getEditText().getText().toString();
            if (inputEmail.equals(""))
            {
                textInputEmail.setError(getResources().getString(R.string.email_error));
            }
            else
            {
                //Verifico se esiste in DB
                User dbUser = db.GetUserByMail("%" + inputEmail + "%");
                if (dbUser == null)
                {
                    textInputEmail.setError(getResources().getString(R.string.email_not_in_db_error));
                }
                else
                {
                    user_id = dbUser.getId();
                    verify_btn.setVisibility(View.GONE);
                    textInputEmail.setVisibility(View.GONE);
                    TextInputLayout textInputPassword = root.findViewById(R.id.textInputPassword);
                    TextInputLayout textInputConfirmPassword= root.findViewById(R.id.textInputConfirmPassword);
                    Button change_btn = root.findViewById(R.id.change_btn);
                    textInputPassword.setVisibility(View.VISIBLE);
                    textInputConfirmPassword.setVisibility(View.VISIBLE);
                    change_btn.setVisibility(View.VISIBLE);
                    change_btn.setOnClickListener(changeBtnListener);
                }
            }
        }
    };

    private View.OnClickListener changeBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextInputLayout textInputPassword = root.findViewById(R.id.textInputPassword);
            TextInputLayout textInputConfirmPassword= root.findViewById(R.id.textInputConfirmPassword);
            String password = textInputPassword.getEditText().getText().toString();
            String confirmPassword = textInputConfirmPassword.getEditText().getText().toString();
            if (Utility.IsPasswordValid(password)) {
                textInputPassword.setError(getResources().getString(R.string.password_validity_error));
            }
            else if (!password.equals(confirmPassword)) {
                textInputPassword.setError(getResources().getString(R.string.password_confirm_error));
                textInputConfirmPassword.setError(getResources().getString(R.string.password_confirm_error));
            }
            else
            {
                //Aggiorno la pw
                db.UpdatePassword(user_id, password);
                PreferencesManager preferencesManager = PreferencesManager.getInstance(getResources().getString(R.string.preferencesManager), myContext);
                User user = db.GetUserById(user_id);
                preferencesManager.PutPreferenceByKey("User_id", String.valueOf(user_id));
                preferencesManager.PutPreferenceByKey("User_type_id", String.valueOf(user.getUser_type_id()));
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, Dashboard.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name") // name can be null
                        .commit();
            }
        }
    };
}
