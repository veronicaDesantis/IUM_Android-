package it.veronica.coursemanagement.controllers.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.coursemanagement.R;

import it.veronica.coursemanagement.controllers.RootActivity;
import it.veronica.coursemanagement.model.dbManager;
import it.veronica.coursemanagement.utility.PreferencesManager;

public class LogoutFragment extends Fragment {

    private dbManager  db = null;
    private Context myContext = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Svuoto le preference e ricarico la dashboard
        myContext = this.getContext();
        db = new dbManager(myContext);
        PreferencesManager.getInstance(getResources().getString(R.string.preferencesManager), myContext).ClearPreference();
        Intent intent = new Intent(getActivity(), RootActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("log_out", true);
        startActivity(intent);
        getActivity().finish();
        return null;
    }
}