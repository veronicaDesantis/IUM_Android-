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
import it.veronica.coursemanagement.model.dbManager;
import it.veronica.coursemanagement.tree_holder.ItemHolder;
import it.veronica.coursemanagement.utility.FormEnum;
import it.veronica.coursemanagement.utility.PreferencesManager;

public class CatalogueRegistryFragment extends Fragment {

    private View root = null;
    private dbManager  db = null;
    private Context myContext = null;
    private FormEnum formEnum = FormEnum.CREATION;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_catalogue_registry, container, false);
        ((RootActivity) getActivity()).getSupportActionBar().setTitle(R.string.catalogue_menu);
        myContext = this.getContext();
        db = new dbManager(myContext);
        ((RootActivity)getActivity()).getSupportActionBar().show();

        FloatingActionButton backButton = root.findViewById(R.id.backButton);
        backButton.setOnTouchListener(backButtonListener);
        Button confirmButton = root.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(confirmButtonListener);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String title = bundle.getString(getResources().getString(R.string.course_title_course));
            String teacher = bundle.getString(getResources().getString(R.string.teacher_name_teacher));
            String date = bundle.getString(getResources().getString(R.string.disponibility_date));
            String slot = bundle.getString(getResources().getString(R.string.disponibility_slot));
            int i = bundle.getInt(getResources().getString(R.string.disponibility_id), 0);
            if (i != 0)
            {
                //Detail
                TextInputLayout titleTextInput = root.findViewById(R.id.titleTextInput);
                TextInputLayout teacherTextInput = root.findViewById(R.id.teacherTextInput);
                TextInputLayout dateTextInput = root.findViewById(R.id.dateTextInput);
                TextInputLayout hourTextInput = root.findViewById(R.id.hourTextInput);
                TextInputLayout idTextInput = root.findViewById(R.id.idTextInput);
                titleTextInput.getEditText().setText(title);
                teacherTextInput.getEditText().setText(teacher);
                dateTextInput.getEditText().setText(date);
                hourTextInput.getEditText().setText(slot);
                idTextInput.getEditText().setText(String.valueOf(i));
                titleTextInput.setFocusable(false);
                titleTextInput.setEnabled(false);
                teacherTextInput.setFocusable(false);
                teacherTextInput.setEnabled(false);
                dateTextInput.setFocusable(false);
                dateTextInput.setEnabled(false);
                hourTextInput.setFocusable(false);
                hourTextInput.setEnabled(false);
            }
        }
        return root;
    }



    //#region LISTENER

    //Listener sul bottone della creazione
    private View.OnClickListener confirmButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Blocco la disponibilità
            TextInputLayout idTextLayout = root.findViewById(R.id.idTextInput);
            int disponibility_id = Integer.parseInt(idTextLayout.getEditText().getText().toString());
            db.UpdateAvailability(disponibility_id, 2);
            //Inserisco la prenotazione
            PreferencesManager preferencesManager = PreferencesManager.getInstance(getResources().getString(R.string.preferencesManager), myContext);
            int user_id = Integer.parseInt(preferencesManager.GetPreferenceByKey(getResources().getString(R.string.user_id)));
            db.InsertReservation(user_id, disponibility_id);
            Fragment catalogueFragment = new CatalogueFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(getResources().getString(R.string.reservation_done), true);
            catalogueFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, catalogueFragment, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(CatalogueFragment.class.getName()) // name can be null
                    .commit();
        }
    };

    //Listener sul bottone del torna indietro
    private View.OnTouchListener backButtonListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, CatalogueFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(CatalogueFragment.class.getName()) // name can be null
                    .commit();
            return  true;
        }
    };
    //#endregion


}