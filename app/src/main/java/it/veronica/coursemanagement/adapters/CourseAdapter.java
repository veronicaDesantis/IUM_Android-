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
import it.veronica.coursemanagement.model.Disponibility;

public class CourseAdapter extends ArrayAdapter<Course> {

    private boolean HideChip;

    public CourseAdapter(Context context, ArrayList<Course> courses, boolean hideChip) {
        super(context, 0, courses);
        HideChip = hideChip;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    public View initView(int position, View convertView, ViewGroup parent) {
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
        if (HideChip)
        {
            chip.setClickable(false);
            chip.setFocusable(false);
        }
        // Return the completed view to render on screen

        return convertView;
    }
}