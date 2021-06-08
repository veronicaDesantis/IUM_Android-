package it.veronica.coursemanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.coursemanagement.R;

import java.util.ArrayList;

import it.veronica.coursemanagement.model.Teacher;

public class TeacherAdapter extends ArrayAdapter<Teacher> {

    public TeacherAdapter(Context context, ArrayList<Teacher> teachers) {
        super(context, 0, teachers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Teacher teacher = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_list_item, parent, false);
        }

        // Lookup view for data population
        TextView teacherName = (TextView) convertView.findViewById(R.id.teacherName);
        // Populate the data into the template view using the data object
        teacherName.setText(teacher.getFullName());
        // Return the completed view to render on screen
        return convertView;

    }

    /*public Teacher getItem(int position){
        return getItem(position);
    }*/

}