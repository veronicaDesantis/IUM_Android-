package it.veronica.coursemanagement.controllers.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
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
import it.veronica.coursemanagement.model.dbManager;
import it.veronica.coursemanagement.utility.PreferencesManager;

public class AssociatedCourseFragment extends Fragment {

    private View root = null;
    private dbManager  db = null;
    private Context myContext = null;
    private Course[] totalCourse = null;
    private int teacher_id = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_associated_course, container, false);
        myContext = this.getContext();
        db = new dbManager(myContext);
        ((RootActivity) getActivity()).getSupportActionBar().setTitle(R.string.course_page);

        FloatingActionButton done = root.findViewById(R.id.done);
        done.setOnTouchListener(doneListener);

        PreferencesManager preferencesManager = PreferencesManager.getInstance(getResources().getString(R.string.preferencesManager), myContext);
        int user_id = Integer.parseInt(preferencesManager.GetPreferenceByKey(getResources().getString(R.string.user_id)));
        Teacher teacher = db.GetTeacherByUserId(user_id);
        teacher_id = teacher.getId();
        totalCourse = db.GetAllCourse(teacher_id);
        ArrayList<Course> list1 = new ArrayList<Course>();
        Collections.addAll(list1, totalCourse);
        TeacherCourseAdapter adapter = new TeacherCourseAdapter(getActivity(), list1);
        ListView listView = root.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(adapter_click_listener);
        return root;
    }

    public AdapterView.OnItemClickListener adapter_click_listener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Course course = (Course) adapterView.getItemAtPosition(i);
            CheckBox checked = view.findViewById(R.id.checked);
            course.setAssociated(1 - (checked.isChecked() ? 1 : 0));
            checked.setChecked(!checked.isChecked());
            for(int j = 0; j < totalCourse.length; j++)
            {
                if (totalCourse[j].getId() == course.getId())
                {
                    totalCourse[j] = course;
                }
            }
        }
    };

    public View.OnTouchListener doneListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            for(Course c: totalCourse)
            {
                if (c.getAssociated() == 1)
                {
                    db.InsertTeacherCourse(teacher_id, c.getId());
                }
                else
                {
                    db.DeleteTeacherCourse(teacher_id, c.getId());
                }
            }
            //Ricarico il fragment con il nuovo id, per visualizzare i dettagli del docente creato
            Fragment teacherCourseFragment = new TeacherCourseFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(getResources().getString(R.string.associated), true);
            teacherCourseFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, teacherCourseFragment, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(TeacherCourseFragment.class.getName()) // name can be null
                    .commit();
            return true;
        }
    };
}