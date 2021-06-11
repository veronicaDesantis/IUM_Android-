package it.veronica.coursemanagement.controllers.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

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
import it.veronica.coursemanagement.utility.FormEnum;
import it.veronica.coursemanagement.utility.PreferencesManager;

public class ApplicationSettingFragment extends Fragment {

    private View root = null;
    private Context myContext = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_application_setting, container, false);
        ((RootActivity) getActivity()).getSupportActionBar().setTitle(R.string.application_setting_page);
        myContext = this.getContext();
        ((RootActivity)getActivity()).getSupportActionBar().show();

        Button saveButton = root.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(saveButtonListener);

        TimePicker startTimePicker = root.findViewById(R.id.startTimePicker);
        startTimePicker.setIs24HourView(true);
        TimePicker endTimePicker = root.findViewById(R.id.endTimePicker);
        endTimePicker.setIs24HourView(true);

        PreferencesManager preferencesManager = PreferencesManager.getInstance(getResources().getString(R.string.preferencesManager), myContext);
        String start_time = preferencesManager.GetPreferenceByKey(getResources().getString(R.string.start_time_reservation));
        String end_time = preferencesManager.GetPreferenceByKey(getResources().getString(R.string.end_time_reservation));

        if (start_time == null)
        {
            start_time = getString(R.string.start_time_default_value);
            preferencesManager.PutPreferenceByKey(getResources().getString(R.string.start_time_reservation), start_time);
        }
        if (end_time == null)
        {
            end_time = getString(R.string.end_time_default_value);
            preferencesManager.PutPreferenceByKey(getResources().getString(R.string.end_time_reservation), end_time);
        }

        startTimePicker.setHour(Integer.parseInt(start_time.split(":")[0]));
        startTimePicker.setMinute(Integer.parseInt(start_time.split(":")[1]));
        endTimePicker.setHour(Integer.parseInt(end_time.split(":")[0]));
        endTimePicker.setMinute(Integer.parseInt(end_time.split(":")[1]));

        CheckBox checkBox_lunedi = root.findViewById(R.id.checkBox_lunedi);
        CheckBox checkBox_martedi = root.findViewById(R.id.checkBox_martedi);
        CheckBox checkBox_mercoledi = root.findViewById(R.id.checkBox_mercoledi);
        CheckBox checkBox_giovedi = root.findViewById(R.id.checkBox_giovedi);
        CheckBox checkBox_venerdi = root.findViewById(R.id.checkBox_venerdi);
        CheckBox checkBox_sabato = root.findViewById(R.id.checkBox_sabato);
        CheckBox checkBox_domenica = root.findViewById(R.id.checkBox_domenica);
        checkBox_lunedi.setChecked(false);
        checkBox_martedi.setChecked(false);
        checkBox_mercoledi.setChecked(false);
        checkBox_giovedi.setChecked(false);
        checkBox_venerdi.setChecked(false);
        checkBox_sabato.setChecked(false);
        checkBox_domenica.setChecked(false);
        String day = preferencesManager.GetPreferenceByKey(getResources().getString(R.string.days_reservation));
        if (day == null)
        {
            day = getString(R.string.days_default_value);
            preferencesManager.PutPreferenceByKey(getResources().getString(R.string.days_reservation), day);
        }
        String[] days = day.split(",");
        for (String d: days) {
            switch (d) {
                case "lunedi":
                    checkBox_lunedi.setChecked(true);
                    break;
                case "martedi":
                    checkBox_martedi.setChecked(true);
                    break;
                case "mercoledi":
                    checkBox_mercoledi.setChecked(true);
                    break;
                case "giovedi":
                    checkBox_giovedi.setChecked(true);
                    break;
                case "venerdi":
                    checkBox_venerdi.setChecked(true);
                    break;
                case "sabato":
                    checkBox_sabato.setChecked(true);
                    break;
                case "domenica":
                    checkBox_domenica.setChecked(true);
                    break;
            }
        }

        return root;
    }

    //#region LISTENER

    //Listener sul bottone del salvataggio
    private View.OnClickListener saveButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TimePicker startTimePicker = root.findViewById(R.id.startTimePicker);
            TimePicker endTimePicker = root.findViewById(R.id.endTimePicker);
            String startDate = startTimePicker.getHour() + ":" + startTimePicker.getMinute();
            String endDate = endTimePicker.getHour() + ":" + endTimePicker.getMinute();
            PreferencesManager preferencesManager = PreferencesManager.getInstance(getResources().getString(R.string.preferencesManager), myContext);
            preferencesManager.PutPreferenceByKey(getResources().getString(R.string.start_time_reservation), startDate);
            preferencesManager.PutPreferenceByKey(getResources().getString(R.string.end_time_reservation), endDate);
            CheckBox checkBox_lunedi = root.findViewById(R.id.checkBox_lunedi);
            CheckBox checkBox_martedi = root.findViewById(R.id.checkBox_martedi);
            CheckBox checkBox_mercoledi = root.findViewById(R.id.checkBox_mercoledi);
            CheckBox checkBox_giovedi = root.findViewById(R.id.checkBox_giovedi);
            CheckBox checkBox_venerdi = root.findViewById(R.id.checkBox_venerdi);
            CheckBox checkBox_sabato = root.findViewById(R.id.checkBox_sabato);
            CheckBox checkBox_domenica = root.findViewById(R.id.checkBox_domenica);
            String days = "";
            if (checkBox_lunedi.isChecked()) { days += "lunedi,"; }
            if (checkBox_martedi.isChecked()) { days += "martedi,"; }
            if (checkBox_mercoledi.isChecked()) { days += "mercoledi,"; }
            if (checkBox_giovedi.isChecked()) { days += "giovedi,"; }
            if (checkBox_venerdi.isChecked()) { days += "venerdi,"; }
            if (checkBox_sabato.isChecked()) { days += "sabato,"; }
            if (checkBox_domenica.isChecked()) { days += "domenica,"; }
            preferencesManager.PutPreferenceByKey(getResources().getString(R.string.days_reservation), days);
            Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.edit_done), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    };

    //#endregion


}