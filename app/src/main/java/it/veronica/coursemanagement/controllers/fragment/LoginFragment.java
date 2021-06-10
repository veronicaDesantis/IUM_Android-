package it.veronica.coursemanagement.controllers.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.coursemanagement.R;

import it.veronica.coursemanagement.controllers.RootActivity;
import it.veronica.coursemanagement.model.User;
import it.veronica.coursemanagement.model.dbManager;
import it.veronica.coursemanagement.utility.AesCrypt;
import it.veronica.coursemanagement.utility.PreferencesManager;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {

    private dbManager db = null;
    private Context myContext = null;
    private View root;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_login, container, false);
        myContext = this.getContext();
        db = new dbManager(myContext);
        ((RootActivity) getActivity()).getSupportActionBar().setTitle(R.string.login_page);
        Button btn_login = root.findViewById(R.id.login_btn);
        btn_login.setOnClickListener(btnLoginListener);
        TextView href_register = root.findViewById(R.id.href_register);
        href_register.setOnClickListener(hrefRegisterListener);
        TextView href_forgot_password = root.findViewById(R.id.href_forgot_password);
        href_forgot_password.setOnClickListener(hrefForgotListener);
        return root;
    }

    private View.OnClickListener btnLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextInputLayout textInputEmail = root.findViewById(R.id.textInputEmail);
            TextInputLayout textInputPassword = root.findViewById(R.id.textInputPassword);
            String textEmail = textInputEmail.getEditText().getText().toString();
            String textPassword = textInputPassword.getEditText().getText().toString();
            if (textEmail.equals(""))
            {
                textInputEmail.setError(getResources().getString(R.string.email_error));
            }
            else if (textPassword.toString().equals("")) {
                textInputPassword.setError(getResources().getString(R.string.password_error));
            }
            else
            {
                //cifro la password
                String ecryptedPassword = AesCrypt.encrypt(textPassword);
                User dbUser = db.GetUserByMail_Password(textEmail, ecryptedPassword);
                if (dbUser != null)
                {
                    PreferencesManager preferencesManager = PreferencesManager.getInstance(getResources().getString(R.string.preferencesManager), myContext);
                    preferencesManager.PutPreferenceByKey(getResources().getString(R.string.user_id), String.valueOf(dbUser.getId()));
                    preferencesManager.PutPreferenceByKey(getResources().getString(R.string.user_type_id), String.valueOf(dbUser.getUser_type_id()));
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
                    .replace(R.id.nav_host_fragment, RegisterFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(RegisterFragment.class.getName()) // name can be null
                    .commit();
        }
    };

    private View.OnClickListener hrefForgotListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Cambio fragment per il login
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, ForgotFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(ForgotFragment.class.getName()) // name can be null
                    .commit();
        }
    };
}