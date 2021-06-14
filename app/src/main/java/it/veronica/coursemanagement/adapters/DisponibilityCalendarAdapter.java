package it.veronica.coursemanagement.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.coursemanagement.R;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

import it.veronica.coursemanagement.controllers.fragment.CourseFragment;
import it.veronica.coursemanagement.controllers.fragment.DisponibilityRegistryFragment;
import it.veronica.coursemanagement.model.Course;
import it.veronica.coursemanagement.model.Disponibility;

public class DisponibilityCalendarAdapter extends ArrayAdapter<Disponibility> {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Context myContext;

    public DisponibilityCalendarAdapter(Context context, ArrayList<Disponibility> disponibilities) {
        super(context, 0, disponibilities);
        myContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Disponibility disponibility = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_disponibility, parent, false);
        }

        // Lookup view for data population
        TextView slot = convertView.findViewById(R.id.slot);
        slot.setText(disponibility.getStartTime() + "-" + disponibility.getEndTime());
        if (disponibility.getCourse() != null)
        {
            TextView name = convertView.findViewById(R.id.name);
            name.setText(disponibility.getCourse().getTitle());
            LinearLayout container = convertView.findViewById(R.id.container);
            if (disponibility.getAvailability() == 1) {
                container.setBackgroundColor(getContext().getColor(R.color.pastel_green));
            }
            else if (disponibility.getAvailability() == 2)
            {
               container.setBackgroundColor(getContext().getColor(R.color.pastel_yellow));
            }
        }
        // Return the completed view to render on screen
        return convertView;
    }
}