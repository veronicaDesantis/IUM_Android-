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
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import it.veronica.coursemanagement.controllers.RootActivity;
import it.veronica.coursemanagement.model.User;
import it.veronica.coursemanagement.model.User_type;
import it.veronica.coursemanagement.model.dbManager;
import it.veronica.coursemanagement.utility.AesCrypt;
import it.veronica.coursemanagement.utility.FormEnum;
import it.veronica.coursemanagement.utility.Utility;

public class ChangePwFragment extends Fragment {

    private View root = null;
    private dbManager  db = null;
    private Context myContext = null;
    private int user_id;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_change_pw, container, false);
        ((RootActivity) getActivity()).getSupportActionBar().setTitle(R.string.profile_page);
        myContext = this.getContext();
        db = new dbManager(myContext);
        ((RootActivity)getActivity()).getSupportActionBar().show();

        Button confirmButton = root.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(confirmButtonListener);
        FloatingActionButton backButton = root.findViewById(R.id.backButton);
        backButton.setOnTouchListener(backButtonListener);

        Bundle bundle = this.getArguments();
        int i = 0;
        if (bundle != null) {
            i = bundle.getInt(getResources().getString(R.string.user_id), 0);
            if (i != 0)
            {
                user_id = i;
            }
        }
        return root;
    }

    //#region LISTENER

    //Listener sul bottone della modifica
    private View.OnTouchListener backButtonListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Fragment profileFragment = new ProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(getResources().getString(R.string.user_id), user_id);
            profileFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, profileFragment, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(ProfileFragment.class.getName()) // name can be null
                    .commit();
            return  true;
        }
    };

    //Listener sul bottone del salvataggio
    private View.OnClickListener confirmButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextInputLayout oldPasswordTextInput = root.findViewById(R.id.textInputOldPassword);
            TextInputLayout passwordTextInput = root.findViewById(R.id.textInputPassword);
            TextInputLayout confirmPasswordTextInput = root.findViewById(R.id.textInputConfirmPassword);
            String oldPassword = oldPasswordTextInput.getEditText().getText().toString();
            String password = passwordTextInput.getEditText().getText().toString();
            String confirmPassword = confirmPasswordTextInput.getEditText().getText().toString();
            //Verify fields
            User user = db.GetUserById(user_id);
            String encryptedOldPassword = AesCrypt.encrypt(oldPassword);
            if (!encryptedOldPassword.equals(user.getPassword())) {
                oldPasswordTextInput.setError(getResources().getString(R.string.old_password_error));
            }
            else if (!Utility.IsPasswordValid(password)) {
                passwordTextInput.setError(getResources().getString(R.string.password_validity_error));
            }
            else if (!password.equals(confirmPassword)) {
                passwordTextInput.setError(getResources().getString(R.string.password_confirm_error));
                confirmPasswordTextInput.setError(getResources().getString(R.string.password_confirm_error));
            }
            else {
                //Aggiorno l'utenza
                String newPassword = AesCrypt.encrypt(password);
                db.UpdatePassword(user_id, newPassword);
                Fragment profileFragment = new ProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(getResources().getString(R.string.user_id), user_id);
                bundle.putBoolean(getResources().getString(R.string.pw_changed), true);
                profileFragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, profileFragment, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(ProfileFragment.class.getName()) // name can be null
                        .commit();
            }
        }
    };
    //#endregion


}