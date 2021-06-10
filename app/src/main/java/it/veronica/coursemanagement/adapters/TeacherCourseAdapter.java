package it.veronica.coursemanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.coursemanagement.R;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

import it.veronica.coursemanagement.model.Course;

public class TeacherCourseAdapter extends ArrayAdapter<Course> {

    public TeacherCourseAdapter(Context context, ArrayList<Course> courses) {
        super(context, 0, courses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Course course = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_associated_course, parent, false);
        }

        // Lookup view for data population
        TextView name = convertView.findViewById(R.id.name);
        Chip chip = convertView.findViewById(R.id.chip);
        TextView description = convertView.findViewById(R.id.description);
        CheckBox checked = convertView.findViewById(R.id.checked);
        // Populate the data into the template view using the data object
        name.setText(course.getTitle());
        chip.setText(String.valueOf(course.getCfu()));
        description.setText(course.getDescription());
        checked.setChecked(course.getAssociated() == 1);
        // Return the completed view to render on screen
        return convertView;

    }
}