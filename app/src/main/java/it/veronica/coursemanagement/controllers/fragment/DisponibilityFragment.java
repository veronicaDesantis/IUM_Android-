package it.veronica.coursemanagement.controllers.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.coursemanagement.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import it.veronica.coursemanagement.adapters.DisponibilityCalendarAdapter;
import it.veronica.coursemanagement.adapters.DisponibilityListAdapter;
import it.veronica.coursemanagement.controllers.RootActivity;
import it.veronica.coursemanagement.model.Application_Setting;
import it.veronica.coursemanagement.model.Course;
import it.veronica.coursemanagement.model.Disponibility;
import it.veronica.coursemanagement.model.Teacher;
import it.veronica.coursemanagement.model.dbManager;
import it.veronica.coursemanagement.utility.PreferencesManager;

public class DisponibilityFragment extends Fragment {

    private View root = null;
    private dbManager  db = null;
    private Context myContext = null;
    private Boolean isCalendar = true;
    private Calendar calendar;
    private Application_Setting applicationSetting;
    private Teacher teacher;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_disponibility, container, false);
        ((RootActivity) getActivity()).getSupportActionBar().setTitle(R.string.disponibility_page);
        myContext = this.getContext();
        db = new dbManager(myContext);

        PreferencesManager preferencesManager = PreferencesManager.getInstance(getResources().getString(R.string.preferencesManager), myContext);
        int user_id = Integer.parseInt(preferencesManager.GetPreferenceByKey(getResources().getString(R.string.user_id)));
        teacher = db.GetTeacherByUserId(user_id);

        applicationSetting = db.GetApplicationSetting();

        ImageView go_before = root.findViewById(R.id.go_before);
        go_before.setOnClickListener(go_beforeListener);
        ImageView go_after = root.findViewById(R.id.go_after);
        go_after.setOnClickListener(go_afterListener);

        TextView day = root.findViewById(R.id.day);
        calendar = Calendar.getInstance();
        boolean isInWeek = IsInWeek();
        while (isInWeek) {
            calendar.add(Calendar.DATE, 1);
            isInWeek = IsInWeek();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dateDisplay = calendar.getTime();
        day.setText(dateFormat.format(dateDisplay));

        LinearLayout listLayout = root.findViewById(R.id.listLayout);
        FloatingActionButton goToCalendar = root.findViewById(R.id.goToCalendar);
        goToCalendar.setVisibility(View.GONE);
        goToCalendar.setOnTouchListener(goToCalendarListener);
        FloatingActionButton goToList = root.findViewById(R.id.goToList);
        goToList.setOnTouchListener(goToListListener);
        listLayout.setVisibility(View.GONE);
        RenderCalendarView();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            boolean deleted = bundle.getBoolean(getResources().getString(R.string.deleted), false);
            if (deleted) {
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.delete_done), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }

        return root;
    }

    //#region LISTENER

    public View.OnClickListener go_beforeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            calendar.add(Calendar.DATE, -1);
            boolean isInWeek = IsInWeek();
            while (isInWeek) {
                calendar.add(Calendar.DATE, -1);
                isInWeek = IsInWeek();
            }
            if (isCalendar) {
                RenderCalendarView();
            } else {
                RenderListView();
            }
        }
    };

    public View.OnClickListener go_afterListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            calendar.add(Calendar.DATE, 1);
            boolean isInWeek = IsInWeek();
            while (isInWeek) {
                calendar.add(Calendar.DATE, 1);
                isInWeek = IsInWeek();
            }
            if (isCalendar) {
                RenderCalendarView();
            } else {
                RenderListView();

            }
        }
    };

    public View.OnTouchListener goToCalendarListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            FloatingActionButton goToCalendar = root.findViewById(R.id.goToCalendar);
            FloatingActionButton goToList = root.findViewById(R.id.goToList);
            LinearLayout listLayout = root.findViewById(R.id.listLayout);
            LinearLayout calendarLayout = root.findViewById(R.id.calendarLayout);
            goToCalendar.setVisibility(View.GONE);
            calendarLayout.setVisibility(View.VISIBLE);
            goToList.setVisibility(View.VISIBLE);
            listLayout.setVisibility(View.GONE);
            isCalendar = true;
            RenderCalendarView();
            return true;
        }
    };

    public View.OnTouchListener goToListListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            FloatingActionButton goToCalendar = root.findViewById(R.id.goToCalendar);
            FloatingActionButton goToList = root.findViewById(R.id.goToList);
            LinearLayout listLayout = root.findViewById(R.id.listLayout);
            LinearLayout calendarLayout = root.findViewById(R.id.calendarLayout);
            goToCalendar.setVisibility(View.VISIBLE);
            calendarLayout.setVisibility(View.GONE);
            goToList.setVisibility(View.GONE);
            listLayout.setVisibility(View.VISIBLE);
            isCalendar = false;
            RenderListView();
            return  true;
        }
    };

    public AdapterView.OnItemClickListener ListListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Disponibility disponibility = (Disponibility) adapterView.getItemAtPosition(i);
            //Ricarico il fragment con il nuovo id, per visualizzare i dettagli del docente creato
            Fragment disponibilityRegistryFragment = new DisponibilityRegistryFragment();
            Bundle bundle = new Bundle();
            if (disponibility.getCourse() != null) {
                bundle.putInt(getResources().getString(R.string.disponibility_id), disponibility.getId());
            }
            bundle.putString(getResources().getString(R.string.date_paremeter), disponibility.getDatetime());
            bundle.putString(getResources().getString(R.string.slot_parameter), disponibility.getSlotTime());
            disponibilityRegistryFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, disponibilityRegistryFragment, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(DisponibilityRegistryFragment.class.getName()) // name can be null
                    .commit();
        }
    };

    //#endregion


    private void RenderCalendarView(){
        TextView day = root.findViewById(R.id.day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dateDisplay = calendar.getTime();
        String dateString = dateFormat.format(dateDisplay);
        day.setText(dateString);

        ListView calendarList = root.findViewById(R.id.calendarList);
        //Prendo la lista di disponibilità per il giorno e per il docente
        DisponibilityCalendarAdapter adapter = new DisponibilityCalendarAdapter(getActivity(), GetDisponibility(dateString));
        calendarList.setAdapter(adapter);
        calendarList.setOnItemClickListener(ListListener);
    }

    private void RenderListView()
    {
        ListView list_view = root.findViewById(R.id.list_view);
        //Prendo la lista di disponibilità per il giorno e per il docente
        DisponibilityListAdapter adapter = new DisponibilityListAdapter(getActivity(), GetDisponibility(""));
        list_view.setAdapter(adapter);
        list_view.setOnItemClickListener(ListListener);
    }

    private boolean IsInWeek()
    {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String dayString = "";
        switch (dayOfWeek){
            case Calendar.MONDAY:
                dayString = "lunedi";
                break;
            case Calendar.TUESDAY:
                dayString = "martedi";
                break;
            case Calendar.WEDNESDAY:
                dayString = "mercoledi";
                break;
            case Calendar.THURSDAY:
                dayString = "giovedi";
                break;
            case Calendar.FRIDAY:
                dayString = "venerdi";
                break;
            case Calendar.SATURDAY:
                dayString = "sabato";
                break;
            case Calendar.SUNDAY:
                dayString = "domenica";
                break;
        }
        return applicationSetting.getDays().indexOf(dayString) == -1;
    }

    private ArrayList<Disponibility> GetDisponibility(String datetime)
    {
        //Prendo la lista di disponibilità per il giorno e per il docente
        String start_time = applicationSetting.getStarTime();
        String end_time = applicationSetting.getEndTime();
        int hour_start = Integer.valueOf(start_time.split(":")[0]);
        String minute_start = start_time.split(":")[1];
        int hour_end = Integer.valueOf(end_time.split(":")[0]);
        ArrayList<Disponibility> disponibilityList = new ArrayList<Disponibility>();
        if (datetime != "") {
            while (hour_start < hour_end) {
                Disponibility disponibility = db.GetDisponibilityByTeacherDateSlot(teacher.getId(), datetime, hour_start + ":" + minute_start);
                int tempStart = hour_start + 1;
                if (disponibility == null) {
                    disponibility = new Disponibility(datetime, hour_start + ":" + minute_start, tempStart + ":" + minute_start);
                }
                hour_start = hour_start + 1;
                disponibilityList.add(disponibility);
            }
        }
        else
        {
            Disponibility[] disponibilities = db.GetDisponibilityByTeacher(teacher.getId());
            for (Disponibility disponibility: disponibilities) {
                disponibilityList.add(disponibility);
            }
        }
        return disponibilityList;
    }
}