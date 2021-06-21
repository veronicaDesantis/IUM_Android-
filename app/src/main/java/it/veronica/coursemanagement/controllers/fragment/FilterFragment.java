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
import androidx.annotation.Nullable;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import it.veronica.coursemanagement.adapters.CourseAdapter;
import it.veronica.coursemanagement.adapters.TeacherAdapter;
import it.veronica.coursemanagement.controllers.RootActivity;
import it.veronica.coursemanagement.model.Course;
import it.veronica.coursemanagement.model.Disponibility;
import it.veronica.coursemanagement.model.FilterModel;
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
        Button resetButton = root.findViewById(R.id.resetButton);
        resetButton.setOnClickListener(resetButtonListener);

        TextInputLayout titleTextInput = root.findViewById(R.id.titleTextInput);
        RangeSlider cfuSlider = root.findViewById(R.id.cfuSlider);
        cfuSlider.addOnChangeListener(cfuSliderListener);
        List<Float> floatValue = cfuSlider.getValues();
        Chip chipCfu = root.findViewById(R.id.chipCfu);
        chipCfu.setText(floatValue.get(0).toString().replace(".0", "") + " - " + floatValue.get(1).toString().replace(".0", ""));

        Teacher[] teachers = db.GetAllTeacher();
        Teacher[] teachers_filter = new Teacher[teachers.length + 1];
        teachers_filter[0] = new Teacher(-1, "", "Seleziona tutti");
        int i = 1;
        for (Teacher t : teachers)
        {
            teachers_filter[i] = t;
            i = i + 1;
        }
        ArrayList<Teacher> list1 = new ArrayList<Teacher>();
        Collections.addAll(list1, teachers_filter);
        TeacherAdapter adapter = new TeacherAdapter(getActivity(), list1);
        Spinner teacherList = root.findViewById(R.id.teacherList);
        teacherList.setAdapter(adapter);

        if (FilterModel.getTitle() != "")
        {
            titleTextInput.getEditText().setText(FilterModel.getTitle());
        }
        if (FilterModel.getStart_cfu() != 0 && FilterModel.getEnd_cfu() != 0)
        {
            float startCfu = FilterModel.getStart_cfu();
            float endCfu = FilterModel.getEnd_cfu();
            cfuSlider.setValues(startCfu, endCfu);
        }
        if (FilterModel.getTeacher_id() != -1){
            teacherList.setSelection(FilterModel.getTeacher_position());
        }

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
            FilterModel.ApplyFilter(title, start_cfu, end_cfu, teacher_id, teacherList.getSelectedItemPosition());
            Fragment catalogueFragment = new CatalogueFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, catalogueFragment, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(CatalogueFragment.class.getName()) // name can be null
                    .commit();
        }
    };

    private View.OnClickListener resetButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FilterModel.Reset();
            Fragment catalogueFragment = new CatalogueFragment();
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