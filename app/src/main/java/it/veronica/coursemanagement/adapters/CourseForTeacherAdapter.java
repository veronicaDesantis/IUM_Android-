package it.veronica.coursemanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coursemanagement.R;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

import it.veronica.coursemanagement.model.Course;
import it.veronica.coursemanagement.model.Teacher;
import it.veronica.coursemanagement.model.dbManager;
import it.veronica.coursemanagement.utility.PreferencesManager;

public class CourseForTeacherAdapter extends ArrayAdapter<Course> {

    private Teacher teacher;
    private  dbManager db;

    public CourseForTeacherAdapter(Context context, ArrayList<Course> courses) {
        super(context, 0, courses);
        db = new dbManager(context);
        PreferencesManager preferencesManager = PreferencesManager.getInstance(getContext().getResources().getString(R.string.preferencesManager), context);
        int user_id = Integer.parseInt(preferencesManager.GetPreferenceByKey(getContext().getResources().getString(R.string.user_id)));
        teacher = db.GetTeacherByUserId(user_id);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Course course = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_course, parent, false);
        }

        // Lookup view for data population
        TextView name = convertView.findViewById(R.id.name);
        Chip chip = convertView.findViewById(R.id.chip);
        TextView description = convertView.findViewById(R.id.description);
        ImageView mine_course = convertView.findViewById(R.id.mine_course);
        mine_course.setTag(position);
        // Populate the data into the template view using the data object
        name.setText(course.getTitle());
        chip.setText(String.valueOf(course.getCfu()));
        description.setText(course.getDescription());
        if (course.getAssociated() == 1)
        {
            mine_course.setImageResource(R.drawable.ic_heart);
        }
        else
        {
            mine_course.setImageResource(R.drawable.ic_heart_empty);
        }
        mine_course.setOnClickListener(mine_courseListener);
        // Return the completed view to render on screen
        return convertView;
    }

    public View.OnClickListener mine_courseListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Course course = getItem(Integer.parseInt(view.getTag().toString()));
            course.setAssociated(1 - (course.getAssociated()));
            if (course.getAssociated() == 1)
            {
                db.InsertTeacherCourse(teacher.getId(), course.getId());
                ((ImageView)view).setImageResource(R.drawable.ic_heart);
            }
            else
            {
                db.DeleteTeacherCourse(teacher.getId(), course.getId());
                ((ImageView)view).setImageResource(R.drawable.ic_heart_empty);
            }
        }
    };
}