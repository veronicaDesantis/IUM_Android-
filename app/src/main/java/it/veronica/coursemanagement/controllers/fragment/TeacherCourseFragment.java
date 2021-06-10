package it.veronica.coursemanagement.controllers.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.coursemanagement.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;

import it.veronica.coursemanagement.adapters.CourseAdapter;
import it.veronica.coursemanagement.adapters.TeacherCourseAdapter;
import it.veronica.coursemanagement.controllers.RootActivity;
import it.veronica.coursemanagement.model.Course;
import it.veronica.coursemanagement.model.Teacher;
import it.veronica.coursemanagement.model.Teacher_course;
import it.veronica.coursemanagement.model.dbManager;
import it.veronica.coursemanagement.utility.PreferencesManager;

public class TeacherCourseFragment extends Fragment {

    private View root = null;
    private dbManager  db = null;
    private Context myContext = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_teacher_course, container, false);
        myContext = this.getContext();
        db = new dbManager(myContext);
        ((RootActivity) getActivity()).getSupportActionBar().setTitle(R.string.teacher_course_page);
        PreferencesManager preferencesManager = PreferencesManager.getInstance(getResources().getString(R.string.preferencesManager), myContext);
        int user_id = Integer.parseInt(preferencesManager.GetPreferenceByKey(getResources().getString(R.string.user_id)));
        Teacher teacher = db.GetTeacherByUserId(user_id);
        Course[] courses = db.GetCourseByTeacherId(teacher.getId());
        ArrayList<Course> list1 = new ArrayList<Course>();
        Collections.addAll(list1, courses);
        CourseAdapter adapter = new CourseAdapter(getActivity(), list1);
        ListView listView = root.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        if (courses.length == 0)
        {
            root.findViewById(R.id.no_result).setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }
        else
        {
            root.findViewById(R.id.no_result).setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }

        FloatingActionButton add = root.findViewById(R.id.add);
        add.setOnTouchListener(add_listener);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            boolean associated = bundle.getBoolean(getResources().getString(R.string.associated), false);
            if (associated) {
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.associated_done), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
        return root;
    }

    /*public AdapterView.OnItemClickListener adapter_click_listener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Course course = (Course) adapterView.getItemAtPosition(i);
            //Ricarico il fragment con il nuovo id, per visualizzare i dettagli del docente creato
            Fragment courseRegistryFragment = new CourseRegistryFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(getResources().getString(R.string.course_id), course.getId());
            courseRegistryFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, courseRegistryFragment, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(CourseRegistryFragment.class.getName()) // name can be null
                    .commit();
        }
    };*/

    public View.OnTouchListener add_listener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, AssociatedCourseFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(AssociatedCourseFragment.class.getName()) // name can be null
                    .commit();
            return true;
        }
    };
}