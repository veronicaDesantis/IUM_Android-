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

public class CourseAdapter extends ArrayAdapter<Course> {

    public CourseAdapter(Context context, ArrayList<Course> courses) {
        super(context, 0, courses);
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
        name.setText(course.getTitle());
        chip.setText(String.valueOf(course.getCfu()));
        description.setText(course.getDescription());
        ImageView mine_course = convertView.findViewById(R.id.mine_course);
        mine_course.setVisibility(View.GONE);
        // Return the completed view to render on screen
        return convertView;
    }
}