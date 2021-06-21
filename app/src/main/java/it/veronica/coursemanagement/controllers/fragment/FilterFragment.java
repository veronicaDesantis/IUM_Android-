package it.veronica.coursemanagement.controllers.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.coursemanagement.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.veronica.coursemanagement.adapters.CourseAdapter;
import it.veronica.coursemanagement.adapters.TeacherAdapter;
import it.veronica.coursemanagement.controllers.RootActivity;
import it.veronica.coursemanagement.model.Course;
import it.veronica.coursemanagement.model.Disponibility;
import it.veronica.coursemanagement.model.Teacher;
import it.veronica.coursemanagement.model.dbManager;
import it.veronica.coursemanagement.tree_holder.ItemHolder;
import it.veronica.coursemanagement.utility.PreferencesManager;

public class FilterFragment extends Fragment {

    private View root = null;
    private dbManager  db = null;
    private Context myContext = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_filter, container, false);
        myContext = this.getContext();
        db = new dbManager(myContext);

        FloatingActionButton closeButton = root.findViewById(R.id.closeButton);
        closeButton.setOnTouchListener(closeButtonListener);
        Button confirmButton = root.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(confirmButtonListener);

        RangeSlider cfuSlider = root.findViewById(R.id.cfuSlider);
        cfuSlider.addOnChangeListener(cfuSliderListener);
        List<Float> floatValue = cfuSlider.getValues();
        Chip chipCfu = root.findViewById(R.id.chipCfu);
        chipCfu.setText(floatValue.get(0).toString().replace(".0", "") + " - " + floatValue.get(1).toString().replace(".0", ""));


        Teacher[] teachers = db.GetAllTeacher();
        ArrayList<Teacher> list1 = new ArrayList<Teacher>();
        Collections.addAll(list1, teachers);
        TeacherAdapter adapter = new TeacherAdapter(getActivity(), list1);
        Spinner courseList = root.findViewById(R.id.teacherList);
        courseList.setAdapter(adapter);

        return root;
    }

    private View.OnTouchListener closeButtonListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, CatalogueFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(CatalogueFragment.class.getName()) // name can be null
                    .commit();
            return true;
        }
    };

    private View.OnClickListener confirmButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextInputLayout titleTextInput = root.findViewById(R.id.titleTextInput);
            String title = titleTextInput.getEditText().getText().toString();
            Spinner teacherList = root.findViewById(R.id.teacherList);
            int teacher_id = ((Teacher)teacherList.getSelectedItem()).getId();
            RangeSlider cfuSlider = root.findViewById(R.id.cfuSlider);
            List<Float> floatValue = cfuSlider.getValues();
            int start_cfu = Integer.parseInt(floatValue.get(0).toString().replace(".0", ""));
            int end_cfu = Integer.parseInt(floatValue.get(1).toString().replace(".0", ""));
            Fragment catalogueFragment = new CatalogueFragment();
            Bundle bundle = new Bundle();
            bundle.putString(getResources().getString(R.string.title_filter), title);
            bundle.putInt(getResources().getString(R.string.teacher_id), teacher_id);
            bundle.putString(getResources().getString(R.string.cfu_filter), start_cfu + "$" + end_cfu);
            catalogueFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, catalogueFragment, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(CatalogueFragment.class.getName()) // name can be null
                    .commit();
        }
    };

    private RangeSlider.OnChangeListener cfuSliderListener = new RangeSlider.OnChangeListener() {
        @Override
        public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
            List<Float> floatValue = slider.getValues();
            Chip chipCfu = root.findViewById(R.id.chipCfu);
            chipCfu.setText(floatValue.get(0).toString().replace(".0", "") + " - " + floatValue.get(1).toString().replace(".0", ""));
        }
    };
}