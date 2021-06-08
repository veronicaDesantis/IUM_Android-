package it.veronica.coursemanagement.controllers.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.coursemanagement.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import it.veronica.coursemanagement.adapters.TeacherAdapter;
import it.veronica.coursemanagement.controllers.RootActivity;
import it.veronica.coursemanagement.model.Teacher;
import it.veronica.coursemanagement.model.dbManager;

public class TeacherFragment extends Fragment {

    private View root = null;
    private dbManager  db = null;
    private Context myContext = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_teacher, container, false);
        myContext = this.getContext();
        db = new dbManager(myContext);
        ((RootActivity) getActivity()).getSupportActionBar().setTitle(R.string.teacher_page);

        Teacher[] teachers = db.GetAllTeacher();
        ArrayList<Teacher> list1 = new ArrayList<Teacher>();
        Collections.addAll(list1, teachers);
        TeacherAdapter adapter = new TeacherAdapter(getActivity(), list1);
        ListView listView = root.findViewById(R.id.teacher_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(adapter_click_listener);
        if (teachers.length == 0)
        {
            root.findViewById(R.id.teacher_no_result).setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }
        else
        {
            root.findViewById(R.id.teacher_no_result).setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }

        FloatingActionButton teacher_add = root.findViewById(R.id.teacher_add);
        teacher_add.setOnTouchListener(teacher_add_listener);

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

    public AdapterView.OnItemClickListener adapter_click_listener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Teacher teacher = (Teacher)adapterView.getItemAtPosition(i);
            //Ricarico il fragment con il nuovo id, per visualizzare i dettagli del docente creato
            Fragment teacherTegisryFragment = new TeacherRegisrtyFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(getResources().getString(R.string.teacher_id), teacher.getId());
            teacherTegisryFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, teacherTegisryFragment, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(TeacherRegisrtyFragment.class.getName()) // name can be null
                    .commit();
        }
    };

    public View.OnTouchListener teacher_add_listener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, TeacherRegisrtyFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(TeacherRegisrtyFragment.class.getName()) // name can be null
                    .commit();
            return true;
        }
    };
}